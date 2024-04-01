package com.example.githubapi.ui

import android.content.Context
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatDelegate
import android.view.Menu
import android.widget.ImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import com.example.githubapi.R

class SettingsManager private constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("theme_pref", Context.MODE_PRIVATE)
    private var toolbarLogo: ImageView? = null

    companion object {
        @Volatile
        private var instance: SettingsManager? = null

        fun getInstance(context: Context): SettingsManager {
            return instance ?: synchronized(this) {
                instance ?: SettingsManager(context).also { instance = it }
            }
        }
    }

    fun saveThemeSetting(isDarkTheme: Boolean) {
        sharedPreferences.edit().putBoolean("theme_dark", isDarkTheme).apply()
    }

    fun getThemeSetting(): Boolean {
        return sharedPreferences.getBoolean("theme_dark", false)
    }

    fun applyTheme(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        updateToolbarLogo(isDarkTheme)
    }

    fun updateFavoriteIcon(menu: Menu, isDarkTheme: Boolean) {
        val favoriteMenuItem = menu.findItem(R.id.favorite_menu)
        val iconResId = if (isDarkTheme) {
            R.drawable.baseline_favorite_white_24
        } else {
            R.drawable.baseline_favorite_24
        }
        favoriteMenuItem.setIcon(iconResId)
    }

    fun setThemeSwitchListener(themeSwitch: SwitchCompat) {
        themeSwitch.isChecked = getThemeSetting()

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveThemeSetting(isChecked)
            applyTheme(isChecked)
        }
    }

    fun setToolbarLogo(imageView: ImageView) {
        toolbarLogo = imageView
    }

    private fun updateToolbarLogo(isDarkTheme: Boolean) {
        val logoResId = if (isDarkTheme) {
            R.drawable.github_logo_white
        } else {
            R.drawable.github_logo_black
        }
        toolbarLogo?.setImageResource(logoResId)
    }

    fun getTabTextColors(context: Context): ColorStateList {
        val textColor = if (getThemeSetting()) {
            ContextCompat.getColor(context, R.color.colorOnPrimary)
        } else {
            ContextCompat.getColor(context, R.color.colorOnPrimaryLight)
        }
        return ColorStateList.valueOf(textColor)
    }

    fun getTabIndicatorColor(context: Context): Int {
        return getTabTextColors(context).defaultColor
    }

    fun getFavoriteButtonDrawableRes(): Int {
        return if (getThemeSetting()) {
            R.drawable.baseline_favorite_border_white_24
        } else {
            R.drawable.baseline_favorite_border_24
        }
    }

    fun setSplashLogo(imageView: ImageView) {
        val isDarkTheme = getThemeSetting()
        val logoResId = if (isDarkTheme) {
            R.drawable.github_logo_white
        } else {
            R.drawable.github_logo_black
        }
        imageView.setImageResource(logoResId)
    }
}
