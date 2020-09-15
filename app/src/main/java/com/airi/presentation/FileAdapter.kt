package com.airi.presentation
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class FileAdapter (
    private val context: Context,
    private var taskList: OrderedRealmCollection<Saved>?,
    private val autoUpdate: Boolean
) :
    RealmRecyclerViewAdapter<Saved, FileAdapter.FileViewHolder>(taskList, autoUpdate) {

    override fun getItemCount(): Int = taskList?.size ?: 0

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val saved: Saved = taskList?.get(position) ?: return

        holder.titleTextView.text = saved.title
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FileViewHolder {
        val v = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, viewGroup, false)
        return FileViewHolder(v)
    }

    class FileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.textView)
    }

}