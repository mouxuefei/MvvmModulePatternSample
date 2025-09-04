package com.mou.mvvmmodule.ui.main.views.patient

import android.R
import android.content.Context
import com.core.commonsdk.base.BaseFragment
import com.core.commonsdk.utils.ActRouter
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.InjectResourceSource
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener
import com.luck.picture.lib.interfaces.OnInjectLayoutResourceListener
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.TitleBarStyle
import com.mou.mvvmmodule.databinding.FragmentFitBinding
import com.mou.mvvmmodule.ui.main.viewmodel.FitViewModel
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginService
import com.orhanobut.logger.Logger
import com.scheartmed.im.utils.GlideEngine
import com.scheartmed.im.views.activity.ChatP2PActivity


/**
 * @FileName: PatientFitFragment.java
 * @author: villa_mou
 * @date: 09-10:59
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class FitFragment : BaseFragment<FitViewModel>() {
    override val binding: FragmentFitBinding by lazy {
        FragmentFitBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<FitViewModel> = FitViewModel::class.java


    override fun initView() {
        binding.btnChat.setOnClickListener {
            NIMClient.getService(V2NIMLoginService::class.java).login("test001", "123456", null, {
                ActRouter.startActivity(mContext, ChatP2PActivity::class.java)
            }) { error ->
                val code = error.code
                val desc = error.desc
                // TODO
                Logger.e("error==" + desc)
            }
        }
        binding.btnVideo.setOnClickListener {
            val video1 =
                "https://nim-nosdn.netease.im/MjYxMzEyNTY=/bmltYV83NzE4NjIzNzQ3M18xNzU2NDUxMTg1NDg3XzI5ZmE0MmUyLTdhYzItNGVmYy04ZTAyLWRhZDU1ZTM2MGQ5NA==?createTime=1756705889369"
            val video2 =
                "https://nim-nosdn.netease.im/MjYxMzEyNTY=/bmltYV83NzE4NjIzNzQ3M18xNzU2NDUxMTg1NDg3XzE4OWIxMDQxLTdjMjYtNGQ2Ni04ZTlmLTI5MWI3ZDc0OGNiMA==?createTime=1756699125062"

            val list = ArrayList<LocalMedia>()
            LocalMedia().apply {
                this.path = video1
                this.mimeType = PictureMimeType.MIME_TYPE_VIDEO
                list.add(this)
            }
            LocalMedia().apply {
                this.path = video2
                this.mimeType = PictureMimeType.MIME_TYPE_VIDEO
                list.add(this)
            }
            val style = PictureSelectorStyle()
            val titleBarStyle = TitleBarStyle()
            titleBarStyle.isHideTitleBar = true
            style.titleBarStyle = titleBarStyle
            PictureSelector.create(this)
                .openPreview()
                .setImageEngine(GlideEngine.createGlideEngine())
                .isVideoPauseResumePlay(true)
                .setSelectorUIStyle(style)
                .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                    override fun onPreviewDelete(position: Int) {}
                    override fun onLongPressDownload(
                        context: Context?,
                        media: LocalMedia?
                    ): Boolean {
                        return false
                    }
                })
                .startActivityPreview(0, false, list)

        }

    }

    override fun initData() {

    }
}