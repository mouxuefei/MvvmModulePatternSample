package com.scheartmed.lib_im.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.scheartmed.lib_im.databinding.ActivityPhotoViewerBinding
import com.scheartmed.lib_im.views.adapter.PhotoPagerAdapter

class PhotoViewerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMAGE_URLS = "extra_image_urls"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var binding: ActivityPhotoViewerBinding
//    private lateinit var indicatorView: CustomIndicatorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
}