package com.nathaniel.motus.cavevin.ui.elements

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.FileProvider
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarPictureUtils

@Composable
fun ImagePicker(
    thereIsAnImage: Boolean,
    modifier: Modifier = Modifier,
    onUriResult: (Uri?) -> Unit,
    onDelete: () -> Unit,
    onPictureTaken: (Boolean) -> Unit
) {
    val context= LocalContext.current

    val imageBrowserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { onUriResult(it) })

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {onPictureTaken(it)})

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (thereIsAnImage)
            ImagePickerButton(resourceID = R.drawable.outline_delete_forever_24, onClick = onDelete)

        ImagePickerButton(
            resourceID = R.drawable.outline_folder_24,
            onClick = { imageBrowserLauncher.launch("image/*") })

        ImagePickerButton(
            resourceID = R.drawable.outline_camera_alt_24,
            onClick = { cameraLauncher.launch(CellarPictureUtils.getUriFromFileProvider(context,
                context.resources.getString(R.string.temporary_photo_file))) })
    }
}

@Composable
private fun ImagePickerButton(
    onClick: () -> Unit = {},
    resourceID: Int,
    contentDescription: String = "",
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = resourceID),
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
        )
    }
}