package com.dudencov.happyhabit.base

import android.Manifest
import android.os.Build
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.dudencov.happyhabit.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule

internal abstract class BaseTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val permissionRule: GrantPermissionRule = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        GrantPermissionRule.grant()
    }

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
        dismissNotificationDialogIfPresent()
        composeTestRule.waitForIdle()
    }

    private fun dismissNotificationDialogIfPresent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            
            Thread.sleep(500)
            
            val dialogAppeared = device.wait(
                Until.hasObject(By.textContains("Allow")),
                3000
            )

            if (dialogAppeared) {
                val allowButton = device.findObject(
                    UiSelector()
                        .textContains("Allow")
                        .className("android.widget.Button")
                )

                if (allowButton.exists()) {
                    allowButton.click()
                    device.waitForIdle()
                    Thread.sleep(500)
                }
            }
        }
    }
}