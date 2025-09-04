package com.scheartmed.im.views.activity

import androidx.viewpager2.widget.ViewPager2
import com.core.commonsdk.base.BaseActivity
import com.core.basemvvm.mvvm.EmptyViewModel
import com.scheartmed.im.databinding.ActivityPhotoViewerBinding
import com.scheartmed.im.views.adapter.PhotoPagerAdapter

class PhotoViewerActivity : BaseActivity<EmptyViewModel>() {

    companion object {
        const val EXTRA_IMAGE_URLS = "extra_image_urls"
        const val EXTRA_POSITION = "extra_position"
    }

    override val binding: ActivityPhotoViewerBinding by lazy {
        ActivityPhotoViewerBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<EmptyViewModel>? = EmptyViewModel::class.java
//    private lateinit var indicatorView: CustomIndicatorView


    override fun initView() {
//        indicatorView = findViewById(R.id.indicatorView)

        val imageUrls = intent.getStringArrayListExtra(EXTRA_IMAGE_URLS) ?: arrayListOf()
        val startPos = intent.getIntExtra(EXTRA_POSITION, 0)

        val adapter = PhotoPagerAdapter(this, imageUrls)
        binding.viewPager.adapter = adapter
//        indicatorView.setCount(imageUrls.size)
//        indicatorView.setCurrentPosition(startPos)
        // 指示器初始化
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
//                indicatorView.setCurrentPosition(position)
            }
        })
        binding.viewPager.setCurrentItem(startPos, false)
    }

    override fun initData() {

    }
}