package ru.glebik.utilslibrary.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.google.android.gms.location.LocationRequest as GmsLocationRequest

object LocationHelperFactory {

    class LocationNotPermittedException : RuntimeException()
    class LocationNotAvailableException : RuntimeException()

    private class LocationHelperImpl(
        caller: ActivityResultCaller,
        private val context: Context,
    ) : LocationHelper {

        private val isPermissionRequestActive = AtomicBoolean(false)
        private val isLocationRequestActive = AtomicBoolean(false)
        private val permissionRequests = mutableListOf<PermissionRequest>()
        private val locationRequests = mutableListOf<LocationRequest>()

        class PermissionRequest(
            val continuation: Continuation<Unit>,
        ) {
            fun resume() = continuation.resume(Unit)
            fun cancel(throwable: Throwable) = continuation.resumeWithException(throwable)
        }

        class LocationRequest(
            val continuation: Continuation<Location>,
        ) {
            fun resume(location: Location) = continuation.resume(location)
            fun cancel(throwable: Throwable) = continuation.resumeWithException(throwable)
        }

        private var locationCallback: LocationCallback? = null

        private val launcher = caller.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val coarse = permissions[Manifest.permission.ACCESS_COARSE_LOCATION]
            val fine = permissions[Manifest.permission.ACCESS_FINE_LOCATION]
            if (coarse == true || fine == true) {
                permissionRequests.forEach(PermissionRequest::resume)
            } else {
                permissionRequests.forEach {
                    it.cancel(LocationNotPermittedException())
                }
            }
            permissionRequests.clear()
            if (isPermissionRequestActive.compareAndSet(true, false).not()) {
                Log.e(TAG, "!!! permission request has wrong flag expected !!!")
            }
        }

        override fun hasPermissions(): Boolean {
            val coarseLocationGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val fineLocationGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            return coarseLocationGranted || fineLocationGranted
        }

        override suspend fun ensurePermissions() {
            val coarseLocationGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val fineLocationGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (coarseLocationGranted || fineLocationGranted) return
            suspendCoroutine {
                permissionRequests += PermissionRequest(it)
                if (isPermissionRequestActive.compareAndSet(false, true)) {
                    launcher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
            }
        }

        @SuppressLint("MissingPermission")
        override suspend fun requireLocation(): Location {
            ensurePermissions()
            return suspendCoroutine { continuation ->

                locationRequests += LocationRequest(continuation)
                if (isLocationRequestActive.compareAndSet(false, true)) {
                    val locationClient = LocationServices.getFusedLocationProviderClient(context)
                    locationCallback = object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            handleLocationResult(locationClient, result)
                        }
                    }

                    locationClient.requestLocationUpdates(
                        GmsLocationRequest.Builder(500L).build(),
                        requireNotNull(locationCallback),
                        Looper.getMainLooper()
                    )
                }
            }
        }

        private fun handleLocationResult(
            client: FusedLocationProviderClient,
            result: LocationResult,
        ) {
            val location = when {
                result.lastLocation != null -> requireNotNull(result.lastLocation)
                result.locations.isNotEmpty() -> result.locations.first()
                else -> {
                    client.removeLocationUpdates(requireNotNull(locationCallback))
                    deliverException(LocationNotAvailableException())
                    return
                }
            }
            deliverResult(
                Location(
                    location.latitude.toFloat(),
                    location.longitude.toFloat()
                )
            )
            client.removeLocationUpdates(requireNotNull(locationCallback))
        }

        private fun deliverResult(location: Location) {
            locationRequests.forEach { it.resume(location) }
            locationRequests.clear()
            if (isLocationRequestActive.compareAndSet(true, false).not()) {
                Log.e(TAG, "!!! location request has wrong flag expected !!!")
            }
        }

        private fun deliverException(throwable: Throwable) {
            locationRequests.forEach { it.cancel(throwable) }
            locationRequests.clear()
            if (isLocationRequestActive.compareAndSet(true, false).not()) {
                Log.e(TAG, "!!! location request has wrong flag expected !!!")
            }
        }

        companion object {
            private const val TAG = "LocationHelper"
        }
    }

    fun create(caller: ActivityResultCaller, context: Context): LocationHelper =
        LocationHelperImpl(caller, context)
}