package com.mou.mvvmmodule.ui.main.views

import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.core.commonsdk.base.BaseActivity
import com.mou.mvvmmodule.R
import com.mou.mvvmmodule.databinding.ActivityMainBinding
import com.mou.mvvmmodule.ui.main.viewmodel.MainViewModel
import com.mou.mvvmmodule.ui.main.views.doctor.PatientManageFragment
import com.mou.mvvmmodule.ui.main.views.patient.FitFragment
import com.mou.mvvmmodule.ui.main.views.patient.MessageFragment
import com.mou.mvvmmodule.ui.main.views.patient.MineFragment
import com.mou.mvvmmodule.widget.BottomBar
import com.mou.mvvmmodule.widget.BottomBarTab
import com.scheartmed.im.event.BottomBarEvent
import com.scheartmed.im.views.fragment.ChatFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : BaseActivity<MainViewModel>() {
    override val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val fragmentList = arrayListOf<Fragment>()
    private val mainFragment by lazy { PatientManageFragment() }
    private val blogFragment by lazy { MessageFragment() }
    private val meFragment by lazy { MineFragment() }
    var isKeyboardVisible = false // 当前键盘状态

    private val bottomIds =
        arrayListOf<Int>(R.id.home, R.id.blog)

    init {
        fragmentList.run {
            add(mainFragment)
            add(blogFragment)
            add(meFragment)
        }
    }

    override fun providerVMClass() = MainViewModel::class.java

    override fun initView() {
        EventBus.getDefault().register(this)
        initViewPager()
        binding.bottomBar.addItem(
            BottomBarTab(
                this,
                R.mipmap.icon_bottombar_home_unselected,
                R.mipmap.icon_bottombar_home_selected,
                "首页"
            )
        )
            .addItem(
                BottomBarTab(
                    this,
                    R.mipmap.icon_bottombar_device_unselected,
                    R.mipmap.icon_bottombar_device_selected,
                    "鹿客智能"
                )
            )
            .addItem(
                BottomBarTab(
                    this,
                    R.mipmap.icon_bottombar_user_unselected,
                    R.mipmap.icon_bottombar_user_selected,
                    "我的"
                )
            )
        binding.bottomBar.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                if (position == prePosition) {
                    return
                }
                switchFragment(position, false)
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabReselected(position: Int) {
                switchFragment(position, false)
            }
        })
    }

    private fun initViewPager() {
        binding.mainViewpager.isUserInputEnabled = true
        binding.mainViewpager.offscreenPageLimit = 2
        val adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun createFragment(position: Int) = fragmentList[position]

            override fun getItemCount() = fragmentList.size
        }
        binding.mainViewpager.adapter = adapter

        binding.mainViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomBar.setCurrentItem(position)
            }
        })

        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            val visible = keypadHeight > screenHeight * 0.15
            if (visible != isKeyboardVisible) { // 状态变化才处理
                isKeyboardVisible = visible
                com.orhanobut.logger.Logger.d("keypadHeight = $keypadHeight")
                rootView.post {
                    if(visible){
                        View.GONE
                    }else{
                        // 假设父 Fragment 的类是 ParentFragment
                        val parentFragment = supportFragmentManager.findFragmentByTag("MessageFragment") as? MessageFragment
                        val childFragment = parentFragment?.childFragmentManager?.findFragmentByTag("ChatFragment") as? ChatFragment
                        childFragment?.let {
                            com.orhanobut.logger.Logger.d("走了吗 = $keypadHeight")
                        }
                    }

                    binding.bottomBar.visibility = if (visible) View.GONE else View.VISIBLE
                }
            }
        }

    }

    override fun initData() {

    }

    private fun switchFragment(position: Int, smoothScroll: Boolean): Boolean {
        binding.mainViewpager.setCurrentItem(position, smoothScroll)
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: BottomBarEvent) {
        if (event.isShow) {
            binding.bottomBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}

