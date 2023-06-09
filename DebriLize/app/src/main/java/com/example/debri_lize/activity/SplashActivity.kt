package com.example.debri_lize.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.example.debri_lize.R
import com.example.debri_lize.activity.auth.LoginActivity
import com.example.debri_lize.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashAnimation()
    }

    @UiThread
    private fun splashAnimation(){
        val imageAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash_logo_img)
        binding.splashImg.startAnimation(imageAnim)
        val textAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash_logo_text)
        binding.splashText.startAnimation(textAnim)

        textAnim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })
    }
}