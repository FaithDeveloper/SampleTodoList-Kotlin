package com.kcs.sampletodolist.view.todo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kcs.sampletodolist.R
import com.kcs.sampletodolist.common.Constants
import com.kcs.sampletodolist.common.Preferences
import com.kcs.sampletodolist.common.Utils
import com.kcs.sampletodolist.dto.TodoDTO
import com.kcs.sampletodolist.dto.UserDTO
import com.kcs.sampletodolist.module.TodoRealmManager
import com.kcs.sampletodolist.module.UserRealmManager
import com.kcs.sampletodolist.view.login.LoginActivity
import com.kcs.sampletodolist.view.main.MainActivity
import kotlinx.android.synthetic.main.fragment_todo_list.*
import org.jetbrains.anko.support.v4.startActivityForResult

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TodoListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TodoListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodoListFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: Bundle? = null
    private lateinit var adapter: TodoAdapter
    private var realmManager = TodoRealmManager()
    private var userTodo: MutableList<TodoDTO>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getBundle(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        getTodoLilst()
        initTodoAdapter()
        initListener()

        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    private fun initTodoAdapter(){
        adapter = TodoAdapter(activity!!.applicationContext, userTodo, object : OnItemClickListener{
            override fun itemClick(position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun itemDeleteClick(position: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        list_todo.adapter = adapter

        if (userTodo == null){
            return
        }else{
            adapter.notifyDataSetChanged()
        }


    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TodoListFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(args: Bundle): TodoListFragment {
            val fragment = TodoListFragment()
//            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

    private fun initListener(){

        btn_logout.setOnClickListener({
            val realmManager = UserRealmManager()
            Preferences.setIDData(this@TodoListFragment.context!!, "")
            Preferences.setEMAILData(this@TodoListFragment.context!!, "")
            Preferences.setPWDData(this@TodoListFragment.context!!, "")
            Preferences.setAutoLogin(this@TodoListFragment.context!!, false)
            startActivity(LoginActivity.newIntent(this@TodoListFragment.context!!))

            activity?.finish()
        })

        btn_add.setOnClickListener({
            startActivityForResult<AddTodoActivity>(Constants.ACTIVITY_REUSLT_ADD_TODO, Constants.INTENT_DATA to (activity as MainActivity).getUserID())
        })
    }

    private fun getTodoLilst(){
        userTodo =  realmManager.findAll((activity as MainActivity).getUserID()!!, "userID", TodoDTO::class.java)
        adapter.setDataList(userTodo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            return
        }

        when (requestCode){
            Constants.ACTIVITY_REUSLT_ADD_TODO -> {
                var todoDTO = TodoDTO()
                val getTodo = data?.getStringExtra(Constants.INTENT_DATA) ?: ""
                if(getTodo.isEmpty()){
                    return
                }
                todoDTO.todoID = System.currentTimeMillis()
                todoDTO.isTodo = false
                todoDTO.userID = (activity as MainActivity).getUserID()
                todoDTO.content = getTodo
                userTodo?.add(todoDTO)
                adapter.setDataList(userTodo)
                adapter.notifyDataSetChanged()
            }
        }
    }
}// Required empty public constructor
