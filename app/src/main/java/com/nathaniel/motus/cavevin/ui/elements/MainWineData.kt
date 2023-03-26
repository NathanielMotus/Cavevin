package com.nathaniel.motus.cavevin.ui.elements

import androidx.compose.foundation.layout.*
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
    Column(modifier=modifier.padding(8.dp)) {
        if (appellation!=null)
            Text(text = appellation, fontSize = 20.sp, modifier = modifier.padding(2.dp))
        if(domain!=null)
            Text(text = domain,fontSize = 16.sp, modifier = modifier.padding(2.dp))
        if (cuvee!=null)
            Text(text = cuvee, fontSize = 16.sp,modifier = modifier.padding(2.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(2.dp)){
            WineGlass(wineColor = wineColor, wineStillness = wineStillness)
            Spacer(modifier = modifier.size(8.dp))
            Text(text = vintage.toString(),fontSize = 16.sp)
        }
        
        Text(text = bottleTypeAndCapacity.second,fontSize = 16.sp,modifier=modifier.padding(2.dp))
    }
}