package com.kcs.sampletodolist.view.todo

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.kcs.sampletodolist.R
import com.kcs.sampletodolist.dto.TodoDTO
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by kcs on 2018. 6. 9..
 */
interface OnItemClickListener {
    fun itemClick(position: Int)
    fun itemDeleteClick(position: Int)
    fun toDoItemClick(isChecked: Boolean,position:Int)
}

class TodoAdapter(val context: Context, val dataList: List<TodoDTO>?, val listener: OnItemClickListener) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var datas : MutableList<TodoDTO>? = null

    /**
     * View Holder 연결
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.cell_todo_list, parent, false)
        return ViewHolder(view)
    }

    /**
     * Recycler 에 표시할 셀 갯수
     */
    override fun getItemCount(): Int {
        if(datas == null){
            return 0
        }
        return datas!!.size
    }

    /**
     * View가 Bind 되었을 때 설정
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder == null || datas == null){
            return
        }

        holder.check_todo?.isChecked = datas!![position].isTodo
        holder.check_todo?.setOnClickListener({
            listener.toDoItemClick( holder.check_todo?.isChecked, position)
        })

        holder.txt_todo?.setText(datas!![position].content)

        val current = datas!![position].todoID
         val formatter = SimpleDateFormat("yyy-MM-dd HH:mm:ss")
        holder.txt_date?.setText(formatter.format(current))

        holder.btn_delete?.setOnClickListener({
            listener.itemDeleteClick(position)
        })

        holder.layout_container?.setOnClickListener({
            listener.itemClick(position)
        })
    }

    /**
     * 데이터 업데이트
     */
    fun setDataList(dataList: List<TodoDTO>?){
        if(dataList == null){
            return
        }
        this@TodoAdapter.datas = dataList.toMutableList()
    }

    /**
     * ViewHolder
     * */
    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        val layout_container= itemView?.findViewById<LinearLayout>(R.id.layout_container)
        val check_todo = itemView?.findViewById<CheckBox>(R.id.check_todo)
        val txt_todo = itemView?.findViewById<TextView>(R.id.txt_todo)
        val btn_delete = itemView?.findViewById<Button>(R.id.btn_delete)
        val txt_date = itemView?.findViewById<TextView>(R.id.txt_date)
    }
}