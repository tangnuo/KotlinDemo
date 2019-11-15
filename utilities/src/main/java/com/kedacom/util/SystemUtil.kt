package com.kedacom.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import android.util.TypedValue
import java.net.NetworkInterface


/**
 * 平台有关信息，例如：density，分辨率等信息。
 * 包含：
 * 1. 密度density；
 * 2. Android平台ActionBar高度；
 * 3. Android平台状态栏(StatusBar)高度；
 */
object SystemUtil {
    /**
     * 密度比
     */
    @JvmField
    val density = Resources.getSystem().displayMetrics.density

    @JvmField
    val densityDpi = Resources.getSystem().displayMetrics.densityDpi

    /**
     * 获取系统版本名——用户可见的系统版本。例如：P2-V0.46。
     */
    @JvmStatic
    fun getVersionName(): String {
        return Build.DISPLAY
    }

    /**
     * 获取系统Android版本，例如： Android 9。
     *
     * @return 设备使用的Android版本
     */
    @JvmStatic
    fun getRelease(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * 基带版本
     *
     * @return 基带版本
     */
    @JvmStatic
    fun getRadioVersion(): String {
        return Build.getRadioVersion()
    }

    /**
     * 终端用户型号，例如：P2。
     *
     * @return 获取终端用户型号
     */
    @JvmStatic
    fun getModel(): String {
        return Build.MODEL
    }

    /**
     * 获取设备名，例如：ptwo。
     *
     * @return 设备名
     */
    @JvmStatic
    fun getDeviceName(): String {
        return Build.DEVICE
    }

    /**
     * 获取设备屏幕分辨率。
     * 宽度高度相对于横竖屏有关。
     *
     * 例如：1080p手机，竖屏：width=1080, height=1920
     *                  横屏：width=1920，height=1080
     *
     * @return 返回大小为2的数组，索引0为宽度，1为高度
     */
    @JvmStatic
    fun getScreenPixels(): Array<Int?> {
        val dimension = arrayOfNulls<Int>(2)
        val displayMetrics = Resources.getSystem().displayMetrics
        dimension[0] = displayMetrics.widthPixels
        dimension[1] = displayMetrics.heightPixels
        return dimension
    }

    /**
     * 获取设备IMEI。设备若有两个或多个卡槽，IMEI可能有多个。
     *
     * @param context 上下文对象
     */
    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getIMEI(context: Context): Array<String?> {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // api >= 23
            val phoneCount = telephonyManager.phoneCount // api >= 23
            println("count of phone: $phoneCount")
            if (phoneCount == 0) {
                return emptyArray()
            }
            val imeiArray = arrayOfNulls<String>(phoneCount)
            println("phoneType: ${telephonyManager.phoneType}")
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                val listImei = arrayListOf<String>()
                val versionInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) "Equal and Above Android 8.0" else "Blow Android 8.0"
                println(versionInfo)
                for (slot in 0 until phoneCount) {
                    val valImei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        telephonyManager.getImei(slot)
                    } else {
                        @Suppress("DEPRECATION")
                        telephonyManager.getDeviceId(slot)
                    }
                    listImei.add(valImei)
                }
                listImei.toArray(imeiArray)
            } else {
                Log.w("SystemUtil", "Permission READ_PHONE_STATE is not granted!!!")
            }
            imeiArray
        } else {
            println("Blow Android 6.0")
            val imeiArray = arrayOfNulls<String>(1)
            @Suppress("DEPRECATION")
            imeiArray[0] = telephonyManager.deviceId
            imeiArray
        }
    }

    /**
     * 获取MEID(Mobile Equipment Identifier)值——移动设备识别码
     *
     * 此方法需要在Android 8.0设备上运行
     *
     * @param context 上下文对象
     */
    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getMEID(context: Context): Array<String?> {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // api >= 26
            val phoneCount = telephonyManager.phoneCount
            println("count of phone: $phoneCount")
            if (phoneCount == 0) {
                return emptyArray()
            }
            val meidArray = arrayOfNulls<String>(phoneCount)
            println("phoneType: ${telephonyManager.phoneType}")
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                val versionInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) "Equal and Above Android 8.0" else "Blow Android 8.0"
                println(versionInfo)
                val listMeid = arrayListOf<String>()
                for (slot in 0 until phoneCount) {
                    listMeid.add(telephonyManager.getMeid(slot))
                }
                listMeid.toArray(meidArray)
            } else {
                Log.w("SystemUtil", "Permission READ_PHONE_STATE is not granted!!!")
            }
            meidArray
        } else emptyArray()
    }

    /**
     * 获取制造商信息。
     *
     * @return 制造商信息
     */
    @JvmStatic
    fun getManufacturer(): String {
        return Build.MANUFACTURER
    }

    /**
     * 获取序列号
     *
     * @return 序列号
     */
    @Suppress("DEPRECATION")
    @SuppressLint("HardwareIds", "MissingPermission")
    @JvmStatic
    fun getSerial(): String {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) Build.SERIAL else Build.getSerial()
    }

    /**
     * 获取设备IP地址。
     *
     * @param context 上下文对象
     * @return 返回IP地址数组，索引0位置标示IPv6表示，索引1位置为IPv4表示。若无网络返回null
     */
    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getIPs(context: Context): Array<String?>? {
        val connMgr = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> { // 6.0 getActiveNetwork
                val lp = connMgr.getLinkProperties(connMgr.activeNetwork)
                return formatIpAddresses(lp)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val networks = connMgr.allNetworks
                networks.forEach { network ->
                    val info = connMgr.getNetworkInfo(network)
                    if (info.isAvailable && info.isConnected) {
                        val lp = connMgr.getLinkProperties(network)
                        return formatIpAddresses(lp)
                    }
                }
            }

            else -> {
                val networks = NetworkInterface.getNetworkInterfaces().toList()
                return formatIpAddresses(networks)
            }
        }
        return null
    }

    /**
     * 格式化IP地址，此方法在API 21以下调用。
     *
     * @param networks 获取NetworkInterface列表
     *
     * @return 获取格式化后的IP地址
     */
    @JvmStatic
    private fun formatIpAddresses(networks: List<NetworkInterface>): Array<String?>? {
        val ipList = mutableListOf<String?>()
        networks.forEach { network ->
            val netAddresses = network.inetAddresses.toList()
            netAddresses.forEach flagAddresses@{ address ->
                if (!address.isLoopbackAddress) {
                    val hostIp = address.hostAddress
                    if (hostIp.indexOf("%", 0, true) != -1) {
                        ipList.add(hostIp.split("%")[0])
                        return@flagAddresses
                    }
                    ipList.add(hostIp)
                }
            }
        }
        return ipList.toTypedArray()
    }

    /**
     * 格式化IP地址，此方法在API 21(含21)以上调用。
     *
     * @param prop 链接属性
     *
     * @return 获取格式化后的IP地址
     */
    @JvmStatic
    private fun formatIpAddresses(prop: LinkProperties?): Array<String?>? {
        return prop?.let {
            val ipAddressArray = mutableListOf<String?>()
            val iter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.linkAddresses.iterator()
            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }
            // If there are no entries, return null
            if (!iter.hasNext()) return null
            while (iter.hasNext()) {
                ipAddressArray.add(iter.next().address.hostAddress)
            }
            ipAddressArray.toTypedArray()
        }
    }

    /**
     * 获取平台的ActionBar的高度。
     *
     * @return 平台ActionBar高度
     */
    @JvmStatic
    fun getActionBarHeight(context: Context): Int {
        var actionBarHeight = 0
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue
                    .complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        }
        return actionBarHeight
    }

    /**
     * 获取状态栏高度。
     *
     * @param context 上下文环境变量
     * @return StatusBar高度
     */
    @JvmStatic
    fun getStatusBarHeight(context: Context): Int {
        val res = context.resources
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) res.getDimensionPixelSize(resourceId) else 0
    }

    /**
     * px转dp
     */
    @JvmStatic
    fun px2dp(pxValue: Float): Int {
        val scale = density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * dp转为px
     *
     * @param dipValue dp数值
     */
    @JvmStatic
    fun dp2px(dipValue: Float): Int {
        val scale = density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * px转sp
     */
    @JvmStatic
    fun px2sp(pxValue: Float): Int {
        val fontScale = density
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * sp转px
     */
    @JvmStatic
    fun sp2px(spValue: Float): Int {
        val fontScale = density
        return (spValue * fontScale + 0.5f).toInt()
    }
}
