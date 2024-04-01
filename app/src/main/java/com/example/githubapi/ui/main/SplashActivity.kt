package com.example.githubapi.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.githubapi.ui.SettingsManager
import com.example.githubapi.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsManager = SettingsManager.getInstance(this)
        settingsManager.setSplashLogo(binding.imageviewSplash)
        if (settingsManager.getThemeSetting()) {
            settingsManager.saveThemeSetting(false)
            settingsManager.applyTheme(false)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            goToMainActivity()
        }, 2000L)
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
