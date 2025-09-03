package com.mou.mvvmmodule.ui.main.views

import android.animation.ValueAnimator
import android.app.Activity
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.core.commonsdk.base.BaseActivity
import com.google.zxing.ResultPoint
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.PermissionLists
import com.hjq.permissions.permission.base.IPermission
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.mou.mvvmmodule.databinding.ActivityQrScanBinding
import com.mou.mvvmmodule.ui.main.viewmodel.QRScanViewModel


/**
 * @FileName: QRScanActivity.java
 * @author: villa_mou
 * @date: 09-13:46
 * @version V1.0 <描述当前版本功能>
 * @desc
 */
class QRScanActivity : BaseActivity<QRScanViewModel>() {
    override val binding: ActivityQrScanBinding by lazy {
        ActivityQrScanBinding.inflate(layoutInflater)
    }

    override fun providerVMClass(): Class<QRScanViewModel> = QRScanViewModel::class.java

    //    private var animator: ValueAnimator? = null
    private var isFlashOn = false
    override fun initView() {
        // 设置扫码回调
        binding.barCodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                if (result.text != null) {
                    Toast.makeText(
                        this@QRScanActivity, "扫描结果：" + result.text, Toast.LENGTH_LONG
                    ).show()
                    // 如果只需要扫描一次，可以停止
                    binding.barCodeView.pause()
                    finish() // 结束页面返回
                }
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
        })


        // 闪光灯开关
        binding.ivFlash.setOnClickListener { v ->
            if (isFlashOn) {
                binding.barCodeView.setTorchOff()
//                binding.ivFlash.setImageResource(R.drawable.ic_flashlight_off)
            } else {
                binding.barCodeView.setTorchOn()
//                binding.ivFlash.setImageResource(R.drawable.ic_flashlight_on)
            }
            isFlashOn = !isFlashOn
        }

    }

    override fun initData() {
    }

    override fun onPause() {
        super.onPause()
        binding.barCodeView.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.barCodeView.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}