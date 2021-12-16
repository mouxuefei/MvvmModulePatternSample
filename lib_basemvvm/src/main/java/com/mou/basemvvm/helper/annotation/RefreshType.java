package com.mou.basemvvm.helper.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

import static com.mou.basemvvm.helper.annotation.RefreshType.LOADMORE;
import static com.mou.basemvvm.helper.annotation.RefreshType.LOADMOREFAIL;
import static com.mou.basemvvm.helper.annotation.RefreshType.NORMAL;
import static com.mou.basemvvm.helper.annotation.RefreshType.NOTMORE;
import static com.mou.basemvvm.helper.annotation.RefreshType.REFRESH;
import static com.mou.basemvvm.helper.annotation.RefreshType.REFRESHFAIL;


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
 * 下拉刷新/上拉加载的标识类
 */

@IntDef({REFRESH, LOADMORE, REFRESHFAIL, LOADMOREFAIL, NOTMORE, NORMAL})
@Retention(RetentionPolicy.SOURCE)
public @interface RefreshType {
    //下拉刷新结束
    int REFRESH = -5;
    //加载更多结束
    int LOADMORE = -4;
    //下拉刷新失败
    int REFRESHFAIL = -3;
    //加载更多失败
    int LOADMOREFAIL = -2;
    //没有更多数据
    int NOTMORE = -1;
    //初始化状态
    int NORMAL = 0;
}
