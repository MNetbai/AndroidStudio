package com.netbai.examenud3marianetbai

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.netbai.examenud3marianetbai.databinding.FragmentListBinding

class ListFragment : Fragment(){
    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        //obtenemos la lista de datos del SharedViewModel
        val sharedViewModel: SharedViewModel by activityViewModels()
        val listaDeDatos = sharedViewModel.listaDeDatos.value?.toMutableList()


        //seteamos el adaptador
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val adapter = listaDeDatos?.let {
                    ListadoAdapter(it, object : ListadoAdapter.OnItemClickListener {
                        override fun onItemClick(listado: Listado) {
                            sharedViewModel.setSelectedItem(listado)
                        }
                    }, sharedViewModel)
                }
                this.adapter = adapter
                //notificamos el item cambiado
                sharedViewModel.nombreModificado.observe(viewLifecycleOwner) { nuevoNombre ->
                    listaDeDatos?.let { lista ->
                        sharedViewModel.selectedItem.value?.let { selectedItem ->
                            val index = lista.indexOf(selectedItem)
                            if (index != -1) {
                                lista[index].nombre = nuevoNombre
                                adapter?.notifyItemChanged(index)
                            }
                        }
                    }
                }
                //notifiacmos el item borrado
                sharedViewModel.itemBorrado.observe(viewLifecycleOwner) { deletedItem ->
                    listaDeDatos?.let { lista ->
                        val index = lista.indexOf(deletedItem)
                        if (index != -1) {
                            lista.removeAt(index)
                            adapter?.notifyItemRemoved(index)
                        }
                    }
                }
                //para el añadido de elementos a la lista cada 5 segundos
                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        listaDeDatos?.let {
                            if (it.size < ListadoProvider.lista.size) {
                                val nuevoListado = ListadoProvider.lista[it.size]
                                Handler(Looper.getMainLooper()).post {
                                    it.add(nuevoListado)
                                    adapter?.notifyItemInserted(it.size - 1)
                                    sharedViewModel.agregarNuevoElemento()
                                }
                            }
                        }
                        //compruebo si el adaptador es nulo antes de usarlo
                        adapter?.let {
                            Handler(Looper.getMainLooper()).postDelayed(this, 5000)
                            //ahora notifico al adaptador después de realizar cambios en la lista
                            it.notifyDataSetChanged()
                        }
                    }
                }, 5000)
            }
        }
        return view
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}