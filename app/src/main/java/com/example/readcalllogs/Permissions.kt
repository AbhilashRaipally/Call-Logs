package com.example.readcalllogs

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhoneStatePermission() {

    // Phone State permission state
    val phoneStatePermissionState = rememberPermissionState(
        Manifest.permission.READ_PHONE_STATE
    )

    when (val status = phoneStatePermissionState.status) {
        // If the phone state permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
            Text("Phone state permission Granted")
        }
        is PermissionStatus.Denied -> {
            Column {
                val textToShow = if (status.shouldShowRationale) {
                    // If the user has denied the permission but the rationale can be shown,
                    // then gently explain why the app requires this permission
                    "The phone state is important for this app. Please grant the permission."
                } else {
                    // If it's the first time the user lands on this feature, or the user
                    // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "Phone state permission required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow)
                Button(onClick = { phoneStatePermissionState.launchPermissionRequest() }) {
                    Text("Request Phone State permission")
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReadContactsPermission() {

    // Phone State permission state
    val readContactsPermissionState = rememberPermissionState(
        Manifest.permission.READ_CONTACTS
    )

    when (val status = readContactsPermissionState.status) {
        // If the phone state permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
            Text("Read contacts permission Granted")
        }
        is PermissionStatus.Denied -> {
            Column {
                val textToShow = if (status.shouldShowRationale) {
                    // If the user has denied the permission but the rationale can be shown,
                    // then gently explain why the app requires this permission
                    "The Read contacts is important for this app. Please grant the permission."
                } else {
                    // If it's the first time the user lands on this feature, or the user
                    // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "Read contacts permission required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow)

                Button(onClick = { readContactsPermissionState.launchPermissionRequest() }) {
                    Text("Request Read Contacts permission")
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReadCallLogs(
    onPermissionGranted: @Composable () -> Unit
) {

    // Phone State permission state
    val readCallLogsPermissionState = rememberPermissionState(
        Manifest.permission.READ_CALL_LOG
    )

    when (val status = readCallLogsPermissionState.status) {
        // If the phone state permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
            Text("Read call logs permission Granted")
            onPermissionGranted()
        }
        is PermissionStatus.Denied -> {
            Column {
                val textToShow = if (status.shouldShowRationale) {
                    // If the user has denied the permission but the rationale can be shown,
                    // then gently explain why the app requires this permission
                    "The Read call logs is important for this app. Please grant the permission."
                } else {
                    // If it's the first time the user lands on this feature, or the user
                    // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "Read call logs permission required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow)

                Button(onClick = { readCallLogsPermissionState.launchPermissionRequest() }) {
                    Text("Request Read call logs permission")
                }
            }
        }
    }
}