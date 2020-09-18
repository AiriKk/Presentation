package com.airi.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Contacts.PresenceColumns.INVISIBLE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_done.*
import kotlinx.android.synthetic.main.fragment_open.*
import kotlinx.android.synthetic.main.fragment_presenting.*
import okhttp3.*
import org.json.JSONException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class OpenFragment : Fragment(){

//    var oRealm : Realm? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_open, container, false)
        var Pkekka = ""
//        oRealm = Realm.getDefaultInstance()
////        requireArguments()?.let {
////            var Pkekka = oRealm!!.where(Saved::class.java)
//////                .equalTo("title","")
////                .findFirst()
////
////        }
        Pkekka = requireArguments().getString("sentences")?:""

        Log.d("###", "setOnClickListener")
        val client: OkHttpClient = OkHttpClient()
        val url: String = "http://maapi.net/apis/mecapi?"
        val body: FormBody = FormBody.Builder()
            .add("sentence", Pkekka)
            .build()

        val request = Request.Builder().url(url).post(body).build()
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                Log.d("faild", e.message)
//            }
//
//            override fun onResponse(call: Call, response: Response) {
////                    Log.d("###", response.message)
                val Ok = requireArguments().getString("text") ?:""
//                Log.d("###", "あと一歩")
//
//                val mainHandler = Handler(Looper.getMainLooper())
//                try {
//                    mainHandler.post {
//                        sSentence.text = Pkekka
//
//                        val Okekka = parseXml(Ok!!)
//
//                        // adapterを作成します
//                        val adapter = CountAdapter(requireActivity(), returnListViewItems(Okekka))
//
//                        // adapterをlistViewに紐付けます。
//                        Plist.adapter = adapter
//
//
//                        var countArray = arrayOf<Pair<String,Int>>()
//                        for (word in Okekka){
//                            var isAdd = true
//                            for (i in countArray.indices ) {
//                                if (countArray[i].first == word) {
//                                    countArray[i] = Pair(word,countArray[i].second + 1)
//                                    isAdd = false
//                                    break
//                                }
//                            }
//                            if (isAdd) {
//                                countArray += Pair(word,1)
//                            }
//                        }
//                    }
//
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//
//            }
//        })


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        sSentence.text = Ok.toString()
        
        textView5.setVisibility(View.INVISIBLE);

        back2.setOnClickListener {
            findNavController().popBackStack()

        }
    }


    var Owords = 0
//    fun returnListViewItems(Olist: MutableList<String>): Array<Pair<String,Int>>  {
//
//        val array = Olist.toList()
//        var countArray = arrayOf<Pair<String,Int>>()
//        for (word in array){
//            var isAdd = true
//            for (i in countArray.indices ) {
//                if (countArray[i].first == word) {
//                    countArray[i] = Pair(word,countArray[i].second + 1)
//                    isAdd = false
//                    Owords = Owords + 1
//                    break
//                }
//            }
//            if (isAdd) {
//                countArray += Pair(word,1)
//                Owords = Owords + 1
//            }
//        }
//        sWords.text = Owords.toString()+"語"
//        return countArray
//    }

//    @Throws(XmlPullParserException::class, IOException::class)
//    private fun parseXml(inputString:String) : MutableList<String> {
//        //配列の初期化・宣言
//        var Okekka = mutableListOf<String>()
//        val factory =
//            XmlPullParserFactory.newInstance()
//        val parser = factory.newPullParser()
//        val responseInputStream: InputStream = ByteArrayInputStream(inputString.toByteArray(charset("utf-8")))
//        parser.setInput(responseInputStream,"utf-8")
//        var eventType = parser.eventType
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//            if (eventType == XmlPullParser.START_TAG) {
//                Log.d(parser.name, "Startタグでした")
//                if (parser.name == "surface") {
//                    //配列に代入
//                    Okekka.add(parser.nextText())
//
//                }
//                if (parser.name == "feature") {
//                }
//            }
//            else if(eventType == XmlPullParser.TEXT) {
//                Log.d(parser.name, "要素でした")
//            }
//            else if(eventType == XmlPullParser.END_TAG){
//                Log.d(parser.name, "Endタグでした")
//
//            }
//            eventType = parser.next()
//        }
//        return Okekka
//    }

}