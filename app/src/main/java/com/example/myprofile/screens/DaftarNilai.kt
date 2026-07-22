package com.example.myprofile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class MataKuliah(
    val nama: String,
    val sks: Int,
    val nilai: String,
    val bobot: Double,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarNilaiScreen(
    onBackPressed: () -> Unit = {}
) {
    val listNilai = listOf(
        MataKuliah("Pemrograman Mobile", 3, "A", 4.0),
        MataKuliah("Basis Data", 3, "A-", 3.7),
        MataKuliah("Jaringan Komputer", 3, "B+", 3.3),
        MataKuliah("Struktur Data", 4, "A", 4.0),
        MataKuliah("Matematika Diskrit", 3, "B", 3.0),
        MataKuliah("Algoritma Pemrograman", 4, "A", 4.0),
        MataKuliah("Sistem Operasi", 3, "B+", 3.3),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Nilai", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Ringkasan Nilai menggunakan Box dan Column
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color(0xFF1A237E), shape = RoundedCornerShape(12.dp))
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("Indeks Prestasi Kumulatif", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text("3.75", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("SKS Lulus", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            Text("84", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("SKS Tempuh", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            Text("84", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Text(
                "Mata Kuliah Semester Ini",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1A237E)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listNilai) { mk ->
                    NilaiItem(mk)
                }
            }
        }
    }
}

@Composable
fun NilaiItem(mk: MataKuliah) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(mk.nama, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${mk.sks} SKS", fontSize = 12.sp, color = Color.Gray)
            }
            
            // Box untuk menampilkan nilai dengan background berwarna
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(
                        color = when(mk.nilai) {
                            "A", "A-" -> Color(0xFF4CAF50)
                            "B+", "B" -> Color(0xFF2196F3)
                            else -> Color(0xFFFF9800)
                        },
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    mk.nilai,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DaftarNilaiPreview() {
    DaftarNilaiScreen()
}
