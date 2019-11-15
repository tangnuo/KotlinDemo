package com.example.caowj.kotlintest.kedacom.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.kedacom.adapter.LibTestAdapter
import com.example.caowj.kotlintest.kedacom.adapter.SimpleTextAdapter

/**
 * Android技术部提取的公共lib
 */
class LibTestActivity : AppCompatActivity(), SimpleTextAdapter.OnItemClickListener {
    private lateinit var mListFunctions: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib_test)

        mListFunctions = findViewById(R.id.list_function_entry)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val adapter = LibTestAdapter(this)
        mListFunctions.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mListFunctions.adapter = adapter

        adapter.listener = this

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), 1000)
        }
    }

    override fun onItemClick(function: String) {
        when (function) {
            //util
            getString(R.string.system_util) -> gotoTarget(SystemUtilTestActivity::class.java)
//            getString(R.string.date_time_util) -> gotoTarget(DateTimeUtilTestActivity::class.java)
//            getString(R.string.file_util) -> gotoTarget(FileUtilTestActivity::class.java)
//            getString(R.string.digest_util) -> gotoTarget(DigestUtilTestActivity::class.java)
//            getString(R.string.codec_util) -> gotoTarget(CodecUtilTestActivity::class.java)
            getString(R.string.native_bar) -> gotoTarget(UniformNativeBarActivity::class.java)
        }
    }

    private fun gotoTarget(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }
}
