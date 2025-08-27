package com.scheartmed.lib_im.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.tools.ScreenUtils;
import com.scheartmed.lib_im.R;


public class GlideUtils {

    public static void loadChatImage(final Context mContext, String imgUrl, final ImageView imageView) {
        final RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.default_img_failed); // 加载失败的图片
        if (imageView != null) {
            Glide.with(mContext)
                    .load(imgUrl) // 图片地址
                    .apply(options)
                    .into(imageView);
        }

    }

    public static void loadChatImageRadius(final Context mContext, String imgUrl, int radius, final ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.default_img_failed); // 加载失败的图片
        if (imageView != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(imgUrl) // 图片地址
                    .apply(options)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            int width = resource.getWidth();//图片原始宽度
                            int height = resource.getHeight();//图片原始高度
                            if (width > height) {
                                int scaledH = ScreenUtils.dip2px(mContext, 100);//固定图片展示高度为180dp
                                int scaledW = (width * scaledH) / height;//计算出按比缩放
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(scaledW, scaledH);
                                imageView.setLayoutParams(lp);
                            } else {
                                int scaledW = ScreenUtils.dip2px(mContext, 100);//固定图片展示高度为180dp
                                int scaledH = (height * scaledW) / width;//计算出按比缩放后的宽度
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(scaledW, scaledH);
                                imageView.setLayoutParams(lp);
                            }
                            imageView.setImageBitmap(getRoundedCornerBitmap(resource, ScreenUtils.dip2px(mContext, radius)));
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                        }
                    });

        }
//            CornerTransform transform = new CornerTransform(mContext, radius);
//            RequestOptions options = new RequestOptions()
//                    .centerCrop()
//                    .transform(transform);
//            Glide.with(mContext)
//                    .load(imgUrl)
//                    .apply(options)
//                    .into(imageView);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int radius) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = radius;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    public static void loadCircleImage(final Context mContext, String imgUrl, final ImageView imageView) {
        try {
            RequestOptions options = new RequestOptions()
                    .bitmapTransform(new CircleCrop())
                    .error(R.mipmap.default_img_failed); // 加载失败的图片
            if (imageView != null) {
                Glide.with(mContext)
                        .load(imgUrl) // 图片地址
                        .apply(options)
                        .into(imageView);

            }
        } catch (Exception e) {

        }

    }
}
