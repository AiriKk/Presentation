package com.airi.presentation
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    lateinit var listener: OnItemClickListener
    override fun getItemCount(): Int = taskList?.size ?: 0

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val saved: Saved = taskList?.get(position) ?: return
        holder.titleTextView.text = saved.title
        holder.itemView.setOnClickListener{
            listener.onItemClickListener(it, position, saved.bunshou)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FileViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.file_item, viewGroup, false)
        return FileViewHolder(v)
    }
    interface OnItemClickListener{
        fun onItemClickListener(view: View, position: Int, clickedText: String)
    }

    // リスナー
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    class FileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.filename)
    }

}