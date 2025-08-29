package com.scheartmed.lib_im.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luck.picture.lib.photoview.PhotoView
import com.scheartmed.lib_im.databinding.ItemPhotoViewBinding

class PhotoPagerAdapter(
    private val context: Context,
    private val imageUrls: List<String>
) : RecyclerView.Adapter<PhotoPagerAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(val binding: ItemPhotoViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val url = imageUrls[position]
        val photoView: PhotoView = holder.binding.photoView

        // Glide 加载图片
        Glide.with(context)
            .load(url)
            .placeholder(android.R.color.darker_gray)
            .error(android.R.color.black)
            .into(photoView)
    }

    override fun getItemCount(): Int = imageUrls.size
}