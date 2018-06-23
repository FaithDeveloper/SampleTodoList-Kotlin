package com.kcs.sampletodolist.view.todo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kcs.sampletodolist.R
import com.kcs.sampletodolist.common.Constants
import com.kcs.sampletodolist.common.Preferences
import com.kcs.sampletodolist.dto.TodoDTO
import com.kcs.sampletodolist.module.TodoRealmManager
import com.kcs.sampletodolist.view.login.LoginActivity
import com.kcs.sampletodolist.view.main.MainDrawerActivity
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_todo_list.*
import org.jetbrains.anko.support.v4.startActivityForResult
import org.jetbrains.anko.support.v4.toast

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
    private var todoRealmManager = TodoRealmManager()
    private var userTodo: RealmResults<TodoDTO>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getBundle(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    /*
    * Kotlin Android Extensions 을 Fragment 에서 사용하려면 onViewCreated 부터 사용할 수 있습니다.
    * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTodoAdapter()
        getTodoList()
    }

    private fun initTodoAdapter(){
        // 중요!! RecyclerView을 사용 시 어떤 형태의 Recycler을 할지 설정해야한다.
        list_todo.layoutManager = LinearLayoutManager(activity)
        adapter = TodoAdapter(activity!!.applicationContext, userTodo, object : OnItemClickListener{
            override fun itemClick(position: Int) {
                Log.d(Constants.LOG_TEST, "postion : " + position)
                toast(String.format("Todo : %s", userTodo?.get(position)?.content))
            }

            override fun itemDeleteClick(position: Int) {
                if(userTodo == null){
                    return
                }
                userTodo = todoRealmManager.removeAt(position, userTodo!!)
                adapter.setDataList(userTodo)
                adapter.notifyDataSetChanged()
                toast(getString(R.string.success_todo))
            }

            override fun toDoItemClick(isChecked: Boolean, position: Int) {
                userTodo = todoRealmManager.updateCheckUseData(isChecked, position, userTodo!!)
                adapter.setDataList(userTodo)
                adapter.notifyDataSetChanged()
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

    /**
     * Realm에 저장된 데이터를 RecyclerView 에 표시
     * */
    private fun getTodoList(){
        try {
            userTodo = todoRealmManager.findAll((activity as MainDrawerActivity).getUserID()!!, "userID", TodoDTO::class.java)
            if (userTodo == null){
                return
            }
            adapter.setDataList(userTodo)
            adapter.notifyDataSetChanged()
        }catch (e: NullPointerException){
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != Activity.RESULT_OK){
            return
        }

        when (requestCode){
            Constants.ACTIVITY_REUSLT_ADD_TODO -> {
                userTodo =  todoRealmManager.findAll((activity as MainDrawerActivity).getUserID()!!, "userID", TodoDTO::class.java)
                if(userTodo == null){
                    return
                }
                adapter.setDataList(userTodo)
                adapter.notifyDataSetChanged()
            }
        }
    }
}// Required empty public constructor
