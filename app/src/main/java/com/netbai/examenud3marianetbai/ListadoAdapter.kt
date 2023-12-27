package com.netbai.examenud3marianetbai

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.netbai.examenud3marianetbai.databinding.ItemListaBinding

class ListadoAdapter(
    private val values: List<Listado>,
    private val itemClickListener: OnItemClickListener, //para manejar el click del item seleccionado
    private val sharedViewModel: SharedViewModel, //se llama desde el listfragment, cuando fijamos el adaptador
) : RecyclerView.Adapter<ListadoAdapter.ViewHolder>() {

    //interfaz para el click
    interface OnItemClickListener {
        fun onItemClick(listado: Listado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.rellena(item, itemClickListener)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val binding: ItemListaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun rellena(listado: Listado, itemClickListener: OnItemClickListener) {
            binding.lblId.text = listado.id.toString()
            binding.lblNombre.text = listado.nombre
            binding.lblTiempo.text = listado.tiempo
            itemView.setOnClickListener {
                itemClickListener.onItemClick(listado)
            }
        }
    }
}