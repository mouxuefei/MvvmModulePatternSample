package com.mou.mvvmmodule.ui.main.views

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.fortunes.commonsdk.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mou.mvvmmodule.R
import com.mou.mvvmmodule.databinding.ActivityMainBinding
import com.mou.mvvmmodule.ui.main.viewmodel.MainViewModel

class MainActivity : BaseActivity<MainViewModel>() {
    override val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val fragmentList = arrayListOf<Fragment>()
    private val mainFragment by lazy { HomeFragment() }
    private val blogFragment by lazy { HomeFragment() }

    private val bottomIds =
        arrayListOf<Int>(R.id.home, R.id.blog)

    init {
        fragmentList.run {
            add(mainFragment)
            add(blogFragment)
        }
    }
    override fun providerVMClass() = MainViewModel::class.java

    override fun initView() {
        initViewPager()
        binding.navView.setOnNavigationItemSelectedListener(onNavigationItemSelected)
    }

    private fun initViewPager() {
        binding.mainViewpager.isUserInputEnabled = true
        binding.mainViewpager.offscreenPageLimit = 2
        val adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun createFragment(position: Int) = fragmentList[position]

            override fun getItemCount() = fragmentList.size
        }
        binding.mainViewpager.adapter = adapter

        binding.mainViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomIds.forEachIndexed { index, item ->
                    if (position == index) {
                        binding.navView.selectedItemId = item
                    }
                }

            }
        })
    }

    override fun initData() {

    }

    private val onNavigationItemSelected = BottomNavigationView.OnNavigationItemSelectedListener {
        bottomIds.forEachIndexed { index, item ->
            if (it.itemId == item) {
                switchFragment(index, false)
            }
        }

        true
    }

    private fun switchFragment(position: Int, smoothScroll: Boolean): Boolean {
        binding.mainViewpager.setCurrentItem(position, smoothScroll)
        return true
    }

}

