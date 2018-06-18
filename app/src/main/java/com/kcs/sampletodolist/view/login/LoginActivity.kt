package com.kcs.sampletodolist.view.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.jakewharton.rxbinding2.widget.RxTextView
import com.kcs.sampletodolist.common.Constants
import com.kcs.sampletodolist.dto.UserDTO
import com.kcs.sampletodolist.view.join.JoinActivity
import com.kcs.sampletodolist.module.UserRealmManager
import com.kcs.sampletodolist.R
import com.kcs.sampletodolist.common.Preferences
import com.kcs.sampletodolist.view.main.MainDrawerActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Created by kcs on 2018. 5. 25..
 */
class LoginActivity  : AppCompatActivity() {
    lateinit var inputDataField : Array<EditText>

    var idData = ""
    var emailData = ""
    var realmManager = UserRealmManager()

    // 여러 디스포저블 객체를 관리할 수 있는 CompositeDisposable 객체를 초기화 합니다.
    internal val viewDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
        setListener()
    }

    override fun onStop() {
        super.onStop()
        // 관리하고 있던 디스포저블 객체를 모두 해제합니다.
        viewDisposables.clear()
    }

    override fun onResume() {
        super.onResume()
        initObservable()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    /**
     * RX
     */
    private fun initObservable(){
        // Observable 설정
        val observableId = RxTextView.textChanges(inputDataField[0])
                .map({ t -> !t.isEmpty()})
        val observablePwd = RxTextView.textChanges(inputDataField[1])
                .map({ t -> !t.isEmpty()})


        // combineLatest 설정
        val combineLatestLoginEnable: Observable<Boolean> = Observable.combineLatest(observableId, observablePwd, BiFunction{ i, e -> i && e})
        val disposable = combineLatestLoginEnable.distinctUntilChanged()
                .subscribe ({ enable ->
                    btnDone.isEnabled = enable
                    when (enable) {
                        true -> {
                            btnDone.setBackgroundColor(resources.getColor(R.color.enableButton))
                        }
                        false -> {
                            btnDone.setBackgroundColor(resources.getColor(R.color.disableButton))
                        }
                    }
                }){
                    //Error Block
                }


        viewDisposables.add(disposable)
    }

    private fun checkEmpty() : Boolean{
        for(dataField in inputDataField){
            if (dataField.text.isEmpty()){
                return true
            }
        }
        return false
    }

    private fun init(){
        inputDataField = arrayOf(editID, editPWD)
        supportActionBar?.title = getString(R.string.login)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#3A99D9")))
    }

    private fun setListener(){
        btnJoin.setOnClickListener({
            startActivity<JoinActivity>()
        })
        btnDone.setOnClickListener {
            if(checkEmpty()){
                toast(R.string.error_join_field_empty)
//                Toast.makeText(this@LoginActivity, getString(R.string.error_join_field_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//                val intent = Intent(this@JoinActivity, MainActivity::class.java)
//                startActivity(intent)

            if (checkSaveUser()){
                val intent = MainDrawerActivity.newIntent(this@LoginActivity)
                intent.putExtra(Constants.INTENT_ID_DATA, idData )
                intent.putExtra(Constants.INTENT_EMAIL_DATA, emailData)
                Preferences.setIDData(this@LoginActivity, inputDataField[0].text.toString())
                Preferences.setPWDData(this@LoginActivity, inputDataField[1].text.toString())
                Preferences.setEMAILData(this@LoginActivity, emailData)
                startActivity(intent)
                finish()
                false
            }else{
                Toast.makeText(this@LoginActivity, getString(R.string.error_fail_login), Toast.LENGTH_SHORT).show()
            }
        }

        btn_clear.setOnClickListener{
            realmManager.clear()
            Preferences.setIDData(this@LoginActivity, "")
            Preferences.setEMAILData(this@LoginActivity, "")
            Preferences.setPWDData(this@LoginActivity, "")
            startActivity(JoinActivity.newIntent(this@LoginActivity))
            finish()
        }

        switch_auto_login.isChecked = Preferences.getAutoLogin(this@LoginActivity)
        switch_auto_login.setOnClickListener{
            Preferences.setAutoLogin(this@LoginActivity, switch_auto_login.isChecked)
        }
    }

    private fun checkSaveUser() : Boolean{

        val userData = realmManager.find(inputDataField[0].text.toString(), "id", UserDTO::class.java)

        if(userData == null){
            Log.d(Constants.LOG_TEST, "UserDTO Realm Data Null!!")
            return false
        }


        for (field in inputDataField){
            when(field.id){
                R.id.editID -> {
                    if(userData.id != field.text.toString()){
                        return false
                    }else{
                        idData = field.text.toString()
                        emailData = userData.email ?: ""
                    }
                }
                R.id.editPWD ->
                    if(userData.password != field.text.toString()){
                        return false
                    }
            }
        }
        return true
    }


}
