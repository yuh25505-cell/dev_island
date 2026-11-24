package com.island.phone.core.ui.appearance

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Typeface
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.island.phone.core.ui.apps.AppId
import com.island.phone.core.ui.theme.ThemeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class FontEntry(val id: String, val name: String)

class AppearanceStore private constructor(context: Context) {
    private val dir: File = context.filesDir
    private val fontsDir: File = File(context.filesDir, "appearance_fonts").also { it.mkdirs() }
    private val resolver = context.contentResolver
    private val prefs = context.getSharedPreferences("appearance_prefs", Context.MODE_PRIVATE)
    private val legacyThemePrefs = context.getSharedPreferences("island_settings", Context.MODE_PRIVATE)
    private val initialWallpaper: Bitmap? = readBitmap(wallpaperFile())

    private var pageAnimationState by mutableStateOf(prefs.getBoolean(KeyPageAnimation, true))

    val pageAnimation: Boolean
        get() = pageAnimationState

    private var themeModeState by mutableStateOf(readThemeMode())

    val themeMode: ThemeMode
        get() = themeModeState

    private val fontList = mutableStateListOf<FontEntry>()

    val fonts: List<FontEntry>
        get() = fontList

    private val emojiList = mutableStateListOf<FontEntry>()

    val emojis: List<FontEntry>
        get() = emojiList

    var activeFontId by mutableStateOf<String?>(null)
        private set

    var activeEmojiId by mutableStateOf<String?>(null)
        private set

    var fontFamily by mutableStateOf<FontFamily?>(null)
        private set

    init {
        restoreFonts()
    }

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
        get() = activeFontId != null

    val hasEmojiFont: Boolean
        get() = activeEmojiId != null

    val emojiFontName: String?
        get() = activeEmojiId?.let { id -> emojiList.firstOrNull { it.id == id }?.name }

    fun iconOf(app: AppId): ImageBitmap? = icons[app]

    fun setPageAnimation(enabled: Boolean) {
        pageAnimationState = enabled
        prefs.edit().putBoolean(KeyPageAnimation, enabled).apply()
    }

    fun setThemeMode(mode: ThemeMode) {
        themeModeState = mode
        prefs.edit().putString(KeyThemeMode, mode.name).apply()
    }

    suspend fun importFont(uri: Uri): Boolean {
        val id = System.currentTimeMillis().toString()
        if (!copyFontTo(uri, fontFile(id))) return false
        val name = withContext(Dispatchers.IO) { displayName(uri) } ?: "字体 ${fontList.size + 1}"
        fontList.add(FontEntry(id, name))
        activeFontId = id
        rebuildFamily()
        if (fontFamily == null) {
            removeFont(id)
            return false
        }
        saveFonts()
        return true
    }

    fun selectFont(id: String?) {
        activeFontId = id?.takeIf { target -> fontList.any { it.id == target } }
        rebuildFamily()
        saveFonts()
    }

    fun resetFont() {
        selectFont(null)
    }

    fun removeFont(id: String) {
        fontFile(id).delete()
        fontList.removeAll { it.id == id }
        if (activeFontId == id) activeFontId = null
        rebuildFamily()
        saveFonts()
    }

    suspend fun importEmoji(uri: Uri): Boolean {
        val id = System.currentTimeMillis().toString()
        if (!copyFontTo(uri, emojiFile(id))) return false
        val name = withContext(Dispatchers.IO) { displayName(uri) } ?: "表情字体 ${emojiList.size + 1}"
        emojiList.add(FontEntry(id, name))
        activeEmojiId = id
        rebuildFamily()
        if (fontFamily == null) {
            removeEmoji(id)
            return false
        }
        saveFonts()
        return true
    }

    fun selectEmoji(id: String?) {
        activeEmojiId = id?.takeIf { target -> emojiList.any { it.id == target } }
        rebuildFamily()
        saveFonts()
    }

    fun resetEmoji() {
        selectEmoji(null)
    }

    fun removeEmoji(id: String) {
        emojiFile(id).delete()
        emojiList.removeAll { it.id == id }
        if (activeEmojiId == id) activeEmojiId = null
        rebuildFamily()
        saveFonts()
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

    private fun oldFontFile() = File(dir, "appearance_font.ttf")

    private fun fontFile(id: String) = File(fontsDir, "font_$id.ttf")

    private fun oldEmojiFile() = File(fontsDir, "emoji.ttf")

    private fun emojiFile(id: String) = File(fontsDir, "emoji_$id.ttf")

    private fun readThemeMode(): ThemeMode {
        val saved = prefs.getString(KeyThemeMode, null)
            ?: legacyThemePrefs.getString(LegacyKeyThemeMode, null)
        return ThemeMode.entries.firstOrNull { it.name == saved } ?: ThemeMode.FOLLOW_SYSTEM
    }

    private fun displayName(uri: Uri): String? {
        return try {
            val raw = resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use {
                if (it.moveToFirst()) it.getString(0) else null
            }
            raw?.substringBeforeLast('.')?.trim()?.takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun copyFontTo(uri: Uri, target: File): Boolean = withContext(Dispatchers.IO) {
        val temp = File(fontsDir, "import.tmp")
        try {
            val input = resolver.openInputStream(uri) ?: return@withContext false
            input.use { source -> temp.outputStream().use { source.copyTo(it) } }
            if (!looksLikeFont(temp)) {
                temp.delete()
                return@withContext false
            }
            Typeface.createFromFile(temp)
            target.delete()
            temp.renameTo(target)
        } catch (e: Exception) {
            temp.delete()
            false
        }
    }

    private fun looksLikeFont(file: File): Boolean {
        val head = ByteArray(4)
        val count = file.inputStream().use { it.read(head) }
        if (count < 4) return false
        val tag = String(head, Charsets.ISO_8859_1)
        val isTrueType = head[0] == 0.toByte() && head[1] == 1.toByte() &&
            head[2] == 0.toByte() && head[3] == 0.toByte()
        return isTrueType || tag == "OTTO" || tag == "true" || tag == "ttcf"
    }

    private fun restoreFonts() {
        loadSavedFonts()
        migrateOldFont()
        migrateOldEmoji()
        rebuildFamily()
    }

    private fun readEntries(json: String?, exists: (String) -> Boolean, into: MutableList<FontEntry>) {
        if (json == null) return
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                val id = item.getString("id")
                if (exists(id)) into.add(FontEntry(id, item.getString("name")))
            }
        } catch (e: Exception) {
            into.clear()
        }
    }

    private fun loadSavedFonts() {
        readEntries(prefs.getString(KeyFonts, null), { fontFile(it).exists() }, fontList)
        val active = prefs.getString(KeyActiveFont, null)
        activeFontId = active?.takeIf { target -> fontList.any { it.id == target } }
        readEntries(prefs.getString(KeyEmojis, null), { emojiFile(it).exists() }, emojiList)
        val activeEmoji = prefs.getString(KeyActiveEmoji, null)
        activeEmojiId = activeEmoji?.takeIf { target -> emojiList.any { it.id == target } }
    }

    private fun migrateOldFont() {
        val old = oldFontFile()
        if (!old.exists() || prefs.contains(KeyFonts)) return
        val id = "legacy"
        if (old.renameTo(fontFile(id))) {
            fontList.add(FontEntry(id, "已导入字体"))
            activeFontId = id
            saveFonts()
        }
    }

    private fun migrateOldEmoji() {
        val old = oldEmojiFile()
        if (!old.exists() || prefs.contains(KeyEmojis)) return
        val id = "legacy"
        if (old.renameTo(emojiFile(id))) {
            val name = prefs.getString(KeyEmojiName, null) ?: "表情字体"
            emojiList.add(FontEntry(id, name))
            activeEmojiId = id
            saveFonts()
        }
    }

    private fun saveFonts() {
        val fontArray = JSONArray()
        fontList.forEach { fontArray.put(JSONObject().put("id", it.id).put("name", it.name)) }
        val emojiArray = JSONArray()
        emojiList.forEach { emojiArray.put(JSONObject().put("id", it.id).put("name", it.name)) }
        prefs.edit()
            .putString(KeyFonts, fontArray.toString())
            .putString(KeyActiveFont, activeFontId)
            .putString(KeyEmojis, emojiArray.toString())
            .putString(KeyActiveEmoji, activeEmojiId)
            .remove(KeyEmojiName)
            .apply()
    }

    private fun rebuildFamily() {
        fontFamily = buildFamily()
    }

    private fun buildFamily(): FontFamily? {
        val text = activeFontId?.let { fontFile(it) }?.takeIf { it.exists() }
        val emoji = activeEmojiId?.let { emojiFile(it) }?.takeIf { it.exists() }
        if (text == null && emoji == null) return null
        return try {
            if (Build.VERSION.SDK_INT >= 29) {
                FontFamily(buildTypeface(text, emoji))
            } else if (text != null) {
                Typeface.createFromFile(text)
                FontFamily(Font(text))
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    @TargetApi(29)
    private fun buildTypeface(text: File?, emoji: File?): Typeface {
        val primary = text ?: emoji!!
        val builder = Typeface.CustomFallbackBuilder(systemFamilyOf(primary))
        if (text != null && emoji != null) builder.addCustomFallback(systemFamilyOf(emoji))
        builder.setSystemFallback("sans-serif")
        return builder.build()
    }

    @TargetApi(29)
    private fun systemFamilyOf(file: File): android.graphics.fonts.FontFamily =
        android.graphics.fonts.FontFamily.Builder(
            android.graphics.fonts.Font.Builder(file).build()
        ).build()

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
        private const val KeyThemeMode = "theme_mode"
        private const val LegacyKeyThemeMode = "theme_mode"
        private const val KeyFonts = "fonts_json"
        private const val KeyActiveFont = "font_active"
        private const val KeyEmojis = "emojis_json"
        private const val KeyActiveEmoji = "emoji_active"
        private const val KeyEmojiName = "emoji_font_name"

        @Volatile
        private var instance: AppearanceStore? = null

        fun get(context: Context): AppearanceStore =
            instance ?: synchronized(this) {
                instance ?: AppearanceStore(context.applicationContext).also { instance = it }
            }
    }
}
