package com.airi.presentation

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.atilika.kuromoji.TokenizerBase
import com.atilika.kuromoji.ipadic.Tokenizer
import kotlinx.android.synthetic.main.fragment_done.*
import kotlinx.android.synthetic.main.fragment_presenting.*
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
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.JAPAN.toString())
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声聞き取り中...")

        try {
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
                    spoken.text = already + results[0] + " "
                    resultText = already + results[0] + " "
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stop.setVisibility(View.GONE);
        lateinit var pTimer: Timer
        val pHandler = Handler()
        var time = 0
        var timed = false

        hajime.setOnClickListener {
            timed =true
            listen()
            hajime.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            hajime.text = "Start"
            pTimer = Timer()
            pTimer.schedule(object : TimerTask() {
                override fun run() {
                    pHandler.post {
                        time++
                        val hour = time / 3600
                        val min = (time - hour * 3600) / 60
                        val sec = time % 60
                        val hourText = String.format("%02d", hour)
                        val minText = String.format("%02d", min)
                        val secText = String.format("%02d", sec)

                        timer.text = "${hourText}:${minText}:${secText}"

                    }
                }
            }, 1000, 1000)
        }

        stop.setOnClickListener {
            stop.setVisibility(View.GONE);
            hajime.setVisibility(View.VISIBLE);
                pTimer.cancel()
            already = resultText;
            AlertDialog.Builder(context)
                .setTitle("")
                .setMessage("一時停止中")
                .setNegativeButton("キャンセル") { dialog, which ->
                    hajime.text = "Restart";
                    hajime.setVisibility(View.VISIBLE);
                    stop.setVisibility(View.VISIBLE);
                }
                .setPositiveButton("終了する") { dialog, which ->

                    val params = bundleOf("time" to time, "sentences" to resultText)

                    findNavController().navigate(R.id.action_PresentingFragment_to_DoneFragment, params)
                }
                .show()
        }

        front.setOnClickListener {
            if(timed == true){
                pTimer.cancel()
                timed =false
            }
            AlertDialog.Builder(context)
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