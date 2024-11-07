package expo.modules.bactrack

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

import android.util.Log
import androidx.core.os.bundleOf
import android.content.Context
import android.content.SharedPreferences
import BACtrackAPI.API.BACtrackAPI;
import BACtrackAPI.API.BACtrackAPICallbacks;
import BACtrackAPI.Constants.BACTrackDeviceType;
import BACtrackAPI.Constants.BACtrackUnit;
import BACtrackAPI.Constants.Errors;
import BACtrackAPI.Exceptions.BluetoothLENotSupportedException;
import BACtrackAPI.Exceptions.BluetoothNotEnabledException;
import BACtrackAPI.Exceptions.LocationServicesNotEnabledException;

private var mAPI: BACtrackAPI? = null
private var mIsConnected = false
private var mBatteryLevel = -1
private var mIsConnecting = false
private var mIsScanning = false
private val TAG = "BACManager"


class ExpoBacTrackModule : Module() {
    // Each module class must implement the definition function. The definition consists of components
    // that describes the module's functionality and behavior.
    // See https://docs.expo.dev/modules/module-api for more details about available components.
    override fun definition() = ModuleDefinition {
        // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
        // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
        // The module will be accessible from `requireNativeModule('ExpoBacTrack')` in JavaScript.
        Name("ExpoBacTrack")

        // Sets constant properties on the module. Can take a dictionary or a closure that returns a dictionary.
        Constants(
            "PI" to Math.PI
        )

        // Defines event names that the module can send to JavaScript.
        Events("onChange", "onChangeTheme", "onBacTrackConnection", "onBacTrackBlow", "onBackTrackCountdown", "onBackTrackResult", "onBacTrackAnalyzing")

        // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
        Function("hello") {
              sendEvent(
                "onChange", mapOf(
                    "value" to "hello"
                )
            )
            "Hello world, or something else 👋"
        }

        // Defines a JavaScript function that always returns a Promise and whose native code
        // is by default dispatched on the different thread than the JavaScript runtime runs on.
        AsyncFunction("setValueAsync") { value: String ->
            // Send an event to JavaScript.
            Log.d(TAG, "setValueAsync")
            this@ExpoBacTrackModule.sendEvent(
                    "onChange",
                    bundleOf("value" to value)
            )
        }

        Function("init") { apiKey: String ->
            Log.d(TAG, "Initialize BACManager")
            // val apiKey = "22315d0c4f2f40968a9896382c5b21"
            // val eventData: WritableMap = Arguments.createMap()
            // eventData.putString("event", "BacTrackError")
            try {
                mAPI = BACtrackAPI(appContext.reactContext, mCallbacks, apiKey)
            } catch (e: BluetoothLENotSupportedException) {
                Log.d(TAG, "BluetoothLENotSupportedException")
                // eventData.putString("errorCode", "100")
                // eventData.putString("description", "BluetoothLENotSupportedException")
                // sendComplexEvent(eventData)
                e.printStackTrace()
                //this.setStatus(R.string.TEXT_ERR_BLE_NOT_SUPPORTED);
            } catch (e: BluetoothNotEnabledException) {
                Log.d(TAG, "BluetoothNotEnabledException")
                // eventData.putString("errorCode", "100")
                // eventData.putString("description", "BluetoothNotEnabledException")
                // sendComplexEvent(eventData)
                e.printStackTrace()
                //this.setStatus(R.string.TEXT_ERR_BT_NOT_ENABLED);
            } catch (e: LocationServicesNotEnabledException) {
                Log.d(TAG, "LocationServicesNotEnabledException")
                // eventData.putString("errorCode", "LocationServicesNotEnabledException")
                // sendComplexEvent(eventData)
                e.printStackTrace()
                //this.setStatus(R.string.TEXT_ERR_LOCATIONS_NOT_ENABLED);
            }
        }

        Function("connectToNearest") {
            Log.d(TAG, "connectToNearest");
            if (mAPI != null) {
                Log.d(TAG, "connectToNearestBreathalyzer");
                mIsScanning = true;
                mIsConnecting = true;
                //BacTrackConnectionUpdate();
                mAPI?.connectToNearestBreathalyzer();
            }
        }

        Function("startCountdown") {
            Log.d(TAG, "startCountdown");
            if (mAPI != null) {
                mAPI?.startCountdown();
            }
        }

        Function("setTheme") { theme: String ->
            getPreferences().edit().putString("theme", theme).commit()
            this@ExpoBacTrackModule.sendEvent("onChangeTheme", bundleOf("theme" to theme))
        }

        Function("getTheme") {
            return@Function getPreferences().getString("theme", "system")
        }
    }

    private val context
        get() = requireNotNull(appContext.reactContext)

    private fun getPreferences(): SharedPreferences {
        return context.getSharedPreferences(context.packageName + ".settings", Context.MODE_PRIVATE)
    }

    private fun BacTrackConnectionUpdate() {
        val eventData = bundleOf(
            "connecting" to mIsConnecting,
            "connected" to mIsConnected,
            "scanning" to mIsScanning,
            "batteryLevel" to mBatteryLevel
        )
        Log.d(TAG, "Sending Event")
        this@ExpoBacTrackModule.sendEvent("onBacTrackConnection", eventData)
        // this@ExpoBacTrackModule.sendEvent("onChange", bundleOf("value" to "connection update"))
    }

    private val mCallbacks: BACtrackAPICallbacks = object : BACtrackAPICallbacks {
        override fun BACtrackAPIKeyDeclined(errorMessage: String?) {
            //APIKeyVerificationAlert verify = new APIKeyVerificationAlert();
            //verify.execute(errorMessage);
            Log.d(TAG, "BacTrackAPIKeyDeclined")
            // sendSimpleEvent("BacTrackAPIKeyDeclined")
        }


        override fun BACtrackAPIKeyAuthorized() {
            Log.d(TAG, "BACtrackAPIKeyAuthorized")
            // sendSimpleEvent("BacTrackAPIKeyAuthorized")
        }


        override fun BACtrackConnected(bacTrackDeviceType: BACTrackDeviceType?) {
            //setStatus(R.string.TEXT_CONNECTED);
            Log.d(TAG, "BacTrackConnected")
            mIsConnected = true
            mBatteryLevel = 5
            mIsConnecting = false
            mIsScanning = false
            // sendSimpleEvent("BacTrackConnected")
            BacTrackConnectionUpdate()
        }


        override fun BACtrackDidConnect(s: String?) {
            // setStatus(R.string.TEXT_DISCOVERING_SERVICES);
            // Log.d(TAG, "BacTrackConnected");
            // mIsConnected = true;
            // mBatteryLevel = 5;
            // mIsConnecting = false;
            // mIsScanning = false;
            // sendSimpleEvent("BacTrackConnected");
            // BacTrackConnectionUpdate();
        }


        override fun BACtrackDisconnected() {
            //setStatus(R.string.TEXT_DISCONNECTED);
            //setBatteryStatus("");
            //setCurrentFirmware(null);
            Log.d(TAG, "BacTrackDisconnected")
            mIsConnected = false
            mBatteryLevel = -1
            mIsConnecting = false
            mIsScanning = false
            // sendSimpleEvent("BacTrackDisconnected")
            BacTrackConnectionUpdate()
        }


        override fun BACtrackConnectionTimeout() {
            Log.d(TAG, "BacTrackConnectTimeout")
            // sendSimpleEvent("BacTrackConnectTimeout")
        }


        override fun BACtrackFoundBreathalyzer(BACtrackDevice: BACtrackAPI.BACtrackDevice) {
            Log.d(TAG, "Found breathalyzer : " + BACtrackDevice.toString())
            // val eventData: WritableMap = Arguments.createMap()
            // eventData.putString("event", "BacTrackFoundBreathalyzer")
            // eventData.putString("UUID", BACtrackDevice.toString())
            // sendComplexEvent(eventData)
        }


        override fun BACtrackCountdown(currentCountdownCount: Int) {
            //setStatus(getString(R.string.TEXT_COUNTDOWN) + " " + currentCountdownCount);
            Log.d(TAG, "BacTrackCountdown")
            // val eventData: WritableMap = Arguments.createMap()
            // eventData.putString("event", "BacTrackCountdown")
            // eventData.putInt("countDown", currentCountdownCount)
            // sendComplexEvent(eventData)

            val eventData = bundleOf(
            "count" to currentCountdownCount
            )
            Log.d(TAG, "Sending count event")
            this@ExpoBacTrackModule.sendEvent("onBackTrackCountdown", eventData)
        }


        override fun BACtrackStart() {
            //setStatus(R.string.TEXT_BLOW_NOW);
            Log.d(TAG, "BacTrackStart")
            // sendSimpleEvent("BacTrackStart")
        }


        override fun BACtrackBlow(breathVolumeRemaining: Float) {
            //setStatus(String.format("Keep Blowing (%d%%)", 100 - (int)(100.0 * breathVolumeRemaining)));
            //setStatus(R.string.TEXT_KEEP_BLOWING);
            Log.d(TAG, "BacTrackBlow")
            // sendSimpleEvent("BacTrackBlow")
            val eventData = bundleOf(
            "remaining" to breathVolumeRemaining
            )
            Log.d(TAG, "Sending blow event")
            this@ExpoBacTrackModule.sendEvent("onBacTrackBlow", eventData)
        }


        override fun BACtrackAnalyzing() {
            Log.d(TAG, "BacTrackAnalyzing")
            this@ExpoBacTrackModule.sendEvent("onBacTrackAnalyzing")
        }

        override fun BACtrackResults(measuredBac: Float) {
            val formattedBac = "%.10f".format(measuredBac)
            val eventData = bundleOf(
            "result" to formattedBac
            )
            Log.d(TAG, "Sending result event")
            this@ExpoBacTrackModule.sendEvent("onBackTrackResult", eventData)
        }


        override fun BACtrackFirmwareVersion(version: String?) {
            //setCurrentFirmware(version);
            //setStatus(getString(R.string.TEXT_FIRMWARE_VERSION) + " " + version);
        }


        override fun BACtrackSerial(serialHex: String?) {
            //setStatus(getString(R.string.TEXT_SERIAL_NUMBER) + " " + serialHex);
        }


        override fun BACtrackUseCount(useCount: Int) {
            Log.d(TAG, "UseCount: $useCount")
            // C6/C8 bug in hardware does not allow getting use count
            if (useCount == 4096) {
                //setStatus("Cannot retrieve use count for C6/C8 devices");
            } else {
                //setStatus(getString(R.string.TEXT_USE_COUNT) + " " + useCount);
            }
        }


        override fun BACtrackBatteryLevel(level: Int) {
            //setBatteryStatus(getString(R.string.TEXT_BATTERY_LEVEL) + " " + level);
            mBatteryLevel = level
            BacTrackConnectionUpdate()
        }


        override fun BACtrackBatteryVoltage(voltage: Float) {
        }


        override fun BACtrackError(errorCode: Int) {
            Log.d(TAG, "BacTrackError")
            // val eventData: WritableMap = Arguments.createMap()
            // eventData.putString("event", "BacTrackError")
            // eventData.putString("errorCode", String.valueOf(errorCode))
            // sendComplexEvent(eventData)
            //if (errorCode == Errors.ERROR_BLOW_ERROR)
            //    setStatus(R.string.TEXT_ERR_BLOW_ERROR);
        }

        override fun BACtrackUnits(units: BACtrackUnit?) {
        }
    }
}

