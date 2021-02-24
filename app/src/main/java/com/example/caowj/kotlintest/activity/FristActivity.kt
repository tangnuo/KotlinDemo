package com.example.caowj.kotlintest.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.Nullable
import android.widget.Toast
import com.example.caowj.kotlintest.R
import com.example.caowj.kotlintest.common.BaseActivity
import com.example.caowj.kotlintest.data.UserInfo
import com.example.caowj.kotlintest.util.ContentUriUtil
import com.example.caowj.kotlintest.util.FileTypeUtil
import com.example.caowj.kotlintest.util.LogUtils
import kotlinx.android.synthetic.main.activity_frist.*


class FristActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frist)

        initView()
    }


    private fun initView() {
        textView.text = "页面跳转，传值、序列化"
        button.setOnClickListener {
            val user = UserInfo("caowj", 18, 10)
            user.id = 10
            SecondActivity.mStartActivity(this, user)
        }


//        button3.text = "函数扩展"
//        button3.setOnClickListener { toast("函数扩展", Toast.LENGTH_LONG) }


        button3.text = "文件选择"
        button3.setOnClickListener { openSystemFile() }

        button4.text = "文档下载"
        button4.setOnClickListener {
//            浏览器下载文件：            https://blog.csdn.net/u011652925/article/details/83029526
            openBrowser(this,"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3819650887,2624778551&fm=26&gp=0.jpg")
            openBrowser(this,"https://yunpan.kedacom.com/lib/703d9ad5-a23f-48d5-b14d-65a984c3d9ce/file/PD10_Printer_Demo.rar")
            openBrowser(this,"https://vip.d0.baidupan.com/file/?VTMHOV5vBDUACVFpU2ZdMVtkVGxW7VbuV8QG5V2RA4oB5QbpDdAGtAXHV7dW5FP+Wr4F51apV9RSsFDKXalbvVXvB+9e5ATEAMlR4VOPXcxb51TrVplWs1fwBoFd7gO+AY0GZQ1gBrYF61erVuRT+lqhBeZWlFfqUntQLF1oWypVcwc2XmoEPAAzUVhTbl04WzxUYFY9VmxXZwY0XTIDNgE3BncNNgYhBW5XNFYzU2ZaNwU0Vj1XYVJzUCZdcVtnVWcHYF4yBGkAeVE2UzVdc1sxVGJWJlZnVzMGNl0xA2QBMgZkDWgGNgVmV2NWZ1NiWmYFZFZtVzBSbVBlXWRbb1VsBzZeNwRpADJRMlMwXWRbNFQwVjFWelcqBmldcQMkAXQGIg01BiAFOldlVj9TYFo2BTFWP1dhUmJQcF11WzNVOAc1XmUEbQBnUTFTNl1vWzhUaVY9Vm1XYAYyXS8DLAEnBjcNPAYlBW5XMFY0U2NaMwU5VjxXYVJjUGVdNVt8VSAHIF50BG0AZ1EyUzBdaVsyVGlWO1ZjV2MGN10nA3cBaAYhDW0GYwVhVzNWLFNrWjcFLlYxV2RSe1BuXTY=")
        }
    }

    /**
     * 调用第三方浏览器打开
     * @param context
     * @param url 要浏览的资源地址
     */
    fun openBrowser(context: Context, url: String?) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
        // 官方解释 : Name of the component implementing an activity that can display the intent
        if (intent.resolveActivity(context.packageManager) != null) {
            val componentName = intent.resolveActivity(context.packageManager) // 打印Log   ComponentName到底是什么
             LogUtils.d("caowj","componentName = " + componentName.className)
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"))
        } else {
            Toast.makeText(context.applicationContext, "请下载浏览器", Toast.LENGTH_SHORT).show()
        }
    }

    // 打开系统的文件选择器
    fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        this.startActivityForResult(intent, 1)
    }

    /**
     * https://blog.csdn.net/tracydragonlxy/article/details/103509238
     */
    private fun openSystemFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        // 所有类型
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择文件"), 1)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT).show()
        }
    }

    // 获取文件的真实路径
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 1 && resultCode === Activity.RESULT_OK) {
            //Get the Uri of the selected file
            val uri = data?.data
            if (null != uri) {
                val path: String = ContentUriUtil.getPath(this, uri)
                textView.text = "文件：$path"
                LogUtils.d("caowj", "后缀：" + FileTypeUtil.getFileType(path))
            }
        }
    }
}
