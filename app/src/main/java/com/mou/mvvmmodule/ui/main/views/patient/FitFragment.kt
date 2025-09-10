package com.mou.mvvmmodule.ui.main.views.patient

import android.content.Context
import com.core.commonsdk.base.BaseFragment
import com.core.commonsdk.utils.ActRouter
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.TitleBarStyle
import com.mou.mvvmmodule.databinding.FragmentFitBinding
import com.mou.mvvmmodule.ui.main.viewmodel.FitViewModel
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.v2.V2NIMError
import com.netease.nimlib.sdk.v2.V2NIMFailureCallback
import com.netease.nimlib.sdk.v2.V2NIMSuccessCallback
import com.netease.nimlib.sdk.v2.auth.V2NIMLoginService
import com.netease.nimlib.sdk.v2.common.V2NIMAntispamConfig
import com.netease.nimlib.sdk.v2.team.V2NIMTeamService
import com.netease.nimlib.sdk.v2.team.enums.V2NIMTeamAgreeMode
import com.netease.nimlib.sdk.v2.team.enums.V2NIMTeamInviteMode
import com.netease.nimlib.sdk.v2.team.enums.V2NIMTeamJoinMode
import com.netease.nimlib.sdk.v2.team.enums.V2NIMTeamType
import com.netease.nimlib.sdk.v2.team.enums.V2NIMTeamUpdateExtensionMode
import com.netease.nimlib.sdk.v2.team.enums.V2NIMTeamUpdateInfoMode
import com.netease.nimlib.sdk.v2.team.params.V2NIMCreateTeamParams
import com.netease.nimlib.sdk.v2.team.result.V2NIMCreateTeamResult
import com.orhanobut.logger.Logger
import com.scheartmed.im.utils.GlideEngine
import com.scheartmed.im.views.activity.ChatGroupActivity
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

        binding.btnCreateGroup.setOnClickListener {
            NIMClient.getService(V2NIMLoginService::class.java)
                .login("user@113722", "123456", null, {
                    val v2TeamService = NIMClient.getService(
                        V2NIMTeamService::class.java
                    )
                    val createTeamParams = V2NIMCreateTeamParams()
                    createTeamParams.name = "群聊test名字2"
                    createTeamParams.teamType = V2NIMTeamType.V2NIM_TEAM_TYPE_NORMAL
                    createTeamParams.setAgreeMode(V2NIMTeamAgreeMode.V2NIM_TEAM_AGREE_MODE_NO_AUTH);
                    createTeamParams.setJoinMode(V2NIMTeamJoinMode.V2NIM_TEAM_JOIN_MODE_FREE);
                    createTeamParams.setInviteMode(V2NIMTeamInviteMode.V2NIM_TEAM_INVITE_MODE_ALL);
                    createTeamParams.setUpdateInfoMode(V2NIMTeamUpdateInfoMode.V2NIM_TEAM_UPDATE_INFO_MODE_ALL);
                    createTeamParams.setUpdateExtensionMode(V2NIMTeamUpdateExtensionMode.V2NIM_TEAM_UPDATE_EXTENSION_MODE_MANAGER);
                    createTeamParams.memberLimit = 100
                    createTeamParams.intro = "test2"
                    createTeamParams.announcement = "test2"
                    createTeamParams.serverExtension = "test2"
                    val inviteeAccountIds: MutableList<String> = ArrayList()
                    inviteeAccountIds.add("user@113722")
                    inviteeAccountIds.add("user@113723")
                    inviteeAccountIds.add("user@113724")
                    val postscript = "test2"
                    v2TeamService.createTeam(createTeamParams, inviteeAccountIds, postscript, null,
                        { result ->
                            Logger.e("createTeam success, teamId: ${result.team.teamId}")


                        },
                        { error ->
                            Logger.e("createTeam fail: ${error.desc}")
                            Logger.e("createTeam fail: ${error.code}")
                            Logger.e("createTeam fail: ${error.detail.toString()}")
                        })
                }) { error ->
                    val code = error.code
                    val desc = error.desc
                    // TODO
                    Logger.e("error==" + desc)
                }


        }

        binding.btnGroupLogin.setOnClickListener {
            NIMClient.getService(V2NIMLoginService::class.java)
                .login("user@113722", "123456", null, {
                ActRouter.startActivity(mContext, ChatGroupActivity::class.java)
                    Logger.e("登录成功==")
                }) { error ->
                    val code = error.code
                    val desc = error.desc
                    // TODO
                    Logger.e("error==" + desc)
                }
        }

        binding.btnGroup2.setOnClickListener {
            NIMClient.getService(V2NIMLoginService::class.java)
                .login("user@113723", "97b168d5-b09b-4bf8-b698-8fdd1505ff3a", null, {
                ActRouter.startActivity(mContext, ChatGroupActivity::class.java)
                    Logger.e("登录成功==")
                }) { error ->
                    val code = error.code
                    val desc = error.desc
                    // TODO
                    Logger.e("error==" + desc)
                }
        }


        binding.btnGroup3.setOnClickListener {
            NIMClient.getService(V2NIMLoginService::class.java)
                .login("user@113724", "123456", null, {
                ActRouter.startActivity(mContext, ChatGroupActivity::class.java)
                    Logger.e("登录成功==")
                }) { error ->
                    val code = error.code
                    val desc = error.desc
                    // TODO
                    Logger.e("error==" + desc)
                }
        }

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