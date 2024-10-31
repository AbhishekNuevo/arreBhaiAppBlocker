package com.arrebhai

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import android.util.Log
import android.widget.Toast
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate

class MainActivity : ReactActivity() {


    val REQUEST_CODE_DRAW_OVERLAY: Int = 1234
    var LOGTAG: String = "ArreBhai"

    /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  override fun getMainComponentName(): String = "ArreBhai"

  /**
   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
   */
  override fun createReactActivityDelegate(): ReactActivityDelegate =
      DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)

    override fun onResume() {
        super.onResume()


        if(!isAccessibilityEnabled()){
            showAlertForAccessibiltity(this)
        } else if(!isOverlayPermissionGranted(this)){
//            Toast.makeText(this@MainActivity, "Hello setting->>1 " + Settings.canDrawOverlays(this), Toast.LENGTH_LONG).show()
            showAlertForOvertheTop(this)
        }

    }


    fun isOverlayPermissionGranted(context: Context): Boolean {

//        Toast.makeText(this@MainActivity, "Hello setting->> " + Settings.canDrawOverlays(context), Toast.LENGTH_LONG).show()

        return Settings.canDrawOverlays(context)
    }

    fun isAccessibilityEnabled(): Boolean {
        var accessibilityEnabled = 0
        val LIGHTFLOW_ACCESSIBILITY_SERVICE =
            "com.example.test/com.example.text.ccessibilityService"
        val accessibilityFound = false
        try {
            accessibilityEnabled =
                Settings.Secure.getInt(this.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
            Log.d(LOGTAG, "ACCESSIBILITY: $accessibilityEnabled")
        } catch (e: SettingNotFoundException) {
            Log.d(LOGTAG, "Error finding setting, default accessibility to not found: " + e.message)
        }

        val mStringColonSplitter = SimpleStringSplitter(':')

        if (accessibilityEnabled == 1) {
            Log.d(LOGTAG, "***ACCESSIBILIY IS ENABLED***: ")

            val settingValue = Settings.Secure.getString(
                contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            Log.d(LOGTAG, "Setting: $settingValue")
            if (settingValue != null) {
                val splitter = mStringColonSplitter
                splitter.setString(settingValue)
                while (splitter.hasNext()) {
                    val accessabilityService = splitter.next()
                    Log.d(LOGTAG, "Setting: $accessabilityService")

                    val packagename = "com.arrebhai";
                    if (accessabilityService.contains(packagename)) {
                        Log.d(
                            LOGTAG,
                            "We've found the correct setting - accessibility is switched on!"
                        )
                        return true
                    }
                }
            }

            Log.d(LOGTAG, "***END***")
        } else {
            Log.d(LOGTAG, "***ACCESSIBILIY IS DISABLED***")
        }
        return accessibilityFound
    }

    fun showAlertForAccessibiltity(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Permission Required")
        builder.setMessage("We need 2 permission Accessibility Services and Show over the top in order to function this app, don't worry this is Open source project. ")

        // Set up the OK button and its click listener
        builder.setPositiveButton("OK") { dialog, _ ->

            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            // request permission via start activity for result
            Toast.makeText(this@MainActivity, "Tried to open accessibility", Toast.LENGTH_SHORT)
                .show()
            startActivity(intent)

            dialog.dismiss() // Close the dialog
        }

//        // Optional: Set up a Cancel button
//        builder.setNegativeButton("Cancel") { dialog, _ ->
//            dialog.dismiss() // Just close the dialog
//        }

        // Show the alert dialog
        builder.create().show()
    }

    fun showAlertForOvertheTop(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Permission Required")
        builder.setMessage("Please provide the Over the top access, don't worry this is Open source project. ")

        // Set up the OK button and its click listener
        builder.setPositiveButton("OK") { dialog, _ ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    val intent2 = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivityForResult(intent2, REQUEST_CODE_DRAW_OVERLAY)
                }
            }

            dialog.dismiss() // Close the dialog
        }

//        // Optional: Set up a Cancel button
//        builder.setNegativeButton("Cancel") { dialog, _ ->
//            dialog.dismiss() // Just close the dialog
//        }

        // Show the alert dialog
        builder.create().show()
    }

}
