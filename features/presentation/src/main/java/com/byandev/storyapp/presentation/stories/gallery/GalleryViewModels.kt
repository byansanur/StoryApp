package com.byandev.storyapp.presentation.stories.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class GalleryViewModels (
    private val context: Context
) : ViewModel() {

    val imageList = MutableLiveData<MutableList<String>>()
    init {
        loadAllImagePath()
    }

    private fun loadAllImagePath() = viewModelScope.launch{
        val tempList: MutableList<String> = mutableListOf()
        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN
            ),
            null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"
        )?.also { cursor ->
            while (cursor.moveToNext()) {
                cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA).let { columnIndexData ->
                    cursor.getString(columnIndexData)?.let { data ->
                        tempList.add(data)
                    }
                }
            }
            cursor.close()
            imageList.postValue(tempList)
        }
    }
}