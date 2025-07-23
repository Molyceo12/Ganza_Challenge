package Screens

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smart_agriculture_kotlin.screens.FarmerRegistrationScreen

data class Farmer(
    val idInDb: Int,
    val name: String,
    val nationalId: String,
    val crop: String,
    val phone: String
)

class FarmerDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "farmer_db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS farmers (
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

    fun getAllFarmers(): List<Farmer> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM farmers ORDER BY id DESC", null)
        val farmers = mutableListOf<Farmer>()

        if (cursor.moveToFirst()) {
            do {
                farmers.add(
                    Farmer(
                        idInDb = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("farmer_name")),
                        nationalId = cursor.getString(cursor.getColumnIndexOrThrow("national_id")),
                        crop = cursor.getString(cursor.getColumnIndexOrThrow("crop_type")),
                        phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return farmers
    }

    fun deleteFarmer(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete("farmers", "id = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    fun updateFarmer(farmer: Farmer): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("farmer_name", farmer.name)
            put("national_id", farmer.nationalId)
            put("crop_type", farmer.crop)
            put("phone", farmer.phone)
        }
        val result = db.update("farmers", cv, "id = ?", arrayOf(farmer.idInDb.toString()))
        db.close()
        return result > 0
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Dashbord() {
    var isGoToHome by remember { mutableStateOf(false) }
    var addNewRecord by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingFarmer by remember { mutableStateOf<Farmer?>(null) }

    var farmerToDelete by remember { mutableStateOf<Farmer?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val dbHelper = remember { FarmerDatabaseHelper(context) }
    var farmers by remember { mutableStateOf<List<Farmer>>(emptyList()) }

    var editName by remember { mutableStateOf("") }
    var editNationalId by remember { mutableStateOf("") }
    var editCrop by remember { mutableStateOf("") }
    var editPhone by remember { mutableStateOf("") }

    fun refreshFarmers() {
        farmers = dbHelper.getAllFarmers()
    }

    LaunchedEffect(Unit) {
        refreshFarmers()
    }

    fun openEditDialog(farmer: Farmer) {
        editingFarmer = farmer
        editName = farmer.name
        editNationalId = farmer.nationalId
        editCrop = farmer.crop
        editPhone = farmer.phone
        showEditDialog = true
    }

    fun saveEdits() {
        editingFarmer?.let {
            val updated = it.copy(
                name = editName,
                nationalId = editNationalId,
                crop = editCrop,
                phone = editPhone
            )
            val success = dbHelper.updateFarmer(updated)
            if (success) {
                showMessage = "Farmer info updated"
                refreshFarmers()
            } else {
                showMessage = "Update failed"
            }
            showEditDialog = false
        }
    }

    if (isGoToHome) {
        HomeScreen()
    } else if (addNewRecord) {
        FarmerRegistrationScreen()
    } else {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Recent Records",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            if (farmers.isNotEmpty()) {
                farmers.forEach { farmer ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Farmer Name: ${farmer.name}", fontSize = 18.sp)
                            Text("National ID: ${farmer.nationalId}", fontSize = 16.sp)
                            Text("Crop Type: ${farmer.crop}", fontSize = 16.sp)
                            Text("Phone: ${farmer.phone}", fontSize = 16.sp)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                    onClick = {
                                        farmerToDelete = farmer
                                        showDeleteDialog = true
                                    }
                                ) {
                                    Text("Delete")
                                }

                                OutlinedButton(onClick = {
                                    openEditDialog(farmer)
                                }) {
                                    Text("Edit")
                                }
                            }
                        }
                    }
                }
            } else {
                Text("No farmer data found.", fontSize = 16.sp)
            }

            if (showMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = showMessage, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { isGoToHome = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Home")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    showMessage = "Functionality coming soon..."
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Download Report")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { addNewRecord = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Add New Record")
            }
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Farmer Info") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editNationalId,
                        onValueChange = { editNationalId = it },
                        label = { Text("National ID") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editCrop,
                        onValueChange = { editCrop = it },
                        label = { Text("Crop Type") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Phone") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { saveEdits() }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteDialog && farmerToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                farmerToDelete = null
            },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete ${farmerToDelete?.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    val deleted = dbHelper.deleteFarmer(farmerToDelete!!.idInDb)
                    if (deleted) {
                        showMessage = "Farmer deleted"
                        refreshFarmers()
                    } else {
                        showMessage = "Delete failed"
                    }
                    showDeleteDialog = false
                    farmerToDelete = null
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    farmerToDelete = null
                }) {
                    Text("No")
                }
            }
        )
    }
}
