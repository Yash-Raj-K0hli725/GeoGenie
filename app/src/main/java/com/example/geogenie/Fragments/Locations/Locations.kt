package com.example.geogenie.Fragments.Locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geogenie.Main.mainModel
import com.example.geogenie.R
import com.example.geogenie.databinding.FragmentLocationsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Locations : Fragment() {
    private lateinit var bind: FragmentLocationsBinding
    private lateinit var sharedVm: mainModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_locations, container, false)
        sharedVm = ViewModelProvider(requireActivity())[mainModel::class.java]
        // Inflate the layout for this fragment
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedVm.SightList.observe(viewLifecycleOwner){
            sharedVm.locationsListAdapter.submitList(it)
        }
        bind.locationsList.adapter = sharedVm.locationsListAdapter
        bind.locationsList.layoutManager = LinearLayoutManager(requireContext())
    }

}