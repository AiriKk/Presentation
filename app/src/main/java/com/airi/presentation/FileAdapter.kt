package com.airi.presentation
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
class FileAdapter (private val activity: Activity, private val items: Array<Saved>) : BaseAdapter() {
    private class ViewHolder(row: View) {
        val textView = row.findViewById(R.id.textView) as TextView
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(android.R.layout.simple_list_item_1, convertView)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val item = items[position]
        viewHolder.textView.text = item.title
        return view
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItem(position: Int): Any {
        return items[position]
    }
    override fun getCount(): Int {
        return items.size
    }

}