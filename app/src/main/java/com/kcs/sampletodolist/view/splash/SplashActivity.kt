package com.kcs.sampletodolist.slash

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.kcs.sampletodolist.R
import com.kcs.sampletodolist.common.Constants
import com.kcs.sampletodolist.common.Preferences
import com.kcs.sampletodolist.dto.UserDTO
import com.kcs.sampletodolist.view.login.LoginActivity
import com.kcs.sampletodolist.module.UserRealmManager
import com.kcs.sampletodolist.view.main.MainDrawerActivity
import org.jetbrains.anko.startActivity


/**
 * Created by kcs on 2018. 4. 28..
 */
class SplashActivity : AppCompatActivity() {
    var realmManager = UserRealmManager()

    val userID : String by lazy {
        Preferences.getIDData(this)
    }
    val userPwd : String by lazy {
        Preferences.getPWDData(this)
    }
    val userEmail : String by lazy {
        Preferences.getEMAILData(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            if(Preferences.getAutoLogin(this) && realmManager.findAll(UserDTO::class.java).size > 0){
                val user = realmManager.find(userID, "id", UserDTO::class.java)
                if (user?.id == userID && user?.password == userPwd){
//                    val intent = MainActivity.newIntent(this@SplashActivity)
//                    intent.putExtra(Constants.INTENT_DATA, userID)
//                    startActivity(intent)
                    //anko 의 StartActivity에 intent data 삽입
                    startActivity<MainDrawerActivity>(Constants.INTENT_ID_DATA to userID, Constants.INTENT_EMAIL_DATA to Preferences.getEMAILData(this))
                    finish()
                    return@postDelayed
                }
            }

//            startActivity(LoginActivity.newIntent(this))
            startActivity<LoginActivity>()
            finish()
        }, 800)
    }

}