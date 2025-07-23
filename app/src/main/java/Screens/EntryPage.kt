package Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_agriculture_kotlin.R

@Composable
fun EntryPage(modifier: Modifier = Modifier) {

   var isOnHomePage by remember { mutableStateOf(false) }

   if (isOnHomePage) {
      HomeScreen()
   } else {
      Box(
         modifier = Modifier.fillMaxSize()
      ) {

         Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
         )


         Box(
            modifier = Modifier
               .fillMaxSize()
               .background(
                  Brush.verticalGradient(
                     colors = listOf(Color(0x80000000), Color(0xCC000000))
                  )
               )
         )

         Column(
            modifier = Modifier
               .fillMaxSize()
               .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
         ) {
            Text(
               text = "Transfer Rwandan",
               fontSize = 34.sp,
               fontWeight = FontWeight.Bold,
               color = Color.White,
               textAlign = TextAlign.Center
            )
            Text(
               text = "Agriculture",
               fontSize = 34.sp,
               fontWeight = FontWeight.Bold,
               color = Color(0xFFB2FF59),
               textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
               onClick = { isOnHomePage = true },
               shape = RoundedCornerShape(20.dp),
               modifier = Modifier
                  .width(220.dp)
                  .height(55.dp),
               colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
            ) {
               Text(text = "Get Started", fontSize = 18.sp, color = Color.White)
            }
         }
      }
   }
}
