package com.baymax104.chatapp.utils

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.Date

/**
 * 相机、相册工具类
 */
object CameraUtil {
    /**
     * 启动相机
     * @param file 照片文件File对象
     * @return 相机启动intent
     */
    @JvmStatic
    fun startCamera(file: File?): Intent? {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (file != null) {
            val uri = UriUtils.file2Uri(file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            return intent
        }
        return null
    }

    /**
     * 在/storage/emulated/0/Android/data/package/files目录下新建文件
     * @param fileDir 是否在files目录下，默认在cache目录下
     * @return 新建文件
     */
    @JvmStatic
    fun createNewFile(fileDir: Boolean): File? {
        val filename = DateDetailFormatter.format(Date())
        val parent =
            if (fileDir) PathUtils.getExternalAppFilesPath() else PathUtils.getExternalAppCachePath()
        if (parent == "") {
            return null
        }
        val file = File(parent, "$filename.jpg")
        file.createNewFile()
        return file
    }

    /**
     * 启动相册
     * @return 启动相册Intent
     */
    @JvmStatic
    fun startPhotoPicker(): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        return intent
    }

    /**
     * 将File对象编码为Base64编码字符串
     * @param file File对象
     * @return Base64字符串
     */
    @JvmStatic
    fun file2Base64(file: File?): String? {
        val bytes = FileIOUtils.readFile2BytesByStream(file) ?: return null
        return EncodeUtils.base64Encode2String(bytes)
    }

    /**
     * 将Base64转换为文件，文件将在files目录下创建，见[createNewFile]
     * @see base64ToFile
     * @param base64
     * @return File对象
     */
    @JvmStatic
    fun base64ToFile(base64: String?): File? {
        val bytes = EncodeUtils.base64Decode(base64)
        if (bytes.isEmpty()) {
            return null
        }
        val file = createNewFile(true)
        return if (FileIOUtils.writeFileFromBytesByStream(file, bytes)) {
            file
        } else null
    }

    /**
     * 图片压缩
     * @param context context
     * @param data 压缩File
     * @param callback 成功回调
     */
    fun compress(context: Context, data: File, callback: (File) -> Unit) {
        val dir = PathUtils.getExternalAppFilesPath()

        Luban.with(context)
            .load(data)
            .ignoreBy(80)
            .setTargetDir(dir)
            .setCompressListener(object : OnCompressListener {
                override fun onStart() {}
                override fun onSuccess(file: File) {
                    callback(file)
                }

                override fun onError(throwable: Throwable) {
                    ToastUtils.showShort("图片压缩失败：" + throwable.message)
                    callback(data)
                }
            })
            .launch()

    }

    /**
     * 将Byte数组转换为File
     * @param byteArray Byte数组
     * @return [File]
     */
    fun bytesToFile(byteArray: ByteArray, fileDir: Boolean = false): File? {
        val file = createNewFile(fileDir) ?: return null
        return if (FileIOUtils.writeFileFromBytesByStream(file, byteArray)) {
            file
        } else null
    }
}