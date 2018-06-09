package com.kcs.sampletodolist.view.todo

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kcs.sampletodolist.R
import com.kcs.sampletodolist.dto.TodoDTO

/**
 * Created by kcs on 2018. 6. 9..
 */
interface OnItemClickListener {
    fun itemClick(position: Int)
    fun itemDeleteClick(position: Int)
}

class TodoAdapter(val context: Context, val dataList: List<TodoDTO>, val listener: OnItemClickListener) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.cell_todo_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder == null){
            return
        }


    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){

    }
}