package com.kcs.sampletodolist.view.main

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import com.kcs.sampletodolist.common.Constants
import com.kcs.sampletodolist.R
import com.kcs.sampletodolist.view.todo.TodoListFragment

class MainActivity : AppCompatActivity() {

    private var currentPageConstant : PageConstant? = null
    private lateinit var todoListFragment: TodoListFragment
    private var userID : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userID = intent.getStringExtra(Constants.INTENT_DATA) as String
//        txt_id.text = useIdD

        replaceFragment(PageConstant.TODO, null)
    }

    // 화면 이동
    private fun replaceFragment(tag: PageConstant?, bundle: Bundle?) {
        if (currentPageConstant != null && currentPageConstant == tag) {
            return
        }

        var intentBundle = Bundle()

        if (bundle != null) {
            intentBundle = bundle
        }
        intentBundle.putString(Constants.INTENT_TAG, tag.toString())

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        if (tag == PageConstant.TODO) {
            todoListFragment = TodoListFragment.newInstance(intentBundle)
            ft.replace(R.id.content_view, todoListFragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
        }

        currentPageConstant = tag
    }

    fun getUserID() : String? {
        return userID
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

}
