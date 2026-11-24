package com.island.phone.core.ui.appearance

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Typeface
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.island.phone.core.ui.apps.AppId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AppearanceStore private constructor(context: Context) {
    private val dir: File = context.filesDir
    private val resolver = context.contentResolver
    private val prefs = context.getSharedPreferences("appearance_prefs", Context.MODE_PRIVATE)
    private val initialWallpaper: Bitmap? = readBitmap(wallpaperFile())

    private var pageAnimationState by mutableStateOf(prefs.getBoolean(KeyPageAnimation, true))

    val pageAnimation: Boolean
        get() = pageAnimationState

    var fontFamily by mutableStateOf<FontFamily?>(loadFontFamily())
        private set

    var wallpaper by mutableStateOf<ImageBitmap?>(initialWallpaper?.asImageBitmap())
        private set

    var wallpaperLuma by mutableStateOf<Float?>(initialWallpaper?.let { lumaOf(it) })
        private set

    private val icons = mutableStateMapOf<AppId, ImageBitmap>()

    init {
        AppId.entries.forEach { id -> readFile(iconFile(id))?.let { icons[id] = it } }
    }

    val hasCustomIcons: Boolean
        get() = icons.isNotEmpty()

    val hasCustomFont: Boolean
        get() = fontFamily != null

    fun iconOf(app: AppId): ImageBitmap? = icons[app]

    fun setPageAnimation(enabled: Boolean) {
        pageAnimationState = enabled
        prefs.edit().putBoolean(KeyPageAnimation, enabled).apply()
    }

    suspend fun importFont(uri: Uri): Boolean {
        val saved = withContext(Dispatchers.IO) {
            val temp = File(dir, "appearance_font.tmp")
            try {
                val input = resolver.openInputStream(uri) ?: return@withContext false
                input.use { source -> temp.outputStream().use { source.copyTo(it) } }
                if (!looksLikeFont(temp)) {
                    temp.delete()
                    return@withContext false
                }
                Typeface.createFromFile(temp)
                fontFile().delete()
                temp.renameTo(fontFile())
            } catch (e: Exception) {
                temp.delete()
                false
            }
        }
        if (!saved) return false
        fontFamily = loadFontFamily()
        return fontFamily != null
    }

    fun resetFont() {
        fontFile().delete()
        fontFamily = null
    }

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
        wallpaperLuma = lumaOf(bitmap)
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
        wallpaperLuma = null
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

    private fun fontFile() = File(dir, "appearance_font.ttf")

    private fun looksLikeFont(file: File): Boolean {
        val head = ByteArray(4)
        val count = file.inputStream().use { it.read(head) }
        if (count < 4) return false
        val tag = String(head, Charsets.ISO_8859_1)
        val isTrueType = head[0] == 0.toByte() && head[1] == 1.toByte() &&
            head[2] == 0.toByte() && head[3] == 0.toByte()
        return isTrueType || tag == "OTTO" || tag == "true" || tag == "ttcf"
    }

    private fun loadFontFamily(): FontFamily? {
        val file = fontFile()
        if (!file.exists()) return null
        return try {
            Typeface.createFromFile(file)
            FontFamily(Font(file))
        } catch (e: Exception) {
            file.delete()
            null
        }
    }

    private fun readBitmap(file: File): Bitmap? {
        if (!file.exists()) return null
        return try {
            BitmapFactory.decodeFile(file.path)
        } catch (e: Exception) {
            null
        }
    }

    private fun readFile(file: File): ImageBitmap? = readBitmap(file)?.asImageBitmap()

    private fun lumaOf(bitmap: Bitmap): Float {
        return try {
            val small = Bitmap.createScaledBitmap(bitmap, 16, 16, true)
            var sum = 0f
            for (y in 0 until 16) {
                for (x in 0 until 16) {
                    val p = small.getPixel(x, y)
                    val r = (p shr 16) and 0xFF
                    val g = (p shr 8) and 0xFF
                    val b = p and 0xFF
                    sum += (0.299f * r + 0.587f * g + 0.114f * b) / 255f
                }
            }
            sum / 256f
        } catch (e: Exception) {
            0.5f
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
        private const val KeyPageAnimation = "page_animation"

        @Volatile
        private var instance: AppearanceStore? = null

        fun get(context: Context): AppearanceStore =
            instance ?: synchronized(this) {
                instance ?: AppearanceStore(context.applicationContext).also { instance = it }
            }
    }
}
