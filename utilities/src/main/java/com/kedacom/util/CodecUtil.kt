package com.kedacom.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import android.util.Base64InputStream
import android.util.Base64OutputStream
import android.util.Log
import com.kedacom.util.file.IOUtil
import org.apache.commons.validator.routines.UrlValidator
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

/**
 * 编解码工具类——Base64等。
 */
object CodecUtil {

    /**
     * 合法的协议头
     */
    @JvmStatic
    private val VALID_SCHEMES = arrayOf("http", "https", "file", "ftp")

    /**
     * 用Base64编码从[InputStream]中读取的内容。
     *
     * @param input 从中读取内容的[InputStream]，不能null
     *
     * @return 编码后的字符串
     * @throws IOException 发生IO错误
     */
    @Throws(IOException::class)
    @JvmStatic
    fun base64Encode(input: InputStream): String {
        IOUtil.toBufferedInputStream(input, 1024 * 512).use { bis ->
            val buffer = ByteArray(4 * 1024)
            val baos = ByteArrayOutputStream()
            Base64OutputStream(baos, Base64.NO_WRAP or Base64.URL_SAFE).use { base64Stream ->
                var total = 0
                var count = bis.read(buffer)
                while (count > -1) {
                    total += count
                    base64Stream.write(buffer, 0, count)
                    count = bis.read(buffer)
                }
                base64Stream.write(buffer, 0, 1)
                base64Stream.flush()
                val base64Ret = baos.toByteArray()
                baos.close()
                return String(base64Ret)
            }
        }
    }

    /**
     * Base64编码文件。
     *
     * @param file 使用Base64编码的文件对象，不能null
     *
     * @return 编码后的字符串
     * @throws IOException 发生IO错误
     * @throws FileNotFoundException 文件不存在
     * @throws IllegalArgumentException 文件无法读取
     */
    @Throws(IOException::class, FileNotFoundException::class, IllegalArgumentException::class)
    @JvmStatic
    fun base64Encode(file: File): String {
        if (!file.exists()) {
            throw FileNotFoundException("$file 找不到指定文件！")
        }
        require(file.canRead()) { "文件无法读取！" }
        return base64Encode(FileInputStream(file))
    }

    /**
     * 将String字符串进行Base64编码。
     *
     * @param input 需要编码的字符串内容，不能为null
     *
     * @return 编码后的字符串
     */
    @JvmStatic
    fun base64Encode(input: String): String {
        return String(Base64.encode(input.trim().toByteArray(Charset.defaultCharset()), Base64.NO_WRAP or Base64.URL_SAFE))
    }

    /**
     * 对URL进行Base64编码。
     *
     * @param url 要编码的URL地址，不能为null
     *
     * @return 编码后的URL地址
     * @throws  MalformedURLException URL基本格式不准确
     * @throws IOException 发生IO错误
     */
    @Throws(MalformedURLException::class, IOException::class)
    @JvmStatic
    fun base64Encode(url: URL): String {
        val validator = UrlValidator(VALID_SCHEMES)
        try {
            if (validator.isValid(url.toString())) {
                return base64Encode(url.toString())
            }
        } catch (e: Exception) {
            return ""
        }
        return ""
    }

    /**
     * 将bitmap进行编码。
     *
     * @param bitmap 进行编码的Bitmap，不能是null
     *
     * @return 编码后返回的字符串
     * @throws IOException 发生IO错误
     */
    @Throws(IOException::class)
    @JvmStatic
    fun base64Encode(bitmap: Bitmap): String {
        val bitmapOS = ByteArrayOutputStream()
        if (bitmap.compress(Bitmap.CompressFormat.WEBP, 100, bitmapOS)) {
            bitmapOS.use { baos ->
                ByteArrayOutputStream().use { retOS ->
                    Base64OutputStream(retOS, Base64.NO_WRAP or Base64.URL_SAFE).use { base64OS ->
                        IOUtil.writeChunked(baos.toByteArray(), base64OS)
                        val retArray = retOS.toByteArray()
                        return String(retArray)
                    }
                }
            }
        }
        return ""
    }

    /**
     * 将Base64的编码值解码为图片(Bitmap)。
     *
     * @param code 编码内容，不能是null，也不可以是空串
     *
     * @return 解码后的图片对象
     * @throws IOException 发生IO错误
     * @throws IllegalArgumentException 编码内容为空字符串
     */
    @Throws(IOException::class, IllegalArgumentException::class)
    @JvmStatic
    fun base64DecodeBitmap(code: String): Bitmap {
        require(!TextUtils.isEmpty(code)) { "编码内容不可以是空字符串" }
        ByteArrayInputStream(code.toByteArray(Charset.defaultCharset())).use { bais ->
            Base64InputStream(bais, Base64.NO_WRAP or Base64.URL_SAFE).use { base64Stream ->
                IOUtil.toBufferedInputStream(base64Stream, 1 * 1024 * 1024).use { bufferedIS ->
                    val buffer = ByteArray(1024 * 512)
                    val byteArray = arrayListOf<Byte>()
                    var count = IOUtil.read(bufferedIS, buffer)
                    while (count > 0) {
                        byteArray.addAll(buffer.toList())
                        count = IOUtil.read(bufferedIS, buffer)
                    }

                    return BitmapFactory.decodeByteArray(byteArray.toByteArray(), 0, byteArray.size)
                }
            }
        }
    }

    /**
     * 将Base64的编码值解码为图片(Bitmap)，并将图片输出到指定图片文件。
     *
     * @param code 编码内容，不能是null，也不可以是空串
     * @param dest 目标图片文件，不能为null
     *
     * @return 解码后的图片对象
     * @throws IOException 发生IO错误
     * @throws IllegalArgumentException 编码内容为空字符串
     */
    @Throws(IllegalArgumentException::class)
    @JvmStatic
    fun base64DecodeBitmapToFile(code: String, dest: File): File? {
        val parent = File(dest.parent)
        if (!parent.exists() && !parent.mkdirs()) {
            Log.e("CodecUtil", "$parent 目录创建失败！")
            return null
        }

        if (!dest.exists() && !dest.createNewFile()) {
            require(dest.exists()) { "无法创建文件！" }
        }
        require(dest.canWrite()) { "无法写文件！" }
        require(!TextUtils.isEmpty(code)) { "图片编码内容不能为空字符串！" }

        try {
            ByteArrayInputStream(code.toByteArray(Charsets.UTF_8)).use { bais ->
                Base64InputStream(bais, Base64.NO_WRAP or Base64.URL_SAFE).use { base64IS ->
                    IOUtil.toBufferedInputStream(base64IS).use { bufferIS ->
                        FileOutputStream(dest, true).use { fos ->
                            BufferedOutputStream(fos, 512 * 1024).use { bos ->
                                val buffer = ByteArray(1 * 1024 * 1024)
                                var count = IOUtil.read(bufferIS, buffer)
                                while (count > 0) {
                                    bos.write(buffer, 0, count)
                                    count = IOUtil.read(bufferIS, buffer)
                                }
                                bos.flush()
                                return dest
                            }
                        }
                    }
                }
            }
        } catch (err: IOException) {
            return null
        }
    }

    /**
     * 对code内容进行Base64解码操作。
     *
     * @param code Bse64编码内容
     *
     * @return 解码后内容
     */
    @JvmStatic
    fun base64DecodeString(code: String): String {
        return String(Base64.decode(code, Base64.NO_WRAP or Base64.URL_SAFE))
    }

    /**
     * 将编码内容解码为URL，若解码后的URL非法，返回null。
     *
     * @param code 编码的内容
     *
     * @return 合法的URL对象，若解码后url非法，返回null
     */
    @Throws(MalformedURLException::class)
    @JvmStatic
    fun base64DecodeURL(code: String): URL? {
        val decoded = base64DecodeString(code)
        return if (UrlValidator(VALID_SCHEMES).isValid(decoded)) URL(decoded) else null
    }

    /**
     * 将code进行解码，并将解码后的内容写入到指定文件。
     *
     * @param code 需要解码的内容，不能是null
     * @param dest 写入解码内容的文件，不能是null
     *
     * @return 准确写入解码内容的文件对象
     * @throws FileNotFoundException  文件创建失败
     * @throws IllegalArgumentException 无法写文件
     * @throws IOException 发生IO错误
     */
    @Throws(FileNotFoundException::class, IllegalArgumentException::class, IOException::class)
    @JvmStatic
    fun base64DecodeFile(code: String, dest: File): File? {
        if (!dest.exists()) {
            var count = 0
            while (!dest.createNewFile() && count < 3) {
                count++
            }
            if (!dest.exists()) {
                throw FileNotFoundException("无法创建指定文件！")
            }
            dest.setWritable(true)
        }
        require(dest.canWrite()) { "文件无法写入！" }
        FileOutputStream(dest).use { fos ->
            OutputStreamWriter(fos).use { osw ->
                val decoded = base64DecodeString(code)
                IOUtil.write(decoded, osw)
                return dest
            }
        }
    }

    /**
     * 读取编码内容的输入流，将内容解码并返回。
     *
     * @param input 从中读取编码的InputStream
     */
    @Throws(IOException::class)
    @JvmStatic
    fun base64DecodeInputStream(input: InputStream): String {
        Base64InputStream(input, Base64.NO_WRAP or Base64.URL_SAFE).use { base64Stream ->
            IOUtil.toBufferedInputStream(base64Stream, 1 * 1024 * 1024).use { bis ->
                val mediateList = mutableListOf<Byte>()
                val buffer = ByteArray(512 * 1024)
                var count = IOUtil.read(bis, buffer)
                while (count > 0) {
                    mediateList.addAll(buffer.toList())
                    count = IOUtil.read(bis, buffer)
                }
                val retArray = mediateList.toByteArray()
                return String(retArray)
            }
        }
    }
}