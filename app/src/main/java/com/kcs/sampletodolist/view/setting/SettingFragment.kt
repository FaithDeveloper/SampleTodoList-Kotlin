package com.kcs.sampletodolist.view.setting

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kcs.sampletodolist.R
import com.kcs.sampletodolist.common.Preferences
import com.kcs.sampletodolist.view.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SettingFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: Bundle? = null

    /** =======================================
     *
     * override Function
     *
     * ========================================*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getBundle(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    /*
    * Kotlin Android Extensions 을 Fragment 에서 사용하려면 onViewCreated 부터 사용할 수 있습니다.
    * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListener()
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
        fun newInstance(args: Bundle): SettingFragment {
            val fragment = SettingFragment()
//            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

    /** =======================================
     *
     * make Function
     *
     * ========================================*/
    private fun initListener(){
        // 로그아웃
        btn_logout.setOnClickListener{
            Preferences.setIDData(this@SettingFragment.context!!, "")
            Preferences.setEMAILData(this@SettingFragment.context!!, "")
            Preferences.setPWDData(this@SettingFragment.context!!, "")
            Preferences.setAutoLogin(this@SettingFragment.context!!, false)
            startActivity(LoginActivity.newIntent(this@SettingFragment.context!!))
            activity?.finish()
        }
    }

}// Required empty public constructor
