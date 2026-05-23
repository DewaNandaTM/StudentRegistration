package com.example.studentregistration.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.studentregistration.data.Siswa
import com.example.studentregistration.viewmodel.StudentViewModel

@Composable
fun MainScreen(viewModel: StudentViewModel) {
    val siswaList by viewModel.siswaList.collectAsState()

    var showFormDialog by remember { mutableStateOf(false) }
    var editingSiswa by remember { mutableStateOf<Siswa?>(null) }
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var studentToDelete by remember { mutableStateOf<Siswa?>(null) }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    nama = ""
                    email = ""
                    editingSiswa = null
                    showFormDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Siswa")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Registrasi Siswa",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Kelola data siswa",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Daftar Siswa",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (siswaList.isEmpty()) {
                Text(text = "Belum ada data siswa. Klik tombol + untuk menambah.")
            }

            LazyColumn {
                items(siswaList) { siswa ->
                    StudentItem(
                        siswa = siswa,
                        onDelete = {
                            studentToDelete = siswa
                        },
                        onEdit = {
                            nama = siswa.nama
                            email = siswa.email
                            editingSiswa = siswa
                            showFormDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showFormDialog) {
        AlertDialog(
            onDismissRequest = { showFormDialog = false },
            title = { Text(if (editingSiswa != null) "Edit Siswa" else "Tambah Siswa") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        label = { Text("Nama") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (nama.isNotBlank() && email.isNotBlank() && email.contains("@")) {
                        if (editingSiswa != null) {
                            viewModel.editSiswa(editingSiswa!!.copy(nama = nama, email = email))
                        } else {
                            viewModel.tambahSiswa(nama, email)
                        }
                        showFormDialog = false
                    }
                }) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFormDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    if (studentToDelete != null) {
        AlertDialog(
            onDismissRequest = { studentToDelete = null },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus data '${studentToDelete?.nama}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.hapusSiswa(studentToDelete!!)
                        studentToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { studentToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }
}