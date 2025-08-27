package com.scheartmed.lib_im.photoviewerlibrary

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.scheartmed.lib_im.R
import java.lang.ref.WeakReference
import java.util.*
import kotlin.concurrent.timerTask


@SuppressLint("StaticFieldLeak")
/**
 * Created by WangLu on 2018/7/15.
 *  PhotoViewer.setData(images)
 *                 .setCurrentPage(position)// 设置预览位置，现在是哪页，即从那张图片开始预览（手指点击的位置）
 *                 .setImgContainer(recyclerView) // 他需要容器的目的是显示缩放动画
 *                 .setShowImageViewInterface(object : PhotoViewer.ShowImageViewInterface{// 设置图片加载回调
 *                     override fun show(iv: ImageView, url: String) {
 *                         Glide.with(this@MainActivity).load(url).into(iv)
 *                     }
 *
 *                 }).start(this)// 启动界面
 * ————————————————
 */
object PhotoViewer {

    const val INDICATOR_TYPE_DOT = "INDICATOR_TYPE_DOT"
    const val INDICATOR_TYPE_TEXT = "INDICATOR_TYPE_TEXT"


    internal var mInterface: ShowImageViewInterface? = null
    private var mCreatedInterface: OnPhotoViewerCreatedListener? = null
    private var mDestroyInterface: OnPhotoViewerDestroyListener? = null

    private lateinit var imgData: ArrayList<String> // 图片数据
    private lateinit var container: WeakReference<ViewGroup>   // 存放图片的容器， ListView/GridView/RecyclerView
    private var currentPage = 0    // 当前页

    private var clickView: WeakReference<View>? = null //点击那一张图片时候的view
    private var longClickListener: OnLongClickListener? = null

    private var indicatorType = INDICATOR_TYPE_DOT   // 默认type为小圆点

    interface OnPhotoViewerCreatedListener {
        fun onCreated()
    }


    interface OnPhotoViewerDestroyListener {
        fun onDestroy()
    }

    fun setOnPhotoViewerCreatedListener(l: () -> Unit): PhotoViewer {

        mCreatedInterface = object : OnPhotoViewerCreatedListener {
            override fun onCreated() {
                l()
            }

        }
        return this
    }

    fun setOnPhotoViewerDestroyListener(l: () -> Unit): PhotoViewer {
        mDestroyInterface = object : OnPhotoViewerDestroyListener {
            override fun onDestroy() {
                l()
            }
        }
        return this
    }

    /**
     * 小圆点的drawable
     * 下标0的为没有被选中的
     * 下标1的为已经被选中的
     */
    private val mDot = intArrayOf(R.drawable.shape_no_selected_dot, R.drawable.shape_selected_dot)


    interface ShowImageViewInterface {
        fun show(iv: ImageView, url: String)
    }

    /**
     * 设置显示ImageView的接口
     */
    fun setShowImageViewInterface(i: ShowImageViewInterface): PhotoViewer {
        mInterface = i
        return this
    }

    /**
     * 设置点击一个图片
     */
    fun setClickSingleImg(data: String, view: View): PhotoViewer {
        imgData = arrayListOf(data)
        clickView = WeakReference(view)
        return this
    }

    /**
     * 设置图片数据
     */
    fun setData(data: ArrayList<String>): PhotoViewer {
        imgData = data
        return this
    }


    fun setImgContainer(container: AbsListView): PhotoViewer {
        this.container = WeakReference(container)
        return this
    }

    fun setImgContainer(container: androidx.recyclerview.widget.RecyclerView): PhotoViewer {
        this.container = WeakReference(container)
        return this
    }

    /**
     * 获取itemView
     */
    private fun getItemView(): View {
        if (clickView == null) {
            val itemView = if (container.get() is AbsListView) {
                val absListView = container.get() as AbsListView
                absListView.getChildAt(currentPage - absListView.firstVisiblePosition)
            } else {
                (container.get() as androidx.recyclerview.widget.RecyclerView).layoutManager?.findViewByPosition(
                    currentPage
                )
            }

            return if (itemView is ViewGroup) {
                findImageView(itemView)!!
            } else {
                itemView as ImageView
            }
        } else {
            return clickView!!.get()!!
        }
    }

    private fun findImageView(group: ViewGroup): ImageView? {
        for (i in 0 until group.childCount) {
            return when {
                group.getChildAt(i) is ImageView -> group.getChildAt(i) as ImageView
                group.getChildAt(i) is ViewGroup -> findImageView(group.getChildAt(i) as ViewGroup)
                else -> throw RuntimeException("未找到ImageView")
            }
        }
        return null
    }

    /**
     * 获取现在查看到的图片的原始位置 (中间)
     */
    private fun getCurrentViewLocation(): IntArray {
        val result = IntArray(2)
        getItemView().getLocationInWindow(result)
        result[0] += getItemView().measuredWidth / 2
        result[1] += getItemView().measuredHeight / 2
        return result
    }


    /**
     * 设置当前页， 从0开始
     */
    fun setCurrentPage(page: Int): PhotoViewer {
        currentPage = page
        return this
    }


    fun start(fragment: Fragment) {
        val activity = fragment.requireActivity()
        start(activity as AppCompatActivity)
    }


    fun start(activity: AppCompatActivity) {
        show(activity)
    }


    fun setOnLongClickListener(longClickListener: OnLongClickListener): PhotoViewer {
        this.longClickListener = longClickListener
        return this
    }


    /**
     * 设置指示器的样式，但是如果图片大于9张，则默认设置为文字样式
     */
    fun setIndicatorType(type: String): PhotoViewer {
        this.indicatorType = type
        return this
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun show(activity: AppCompatActivity) {


        val decorView = activity.window.decorView as ViewGroup


        // 设置添加layout的动画
        val layoutTransition = LayoutTransition()
        val alphaOa = ObjectAnimator.ofFloat(null, "alpha", 0f, 1f)
        alphaOa.duration = 50
        layoutTransition.setAnimator(LayoutTransition.APPEARING, alphaOa)
        decorView.layoutTransition = layoutTransition
        val frameLayout = FrameLayout(activity)

        val photoViewLayout =
            LayoutInflater.from(activity).inflate(R.layout.activity_photoviewer, null)
        val viewPager =
            photoViewLayout.findViewById<androidx.viewpager.widget.ViewPager>(R.id.mLookPicVP)

        val fragments = mutableListOf<PhotoViewerFragment>()

        /**
         * 存放小圆点的Group
         */
        var mDotGroup: LinearLayout? = null

        /**
         * 存放没有被选中的小圆点Group和已经被选中小圆点
         * 或者存放数字
         */
        var mFrameLayout: FrameLayout? = null

        /**
         * 选中的小圆点
         */
        var mSelectedDot: View? = null


        /**
         * 文字版本当前页
         */
        var tv: TextView? = null


        for (i in 0 until imgData.size) {
            val f = PhotoViewerFragment()
            f.exitListener = object : PhotoViewerFragment.OnExitListener {
                override fun exit() {
                    activity.runOnUiThread {
                        if (mDotGroup != null)
                            mDotGroup!!.removeAllViews()
                        frameLayout.removeAllViews()
                        decorView.removeView(frameLayout)
                        fragments.clear()


                        if (mDestroyInterface != null) {
                            mDestroyInterface!!.onDestroy()
                        }
                    }
                }

            }
            f.setData(
                intArrayOf(getItemView().measuredWidth, getItemView().measuredHeight),
                getCurrentViewLocation(),
                imgData[i],
                true
            )
            f.longClickListener = longClickListener
            fragments.add(f)
        }

        val adapter = PhotoViewerPagerAdapter(fragments, activity.supportFragmentManager)


        viewPager.adapter = adapter
        viewPager.currentItem = currentPage
        viewPager.offscreenPageLimit = 100
        viewPager.addOnPageChangeListener(object :
            androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                if (mSelectedDot != null && imgData.size > 1) {
                    val dx = mDotGroup!!.getChildAt(1).x - mDotGroup!!.getChildAt(0).x
                    mSelectedDot!!.translationX = (position * dx) + positionOffset * dx
                }
            }

            override fun onPageSelected(position: Int) {
                currentPage = position


                /**
                 * 解决RecyclerView获取不到itemView的问题
                 * 如果滑到的view不在当前页面显示，那么则滑动到那个position，再获取itemView
                 */
                if (container.get() !is AbsListView) {
                    val layoutManager =
                        (container.get() as androidx.recyclerview.widget.RecyclerView).layoutManager
                    if (layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
                        if (currentPage < layoutManager.findFirstVisibleItemPosition() || currentPage > layoutManager.findLastVisibleItemPosition()) {
                            layoutManager.scrollToPosition(currentPage)
                        }
                    } else if (layoutManager is androidx.recyclerview.widget.GridLayoutManager) {
                        if (currentPage < layoutManager.findFirstVisibleItemPosition() || currentPage > layoutManager.findLastVisibleItemPosition()) {
                            layoutManager.scrollToPosition(currentPage)
                        }
                    }
                }

                /**
                 * 设置文字版本当前页的值
                 */
                if (tv != null) {
                    tv!!.text = "${currentPage + 1}/${imgData.size}"
                }

                // 这里延时0.2s是为了解决上面👆的问题。因为如果刚调用ScrollToPosition方法，就获取itemView是获取不到的，所以要延时一下
                Timer().schedule(timerTask {
                    fragments[currentPage].setData(
                        intArrayOf(
                            getItemView().measuredWidth,
                            getItemView().measuredHeight
                        ), getCurrentViewLocation(), imgData[currentPage], false
                    )
                }, 200)

            }

        })

        frameLayout.addView(photoViewLayout)


        frameLayout.post {
            mFrameLayout = FrameLayout(activity)
            if (imgData.size in 2..9 && indicatorType == INDICATOR_TYPE_DOT) {

                /**
                 * 实例化两个Group
                 */
                if (mFrameLayout != null) {
                    mFrameLayout!!.removeAllViews()
                }
                if (mDotGroup != null) {
                    mDotGroup!!.removeAllViews()
                    mDotGroup = null
                }
                mDotGroup = LinearLayout(activity)

                if (mDotGroup!!.childCount != 0)
                    mDotGroup!!.removeAllViews()
                val dotParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                /**
                 * 未选中小圆点的间距
                 */
                dotParams.rightMargin = Utils.dp2px(activity, 12)

                /**
                 * 创建未选中的小圆点
                 */
                for (i in 0 until imgData.size) {
                    val iv = ImageView(activity)
                    iv.setImageDrawable(activity.resources.getDrawable(mDot[0]))
                    iv.layoutParams = dotParams
                    mDotGroup!!.addView(iv)
                }

                /**
                 * 设置小圆点Group的方向为水平
                 */
                mDotGroup!!.orientation = LinearLayout.HORIZONTAL
                /**
                 * 设置小圆点在中间
                 */
                mDotGroup!!.gravity = Gravity.CENTER or Gravity.BOTTOM
                /**
                 * 两个Group的大小都为match_parent
                 */
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )


                params.bottomMargin = Utils.dp2px(activity, 70)
                /**
                 * 首先添加小圆点的Group
                 */
                frameLayout.addView(mDotGroup, params)

                mDotGroup!!.post {
                    if (mSelectedDot != null) {
                        mSelectedDot = null
                    }
                    if (mSelectedDot == null) {
                        val iv = ImageView(activity)
                        iv.setImageDrawable(activity.resources.getDrawable(mDot[1]))
                        val params = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        /**
                         * 设置选中小圆点的左边距
                         */
                        params.leftMargin = mDotGroup!!.getChildAt(0).x.toInt()
                        iv.translationX =
                            (dotParams.rightMargin * currentPage + mDotGroup!!.getChildAt(0).width * currentPage).toFloat()
                        params.gravity = Gravity.BOTTOM
                        mFrameLayout!!.addView(iv, params)
                        mSelectedDot = iv
                    }
                    /**
                     * 然后添加包含未选中圆点和选中圆点的Group
                     */
                    frameLayout.addView(mFrameLayout, params)
                }
            } else {
                tv = TextView(activity)
                tv!!.text = "${currentPage + 1}/${imgData.size}"
                tv!!.setTextColor(Color.WHITE)
                tv!!.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                tv!!.textSize = 18f
                mFrameLayout!!.addView(tv)
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                params.bottomMargin = Utils.dp2px(activity, 80)
                frameLayout.addView(mFrameLayout, params)

            }
        }
        decorView.addView(
            frameLayout,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        if (mCreatedInterface != null) {
            mCreatedInterface!!.onCreated()
        }
    }


}