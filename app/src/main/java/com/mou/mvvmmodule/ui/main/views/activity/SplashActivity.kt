package com.mou.mvvmmodule.ui.main.views.activity

import android.content.Intent
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.core.commonsdk.base.BaseActivity
import com.core.basemvvm.mvvm.EmptyViewModel
import com.mou.mvvmmodule.databinding.ActivitySplashBinding
import com.mou.mvvmmodule.ui.login.views.LoginActivity
import kotlinx.coroutines.launch

/**
 * @FileName: SplashActivity.java
 * @author: villa_mou
 * @date: 09-09:49
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class SplashActivity : BaseActivity<EmptyViewModel>() {
    override val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<EmptyViewModel> = EmptyViewModel::class.java

    private val splashDelay: Long = 2000 // 最短动画时间
    override fun initView() {
        startAnimation(binding.ivLogo)
        lifecycleScope.launch {

            // 登录与 Token 校验
            val loggedIn = isUserLoggedIn()
            val tokenValid = if (loggedIn) validateToken() else false

            // 版本更新检查
            val needUpdate = checkAppVersion()

            // 跳转逻辑
            binding.ivLogo.postDelayed({
                navigateNext(loggedIn, tokenValid, needUpdate)
            }, splashDelay)
        }
    }

    private fun startAnimation(logo: ImageView) {
        val scaleAnim = ScaleAnimation(
            0.5f, 1f, 0.5f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = splashDelay
            interpolator = AccelerateDecelerateInterpolator()
        }

        val alphaAnim = AlphaAnimation(0f, 1f).apply {
            duration = splashDelay
            interpolator = AccelerateDecelerateInterpolator()
        }
        logo.startAnimation(scaleAnim)
        logo.startAnimation(alphaAnim)
    }

    override fun initData() {
    }

    private fun isUserLoggedIn(): Boolean {
        return false
    }

    private suspend fun validateToken(): Boolean {
        return false
    }

    private suspend fun checkAppVersion(): Boolean {
        return false
    }

    private fun navigateNext(loggedIn: Boolean, tokenValid: Boolean, needUpdate: Boolean) {
        when {
            needUpdate -> {
                // TODO: 跳转到更新页面
            }

            loggedIn && tokenValid -> {
                startActivity(Intent(this, MainActivity::class.java))
            }

            else -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        finish()
    }
}