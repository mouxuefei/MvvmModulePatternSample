package com.scheartmed.im.widget.photoviewerlibrary

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.scheartmed.im.databinding.ItemPictureBinding

class PhotoViewerFragment : BaseLazyFragment() {


    var exitListener: OnExitListener? = null
    var longClickListener: OnLongClickListener? = null
    var binding: ItemPictureBinding? = null

    private var mImgSize = IntArray(2)
    private var mExitLocation = IntArray(2)
    private var mInAnim = true
    private var mPicData = ""

    /**
     * 每次选中图片后设置图片信息
     */
    fun setData(imgSize: IntArray, exitLocation: IntArray, picData: String, inAnim: Boolean) {
        mImgSize = imgSize
        mExitLocation = exitLocation
        mInAnim = inAnim
        mPicData = picData
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = ItemPictureBinding.inflate(inflater,container,false)
        return binding?.root
    }

    interface OnExitListener {
        fun exit()
    }


    override fun onLazyLoad() {
        if (PhotoViewer.mInterface != null) {
            binding?.mIv?.let { PhotoViewer.mInterface?.show(it, mPicData) }
        } else {
            throw RuntimeException("请设置图片加载回调 ShowImageViewInterface")
        }

        var alpha = 1f  // 透明度
        binding?.mIv?.setExitLocation(mExitLocation)
        binding?.mIv?.setImgSize(mImgSize)
        binding?.mIv?.setOnLongClickListener {
            if (longClickListener != null) {
                longClickListener?.onLongClick(it)
            }
            true
        }
        var intAlpha = 255
        binding?.root?.background?.alpha = intAlpha
        binding?.mIv?.rootView = binding?.root
        binding?.mIv?.setOnViewFingerUpListener {
            alpha = 1f
            intAlpha = 255
        }

        // 注册退出Activity 滑动大于一定距离后退出
        binding?.mIv?.setExitListener {
            if (exitListener != null) {
                exitListener?.exit()
            }
        }

        // 添加点击进入时的动画
        if (mInAnim)  binding?.mIv?.post {
            val scaleOa =
                ObjectAnimator.ofFloat( binding?.mIv, "scale", mImgSize[0].toFloat() /  binding?.mIv!!.width, 1f)
            val xOa = ObjectAnimator.ofFloat(
                binding?.mIv, "translationX", mExitLocation[0].toFloat() -  binding?.mIv!!.width / 2, 0f
            )
            val yOa = ObjectAnimator.ofFloat(
                binding?.mIv, "translationY", mExitLocation[1].toFloat() -  binding?.mIv!!.height / 2, 0f
            )

            val set = AnimatorSet()
            set.duration = 250
            set.playTogether(scaleOa, xOa, yOa)
            set.start()
        }


        binding?.root?.isFocusableInTouchMode = true
        binding?.root?.requestFocus()
        binding?.root?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                binding?.mIv?.exit()

                return@OnKeyListener true
            }
            false
        })

        binding?.mIv?.setOnViewDragListener { dx, dy ->

            binding?.mIv!!.scrollBy((-dx).toInt(), (-dy).toInt())  // 移动图像
            alpha -= dy * 0.001f
            intAlpha -= (dy * 0.5).toInt()
            if (alpha > 1) alpha = 1f
            else if (alpha < 0) alpha = 0f
            if (intAlpha < 0) intAlpha = 0
            else if (intAlpha > 255) intAlpha = 255
            binding?.root?.background?.alpha = intAlpha    // 更改透明度

            if (alpha >= 0.6)  binding?.mIv?.attacher?.scale = alpha   // 更改大小


        }


        binding?.mIv?.setOnClickListener {
            binding?.mIv?.exit()
        }

    }

}