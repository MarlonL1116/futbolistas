package com.example.sem_05_futbolistas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sem_05_futbolistas.ui.theme.Sem_05_FutbolistasTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val futbolistas = mutableStateListOf<Futbolista>()
        val listaFutbolistas = listOf(
            Futbolista("Paulo Dybala", "Argentina", "AS Roma"),
            Futbolista("Karim Benzema", "Francia", "Al Ittihad"),
            Futbolista("Vinícius Jr", "Brasil", "Real Madrid"),
            Futbolista("Jude Bellingham", "Inglaterra", "Real Madrid"),
            Futbolista("Pedri González", "España", "Barcelona"),

        )




        val db = FirebaseFirestore.getInstance()
        val coleccion = db.collection("futbolistas")

        for (jugador in listaFutbolistas) {
            coleccion.add(jugador)
        }
        // Escuchar cambios en la colección
        db.collection("futbolistas")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("Firestore", "Error al leer futbolistas", e)
                    return@addSnapshotListener
                }

                futbolistas.clear()
                for (doc in snapshots!!) {
                    val jugador = doc.toObject(Futbolista::class.java)
                    futbolistas.add(jugador)
                }
            }

        setContent {
            Sem_05_FutbolistasTheme {
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            startActivity(Intent(this, AddFutbolistaActivity::class.java))
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar")
                        }
                    }
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        items(futbolistas) { jugador ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = jugador.nombre, style = MaterialTheme.typography.titleMedium)
                                    Text(text = "Nacionalidad: ${jugador.nacionalidad}")
                                    Text(text = "Equipo: ${jugador.equipo}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}