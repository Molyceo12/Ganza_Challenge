package Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.example.smart_agriculture_kotlin.screens.FarmerRegistrationScreen

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var goToEntryPage by remember { mutableStateOf(false) }
    var isRecordNewPage by remember { mutableStateOf(false) }
    var isDashbordLive by remember { mutableStateOf(false) }
    var isAboutUs by remember { mutableStateOf(false) }

    when {
        isRecordNewPage -> FarmerRegistrationScreen()
        isDashbordLive -> Dashbord()
        goToEntryPage -> EntryPage()
        isAboutUs -> AboutUsScreen()
        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Smart Agriculture",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Choose an option below",
                        fontSize = 18.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = { isRecordNewPage = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(220.dp)
                            .height(55.dp)
                    ) {
                        Text(text = "➕ Record New", fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            isDashbordLive = true
                            isRecordNewPage = false
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(220.dp)
                            .height(55.dp)
                    ) {
                        Text(text = "📄 View Records", fontSize = 18.sp)
                    }


                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            isAboutUs = true
                            goToEntryPage = false
                            isDashbordLive = false
                            isRecordNewPage = false
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(220.dp)
                            .height(55.dp)
                    ) {
                        Text(text = "ℹ️ About Us", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            goToEntryPage = true
                            isDashbordLive = false
                            isRecordNewPage = false
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(220.dp)
                            .height(55.dp)
                    ) {
                        Text(text = "⬅️ Back", fontSize = 18.sp)
                    }
                }
            }
        }
    }
}
