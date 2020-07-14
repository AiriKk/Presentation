package com.airi.presentation

//import com.github.kittinunf.fuel.core.FuelError
//import com.github.kittinunf.fuel.core.Request
//import com.github.kittinunf.fuel.core.Response
//import com.github.kittinunf.fuel.httpGet
//import com.github.kittinunf.result.Result
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_done.*
import kotlinx.android.synthetic.main.fragment_presenting.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import java.io.IOException

class DoneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_done, container, false)

        val pref: SharedPreferences = requireContext().getSharedPreferences("Data", Context.MODE_PRIVATE)
        val kekka = pref.getString("sentences","読み込めませんでした")
        Log.d("###", "setOnClickListener")
        val client: OkHttpClient = OkHttpClient()
        Log.d("$$$$$$$", kekka)
        val url: String = "http://maapi.net/apis/mecapi?"
        val body: FormBody = FormBody.Builder()
            .add("sentence", kekka)
            .add("response", "")
            .add("filter", "")
            .add("format", "")
            .add("dic", "")
            .build()

        val request = Request.Builder().url(url).post(body).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d("faild", e.message)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
//                    Log.d("###", response.message)
                val responseText: String? = response.body?.string()
                Log.d("###", "あと一歩")

                val mainHandler = Handler(Looper.getMainLooper())
                try {
                    mainHandler.post {
                        sentences.text = responseText
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })

//        "http://maapi.net/apis/mecapi".httpGet()
//            .response { request: Request, response: Response, result: Result<ByteArray, FuelError> ->
//            }



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back.setOnClickListener {
            findNavController().navigate(R.id.action_DoneFragment_to_StartFragment)
        }
    }
}