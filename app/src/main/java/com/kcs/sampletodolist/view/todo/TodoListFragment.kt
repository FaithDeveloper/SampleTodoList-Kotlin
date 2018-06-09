package com.kcs.sampletodolist.view.todo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kcs.sampletodolist.R
import com.kcs.sampletodolist.common.Preferences
import com.kcs.sampletodolist.common.Utils
import com.kcs.sampletodolist.module.UserRealmManager
import com.kcs.sampletodolist.view.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_todo_list.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getBundle(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        initListener()

        return inflater.inflate(R.layout.fragment_todo_list, container, false)
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
    }

}// Required empty public constructor
