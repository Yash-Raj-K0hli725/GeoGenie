package com.example.geogenie.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.geogenie.Main.MainActivity
import com.example.geogenie.Main.mainModel
import com.example.geogenie.R
import com.example.geogenie.databinding.FragmentDetailedViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailedView : Fragment() {
    private lateinit var bind: FragmentDetailedViewBinding
    private lateinit var sharedVm: mainModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_detailed_view, container, false)
        sharedVm = ViewModelProvider(requireActivity())[mainModel::class.java]
        // Inflate the layout for this fragment
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gemini = DetailedViewArgs.fromBundle(requireArguments()).loactionInfo
        bind.placeName.text = gemini.name
        bind.ratingButton.text = gemini.rating

        lifecycleScope.launch(Dispatchers.IO) {
            sharedVm.getDetailedDescription(gemini.name)
        }

        sharedVm.DetailedDescription.observe(viewLifecycleOwner) {
            bind.descLoader.visibility = View.GONE
            bind.description.text = formateResponse(it)

        }
    }

    fun formateResponse(response: String): String {
        val temp_res = response.replace("*", "")
        return temp_res
    }
}