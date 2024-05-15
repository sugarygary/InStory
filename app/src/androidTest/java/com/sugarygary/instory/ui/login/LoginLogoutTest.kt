package com.sugarygary.instory.ui.login

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.sugarygary.instory.R
import com.sugarygary.instory.launchFragmentInHiltContainer
import com.sugarygary.instory.ui.home.HomeFragment
import com.sugarygary.instory.util.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class LoginLogoutTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testLoginAndLogout() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        launchFragmentInHiltContainer<LoginFragment> {
            navController.setGraph(R.navigation.navigation_main)
            navController.setCurrentDestination(R.id.loginFragment)
            Navigation.setViewNavController(this.requireView(), navController)
        }
        onView(withId(R.id.ed_login_email)).perform(typeText("gary@gary.com"))
        onView(withId(R.id.ed_login_password)).perform(typeText("123123123"))
        closeSoftKeyboard()
        onView(withId(R.id.btnLogin)).perform(ViewActions.click())
        Assert.assertEquals(navController.currentDestination?.id, R.id.homeFragment)
        launchFragmentInHiltContainer<HomeFragment> {
            navController.setCurrentDestination(R.id.homeFragment)
            Navigation.setViewNavController(this.requireView(), navController)
        }
        onView(withId(R.id.action_logout)).perform(ViewActions.click())
        Assert.assertEquals(navController.currentDestination?.id, R.id.loginFragment)
        launchFragmentInHiltContainer<LoginFragment> {
            navController.setCurrentDestination(R.id.loginFragment)
            Navigation.setViewNavController(this.requireView(), navController)
        }
        onView(withId(R.id.ed_login_email)).perform(typeText("BERHASIL"))
        Thread.sleep(2000)
        closeSoftKeyboard()
    }
}