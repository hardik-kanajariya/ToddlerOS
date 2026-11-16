package com.toddleros.kiosk

import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Build

class KioskController(private val context: Context) {

    private val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as? DevicePolicyManager
    private val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    private val adminComponent = ToddlerDeviceAdminReceiver.getComponentName(context)

    fun isDeviceOwner(): Boolean {
        return dpm?.isDeviceOwnerApp(context.packageName) == true
    }

    fun isProfileOwner(): Boolean {
        return dpm?.isProfileOwnerApp(context.packageName) == true
    }

    fun isLockTaskPermitted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            dpm?.isLockTaskPermitted(context.packageName) == true
        } else {
            false
        }
    }

    fun isKioskActive(activity: Activity): Boolean {
        return am?.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE
    }

    /**
     * Configures Lock Task packages and features if ToddlerOS has Device Owner privileges.
     */
    fun configureLockTaskRestrictions(): Boolean {
        val policyManager = dpm ?: return false
        if (!isDeviceOwner()) return false

        return try {
            policyManager.setLockTaskPackages(adminComponent, arrayOf(context.packageName))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // Restricts notification shade, Home navigation, and Overview/Recent apps
                policyManager.setLockTaskFeatures(
                    adminComponent,
                    DevicePolicyManager.LOCK_TASK_FEATURE_NONE
                )
            }
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Activates Lock Task Mode.
     * When configured as Device Owner, this provides an unpinned kiosk without user confirmation prompts.
     * When unmanaged, standard Android Screen Pinning is invoked.
     */
    fun startChildKioskMode(activity: Activity): Boolean {
        return try {
            configureLockTaskRestrictions()
            activity.startLockTask()
            isKioskActive(activity)
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Leaves Lock Task Mode, restoring normal system navigation.
     */
    fun stopChildKioskMode(activity: Activity): Boolean {
        return try {
            activity.stopLockTask()
            true
        } catch (_: Exception) {
            false
        }
    }

    fun getProvisioningInstructions(): String {
        return buildString {
            append("Device Owner Provisioning Guide:\n")
            append("1. Device Owner must be set before any Google/user accounts are added (or on a factory-reset device).\n")
            append("2. Connect device to a computer with USB debugging enabled.\n")
            append("3. Execute via terminal:\n")
            append("   adb shell dpm set-device-owner com.toddleros.launcher/.kiosk.ToddlerDeviceAdminReceiver\n")
            append("4. Once provisioned, Child Mode completely secures the screen without notification drawer or recent apps access.")
        }
    }
}
