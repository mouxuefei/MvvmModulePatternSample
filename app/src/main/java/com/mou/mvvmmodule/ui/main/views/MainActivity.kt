package com.mou.mvvmmodule.ui.main.views

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.core.commonsdk.base.BaseActivity
import com.mou.mvvmmodule.R
import com.mou.mvvmmodule.databinding.ActivityMainBinding
import com.mou.mvvmmodule.ui.main.viewmodel.MainViewModel
import com.mou.mvvmmodule.ui.main.views.doctor.MedicalResearchFragment
import com.mou.mvvmmodule.ui.main.views.doctor.PatientManageFragment
import com.mou.mvvmmodule.ui.main.views.doctor.WorkshopFragment
import com.mou.mvvmmodule.ui.main.views.patient.FitFragment
import com.mou.mvvmmodule.ui.main.views.patient.MessageFragment
import com.mou.mvvmmodule.ui.main.views.patient.MineFragment
import com.mou.mvvmmodule.widget.BottomBar
import com.mou.mvvmmodule.widget.BottomBarTab
import com.scheartmed.im.event.BottomBarEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : BaseActivity<MainViewModel>() {
    override val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun providerVMClass() = MainViewModel::class.java

    private val fragmentList = arrayListOf<Fragment>()

    //患者端
    private val fitFragment by lazy { FitFragment() }
    private val messageFragment by lazy { MessageFragment() }
    private val mineFragment by lazy { MineFragment() }

    //专家端
    private val patientManageFragment by lazy { PatientManageFragment() }
    private val workshopFragment by lazy { WorkshopFragment() }
    private val medicalResearchFragment by lazy { MedicalResearchFragment() }

    private fun createBottomBarTab(
        unselectedIcon: Int,
        selectedIcon: Int,
        title: String
    ): BottomBarTab {
        return BottomBarTab(this, unselectedIcon, selectedIcon, title)
    }

    private val bottomBarFit by lazy {
        createBottomBarTab(
            R.mipmap.icon_bottombar_home_unselected,
            R.mipmap.icon_bottombar_home_selected,
            "管理计划"
        )
    }

    private val bottomBarMessage by lazy {
        createBottomBarTab(
            R.mipmap.icon_bottombar_device_unselected,
            R.mipmap.icon_bottombar_device_selected,
            "消息"
        )
    }

    private val bottomBarMine by lazy {
        createBottomBarTab(
            R.mipmap.icon_bottombar_user_unselected,
            R.mipmap.icon_bottombar_user_selected,
            "我的"
        )
    }

    private val bottomBarPatientManage by lazy {
        createBottomBarTab(
            R.mipmap.icon_bottombar_home_unselected,
            R.mipmap.icon_bottombar_home_selected,
            "患者管理"
        )
    }

    private val bottomBarMedicalResearch by lazy {
        createBottomBarTab(
            R.mipmap.icon_bottombar_user_unselected,
            R.mipmap.icon_bottombar_user_selected,
            "医学研究"
        )
    }

    private val bottomBarWorkShop by lazy {
        createBottomBarTab(
            R.mipmap.icon_bottombar_device_unselected,
            R.mipmap.icon_bottombar_device_selected,
            "工作室"
        )
    }

    init {
        fragmentList.run {
            add(fitFragment)
            add(messageFragment)
            add(mineFragment)
        }
    }


    override fun initView() {
        EventBus.getDefault().register(this)
        initViewPager()
        binding.bottomBar
            .addItem(bottomBarFit)
            .addItem(bottomBarMessage)
            .addItem(bottomBarMine)
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

        setUnread(bottomBarFit,100)
    }

    private fun setUnread(bottomBarTab: BottomBarTab, count: Int) {
        bottomBarTab.setUnreadCount(count)
        bottomBarTab.showDot()
    }

    private fun initViewPager() {
        binding.mainViewpager.isUserInputEnabled = false  // 禁止用户手势滑动
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
//        Keyboard4Utils.registerKeyboardHeightListener(this) {
//            com.orhanobut.logger.Logger.e("第四种方式：当前的软键盘高度：$it")
//        }

    }

    override fun initData() {

    }

    private fun switchFragment(position: Int, smoothScroll: Boolean): Boolean {
        binding.mainViewpager.setCurrentItem(position, smoothScroll)
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: BottomBarEvent) {
        com.orhanobut.logger.Logger.d("接收到消息了 = ${event.isShow}")
//        if (event.isShow) {
        binding.bottomBar.visibility = if (event.isShow) View.VISIBLE else View.GONE
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}

