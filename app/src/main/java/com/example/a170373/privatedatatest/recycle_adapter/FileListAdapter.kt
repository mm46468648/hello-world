package com.example.a170373.privatedatatest.recycle_adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.a170373.privatedatatest.activity.MainActivity

class FileListAdapter(val context: Context,var fileList:ArrayList<String> ): RecyclerView.Adapter<FileListViewHolder>() {
    var onItemClickLis: ((filePath:String)->Unit)? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FileListViewHolder{
        val textView = TextView(context)
        textView.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        return FileListViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    override fun onBindViewHolder(p0: FileListViewHolder, p1: Int) {
        val textView = p0.itemView as TextView
        textView.text = fileList[p1]
        textView.setOnClickListener {
            onItemClickLis?.invoke( fileList[p1])
        }
    }

}
class FileListViewHolder(view: View):RecyclerView.ViewHolder(view)