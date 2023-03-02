package com.example.homesweethomes2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val handle = Handler()
        handle.postDelayed({delayScreen()
        },2500)
    }

    private fun delayScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}