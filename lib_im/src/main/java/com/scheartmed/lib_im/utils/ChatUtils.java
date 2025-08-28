package com.scheartmed.lib_im.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.netease.nimlib.sdk.v2.message.V2NIMMessage;
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageImageAttachment;
import com.netease.nimlib.sdk.v2.message.attachment.V2NIMMessageVideoAttachment;
import com.netease.nimlib.sdk.v2.message.enums.V2NIMMessageSendingState;

/**
 * Created by wudeng on 2017/11/1.
 */

public class ChatUtils {

    private Context mContext;

    public ChatUtils(Context context) {
        mContext = context;


    }


    /**
     * 图片发送中
     */
    public boolean isImageTransferring(V2NIMMessage message) {
        return message.isSelf() && (message.getSendingState() == V2NIMMessageSendingState.V2NIM_MESSAGE_SENDING_STATE_SENDING);
    }

    public String getAudioTime(long duration) {
        return String.valueOf(duration / 1000.0) + "‘";
    }


    public void setAudioLayoutWidth(RelativeLayout layout, long duration) {
        float perSecondWidth = 4.0f;
        float second = duration / 1000.0f;
        float width = second * perSecondWidth;

        if (width < 60) {
            width = 60.0f;
        } else if (width > 240) {
            width = 240.0f;
        }

        int dpWidth = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, mContext.getResources().getDisplayMetrics()));

        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = dpWidth;
        layout.setLayoutParams(params);
    }


}
