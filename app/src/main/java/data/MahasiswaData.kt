package com.example.myprofile.data

import android.content.Context
import android.content.SharedPreferences
import com.example.myprofile.screens.BiodataMahasiswa
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MahasiswaRepository {
    private var mahasiswaList = mutableListOf<BiodataMahasiswa>()

    init {
        // Data dummy untuk demo
        mahasiswaList.add(
            BiodataMahasiswa(
                nim = "Nim",
                nama = "Dwi Hikmal Akbar Ramadhan",
                jurusan = "Teknologi Informasi",
                prodi = "Sistem Informasi",
                angkatan = "2023",
                email = "nim@student.unmer.ac.id",
                noHp = "08815520154",
                alamat = "JL. Dieng Atas",
                tempatLahir = "Lumajang",
                tanggalLahir = "03 November 2004",
                fotoProfil = "",
                ipk = "3.75",
                semester = "6"
            )
        )
    }

    fun getMahasiswaByNim(nim: String): BiodataMahasiswa? {
        return mahasiswaList.find { it.nim == nim }
    }

    fun saveMahasiswa(biodata: BiodataMahasiswa) {
        val index = mahasiswaList.indexOfFirst { it.nim == biodata.nim }
        if (index != -1) {
            mahasiswaList[index] = biodata
        } else {
            mahasiswaList.add(biodata)
        }
    }

    fun getAllMahasiswa(): List<BiodataMahasiswa> {
        return mahasiswaList
    }
}