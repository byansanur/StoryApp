package com.byandev.storyapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetImageAndroid @Inject constructor(
    private val context: Context
) {
    @SuppressLint("Recycle")
    fun getImageGallery(data: Intent): String? {
        data.data?.let { selectedImage ->
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            context.contentResolver.query(selectedImage, filePathColumn, null, null, null)?.let { cursor ->
                cursor.moveToFirst()
                cursor.getColumnIndex(filePathColumn[0]).let { columnIndex ->
                    return cursor.getString(columnIndex)
                }
            }

        }
        return null
    }
}