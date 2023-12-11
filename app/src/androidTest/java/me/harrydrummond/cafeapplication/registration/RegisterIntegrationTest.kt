package me.harrydrummond.cafeapplication.registration

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import me.harrydrummond.cafeapplication.IntentExtra
import me.harrydrummond.cafeapplication.R
import me.harrydrummond.cafeapplication.data.model.Role
import me.harrydrummond.cafeapplication.ui.common.profile.CreateProfileActivity
import me.harrydrummond.cafeapplication.ui.common.register.RegisterActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterIntegrationTest {

    val intent = Intent(ApplicationProvider.getApplicationContext(), RegisterActivity::class.java).apply {
        putExtra(IntentExtra.ACCOUNT_TYPE, Role.CUSTOMER)
    }

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
     * Tests that when the correct fields are entered in the fields and register button is clicked
     * that the createprofileactivity is shown.
     */
    @Test
    fun testRegisterBtn() {
        val usernameText = "Harry"
        val passwordText = "Password123"


        onView(withId(R.id.registerEmail)).perform(typeText(usernameText), closeSoftKeyboard())
        onView(withId(R.id.registerPassword)).perform(typeText(passwordText), closeSoftKeyboard())
        onView(withId(R.id.btnAuthenticate2)).perform(click())
        intended(hasComponent(CreateProfileActivity::class.java.name))
    }
}