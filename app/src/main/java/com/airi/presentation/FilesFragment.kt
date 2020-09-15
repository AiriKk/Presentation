package com.airi.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_files.*




class FilesFragment : Fragment(){

    var mRealm: Realm? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_files, container, false)
        Realm.init(context)
        mRealm = Realm.getDefaultInstance()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = read()
//        val adapter = ArrayAdapter(
//             context,
//            android.R.layout.simple_list_item_1,
//            data
//        )

        val adapter = FileAdapter(requireContext(), data, true)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        cancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    fun read() : RealmResults<Saved> {
        return mRealm!!.where(Saved::class.java).findAll()
//            .contains("title", "").contains("honbun", "").
//        var taitoru = mRealm!!.where(Saved::class.java).equalTo("title","").findFirst()
//        var honbun = mRealm!!.where(Saved::class.java).equalTo("honbun","").findFirst()
    }
    override fun onDestroy() {
        super.onDestroy()
        mRealm!!.close()
    }

}