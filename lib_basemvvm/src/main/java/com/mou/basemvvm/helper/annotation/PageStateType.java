package com.mou.basemvvm.helper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

import static com.mou.basemvvm.helper.annotation.PageStateType.CONTENT;
import static com.mou.basemvvm.helper.annotation.PageStateType.EMPTY;
import static com.mou.basemvvm.helper.annotation.PageStateType.ERROR;
import static com.mou.basemvvm.helper.annotation.PageStateType.LOADING;
import static com.mou.basemvvm.helper.annotation.PageStateType.NORMAL;
import static com.mou.basemvvm.helper.annotation.PageStateType.NOWORK;


/***
 *
 *   █████▒█    ██  ▄████▄   ██ ▄█▀       ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒        ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░        ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄        ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄       ██████╔╝╚██████╔╝╚██████╔╝
 *  ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒       ╚═════╝  ╚═════╝  ╚═════╝
 *  ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 *  ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 *           ░     ░ ░      ░  ░
 *
 * Created by mou on 2018/8/20.
 * 页面状态标识类
 */

@IntDef({LOADING, EMPTY, ERROR, NOWORK, NORMAL, CONTENT})
@Retention(RetentionPolicy.SOURCE)
public @interface PageStateType {
    int NOWORK = -4;
    //加载中
    int LOADING = -3;
    //暂无数据
    int EMPTY = -2;
    //加载失败
    int ERROR = -1;
    //初始化状态
    int NORMAL = 0;
    //显示内容布局
    int CONTENT = 1;
}
