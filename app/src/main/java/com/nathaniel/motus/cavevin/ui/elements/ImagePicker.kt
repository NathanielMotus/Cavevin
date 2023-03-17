package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nathaniel.motus.cavevin.R

@Composable
fun ImagePicker(
    thereIsAnImage: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (thereIsAnImage)
            ImagePickerButton(resourceID = R.drawable.outline_delete_forever_24)

        ImagePickerButton(resourceID = R.drawable.outline_folder_24)

        ImagePickerButton(resourceID = R.drawable.outline_camera_alt_24)
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