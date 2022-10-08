package com.byandev.storyapp

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import androidx.test.core.app.ApplicationProvider

inline fun <reified T: Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = androidx.fragment.testing.R.style.FragmentScenarioEmptyFragmentActivityTheme,
    testNavHostController: TestNavHostController? = null,
    crossinline action: Fragment.() -> Unit = {}
) {
    val mainActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
        themeResId
    )

    launch<HiltTestActivity>(mainActivityIntent).onActivity { activity ->

        val fragment = activity
            .supportFragmentManager
            .fragmentFactory
            .instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader), T::class.java.name)
            .also { it.arguments = fragmentArgs }

        fragment.viewLifecycleOwnerLiveData.observeForever {
            if (it != null) {
                testNavHostController?.let { controller ->
                    controller.setGraph(R.navigation.story_navigation)
                    controller.setCurrentDestination(R.id.fragmentHome)
                    Navigation.setViewNavController(fragment.requireView(), controller)
                }
            }
        }

        activity.supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        fragment.action()
    }
}