package com.kcs.sampletodolist.view.join

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import com.kcs.sampletodolist.common.Constants
import com.kcs.sampletodolist.dto.UserDTO
import com.kcs.sampletodolist.module.UserRealmManager
import com.kcs.sampletodolist.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_join_degin.*
import org.jetbrains.anko.toast
import java.util.regex.Pattern

/**
 * Created by kcs on 2018. 4. 28..
 */
class JoinActivity : AppCompatActivity() {
    // 여러 디스포저블 객체를 관리할 수 있는 CompositeDisposable 객체를 초기화 합니다.
    internal val viewDisposables = CompositeDisposable()

    private lateinit var inputDataField: Array<EditText>
    private lateinit var textInputLayoutArray: Array<TextInputLayout>
    private lateinit var inputInfoMessage: Array<String>
    private var isInputCorrectData: Array<Boolean> = arrayOf(false, false, false, false)
    private var isCheckID = false
      set(value){
          when (value) {
              true -> {
                  btnCheckExistID.setBackgroundResource(R.drawable.round_green)
              }
              false -> {
                  btnCheckExistID.setBackgroundResource(R.drawable.round_light_brown)
              }
          }
          field = value
      }


    private lateinit var userRealmManager: UserRealmManager

    /** =======================================
     *
     * Override Method
     *
     *  =======================================
     */

    override fun onResume() {
        super.onResume()

        typingListener()
    }

    // disposable 을 했을 경우 갖고있는 overable 초기화 되므로 onResume 에서 해당 데이터를 가져올 수 있도록 해야한다.
    override fun onStop() {
        super.onStop()
        viewDisposables.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_degin)

        userRealmManager = UserRealmManager()
        init()
        setListener()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    /** =======================================
     *
     * Make Function
     *
     * ========================================*/

    private fun init() {
        inputDataField = arrayOf(editID, editPWD, editPWDConfirm, editEmail)
        textInputLayoutArray = arrayOf(editIDLayout, editPWDLayout, editPWDConfirmLayout, editEmailLayout)
        inputInfoMessage = arrayOf(getString(R.string.txtInputInfoID), getString(R.string.txtInputInfoPWD), getString(R.string.txtInputInfoRePWD), getString(R.string.error_discorrent_email))

        //ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.join)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#3A99D9")))
    }

    private fun setListener() {
        btnDone.setOnClickListener {

            if (isCheckID) {
                saveLoginUserData()
//                startActivity(LoginActivity.newIntent(this@JoinActivity))
                finish()
            }else{
                toast(getString(R.string.error_do_not_check_id))
            }

        }

        btnCheckExistID.setOnClickListener {
            if(editID.text.toString().isEmpty()){
                isCheckID = false
                toast(getString(R.string.error_do_not_input_id))
                return@setOnClickListener
            }
            val user = userRealmManager.find(editID.text.toString(), Constants.USER_TABLE_ID, UserDTO::class.java)
            if (user != null) {
                toast(getString(R.string.error_exist_id))
                isCheckID = false
            }else{
                isCheckID = true
            }
        }

    }

    /**
     * 가장 마지막에 로그인한 유저 정보 저장
     */
    private fun saveLoginUserData(){
        val dataID = inputDataField[0].text.toString()
        val dataPassword = inputDataField[1].text.toString()
        val dataEmail = inputDataField[3].text.toString()

        var user = UserDTO()
        user.id = dataID
        user.password = dataPassword
        user.email = dataEmail
        userRealmManager.insertUser(UserDTO::class.java, user)
    }

    /**
     * 각 필드별 회원가입 조건이 맞는지 비동기 체크
     */
    private fun typingListener() {
        // ID
        val disposableID = RxTextView.textChanges(inputDataField[0])
                .map { t -> t.length in 1..7 }
                .subscribe({ it ->
                    isCheckID = false
                    reactiveInputTextViewData(0, !it)
                }){
                    //Error Block
                    settingEmptyInputUI(0)
                }

        // Password
        val disposablePwd = RxTextView.textChanges(inputDataField[1])
                .map { t -> t.isEmpty() || Pattern.matches(Constants.PASSWORD_RULS, t) }
                .subscribe({ it ->
                    inputDataField[2].setText("")
                    reactiveInputTextViewData(1, it)
                }){
                    //Error Block
                }

        // RePassword
        val disposableRePwd = RxTextView.textChanges(inputDataField[2])
                .map { t -> t.isEmpty() || inputDataField[1].text.toString() == inputDataField[2].text.toString() }
                .subscribe({ it ->
                    reactiveInputTextViewData(2, it)
                }){
                    //Error Block
                }


        //Email
        val disposableEmail = RxTextView.textChanges(inputDataField[3])
                .map { t -> t.isEmpty() || Pattern.matches(Constants.EMAIL_RULS, t) }
                .subscribe({
                    reactiveInputTextViewData(3, it)
                }){
                    //Error Block
                }

        viewDisposables.addAll(disposableID, disposablePwd, disposableRePwd, disposableEmail)
    }

    private fun settingEmptyInputUI(indexPath: Int){
        isInputCorrectData[indexPath] = false
        textInputLayoutArray[indexPath].isErrorEnabled = false
    }

    var isSuccess = false
    /**
     * 올바른 회원정보를 입력 받았는지 체크
     */
    private fun reactiveCheckCorrectData() {
        for (check in isInputCorrectData) {
            if (!check) {
                btnDone.setBackgroundColor(resources.getColor(R.color.disableButton))
                btnDone.setTextColor(resources.getColor(R.color.gray))
                btnDone.isEnabled = false
                isSuccess = false
                return
            }
        }
        btnDone.setBackgroundColor(resources.getColor(R.color.enableButton))
        btnDone.setTextColor(resources.getColor(R.color.white))
        btnDone.isEnabled = true
        isSuccess =true
    }

    /**
     * ReActive 로 입력 들어오는 데이터에 대한 결과를 UI 로 표시합니다
     */
    private fun reactiveInputTextViewData(indexPath: Int, it: Boolean) {
        if(!inputDataField[indexPath].text.toString().isEmpty()){
            isInputCorrectData[indexPath] = it
        }else{
            isInputCorrectData[indexPath] = false
        }

        textInputLayoutArray[indexPath].error = inputInfoMessage[indexPath]
        textInputLayoutArray[indexPath].isErrorEnabled = !it


//        if (it) {
//            inputInfoLayoutField[indexPath].visibility = View.GONE
//        } else {
//            inputInfoLayoutField[indexPath].visibility = View.VISIBLE
//            inputInfoField[indexPath].text = inputInfoMessage[indexPath]
//        }

        reactiveCheckCorrectData()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, JoinActivity::class.java)
        }
    }





}