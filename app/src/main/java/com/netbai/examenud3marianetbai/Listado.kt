package com.netbai.examenud3marianetbai

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Listado(
    var id: Int,
    var nombre: String = nombres.random(),
    var tiempo: String = obtenerTiempoActual()
) {

    companion object {
        //lista de nombres aleatorios
        val nombres = listOf(
            "Ana", "Beatriz", "Carlos", "Diana", "Ernesto", "Francisco", "Gema", "Héctor", "Isabel",
            "Juan", "Luis", "María", "Nicolás", "Olga", "Pedro", "Rosa", "Salvador", "Bernardo"
        )

        fun obtenerTiempoActual(): String {
            val formato = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            return formato.format(Date())
        }
    }
}
