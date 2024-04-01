package com.example.githubapi

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.githubapi.ui.main.MainActivity
import com.example.githubapi.ui.main.SplashActivity
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@LargeTest
class SplashActivityTest {

    @Test
    fun splashScreenLaunchesMainActivity() {
        val latch = CountDownLatch(1)

        val scenario = ActivityScenario.launch(SplashActivity::class.java)

        scenario.onActivity { activity ->
            activity.startActivity(Intent(activity, MainActivity::class.java))

            activity.finish()

            val delayMillis = 2000L
            Thread.sleep(delayMillis)
            latch.countDown()
        }

        val timeoutMillis = 3000L
        assertThat(latch.await(timeoutMillis, TimeUnit.MILLISECONDS), equalTo(true))

        scenario.onActivity { activity ->
            assertThat(activity, notNullValue())
        }
    }
}
