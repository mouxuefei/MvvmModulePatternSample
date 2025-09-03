package com.mou.mvvmmodule.ui.main.views

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
import com.mou.mvvmmodule.widget.BottomBar
import com.mou.mvvmmodule.widget.BottomBarTab


class MainActivity : BaseActivity<MainViewModel>() {
    override val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val fragmentList = arrayListOf<Fragment>()
    private val mainFragment by lazy { PatientManageFragment() }
    private val blogFragment by lazy { MedicalResearchFragment() }
    private val meFragment by lazy { WorkshopFragment() }

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
    }

    override fun initData() {

    }

    private fun switchFragment(position: Int, smoothScroll: Boolean): Boolean {
        binding.mainViewpager.setCurrentItem(position, smoothScroll)
        return true
    }

}

