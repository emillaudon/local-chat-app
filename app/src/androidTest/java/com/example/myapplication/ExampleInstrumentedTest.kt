package com.example.myapplication

import android.app.Instrumentation
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.activities.LoginActivity
import com.example.myapplication.activities.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.myapplication", appContext.packageName)
    }

    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun onLaunchCheckUserNameFieldDisplayed() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.userNameTextField))
    }

    @Test
    fun onLaunchCheckRecyclerViewDisplayed() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recyclerView))
    }

    @Test
    fun onClickFabChangeActivity() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.fab))
            .perform(click())
    }

    @Test
    fun postAppearsAfterCreation() {
        ActivityScenario.launch(MainActivity::class.java)
        MainActivity.postsHandler.posts

        onView(withId(R.id.fab))
            .perform(click())
    }

    @Test
    fun onLaunchLoginAndCreatePost() {
        ActivityScenario.launch(LoginActivity::class.java)

        onView(withId(R.id.userNameTextField))
            .perform(clearText())
            .perform(typeText("TestUser"))

        onView(withId(R.id.loginBtn))
            .perform(click())


        ActivityScenario.launch(MainActivity::class.java)


        onView(withId(R.id.fab))
            .perform(click())

        onView(withId(R.id.editTextPost))
            .perform(clearText())
            .perform(typeText("Test post"))

        onView(withId(R.id.postbutton))
            .perform(click())




    }



}
