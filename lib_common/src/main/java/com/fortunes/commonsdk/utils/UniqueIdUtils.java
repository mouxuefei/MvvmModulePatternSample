package com.fortunes.commonsdk.utils;

import android.os.Build;

import java.util.UUID;

/**
 * Created by MR-Cheng on 2017/12/25.
 */

public class UniqueIdUtils {

    /**
     * 生成设备唯一id
     *
     * @return
     */
    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35"
                + Build.BOARD.length() % 10
                + Build.BRAND.length() % 10
                + Build.CPU_ABI.length() % 10
                + Build.DEVICE.length() % 10
                + Build.DISPLAY.length() % 10
                + Build.HOST.length() % 10
                + Build.ID.length() % 10
                + Build.MANUFACTURER.length() % 10
                + Build.MODEL.length() % 10
                + Build.PRODUCT.length() % 10
                + Build.TAGS.length() % 10
                + Build.TYPE.length() % 10
                + Build.USER.length() % 10; //13 位
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();//API>=9 使用serial号
            String str = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            return MD5.getMD5(str);
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        String str = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();

        return MD5.getMD5(str);
    }

}
