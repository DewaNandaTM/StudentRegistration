package com.example.studentregistration

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.studentregistration.data.AppDatabase
import com.example.studentregistration.ui.MainScreen
import com.example.studentregistration.viewmodel.StudentViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = AppDatabase.getDatabase(applicationContext).siswaDao()

        setContent {
            val viewModel = StudentViewModel(dao)

            MainScreen(viewModel = viewModel)
        }
    }
}
