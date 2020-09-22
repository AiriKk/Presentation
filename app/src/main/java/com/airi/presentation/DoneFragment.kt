package com.airi.presentation

//import com.github.kittinunf.fuel.core.FuelError
//import com.github.kittinunf.fuel.core.Request
//import com.github.kittinunf.fuel.core.Response
//import com.github.kittinunf.fuel.httpGet
//import com.github.kittinunf.result.Result
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.fragment_done.*
import kotlinx.android.synthetic.main.fragment_open.*
import okhttp3.*
import org.json.JSONException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class DoneFragment : Fragment() {

    var mRealm:Realm? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_done, container, false)
        var kekka = ""
        requireArguments()?.let {
            kekka = requireArguments().getString("sentences") ?:""
        }

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
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
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
                        val adapter = CountAdapter(requireActivity(), returnListViewItems(list))

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
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })




        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scoreB.setVisibility(View.INVISIBLE);
    val time = requireArguments()!!.getInt("time")
    val hour = time / 3600
    val min = (time - hour * 3600) / 60
    val sec = time % 60
    val hourText = String.format("%02d", hour)
    val minText = String.format("%02d", min)
    val secText = String.format("%02d", sec)

        //ここの時間の表示方法ってもっと良いやり方ないのかな、、
        val editText = editText.findViewById(R.id.editText) as EditText

        timerr.text = "${hourText}:${minText}:${secText}"

        Realm.init(context)
        mRealm = Realm.getDefaultInstance()

        back.setOnClickListener {

            if(editText.getText().toString().equals("") == false){

                if(sentences.getText().toString().equals("") == false){
                    val textTitle = editText.text.toString()
                    val sentences = sentences.text.toString()
                    Toast.makeText(context,"保存しました",Toast.LENGTH_LONG).show()
//            レルムにタイトルと本文をidを鍵にして保存する
                    create(title = textTitle, bunshou = sentences)
                }
                else{
                    Toast.makeText(context,"中身がからなので保存できませんでした",Toast.LENGTH_LONG).show()
                }
            }
            findNavController().navigate(R.id.action_DoneFragment_to_StartFragment)
        }
//        scoreB.setOnClickListener {
//            findNavController().navigate(R.id.action_DoneFragment_to_ScoreFragment)
//        }
    }

    fun create(title:String, bunshou:String){
        mRealm!!.executeTransaction {
            var saved = mRealm!!.createObject(Saved::class.java , UUID.randomUUID().toString())
            saved.title = title
            saved.bunshou = bunshou
            mRealm!!.copyToRealm(saved)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mRealm!!.close()
    }
    var words = 0
    fun returnListViewItems(list: MutableList<String>): Array<Pair<String,Int>>  {

        val array = list.toList()
        var countArray = arrayOf<Pair<String,Int>>()
        for (word in array){
            var isAdd = true
            for (i in countArray.indices ) {
                if (countArray[i].first == word) {
                    countArray[i] = Pair(word,countArray[i].second + 1)
                    isAdd = false
                    words = words + 1
                    break
                }
            }
            if (isAdd) {
                countArray += Pair(word,1)
                words = words + 1
            }
        }
        wordCount.text = words.toString()+"語"
        return countArray
    }
//    val speed = ((time /words)*60).toString()+"語/min"

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