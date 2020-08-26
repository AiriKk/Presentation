package com.airi.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_done.*
import kotlinx.android.synthetic.main.fragment_files.*
import kotlinx.android.synthetic.main.fragment_start.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONException

class FilesFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_files, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancel.setOnClickListener {
            findNavController().navigate(R.id.action_FilesFragment_to_StartFragment)
        }
    }
}