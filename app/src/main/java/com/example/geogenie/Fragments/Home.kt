package com.example.geogenie.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.geogenie.Main.mainModel
import com.example.geogenie.R
import com.example.geogenie.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class Home : Fragment() {
    private lateinit var bind: FragmentHomeBinding
    private lateinit var sharedVm: mainModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        sharedVm = ViewModelProvider(requireActivity())[mainModel::class.java]
        // Inflate the layout for this fragment
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.gitHub.setOnClickListener {
            gitProfile()
        }

        bind.submit.setOnClickListener {
            val destination = bind.destination.text.toString()
            if (checkTxtField()) {
                makeRequest(destination)
            }
        }

    }

    private fun checkTxtField(): Boolean {
        return bind.destination.text!!.isNotEmpty()
    }

    private fun gitProfile() {
        val gitProfile = "https://github.com/Yash-Raj-K0hli725"
        val gitIntent = Intent(Intent.ACTION_VIEW).apply {
            setData(Uri.parse(gitProfile))
        }
        startActivity(gitIntent)
    }

    private fun makeRequest(prompt: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                bind.loader.visibility = View.VISIBLE
                bind.submit.visibility = View.GONE
                val reponse = lifecycleScope.async(Dispatchers.IO) {
                    sharedVm.getSuggestions(prompt)
                }
                reponse.await()
                findNavController().navigate(HomeDirections.actionHomeToLocations())
            } catch (e: Exception) {
                bind.submit.visibility = View.VISIBLE
                bind.loader.visibility = View.GONE
                errorAlert(prompt)
            }
        }

    }

    private fun errorAlert(prompt: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Connection Error")
            .setMessage("Error while Connecting Please Retry")
            .setNegativeButton("Close") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("Retry") { _, _ ->
                makeRequest(prompt)
            }.show()
    }
}