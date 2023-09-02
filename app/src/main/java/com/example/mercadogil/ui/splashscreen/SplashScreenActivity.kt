package com.example.mercadogil.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.mercadogil.databinding.ActivitySplashScreenBinding
import com.example.mercadogil.ui.main.MainActivity
import com.example.mercadogil.ui.welcome.WelcomeActivity
import com.example.mercadogil.utils.Constants

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    companion object {
        val TAG = SplashScreenActivity::class.simpleName
        private const val SPLASH_SCREEN_TIME_OUT = 3000L
    }

    private lateinit var binding: ActivitySplashScreenBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullscreen()
        goToNextActivityAfterTimeOut()
    }

    private fun setFullscreen() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController

            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    private fun goToNextActivityAfterTimeOut() {
        val intent = getNextActivityIntent()
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN_TIME_OUT)
    }

    private fun getNextActivityIntent(): Intent {
        val sharedPref = getSharedPreferences(Constants.MY_SHARED_PREF, MODE_PRIVATE)
        val isNewUser = sharedPref.getBoolean(Constants.IS_NEW_USER, true)

        return if (isNewUser) {
            Intent(this, WelcomeActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
    }
}