package com.example.sem_05_futbolistas


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sem_05_futbolistas.ui.theme.Sem_05_FutbolistasTheme
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
class AddFutbolistaActivity : ComponentActivity() {

        private val db = FirebaseFirestore.getInstance()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContent {
                Sem_05_FutbolistasTheme {
                    var nombre by remember { mutableStateOf("") }
                    var nacionalidad by remember { mutableStateOf("") }
                    var equipo by remember { mutableStateOf("") }

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Agregar Futbolista") }
                            )
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            OutlinedTextField(
                                value = nombre,
                                onValueChange = { nombre = it },
                                label = { Text("Nombre") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = nacionalidad,
                                onValueChange = { nacionalidad = it },
                                label = { Text("Nacionalidad") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = equipo,
                                onValueChange = { equipo = it },
                                label = { Text("Equipo") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (nombre.isNotBlank() && nacionalidad.isNotBlank() && equipo.isNotBlank()) {
                                        val futbolista = Futbolista(
                                            nombre = nombre,
                                            nacionalidad = nacionalidad,
                                            equipo = equipo
                                        )

                                        db.collection("futbolistas")
                                            .add(futbolista)
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    this@AddFutbolistaActivity,
                                                    "Futbolista agregado",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                finish() // volver a la lista
                                            }
                                            .addOnFailureListener { e ->
                                                Log.w("Firestore", "Error al guardar", e)
                                                Toast.makeText(
                                                    this@AddFutbolistaActivity,
                                                    "Error al guardar",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    } else {
                                        Toast.makeText(
                                            this@AddFutbolistaActivity,
                                            "Completa todos los campos",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Guardar Futbolista")
                            }
                        }
                    }
                }
            }
        }
    }