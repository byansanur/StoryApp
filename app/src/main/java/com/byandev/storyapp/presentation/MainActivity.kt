package com.byandev.storyapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.byandev.storyapp.R
import com.byandev.storyapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.flContainerView) as NavHostFragment
        val navController :NavController = navHost.navController
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.e("TAG", "onDestinationChanged: "+destination.label);

            if (destination.id == R.id.fragmentHome || destination.id == R.id.mapsFragment) {
                binding.toolbar.visibility = View.VISIBLE
                setSupportActionBar(binding.toolbar)
            } else {
                binding.toolbar.visibility = View.GONE
            }
        }
    }

}