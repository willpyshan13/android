package com.owncloud.android.authentication

import android.Manifest
import android.accounts.AccountManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blikoon.qrcodescanner.QrCodeActivity
import com.nextcloud.client.account.UserAccountManager
import com.owncloud.android.R
import com.owncloud.android.utils.PermissionUtil
import kotlinx.android.synthetic.main.activity_login_layout.*
import javax.inject.Inject

/**
 * new login activity
 */

class LoginActivity : AppCompatActivity() {

    private val mAccountMgr = AccountManager.get(this)

    @Inject
    var accountManager: UserAccountManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_layout)

        iv_scan_qr.setOnClickListener { onScan() }
    }

    /**
     * 扫描二维码
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-06
     */
    private fun onScan() {
        if (PermissionUtil.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            startQRScanner()
        } else {
            PermissionUtil.requestCameraPermission(this)
        }
    }

    private fun startQRScanner() {
        val intent = Intent(this, QrCodeActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_QR_SCAN)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtil.PERMISSIONS_CAMERA) { // If request is cancelled, result arrays are empty.
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                startQRScanner()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null) {
                return
            }
            val result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult")
            if (result == null) {
                Toast.makeText(this, "QR Code could not be read!", Toast.LENGTH_SHORT).show()
                return
            }
            if (!resources.getBoolean(R.bool.multiaccount_support) && accountManager?.accounts?.size == 1) {
                Toast.makeText(this, R.string.no_mutliple_accounts_allowed, Toast.LENGTH_LONG).show()
            } else {
                login_et_server.setText(result)
            }
        }
    }

    companion object {

        const val REQUEST_CODE_QR_SCAN = 101
    }
}
