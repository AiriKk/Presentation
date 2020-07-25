package com.airi.presentation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_presenting.*
import java.io.IOException
import java.util.*

class PresentingFragment : Fragment() {

    val REQUEST_CODE = 1000
    var resultText = ""
    var already = ""


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
                    spoken.text = already + results[0] + "。"
                    resultText = already + results[0] + "。"
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stop.setVisibility(View.GONE);

        hajime.setOnClickListener {
            listen()
            hajime.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            hajime.text = "Start"
        }
        stop.setOnClickListener {
            stop.setVisibility(View.GONE);
            hajime.setVisibility(View.VISIBLE);
            already = resultText;
            AlertDialog.Builder(context) // FragmentではActivityを取得して生成
                .setTitle("")
                .setMessage("一時停止中")
                .setNegativeButton("キャンセル") { dialog, which ->
                    hajime.text = "Restart";
                    hajime.setVisibility(View.VISIBLE);
                    stop.setVisibility(View.VISIBLE);
                }
                .setPositiveButton("終了する") { dialog, which ->
                    val pref: SharedPreferences =
                        requireContext().getSharedPreferences("Data", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = pref.edit()
                    editor.putString("sentences", resultText)
                    editor.commit()

                    findNavController().navigate(R.id.action_PresentingFragment_to_DoneFragment)
                }
                .show()
        }

        front.setOnClickListener {
            AlertDialog.Builder(context) // FragmentではActivityを取得して生成
                .setTitle("")
                .setMessage("本当にやめますか")
                .setNegativeButton("キャンセル") { dialog, which ->
                }
                .setPositiveButton("やめる") { dialog, which ->
                    findNavController().popBackStack()
                }
                .show()
        }
    }

}