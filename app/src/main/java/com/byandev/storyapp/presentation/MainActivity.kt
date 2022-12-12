package com.byandev.storyapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.byandev.storyapp.R
import com.byandev.storyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.flContainerView) as NavHostFragment
        val navController :NavController = navHost.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.e("TAG", "onDestinationChanged: "+destination.label);

            if (destination.id == com.byandev.storyapp.presentation.R.id.fragmentHome || destination.id == com.byandev.storyapp.presentation.R.id.fragmentMaps) {
                binding.toolbar.visibility = View.VISIBLE
                setSupportActionBar(binding.toolbar)
            } else {
                binding.toolbar.visibility = View.GONE
            }
        }
    }

}