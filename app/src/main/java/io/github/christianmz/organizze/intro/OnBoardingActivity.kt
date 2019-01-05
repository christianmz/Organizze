package io.github.christianmz.organizze.intro

import android.os.Bundle
import android.view.View
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import io.github.christianmz.organizze.R
import org.jetbrains.anko.startActivity

class OnBoardingActivity : IntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_onboarding)

        isButtonNextVisible = false

        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.onboarding_one)
                .build()
        )

        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.onboarding_two)
                .build()
        )

        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.onboarding_three)
                .build()
        )

        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.onboarding_four)
                .build()
        )

        addSlide(
            FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.onboarding_login)
                .canGoForward(false)
                .build()
        )

    }

    fun goToLogin(view: View) {
        startActivity<LoginActivity>()
        overridePendingTransition(
            android.R.anim.fade_in, android.R.anim.fade_out
        )
    }

    fun goToRegister(view: View) {
        startActivity<RegisterActivity>()
        overridePendingTransition(
            android.R.anim.fade_in, android.R.anim.fade_out
        )
    }
}
