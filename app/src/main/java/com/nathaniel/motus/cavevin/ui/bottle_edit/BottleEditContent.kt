package com.nathaniel.motus.cavevin.ui.bottle_edit

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.ui.elements.*
import com.nathaniel.motus.cavevin.ui.theme.*
import com.nathaniel.motus.cavevin.viewmodels.BottleDetailViewModel
import java.util.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun BottleEditContent(
    viewModel: BottleDetailViewModel,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Surface(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication= null
        ) { focusManager.clearFocus() }
    ) {

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            val inputImageName by viewModel.imageName.observeAsState("")
            //val inputImageName = ""
            val inputAppellation by viewModel.appellation.observeAsState("")
            val inputDomain by viewModel.domain.observeAsState("")
            val inputCuvee by viewModel.cuvee.observeAsState("")
            val inputRating by viewModel.rating.observeAsState(0)
            val inputWineColor by viewModel.wineColor.observeAsState(initial = "")
            val redWineTranslation by viewModel.redWineTranslation.observeAsState("")
            val whiteWineTranslation by viewModel.whiteWineTranslation.observeAsState("")
            val pinkWineTranslation by viewModel.pinkWineTranslation.observeAsState(initial = "")
            val inputWineStillness by viewModel.wineStillness.observeAsState(initial = "")
            val stillWineTranslation by viewModel.stillWineTranslation.observeAsState(
                initial = ""
            )
            val sparklingWineTranslation by viewModel.sparklingWineTranslation.observeAsState(
                initial = ""
            )
            val bottleTypesAndCapacities by viewModel.bottleTypesAndCapacities.observeAsState(
                initial = listOf(Pair(0, ""))
            )
            val inputSelectedBottleTypeItem by viewModel.selectedBottleTypeItem.observeAsState(
                initial = Pair(0, "")
            )
            val appellations by viewModel.appellations.observeAsState(listOf())
            val domains by viewModel.domains.observeAsState(initial = listOf())
            val cuvees by viewModel.cuvees.observeAsState(initial = listOf())
            val inputVintage by viewModel.vintage.observeAsState(initial = null)
            val inputOrigin by viewModel.origin.observeAsState(initial = "")
            val inputComment by viewModel.comment.observeAsState(initial = "")
            val inputPrice by viewModel.price.observeAsState(initial = null)
            val inputCurrency by viewModel.currency.observeAsState(initial = null)
            val inputAgingCapacity by viewModel.agingCapacity.observeAsState(initial = null)

            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter)
            {
                if (inputImageName != "" && inputImageName != null)
                    BottleImage(
                        imageName = inputImageName
                    )
                else
                    BottleImagePlaceHolder(
                        wineColor = inputWineColor,
                        appellation = inputAppellation
                    )
            }

            ImagePicker(
                thereIsAnImage = inputImageName != "",
                modifier = Modifier.fillMaxWidth()
            )

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                RatingBar(
                    rating = inputRating,
                    onRatingChange = { rating: Int -> viewModel.onRatingChange(rating) },
                    isEditable = true
                )
            }

            AutocompleteTextInput(
                value = inputAppellation,
                onValueChange = { viewModel.onAppellationChange(it) },
                labelText = stringResource(id = R.string.appellation),
                suggestions = appellations
            )

            AutocompleteTextInput(
                value = inputDomain, onValueChange = { viewModel.onDomainChange(it) },
                labelText = stringResource(id = R.string.domain), suggestions = domains
            )

            AutocompleteTextInput(
                value = inputCuvee,
                onValueChange = { viewModel.onCuveeChange(it) },
                labelText = stringResource(
                    id = R.string.cuvee
                ),
                suggestions = cuvees
            )

            WineColorRadioGroup(
                selectedWineColor = inputWineColor,
                onWineColorChange = { wineColor: String ->
                    viewModel.onWineColorChange(
                        wineColor
                    )
                },
                redWineTranslation = redWineTranslation,
                whiteWineTranslation = whiteWineTranslation,
                pinkWineTranslation = pinkWineTranslation
            )

            WineStillnessRadioGroup(
                selectedWineStillness = inputWineStillness,
                onWineStillnessChange = { wineStillness: String ->
                    viewModel.onWineStillnessChange(
                        wineStillness
                    )
                },
                stillWineTranslation = stillWineTranslation,
                sparklingWineTranslation = sparklingWineTranslation
            )

            PairSpinner(
                itemList = bottleTypesAndCapacities,
                selectedItem = inputSelectedBottleTypeItem,
                onSelectionChanged = { item: Pair<Int, String> ->
                    viewModel.onBottleTypeIdChange(
                        item.first
                    )
                },
                labelText = stringResource(id = R.string.bottle_type)
            )

            GenericOutlinedIntegerField(
                value = inputVintage,
                onValueChange = { viewModel.onVintageChange(it) },
                labelText = stringResource(
                    id = R.string.vintage
                )
            )

            GenericOutlineTextField(
                value = inputOrigin,
                onValueChange = { viewModel.onOriginChange(it) },
                labelText = stringResource(
                    id = R.string.origin
                )
            )

            GenericOutlineTextField(
                value = inputComment,
                onValueChange = { viewModel.onCommentChange(it) },
                labelText = stringResource(R.string.comments)
            )

            OutlinedPriceEditor(
                price = inputPrice,
                currency = inputCurrency,
                onPriceChange = { price: Double? -> viewModel.onPriceChange(price) },
                onCurrencyChange = { currency: String? ->
                    viewModel.onCurrencyChange(
                        currency
                    )
                },
                priceLabelText = stringResource(id = R.string.price),
            )

            GenericOutlinedIntegerField(
                value = inputAgingCapacity,
                onValueChange = { agingCapacity: Int? ->
                    viewModel.onAgingCapacityChange(agingCapacity)
                },
                labelText = stringResource(id = R.string.aging_capacity)
            )
        }
    }
}


