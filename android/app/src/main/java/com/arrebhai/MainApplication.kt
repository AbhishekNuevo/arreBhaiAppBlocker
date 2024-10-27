package com.arrebhai
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.soloader.SoLoader

class MainApplication : Application(), ReactApplication {
  var LOGTAG: String = "ArreBhai";
  val REQUEST_CODE_DRAW_OVERLAY: Int = 1234

  override val reactNativeHost: ReactNativeHost =
      object : DefaultReactNativeHost(this) {
        override fun getPackages(): List<ReactPackage> =
            PackageList(this).packages.apply {
              // Packages that cannot be autolinked yet can be added manually here, for example:
              add(AccessbilityPackage()) // Add the custom module here
            }

        override fun getJSMainModuleName(): String = "index"

        override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

        override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
        override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
      }

  override val reactHost: ReactHost
    get() = getDefaultReactHost(applicationContext, reactNativeHost)

  override fun onCreate() {
    super.onCreate()
    SoLoader.init(this, false)
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      load()
    }


    Toast.makeText(this, "Hellow", Toast.LENGTH_LONG).show()
//    if (!isAccessibilityEnabled()) {
//      Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
//
//      val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//      // request permission via start activity for result
//      Toast.makeText(this, "Tried to open accessibility", Toast.LENGTH_SHORT).show()
//      startActivity(intent)
//    }


//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//      if (!Settings.canDrawOverlays(this)) {
//        val intent = Intent(
//          Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//          Uri.parse("package:$packageName")
//        )
//        startActivityForResult(intent, REQUEST_CODE_DRAW_OVERLAY)
//      }
//    }
  }



  fun isAccessibilityEnabled(): Boolean {
    var accessibilityEnabled = 0
    val LIGHTFLOW_ACCESSIBILITY_SERVICE = "com.example.test/com.example.text.ccessibilityService"
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

          val packagename = "com.arre.bhai"
          if (accessabilityService.contains(packagename)) {
            Log.d(LOGTAG, "We've found the correct setting - accessibility is switched on!")
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
}
