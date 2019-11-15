package com.example.caowj.kotlintest.kedacom.adapter

import android.app.Activity
import com.example.caowj.kotlintest.R

/**
 * <pre>
 *     作者：Caowj
 *     邮箱：caoweijian@kedacom.com
 *     日期：2019/11/15 14:59
 * </pre>
 */

class LibTestAdapter(context: Activity) : SimpleTextAdapter(context) {
    init {
        // utilities resource
        mFunctionList.add(context.getString(R.string.system_util))
//        mFunctionList.add(context.getString(R.string.date_time_util))
//        mFunctionList.add(context.getString(R.string.file_util))
//        mFunctionList.add(context.getString(R.string.digest_util))
//        mFunctionList.add(context.getString(R.string.codec_util))


        mFunctionList.add(context.getString(R.string.native_bar))


    }
}