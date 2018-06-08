package com.kcs.sampletodolist.main

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kcs.sampletodolist.common.Constants
import com.kcs.sampletodolist.common.Utils
import com.kcs.sampletodolist.login.LoginActivity
import com.kcs.sampletodolist.module.UserRealmManager
import com.kcs.sampletodolist.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val useIdD = intent.getStringExtra(Constants.INTENT_DATA) as String
        txt_id.text = useIdD

        initListener()
    }

    private fun initListener(){
        btn_logout.setOnClickListener({
            val realmManager = UserRealmManager()
            Utils.setIDData(this@MainActivity, "")
            Utils.setEMAILData(this@MainActivity, "")
            Utils.setPWDData(this@MainActivity, "")
            Utils.setAutoLogin(this@MainActivity, false)
            startActivity(LoginActivity.newIntent(this@MainActivity))

            finish()
        })
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

}
