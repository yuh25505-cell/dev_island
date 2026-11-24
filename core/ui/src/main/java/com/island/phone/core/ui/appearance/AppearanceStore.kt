package com.island.phone.core.ui.appearance

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.island.phone.core.ui.apps.AppId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AppearanceStore private constructor(context: Context) {
    private val dir: File = context.filesDir
    private val resolver = context.contentResolver

    var wallpaper by mutableStateOf<ImageBitmap?>(readFile(wallpaperFile()))
        private set

    private val icons = mutableStateMapOf<AppId, ImageBitmap>()

    init {
        AppId.entries.forEach { id -> readFile(iconFile(id))?.let { icons[id] = it } }
    }

    val hasCustomIcons: Boolean
        get() = icons.isNotEmpty()

    fun iconOf(app: AppId): ImageBitmap? = icons[app]

    suspend fun importWallpaper(uri: Uri): Boolean {
        val bitmap = withContext(Dispatchers.IO) {
            try {
                val decoded = decode(uri, WallpaperMaxSide) ?: return@withContext null
                wallpaperFile().outputStream().use {
                    decoded.compress(Bitmap.CompressFormat.JPEG, 92, it)
                }
                decoded
            } catch (e: Exception) {
                null
            }
        } ?: return false
        wallpaper = bitmap.asImageBitmap()
        return true
    }

    suspend fun importIcon(app: AppId, uri: Uri): Boolean {
        val bitmap = withContext(Dispatchers.IO) {
            try {
                val decoded = decode(uri, IconDecodeSide) ?: return@withContext null
                val side = minOf(decoded.width, decoded.height)
                val square = Bitmap.createBitmap(
                    decoded,
                    (decoded.width - side) / 2,
                    (decoded.height - side) / 2,
                    side,
                    side
                )
                val out = if (side > IconMaxSide) {
                    Bitmap.createScaledBitmap(square, IconMaxSide, IconMaxSide, true)
                } else {
                    square
                }
                iconFile(app).outputStream().use {
                    out.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                out
            } catch (e: Exception) {
                null
            }
        } ?: return false
        icons[app] = bitmap.asImageBitmap()
        return true
    }

    fun resetWallpaper() {
        wallpaperFile().delete()
        wallpaper = null
    }

    fun resetIcon(app: AppId) {
        iconFile(app).delete()
        icons.remove(app)
    }

    fun resetAllIcons() {
        AppId.entries.forEach { iconFile(it).delete() }
        icons.clear()
    }

    private fun wallpaperFile() = File(dir, "appearance_wallpaper.jpg")

    private fun iconFile(app: AppId) = File(dir, "appearance_icon_${app.name}.png")

    private fun readFile(file: File): ImageBitmap? {
        if (!file.exists()) return null
        return try {
            BitmapFactory.decodeFile(file.path)?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    private fun decode(uri: Uri, maxSide: Int): Bitmap? {
        return try {
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
            var sample = 1
            val longest = maxOf(bounds.outWidth, bounds.outHeight)
            while (longest / sample > maxSide) sample *= 2
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
            if (degrees != 0f) {
                val matrix = Matrix().apply { postRotate(degrees) }
                Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true)
            } else {
                decoded
            }
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val WallpaperMaxSide = 2048
        private const val IconDecodeSide = 1024
        private const val IconMaxSide = 512

        @Volatile
        private var instance: AppearanceStore? = null

        fun get(context: Context): AppearanceStore =
            instance ?: synchronized(this) {
                instance ?: AppearanceStore(context.applicationContext).also { instance = it }
            }
    }
}
