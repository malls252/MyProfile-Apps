package com.example.myprofile.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myprofile.data.MahasiswaRepository

// Data class untuk Biodata Mahasiswa
data class BiodataMahasiswa(
    val nim: String = "",
    val nama: String = "",
    val jurusan: String = "",
    val prodi: String = "",
    val angkatan: String = "",
    val email: String = "",
    val noHp: String = "",
    val alamat: String = "",
    val tempatLahir: String = "",
    val tanggalLahir: String = "",
    val fotoProfil: String = "",
    val ipk: String = "3.75",
    val semester: String = "6",
    val hobi: List<String> = listOf("Coding", "Membaca", "Traveling")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    nim: String = "",
    onBackPressed: () -> Unit = {},
    onUpdateBiodata: (BiodataMahasiswa) -> Unit = {},
    isDarkMode: Boolean = false,
    onDarkModeToggle: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current

    // State untuk biodata mahasiswa
    var biodata by remember {
        mutableStateOf(
            MahasiswaRepository.getMahasiswaByNim(nim) ?: BiodataMahasiswa(
                nim = nim,
                nama = "Dwi Hikmal Akbar Ramadhan",
                jurusan = "Teknologi Informasi",
                prodi = "Sistem Informasi",
                angkatan = "2023",
                email = "$nim@student.unmer.ac.id",
                noHp = "08815520154",
                alamat = "JL. Dieng Atas",
                tempatLahir = "Lumajang",
                tanggalLahir = "03 November 2004",
                fotoProfil = "",
                ipk = "3.75",
                semester = "6",
                hobi = listOf("Coding", "Membaca", "Traveling")
            )
        )
    }

    var showEditDialog by remember { mutableStateOf(false) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    // State untuk form edit - menggunakan BiodataMahasiswa sementara
    var tempBiodata by remember { mutableStateOf(biodata) }

    // Reset temp biodata ketika dialog dibuka
    LaunchedEffect(showEditDialog) {
        if (showEditDialog) {
            tempBiodata = biodata
        }
    }

    // Launcher untuk memilih gambar dari galeri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val updatedBiodata = biodata.copy(fotoProfil = it.toString())
            biodata = updatedBiodata
            MahasiswaRepository.saveMahasiswa(updatedBiodata)
            onUpdateBiodata(updatedBiodata)
            showImagePickerDialog = false
        }
    }

    // Launcher untuk mengambil gambar dari kamera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val updatedBiodata = biodata.copy(fotoProfil = "camera_demo")
            biodata = updatedBiodata
            MahasiswaRepository.saveMahasiswa(updatedBiodata)
            onUpdateBiodata(updatedBiodata)
            showImagePickerDialog = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profil Mahasiswa",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Toggle Dark Mode",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = onDarkModeToggle,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.LightGray
                            )
                        )
                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Profile dengan Foto
                item {
                    ProfileHeader(
                        biodata = biodata,
                        onImageClick = { showImagePickerDialog = true }
                    )
                }

                // Informasi Akademik
                item {
                    AcademicInfoCard(biodata = biodata)
                }

                // Informasi Pribadi
                item {
                    PersonalInfoCard(biodata = biodata)
                }

                // Hobby Section
                item {
                    HobbyCard(hobbies = biodata.hobi)
                }

                // Tombol Share
                item {
                    OutlinedButton(
                        onClick = {
                            val shareIntent = android.content.Intent().apply {
                                action = android.content.Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(
                                    android.content.Intent.EXTRA_TEXT,
                                    "Profil Mahasiswa:\nNama: ${biodata.nama}\nNIM: ${biodata.nim}\nJurusan: ${biodata.jurusan}"
                                )
                            }
                            context.startActivity(android.content.Intent.createChooser(shareIntent, "Share Profil via"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share Profil", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }

    // Dialog Edit Profile
    if (showEditDialog) {
        EditProfileDialog(
            tempBiodata = tempBiodata,
            onTempBiodataChange = { tempBiodata = it },
            onDismiss = { showEditDialog = false },
            onSave = {
                biodata = tempBiodata
                MahasiswaRepository.saveMahasiswa(tempBiodata)
                onUpdateBiodata(tempBiodata)
                showEditDialog = false
            }
        )
    }

    // Dialog Pilih Foto
    if (showImagePickerDialog) {
        ImagePickerDialog(
            onGalleryClick = { galleryLauncher.launch("image/*") },
            onCameraClick = { cameraLauncher.launch(null) },
            onDismiss = { showImagePickerDialog = false }
        )
    }
}

@Composable
fun ProfileHeader(
    biodata: BiodataMahasiswa,
    onImageClick: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto Profil dengan border gradient
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
                        ),
                        shape = CircleShape
                    )
                    .clickable { onImageClick() }
            ) {
                if (biodata.fotoProfil.isNotEmpty() && biodata.fotoProfil != "camera_demo") {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(biodata.fotoProfil)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto Profil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Default Avatar",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = biodata.nama,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = biodata.nim,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Status badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Mahasiswa Aktif",
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun AcademicInfoCard(biodata: BiodataMahasiswa) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Informasi Akademik",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(label = "Jurusan", value = biodata.jurusan)
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow(label = "Program Studi", value = biodata.prodi)
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow(label = "Angkatan", value = biodata.angkatan)
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow(label = "Semester", value = biodata.semester)
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow(label = "IPK", value = biodata.ipk)
        }
    }
}

@Composable
fun PersonalInfoCard(biodata: BiodataMahasiswa) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Informasi Pribadi",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(label = "Email", value = biodata.email)
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow(label = "No. Telepon", value = biodata.noHp)
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow(label = "Tempat Lahir", value = biodata.tempatLahir)
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow(label = "Tanggal Lahir", value = biodata.tanggalLahir)
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
            InfoRow(label = "Alamat", value = biodata.alamat)
        }
    }
}

@Composable
fun HobbyCard(hobbies: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hobby",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                hobbies.forEach { hobby ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = hobby, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = ":",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.5f)
        )
    }
}

@Composable
fun EditProfileDialog(
    tempBiodata: BiodataMahasiswa,
    onTempBiodataChange: (BiodataMahasiswa) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    // State untuk setiap field
    var nama by remember { mutableStateOf(tempBiodata.nama) }
    var jurusan by remember { mutableStateOf(tempBiodata.jurusan) }
    var prodi by remember { mutableStateOf(tempBiodata.prodi) }
    var angkatan by remember { mutableStateOf(tempBiodata.angkatan) }
    var email by remember { mutableStateOf(tempBiodata.email) }
    var noHp by remember { mutableStateOf(tempBiodata.noHp) }
    var alamat by remember { mutableStateOf(tempBiodata.alamat) }
    var tempatLahir by remember { mutableStateOf(tempBiodata.tempatLahir) }
    var tanggalLahir by remember { mutableStateOf(tempBiodata.tanggalLahir) }
    var hobi by remember { mutableStateOf(tempBiodata.hobi.joinToString(", ")) }
    var fotoProfil by remember { mutableStateOf(tempBiodata.fotoProfil) }

    // Launcher untuk memilih gambar dari galeri di dalam dialog
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            fotoProfil = it.toString()
            onTempBiodataChange(tempBiodata.copy(fotoProfil = it.toString()))
        }
    }

    // Update state ketika tempBiodata berubah
    LaunchedEffect(tempBiodata) {
        nama = tempBiodata.nama
        jurusan = tempBiodata.jurusan
        prodi = tempBiodata.prodi
        angkatan = tempBiodata.angkatan
        email = tempBiodata.email
        noHp = tempBiodata.noHp
        alamat = tempBiodata.alamat
        tempatLahir = tempBiodata.tempatLahir
        tanggalLahir = tempBiodata.tanggalLahir
        hobi = tempBiodata.hobi.joinToString(", ")
        fotoProfil = tempBiodata.fotoProfil
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Text(
                    text = "Edit Profil",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Edit data biodata Anda di bawah ini",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Form fields
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        // Input Foto Profil
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .clickable { galleryLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                if (fotoProfil.isNotEmpty()) {
                                    AsyncImage(
                                        model = fotoProfil,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                            TextButton(onClick = { galleryLauncher.launch("image/*") }) {
                                Text("Ganti Foto Profil", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                    item {
                        OutlinedTextField(
                            value = nama,
                            onValueChange = {
                                nama = it
                                onTempBiodataChange(
                                    tempBiodata.copy(nama = it)
                                )
                            },
                            label = { Text("Nama Lengkap") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = jurusan,
                            onValueChange = {
                                jurusan = it
                                onTempBiodataChange(
                                    tempBiodata.copy(jurusan = it)
                                )
                            },
                            label = { Text("Jurusan") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = prodi,
                            onValueChange = {
                                prodi = it
                                onTempBiodataChange(
                                    tempBiodata.copy(prodi = it)
                                )
                            },
                            label = { Text("Program Studi") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = angkatan,
                            onValueChange = {
                                angkatan = it
                                onTempBiodataChange(
                                    tempBiodata.copy(angkatan = it)
                                )
                            },
                            label = { Text("Angkatan") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                onTempBiodataChange(
                                    tempBiodata.copy(email = it)
                                )
                            },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = noHp,
                            onValueChange = {
                                noHp = it
                                onTempBiodataChange(
                                    tempBiodata.copy(noHp = it)
                                )
                            },
                            label = { Text("No. Telepon") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = tempatLahir,
                            onValueChange = {
                                tempatLahir = it
                                onTempBiodataChange(
                                    tempBiodata.copy(tempatLahir = it)
                                )
                            },
                            label = { Text("Tempat Lahir") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = tanggalLahir,
                            onValueChange = {
                                tanggalLahir = it
                                onTempBiodataChange(
                                    tempBiodata.copy(tanggalLahir = it)
                                )
                            },
                            label = { Text("Tanggal Lahir") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = alamat,
                            onValueChange = {
                                alamat = it
                                onTempBiodataChange(
                                    tempBiodata.copy(alamat = it)
                                )
                            },
                            label = { Text("Alamat") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 2,
                            maxLines = 3
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = hobi,
                            onValueChange = {
                                hobi = it
                                onTempBiodataChange(
                                    tempBiodata.copy(hobi = it.split(",").map { s -> s.trim() }.filter { s -> s.isNotEmpty() })
                                )
                            },
                            label = { Text("Hobi (pisahkan dengan koma)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Batal", color = MaterialTheme.colorScheme.primary)
                    }

                    Button(
                        onClick = onSave,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Simpan Perubahan")
                    }
                }
            }
        }
    }
}

@Composable
fun ImagePickerDialog(
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Ganti Foto Profil",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text("Pilih sumber gambar:")
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onGalleryClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Photo, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Galeri")
                    }
                    Button(
                        onClick = onCameraClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Kamera")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileScreen(
            nim = "23083000096",
            isDarkMode = false,
            onDarkModeToggle = {}
        )
    }
}