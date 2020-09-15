package com.airi.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_done.*
import kotlinx.android.synthetic.main.fragment_score.*
import kotlinx.android.synthetic.main.fragment_start.*

class ScoreFragment : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_score, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//            val pref: SharedPreferences = requireContext().getSharedPreferences("Data", Context.MODE_PRIVATE)
//        val sokudo = pref.getString("Speed","測定できませんでした")
//        Sokudo.text =sokudo

        B2don.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}