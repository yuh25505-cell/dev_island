package com.island.phone.feature.home

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import java.io.File

class VinylStore private constructor(private val context: Context) {
    private val file = File(context.filesDir, "vinyl_cover.jpg")

    var cover by mutableStateOf<ImageBitmap?>(LocalImage.load(file))
        private set

    suspend fun importCover(uri: Uri) {
        val bitmap = LocalImage.import(context, uri, file)
        if (bitmap != null) cover = bitmap
    }

    fun reset() {
        file.delete()
        cover = null
    }

    companion object {
        @Volatile
        private var instance: VinylStore? = null

        fun get(context: Context): VinylStore =
            instance ?: synchronized(this) {
                instance ?: VinylStore(context.applicationContext).also { instance = it }
            }
    }
}
