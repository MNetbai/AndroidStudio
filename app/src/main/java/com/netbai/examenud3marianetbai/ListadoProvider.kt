package com.netbai.examenud3marianetbai

import android.os.Handler
import android.os.Looper
import com.netbai.examenud3marianetbai.Listado.Companion.obtenerTiempoActual

object ListadoProvider {
    val lista = mutableListOf<Listado>()
    private var contador = 1

    //para manejar el tiempo de los elementos insertado
    private val handler = Handler(Looper.getMainLooper())

    init {
        inicializaLista()
        programarInsercionPeriodica()
    }

    private fun inicializaLista() {
        lista.clear()
        for (i in 0 until 5) {
            val nuevoListado = Listado(
                id = contador,
                nombre = Listado.nombres[(contador - 1) % Listado.nombres.size],
                tiempo = obtenerTiempoActual()
            )
            lista.add(nuevoListado)
            contador++
        }
    }

    fun generarNuevoListado(): Listado {
        val nuevoListado = Listado(
            id = contador,
            nombre = Listado.nombres[(contador - 1) % Listado.nombres.size],
            tiempo = obtenerTiempoActual()
        )
        contador++
        return nuevoListado
    }

    private fun programarInsercionPeriodica() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val nuevoListado = generarNuevoListado()

                handler.post {
                    lista.add(nuevoListado)
                }

                handler.postDelayed(this, 5000)
            }
        }, 5000)
    }

    fun get(): List<Listado> = lista

    fun insertaPersonas(persona: Listado) {
        lista.add(persona)
    }
}