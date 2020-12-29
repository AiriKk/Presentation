package com.airi.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_files.*

class FilesFragment : Fragment() {

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

        var adapter = FileAdapter(requireContext(), data, false)

        adapter?.let {

            adapter!!.setOnItemClickListener(object : FileAdapter.OnItemClickListener {
                override fun onItemClickListener(
                    view: View,
                    position: Int,
                    clickedText: String,
                    clickedTitle: String,
                    clickedTime: Int,
                    clickedDate: String?
                ) {
                    //画面遷移処理
//                TODO("Not yet implemented")
                    val par = bundleOf(
                        "text" to clickedText,
                        "tTitle" to clickedTitle,
                        "tTime" to clickedTime
                    )
                    findNavController().navigate(R.id.action_FilesFragment_to_OpenFragment, par)
                }

            })

            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter

            val swipeHandler = object : SwipeDelete(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewHolder?.let {
                        adapter.removeAt(it.adapterPosition)
                    }
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(recyclerView)

            cancel.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    fun read(): RealmResults<Saved> {
        return mRealm!!.where(Saved::class.java).findAll()
    }


    fun delete(): RealmResults<Saved> {
        val chosen = mRealm!!.where(Saved::class.java)
            .equalTo("id", id)
            .findAll()
        mRealm!!.executeTransaction {
            chosen.deleteFromRealm(0)
        }
        return mRealm!!.where(Saved::class.java).findAll()
    }


    override fun onDestroy() {
        super.onDestroy()
        mRealm!!.close()
    }

}