package com.scheartmed.lib_im.views.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.fortunes.commonsdk.base.BaseActivity
import com.mou.basemvvm.mvvm.EmptyViewModel
import com.scheartmed.lib_im.databinding.ActivityPhotoViewerBinding
import com.scheartmed.lib_im.views.adapter.PhotoPagerAdapter

class PhotoViewerActivity : BaseActivity<EmptyViewModel>() {

    companion object {
        const val EXTRA_IMAGE_URLS = "extra_image_urls"
        const val EXTRA_POSITION = "extra_position"
    }

    override val binding: ActivityPhotoViewerBinding = ActivityPhotoViewerBinding.inflate(layoutInflater)

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