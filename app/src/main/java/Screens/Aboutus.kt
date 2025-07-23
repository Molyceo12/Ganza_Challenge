package Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_agriculture_kotlin.R

@Composable
fun AboutUsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {


    var IsHomeLive by remember {
        mutableStateOf(false)
    }


    if (IsHomeLive) {
        HomeScreen()
    } else {


        Box(
            modifier = modifier.fillMaxSize()
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
                    .background(Color(0xCC000000))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ganza",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Empowering Rwandan Agriculture",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Ganza is dedicated to supporting and empowering Rwandan farmers by providing modern tools and technology to enhance agricultural productivity and sustainability.",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {IsHomeLive=true},
                        modifier = Modifier
                            .width(150.dp)
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(text = "⬅️ Back", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}