package com.byandev.storyapp.presentation.stories

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.byandev.storyapp.R
import com.byandev.storyapp.databinding.ActivityStoryFormsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ActivityStoryForms : AppCompatActivity() {

    private lateinit var binding: ActivityStoryFormsBinding

    private var fileName: String = ""

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityStoryFormsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fileName = intent.getStringExtra("file") ?: ""
        Log.e("TAG", "onCreate: $fileName")

        binding.apply {

            Glide.with(imgPreview)
                .load(File(fileName))
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions().override(200, 200))
                .error(R.drawable.ic_launcher_background)
                .into(imgPreview)

            btnPostStory.setOnClickListener { postStory() }

            toolbar.setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun postStory() {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
    }

}