package com.airi.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.atilika.kuromoji.TokenizerBase
import com.atilika.kuromoji.ipadic.Tokenizer
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_start, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tokenizer = Tokenizer.Builder().mode(TokenizerBase.Mode.NORMAL).build()
        val tokens = tokenizer.tokenize("我輩は猫である。名前はまだない。");
        tokens.forEach {
            Log.d("Kuromoji", "${it.allFeatures}")
        }

        start.setOnClickListener {
            findNavController().navigate(R.id.action_StartFragment_to_PresentingFragment)
        }
        info.setOnClickListener {
            findNavController().navigate(R.id.action_StartFragment_to_InfoFragment)
        }
        previous.setOnClickListener {
            findNavController().navigate(R.id.action_StartFragment_to_FilesFragment)
        }
    }
}