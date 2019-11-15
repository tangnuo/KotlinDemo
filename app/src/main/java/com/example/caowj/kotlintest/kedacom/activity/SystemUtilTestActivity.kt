package com.example.caowj.kotlintest.kedacom.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.kedacom.adapter.SimpleTextAdapter
import com.example.caowj.kotlintest.kedacom.adapter.SystemUtilFunctionAdapter
import com.kedacom.util.SystemUtil

/**
 * SystemUtil测试页面
 */
class SystemUtilTestActivity : AppCompatActivity(), SimpleTextAdapter.OnItemClickListener {

    private lateinit var mListFunctions: RecyclerView
    private lateinit var mTextInfo: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_util_test)

        mListFunctions = findViewById(R.id.list_test)
        mTextInfo = findViewById(R.id.text_info)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val adapter = SystemUtilFunctionAdapter(this)
        mListFunctions.layoutManager = GridLayoutManager(this, 2)
        mListFunctions.adapter = adapter

        adapter.listener = this

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), 1000)
        }
    }

    override fun onItemClick(function: String) {
        when (function) {
            getString(R.string.os_ip) -> {
                val appender = StringBuilder()
                SystemUtil.getIPs(this)?.forEach {
                    appender.append(it).append("\n")
                }
                showResultOfFunction("IPs: $appender")
            }

            getString(R.string.os_serial) -> {
                showResultOfFunction("Serial Number: ${SystemUtil.getSerial()}")
            }

            getString(R.string.os_density_dpi) -> showResultOfFunction("density: ${SystemUtil.density}\ndensityDpi: ${SystemUtil.densityDpi}")
            getString(R.string.os_height_actionbar) -> showResultOfFunction("ActionBarHeight: ${SystemUtil.getActionBarHeight(this)}")
            getString(R.string.os_height_statusbar) -> showResultOfFunction("StatusBarHeight: ${SystemUtil.getStatusBarHeight(this)}")
            getString(R.string.os_manufacturer) -> showResultOfFunction("Manufacturer: ${SystemUtil.getManufacturer()}")
            getString(R.string.os_imei) -> {
                val imeis = SystemUtil.getIMEI(this)
                val appender = StringBuilder()
                imeis.forEachIndexed { index, s ->
                    appender.append(s).append(if (index == imeis.size - 1) "" else "\n")
                }

                showResultOfFunction("IMEIs: $appender")
            }
            getString(R.string.os_meid) -> {
                val meid = SystemUtil.getMEID(this)
                val appender = StringBuilder()
                meid.forEachIndexed { index, s ->
                    appender.append(s).append(if (index == meid.size - 1) "" else "\n")
                }
                showResultOfFunction("MEID: $appender")
            }
            getString(R.string.os_pixels) -> {
                val dimens = SystemUtil.getScreenPixels()
                showResultOfFunction("(width, height) = (${dimens[0]}, ${dimens[1]})")
            }
            getString(R.string.os_device) -> showResultOfFunction("Device Name: ${SystemUtil.getDeviceName()}")
            getString(R.string.os_model) -> showResultOfFunction("Model: ${SystemUtil.getModel()}")
            getString(R.string.os_radio_version) -> showResultOfFunction("Radio Version: ${SystemUtil.getRadioVersion()}")
            getString(R.string.os_release) -> showResultOfFunction("OS Version: ${SystemUtil.getRelease()}")
            getString(R.string.os_display) -> showResultOfFunction("Display: ${SystemUtil.getVersionName()}")
        }
    }

    private fun showResultOfFunction(result: String) {
        mTextInfo.text = result
    }

}