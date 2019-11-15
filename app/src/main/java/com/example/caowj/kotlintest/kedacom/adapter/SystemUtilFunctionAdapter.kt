package com.example.caowj.kotlintest.kedacom.adapter

import android.app.Activity
import com.example.caowj.kotlintest.R

class SystemUtilFunctionAdapter(context: Activity) : SimpleTextAdapter(context) {

    init {
        mFunctionList.add(context.getString(R.string.os_ip))
        mFunctionList.add(context.getString(R.string.os_serial))
        mFunctionList.add(context.getString(R.string.os_density_dpi))
        mFunctionList.add(context.getString(R.string.os_height_actionbar))
        mFunctionList.add(context.getString(R.string.os_height_statusbar))
        mFunctionList.add(context.getString(R.string.os_manufacturer))
        mFunctionList.add(context.getString(R.string.os_imei))
        mFunctionList.add(context.getString(R.string.os_meid))
        mFunctionList.add(context.getString(R.string.os_pixels))
        mFunctionList.add(context.getString(R.string.os_device))
        mFunctionList.add(context.getString(R.string.os_model))
        mFunctionList.add(context.getString(R.string.os_radio_version))
        mFunctionList.add(context.getString(R.string.os_release))
        mFunctionList.add(context.getString(R.string.os_display))
    }
}
