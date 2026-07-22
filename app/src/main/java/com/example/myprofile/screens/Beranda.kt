package com.example.myprofile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myprofile.data.MahasiswaRepository

// Data class untuk item menu
data class MenuItem(
    val id: Int,
    val title: String,
    val icon: ImageVector, // Untuk demo, kita pakai icon dari Icons.Default
    val color: Color
)

// Data class untuk pengumuman
data class Announcement(
    val id: Int,
    val title: String,
    val date: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(
    nim: String = "",
    onLogout: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToNilai: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    // Ambil data terbaru dari repository
    var biodata by remember { 
        mutableStateOf(MahasiswaRepository.getMahasiswaByNim(nim) ?: BiodataMahasiswa(nim = nim)) 
    }

    // Update data saat halaman kembali fokus
    LaunchedEffect(Unit) {
        MahasiswaRepository.getMahasiswaByNim(nim)?.let {
            biodata = it
        }
    }

    // Sample data untuk menu
    val menuItems = listOf(
        MenuItem(1, "Profil", Icons.Default.Person, Color(0xFF42A5F5)),
        MenuItem(2, "Jadwal", Icons.Default.CalendarToday, Color(0xFF66BB6A)),
        MenuItem(3, "Nilai", Icons.Default.Grade, Color(0xFFFFA726)),
        MenuItem(4, "Absensi", Icons.Default.Fingerprint, Color(0xFFEF5350)),
        MenuItem(5, "Tugas", Icons.AutoMirrored.Filled.Assignment, Color(0xFFAB47BC)),
        MenuItem(6, "E-Learning", Icons.Default.School, Color(0xFF26C6DA))
    )

    // Sample data untuk pengumuman
    val announcements = listOf(
        Announcement(
            1,
            "Pendaftaran KRS Semester Genap",
            "15 Januari 2024",
            "Pendaftaran KRS dibuka mulai tanggal 15-30 Januari 2024"
        ),
        Announcement(
            2,
            "Libur Semester",
            "20 Desember 2024",
            "Libur semester dimulai dari tanggal 20 Desember 2024 - 10 Januari 2025"
        ),
        Announcement(
            3,
            "Pengumuman UAS",
            "10 Desember 2024",
            "Jadwal UAS akan diumumkan minggu depan"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Beranda",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "NIM: $nim",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Buka drawer atau menu */ }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Notifikasi */ }) {
                        Badge(
                            containerColor = Color.Red
                        ) {
                            Text("3", fontSize = 10.sp)
                        }
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifikasi",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E)
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    label = { Text("Beranda") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1A237E),
                        selectedTextColor = Color(0xFF1A237E)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Jadwal") },
                    label = { Text("Jadwal") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1A237E),
                        selectedTextColor = Color(0xFF1A237E)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Grade, contentDescription = "Nilai") },
                    label = { Text("Nilai") },
                    selected = selectedTab == 2,
                    onClick = { 
                        selectedTab = 2
                        onNavigateToNilai()
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1A237E),
                        selectedTextColor = Color(0xFF1A237E)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 3,
                    onClick = { 
                        selectedTab = 3
                        onNavigateToProfile()
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF1A237E),
                        selectedTextColor = Color(0xFF1A237E)
                    )
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Profile Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF1A237E),
                                        Color(0xFF283593)
                                    )
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (biodata.fotoProfil.isNotEmpty() && biodata.fotoProfil != "camera_demo") {
                                        AsyncImage(
                                            model = biodata.fotoProfil,
                                            contentDescription = "Foto Profil",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = "Profile",
                                            modifier = Modifier.size(40.dp),
                                            tint = Color(0xFF1A237E)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = biodata.nama,
                                        fontSize = 14.sp,
                                        color = Color.White.copy(alpha = 0.9f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Semester ${biodata.semester}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "IPK: ${biodata.ipk}",
                                        fontSize = 16.sp,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick Menu Section
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Menu Cepat",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(menuItems) { menu ->
                            QuickMenuItem(
                                menu = menu,
                                onClick = {
                                    if (menu.title == "Nilai") {
                                        onNavigateToNilai()
                                    } else if (menu.title == "Profil") {
                                        onNavigateToProfile()
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Announcement Section
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pengumuman Terbaru",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E)
                        )
                        TextButton(onClick = { /* Lihat semua pengumuman */ }) {
                            Text("Lihat Semua", color = Color(0xFF1A237E))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    announcements.forEach { announcement ->
                        AnnouncementCard(announcement = announcement)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // Info Akademik
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Info",
                            tint = Color(0xFF1A237E),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Info Akademik",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF1A237E)
                            )
                            Text(
                                text = "Ujian Semester dimulai 2 minggu lagi",
                                fontSize = 14.sp,
                                color = Color(0xFF1A237E).copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickMenuItem(menu: MenuItem, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(menu.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    menu.icon,
                    contentDescription = menu.title,
                    tint = menu.color,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = menu.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun AnnouncementCard(announcement: Announcement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A237E).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Campaign,
                    contentDescription = "Announcement",
                    tint = Color(0xFF1A237E),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = announcement.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = announcement.date,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = announcement.description,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BerandaScreenPreview() {
    MaterialTheme {
        BerandaScreen(nim = "23083000096")
    }
}