package com.example.mercadogil.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mercadogil.databinding.ActivityWelcomeBinding
import com.example.mercadogil.ui.main.MainActivity
import com.example.mercadogil.utils.Constants

class WelcomeActivity : AppCompatActivity() {
    companion object {
        val TAG = WelcomeActivity::class.simpleName
    }

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnStartUse.setOnClickListener {
            setNewUserStatusToFalse()
            startMainActivity()
        }
    }

    private fun setNewUserStatusToFalse() {
        val sharedPref = getSharedPreferences(Constants.MY_SHARED_PREF, MODE_PRIVATE)
        sharedPref.edit().putBoolean(Constants.IS_NEW_USER, false).apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}