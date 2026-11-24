package com.island.phone.feature.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

const val CardDefaultName = "我的名字"
const val CardDefaultTopic = ">添加话题"
const val CardDefaultBody = "在这里写下你想说的话"
const val CardDefaultMusic = "添加音乐"

class PhotoCardStore private constructor(private val context: Context) {
    private val prefs = context.getSharedPreferences("photo_card", Context.MODE_PRIVATE)

    var name by mutableStateOf(prefs.getString("name", CardDefaultName) ?: CardDefaultName)
        private set
    var topic by mutableStateOf(prefs.getString("topic", CardDefaultTopic) ?: CardDefaultTopic)
        private set
    var body by mutableStateOf(prefs.getString("body", CardDefaultBody) ?: CardDefaultBody)
        private set
    var music by mutableStateOf(prefs.getString("music", CardDefaultMusic) ?: CardDefaultMusic)
        private set
    var avatar by mutableStateOf<ImageBitmap?>(loadImage(AVATAR))
        private set
    var photo by mutableStateOf<ImageBitmap?>(loadImage(PHOTO))
        private set

    fun setName(value: String) {
        name = value
        prefs.edit().putString("name", value).apply()
    }

    fun setTopic(value: String) {
        topic = value
        prefs.edit().putString("topic", value).apply()
    }

    fun setBody(value: String) {
        body = value
        prefs.edit().putString("body", value).apply()
    }

    fun setMusic(value: String) {
        music = value
        prefs.edit().putString("music", value).apply()
    }

    fun reset() {
        prefs.edit().clear().apply()
        fileOf(AVATAR).delete()
        fileOf(PHOTO).delete()
        name = CardDefaultName
        topic = CardDefaultTopic
        body = CardDefaultBody
        music = CardDefaultMusic
        avatar = null
        photo = null
    }

    suspend fun importImage(key: String, uri: Uri) {
        val bitmap = withContext(Dispatchers.IO) { decodeAndSave(key, uri) }
        if (bitmap != null) {
            if (key == AVATAR) avatar = bitmap else photo = bitmap
        }
    }

    private fun decodeAndSave(key: String, uri: Uri): ImageBitmap? {
        return try {
            val resolver = context.contentResolver
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
            var sample = 1
            val longest = maxOf(bounds.outWidth, bounds.outHeight)
            while (longest / sample > MaxSide) sample *= 2
            val options = BitmapFactory.Options().apply { inSampleSize = sample }
            val decoded = resolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            } ?: return null
            val orientation = resolver.openInputStream(uri)?.use {
                ExifInterface(it).getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            } ?: ExifInterface.ORIENTATION_NORMAL
            val degrees = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
            val result = if (degrees != 0f) {
                val matrix = Matrix().apply { postRotate(degrees) }
                Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true)
            } else {
                decoded
            }
            fileOf(key).outputStream().use { result.compress(Bitmap.CompressFormat.JPEG, 90, it) }
            result.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    private fun fileOf(key: String) = File(context.filesDir, "photo_card_$key.jpg")

    private fun loadImage(key: String): ImageBitmap? {
        val file = File(context.filesDir, "photo_card_$key.jpg")
        if (!file.exists()) return null
        return try {
            BitmapFactory.decodeFile(file.path)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val AVATAR = "avatar"
        const val PHOTO = "photo"
        private const val MaxSide = 1280

        @Volatile
        private var instance: PhotoCardStore? = null

        fun get(context: Context): PhotoCardStore =
            instance ?: synchronized(this) {
                instance ?: PhotoCardStore(context.applicationContext).also { instance = it }
            }
    }
}
