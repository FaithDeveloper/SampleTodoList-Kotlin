package com.kcs.sampletodolist.view.todo

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import com.kcs.sampletodolist.R
import com.kcs.sampletodolist.common.Constants
import com.kcs.sampletodolist.dto.TodoDTO
import com.kcs.sampletodolist.dto.UserDTO
import com.kcs.sampletodolist.module.TodoRealmManager
import com.kcs.sampletodolist.module.UserRealmManager
import kotlinx.android.synthetic.main.activity_add_todo.*

class AddTodoActivity : AppCompatActivity() {

    private var userID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)

        userID = intent.getStringExtra(Constants.INTENT_DATA) ?: ""

        RxTextView.textChangeEvents(editToDoText)
                .map { t -> t.text().isNotEmpty() }
                .subscribe({
                    btn_done.isEnabled = it
                })

        initListener()
    }

    private fun initListener(){
        btn_done.setOnClickListener({
            var todoRealmManager = TodoRealmManager()
            var todo = TodoDTO()
            todo.todoID = System.currentTimeMillis()
            todo.userID = userID
            todo.content =  editToDoText.text.toString()
            todo.isTodo = false
            todoRealmManager.insertTodo(TodoDTO::class.java, todo)
            setResult(Activity.RESULT_OK)
            finish()
        })
        btn_cancel.setOnClickListener({
            finish()
        })
    }
}
