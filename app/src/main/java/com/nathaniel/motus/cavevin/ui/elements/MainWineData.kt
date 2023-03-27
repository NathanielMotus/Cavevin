package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainWineData(appellation:String?,
domain:String?,
cuvee:String?,
wineColor:String,
wineStillness:String,
vintage:Int?,
bottleTypeAndCapacity:Pair<Int,String>,
modifier: Modifier=Modifier)
{
    Column() {
        if (appellation!=null)
            Text(text = appellation, style = MaterialTheme.typography.titleLarge  ,modifier = modifier.padding(2.dp))
        if(domain!=null)
            Text(text = domain,style = MaterialTheme.typography.bodyLarge , modifier = modifier.padding(2.dp))
        if (cuvee!=null)
            Text(text = cuvee, style = MaterialTheme.typography.bodyLarge ,modifier = modifier.padding(2.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(2.dp)){
            WineGlass(wineColor = wineColor, wineStillness = wineStillness)
            Spacer(modifier = modifier.size(8.dp))
            Text(text = vintage?.toString() ?: "",style = MaterialTheme.typography.bodyLarge)
        }
        
        Text(text = bottleTypeAndCapacity.second,style = MaterialTheme.typography.bodyLarge ,modifier=modifier.padding(2.dp))
    }
}