package com.example.smart_agriculture_kotlin.screens

import Screens.Dashbord
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_agriculture_kotlin.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class FarmerDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "farmer_db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE farmers (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                farmer_name TEXT,
                national_id TEXT,
                sector TEXT,
                cell TEXT,
                village TEXT,
                crop_type TEXT,
                crop_variety TEXT,
                planting_date TEXT,
                harvest_date TEXT,
                phone TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun insertFarmer(
        farmerName: String,
        nationalId: String,
        sector: String,
        cell: String,
        village: String,
        cropType: String,
        cropVariety: String,
        plantingDate: String,
        harvestDate: String,
        phone: String
    ): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("farmer_name", farmerName)
            put("national_id", nationalId)
            put("sector", sector)
            put("cell", cell)
            put("village", village)
            put("crop_type", cropType)
            put("crop_variety", cropVariety)
            put("planting_date", plantingDate)
            put("harvest_date", harvestDate)
            put("phone", phone)
        }
        val result = db.insert("farmers", null, cv)
        db.close()
        return result != -1L
    }
}

// Validation helpers
fun isOnlyText(input: String): Boolean {
    return input.matches(Regex("^[a-zA-Z\\s]+\$"))
}

fun isOnlyDigits(input: String): Boolean {
    return input.matches(Regex("^\\d+\$"))
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FarmerRegistrationScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isDashBordShown by remember { mutableStateOf(false) }
    val dbHelper = remember { FarmerDatabaseHelper(context) }

    var farmerName by remember { mutableStateOf("") }
    var nationalId by remember { mutableStateOf("") }
    var sector by remember { mutableStateOf("") }
    var cell by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var cropType by remember { mutableStateOf("") }
    var cropVariety by remember { mutableStateOf("") }
    var plantingDate by remember { mutableStateOf("") }
    var harvestDate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun showDatePicker(initialDate: String, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val parts = initialDate.split("-")
        if (parts.size == 3) {
            calendar.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
        }

        DatePickerDialog(
            context,
            { _, year, month, day ->
                val selected = String.format("%04d-%02d-%02d", year, month + 1, day)
                onDateSelected(selected)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    if (isDashBordShown) {
        Dashbord()
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xCC000000))
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                val labelColor = Color.White

                @Composable
                fun Label(text: String) = Text(
                    text = text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = labelColor,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                @Composable
                fun WhiteOutlinedTextField(value: String, onValueChange: (String) -> Unit) =
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        textStyle = TextStyle(color = Color.White),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = Color.White,
                            cursorColor = Color.White
                        )
                    )

                Label("Farmer Name")
                WhiteOutlinedTextField(farmerName) { farmerName = it }

                Label("National ID Number")
                WhiteOutlinedTextField(nationalId) { nationalId = it }

                Label("Sector")
                WhiteOutlinedTextField(sector) { sector = it }

                Label("Cell")
                WhiteOutlinedTextField(cell) { cell = it }

                Label("Village")
                WhiteOutlinedTextField(village) { village = it }

                Label("Crop Type")
                WhiteOutlinedTextField(cropType) { cropType = it }

                Label("Crop Variety")
                WhiteOutlinedTextField(cropVariety) { cropVariety = it }

                Label("Planting Date (YYYY-MM-DD)")
                Button(
                    onClick = { showDatePicker(plantingDate) { plantingDate = it } },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text(
                        text = if (plantingDate.isBlank()) "Select Planting Date" else plantingDate,
                        color = Color.White
                    )
                }

                Label("Expected Harvest Date (YYYY-MM-DD)")
                Button(
                    onClick = { showDatePicker(harvestDate) { harvestDate = it } },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text(
                        text = if (harvestDate.isBlank()) "Select Harvest Date" else harvestDate,
                        color = Color.White
                    )
                }

                Label("Contact Phone")
                WhiteOutlinedTextField(phone) { phone = it }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val planting = try { LocalDate.parse(plantingDate, formatter) } catch (e: Exception) { null }
                        val harvest = try { LocalDate.parse(harvestDate, formatter) } catch (e: Exception) { null }

                        when {
                            farmerName.isBlank() || nationalId.isBlank() || sector.isBlank() ||
                                    cell.isBlank() || village.isBlank() || cropType.isBlank() ||
                                    cropVariety.isBlank() || plantingDate.isBlank() || harvestDate.isBlank() || phone.isBlank() ->
                                message = "Please fill all fields"

                            !isOnlyText(farmerName) ->
                                message = "Farmer name should contain only letters."

                            !isOnlyDigits(nationalId) ->
                                message = "National ID should contain only numbers."

                            !isOnlyText(sector) ->
                                message = "Sector should contain only letters."

                            !isOnlyText(cell) ->
                                message = "Cell should contain only letters."

                            !isOnlyText(village) ->
                                message = "Village should contain only letters."

                            !isOnlyText(cropType) ->
                                message = "Crop type should contain only letters."

                            !isOnlyText(cropVariety) ->
                                message = "Crop variety should contain only letters."

                            !isOnlyDigits(phone) ->
                                message = "Phone number should contain only digits."

                            planting == null || harvest == null ->
                                message = "Invalid date format."

                            harvest.isBefore(planting) ->
                                message = "Harvest date cannot be before planting date."

                            else -> {
                                val success = dbHelper.insertFarmer(
                                    farmerName,
                                    nationalId,
                                    sector,
                                    cell,
                                    village,
                                    cropType,
                                    cropVariety,
                                    plantingDate,
                                    harvestDate,
                                    phone
                                )
                                message = if (success) "Farmer data saved successfully!" else "Failed to save data."
                                if (success) {
                                    farmerName = ""
                                    nationalId = ""
                                    sector = ""
                                    cell = ""
                                    village = ""
                                    cropType = ""
                                    cropVariety = ""
                                    plantingDate = ""
                                    harvestDate = ""
                                    phone = ""
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit", fontWeight = FontWeight.Bold)
                }

                if (message.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = message, color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = { isDashBordShown = true }, modifier = Modifier.fillMaxWidth()) {
                    Text("View Dashboard")
                }
            }
        }
    }
}
