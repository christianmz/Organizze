package io.github.christianmz.organizze.activities

import android.os.Bundle
import android.view.View
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import io.github.christianmz.organizze.R
import org.jetbrains.anko.startActivity

class OnBoardingActivity : IntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isButtonNextVisible = false
        setOnboarding()
    }

    private fun setOnboarding() {

        val layoutList = intArrayOf(
            R.layout.onboarding_one,
            R.layout.onboarding_two,
            R.layout.onboarding_three,
            R.layout.onboarding_four
        )

        for (layout in layoutList) {
            addSlide(
                FragmentSlide.Builder()
                    .background(android.R.color.white)
                    .fragment(layout)
                    .build()
            )
        }

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
        startActivity<SignUpActivity>()
        overridePendingTransition(
            android.R.anim.fade_in, android.R.anim.fade_out
        )
    }
}
