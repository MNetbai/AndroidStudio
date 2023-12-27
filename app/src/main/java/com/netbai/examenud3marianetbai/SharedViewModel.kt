package com.netbai.examenud3marianetbai

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    //para el item en cuestión
    private val _selectedItem = MutableLiveData<Listado?>()
    val selectedItem: MutableLiveData<Listado?> get() = _selectedItem

    //para el listado
    private val _listaDeDatos = MutableLiveData<List<Listado>>()
    val listaDeDatos: LiveData<List<Listado>> get() = _listaDeDatos

    //para el item borrado
    private val _itemBorrado = MutableLiveData<Listado>()
    val itemBorrado: LiveData<Listado> get() = _itemBorrado

    //para la modificación del nombre
    private val _nombreModificado = MutableLiveData<String>()
    val nombreModificado: LiveData<String> get() = _nombreModificado

    //Para llevar la cuenta de los elementos que se van insertando
    private val _contadorInsertados = MutableLiveData<Int>().apply { value = 5 } //se inicializa con el número de elementos fijos
    val contadorInsertados: LiveData<Int> get() = _contadorInsertados

    //Para llevar la cuenta de los elementos que se van modificando
    private val _contadorModificados = MutableLiveData<Int>().apply { value = 0 }
    val contadorModificados: LiveData<Int> get() = _contadorModificados

    //para el borrado de un item seleccionado
    private val _contadorBorrados = MutableLiveData<Int>().apply { value = 0 }
    val contadorBorrados: LiveData<Int> get() = _contadorBorrados

    //////////////// FUNCIONES ////////////////
    init {
        _listaDeDatos.value = ListadoProvider.get()
    }

    fun agregarNuevoElemento() {
        val nuevoListado = ListadoProvider.generarNuevoListado()
        ListadoProvider.insertaPersonas(nuevoListado)

        //se incrementa el contador de elementos insertados
        val contadorActual = _contadorInsertados.value ?: 0
        _contadorInsertados.value = contadorActual + 1
    }

    fun setSelectedItem(listado: Listado) {
        _selectedItem.value = listado
    }

    //función para modificar un item
    fun modificarItem(nuevoNombre: String) {
        val selectedItem = _selectedItem.value
        selectedItem?.let {
            val updatedItem = it.copy(nombre = nuevoNombre)
            //se actualiza la lista con el item modificado
            val currentList = _listaDeDatos.value.orEmpty().toMutableList()
            val indexOfItem = currentList.indexOf(it)
            if (indexOfItem != -1) {
                currentList[indexOfItem] = updatedItem
                _listaDeDatos.value = currentList
                //se incrementa el contador de modificados solo si el nombre realmente cambió
                if (it.nombre != nuevoNombre) {
                    _contadorModificados.value = (_contadorModificados.value ?: 0) + 1
                }
            }
            //se limpia el item seleccionado después de modificarlo
            _selectedItem.value = null
        }
    }

    //función para eliminar un item
    fun eliminarItem(item: Listado) {
        //eliminamos el item seleccionado
        _selectedItem.value = null

        //obtenemos el listado
        val currentList = _listaDeDatos.value.orEmpty().toMutableList()

        //se elimina el elemento seleccionado de la lista
        val removed = currentList.remove(item)

        if (removed) {
            //actualizamos la lista solo si el elemento es eliminado
            _listaDeDatos.value = currentList

            //notificamos el ítem  eliminado
            _contadorBorrados.value = (_contadorBorrados.value ?: 0) + 1
            _itemBorrado.value = item

            //ahora actualizamos los IDs y tiempos de los elementos restantes
            for ((index, listItem) in currentList.withIndex()) {
                listItem.id = index + 1 //volvemos a calcular el ID
                listItem.tiempo = Listado.obtenerTiempoActual() //aquó recalculamos la fecha y hora
            }
        }
    }

    //Esta función se llama cuando el viewmodel ya no sea necesario y sea destruido.
    override fun onCleared() {
        super.onCleared()
        Log.i("Maria","Se ha DESTRUIDO el viewmodel")
    }
}


