package me.harrydrummond.cafeapplication.appmenu

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.ui.AppActivity
import me.harrydrummond.cafeapplication.ui.common.profile.CreateProfileActivity
import me.harrydrummond.cafeapplication.ui.common.register.RegisterActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class AppMenuTest {

    val intent = Intent(ApplicationProvider.getApplicationContext(), AppActivity::class.java)

    @get:Rule
    val activityRule = ActivityScenarioRule<RegisterActivity>(intent)

    @Before
    fun setUp() {
        // Initialize Intents before the test
        Intents.init()
    }

    @After
    fun tearDown() {
        // Release Intents resources after the test
        Intents.release()
    }

    /**
     * Tests the order use case behaves as expected
     */
    @Test
    fun testMenuIsDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.c_menu)) // Tap on menu button
            .perform(ViewActions.click())
        // Test button is displayed
        Espresso.onView(ViewMatchers.withId(R.id.c_menu_fragment)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }
}