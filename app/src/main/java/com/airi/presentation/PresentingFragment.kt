package com.airi.presentation

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_presenting.*
import java.util.*

class PresentingFragment : Fragment() {

    val REQUEST_CODE = 1000
    var resultText = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_presenting, container, false)
        return view
    }

    private fun listen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        // 言語が日本語
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.JAPAN.toString())
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声聞き取り中")

        try {
            // インテントを発行
            startActivityForResult(intent, REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            spoken.text = e.message
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // 認識結果を取得
                val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                // 内容があれば
                if (results.size > 0) {
                    // インデックス0の結果を表示
                    spoken.text = results[0]
                    resultText = results[0]
                }
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hajime.setOnClickListener {
            listen()
        }
        stop.setOnClickListener {
            AlertDialog.Builder(context) // FragmentではActivityを取得して生成
                .setTitle("Stop")
                .setMessage("メッセージ")
                .setPositiveButton("続ける", { dialog, which ->
                })
                .setNegativeButton("終了する", { dialog, which ->
                    val pref: SharedPreferences =
                        requireContext().getSharedPreferences("Data", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = pref.edit()
                    editor.putString("sentences", resultText)
                    editor.commit()

                    findNavController().navigate(R.id.action_PresentingFragment_to_DoneFragment)
                })
                .show()
        }

        front.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}