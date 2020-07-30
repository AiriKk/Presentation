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
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_done.*
import okhttp3.*
import org.json.JSONException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

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

            override fun onResponse(call: Call, response: Response) {
//                    Log.d("###", response.message)
                val responseText: String? = response.body?.string()
                Log.d("###", "あと一歩")

                val mainHandler = Handler(Looper.getMainLooper())
                try {
                    mainHandler.post {
                        sentences.text = kekka

                        val list = parseXml(responseText!!)

                        // adapterを作成します
                        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, list)

                        // adapterをlistViewに紐付けます。
                        lists.adapter = adapter


                        var countArray = arrayOf<Pair<String,Int>>()
                        for (word in list){
                            var isAdd = true
                            for (i in countArray.indices ) {
                                if (countArray[i].first == word) {
                                    countArray[i] = Pair(word,countArray[i].second + 1)
                                    isAdd = false
                                    break
                                }
                            }
                            if (isAdd) {
                                countArray += Pair(word,1)
                            }
                        }
//                        println(countArray.flatMap { listOf(it.first, it.second) })
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

    @Throws(XmlPullParserException::class, IOException::class)
    private fun parseXml(inputString:String) : MutableList<String> {
        //配列の初期化・宣言
        var list = mutableListOf<String>()
        val factory =
            XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        val responseInputStream: InputStream = ByteArrayInputStream(inputString.toByteArray(charset("utf-8")))
        parser.setInput(responseInputStream,"utf-8")
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                Log.d(parser.name, "Startタグでした")
                if (parser.name == "surface") {
                    //配列に代入
                    list.add(parser.nextText())

                }
                if (parser.name == "feature") {
                }
            }
            else if(eventType == XmlPullParser.TEXT) {
                Log.d(parser.name, "要素でした")
            }
            else if(eventType == XmlPullParser.END_TAG){
                Log.d(parser.name, "Endタグでした")

            }
            eventType = parser.next()
        }
        return list
    }
}