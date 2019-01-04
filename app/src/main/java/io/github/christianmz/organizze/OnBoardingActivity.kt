package io.github.christianmz.organizze

import android.os.Bundle
import com.heinrichreimersoftware.materialintro.app.IntroActivity

class OnBoardingActivity : IntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
    }
}
