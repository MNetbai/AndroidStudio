package com.netbai.examenud3marianetbai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.netbai.examenud3marianetbai.databinding.FragmentCountersBinding

class CountersFragment : Fragment() {
    private lateinit var lblIns: TextView
    private lateinit var lblMod: TextView
    private lateinit var lblBor: TextView
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_counters, container, false)
        lblIns = view.findViewById(R.id.lbl_ins)
        lblMod = view.findViewById(R.id.lbl_mod)
        lblBor = view.findViewById(R.id.lbl_bor)

        //utilizamos el SharedViewModel
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        //para el contador de insertados
        sharedViewModel.contadorInsertados.observe(viewLifecycleOwner) { count ->
            lblIns.text = "Ins $count"
        }

        //contador de modificados
        sharedViewModel.contadorModificados.observe(viewLifecycleOwner) { count ->
            lblMod.text = "Mod $count"
        }

        //contador de borrados
        sharedViewModel.contadorBorrados.observe(viewLifecycleOwner) { count ->
            lblBor.text = "Bor $count"
        }

        return view
    }
}
