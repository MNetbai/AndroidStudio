package com.netbai.examenud3marianetbai

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView


class DetailFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var editTextNombre: EditText
    lateinit var txtId: TextView
    lateinit var txtNombre: TextView
    private lateinit var btnModificar: Button
    private lateinit var btnBorrar: Button
    private var listado: Listado? = null
    //private var onItemDeletedListener: OnItemDeletedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ocultamos al principio el fragmento
        view.visibility = View.INVISIBLE

        editTextNombre = view.findViewById(R.id.editTextNombre)
        txtId = view.findViewById(R.id.txtId)
        txtNombre = view.findViewById(R.id.txtNombre)
        btnModificar = view.findViewById(R.id.btnModificar)
        btnBorrar = view.findViewById(R.id.btnBorrar)

        //cuando seleccionamos un item para que muestra info
        sharedViewModel.selectedItem.observe(viewLifecycleOwner, Observer { selectedItem ->
            selectedItem?.let {
                listado = it
                if (it.id != null) {
                    txtId.text = "ID: ${it.id}"
                } else {
                    txtId.text = "ID: "
                }
                editTextNombre.setText(it.nombre)
                //al hacer click en un elemento mostramos fragmento
                view.visibility = View.VISIBLE
            }
        })

        //funcionamiento del modificar
        btnModificar.setOnClickListener {
            val nuevoNombre: String = obtenerNuevoNombre()
            if (nuevoNombre.isNotEmpty()) {
                sharedViewModel.modificarItem(nuevoNombre)
                //ahora actualizamos el nombre cambiado
                actualizarNombreEnDetailFragment(nuevoNombre)
                mostrarMensaje("Nombre modificado")
            } else {
                mostrarMensaje("El nombre no puede estar vacío")
            }
            view.visibility = View.INVISIBLE
        }

        //funcionamiento del eliminar
        btnBorrar.setOnClickListener {
            listado?.let {
                sharedViewModel.eliminarItem(it)
                mostrarMensaje("Persona eliminada de la lista")
                view.visibility = View.INVISIBLE
            }
        }
    }

    //OTRAS FUNCIONES
    fun mostrarMensaje(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }

    fun actualizarNombreEnDetailFragment(nuevoNombre: String) {
        listado?.nombre = nuevoNombre
        editTextNombre.setText(nuevoNombre)
    }

    private fun obtenerNuevoNombre(): String {
        return editTextNombre.text.toString()
    }

}