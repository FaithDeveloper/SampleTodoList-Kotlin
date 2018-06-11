package com.kcs.sampletodolist.common

/**
 * Created by kcs on 2018. 4. 28..
 */
class Constants {
    companion object {
        val LOG_TEST = "LOG_LOGIN"
        val INTENT_DATA = "com.kcs.sampletodolist.common.constants.intent_data"
        val INTENT_ID_DATA = "com.kcs.sampletodolist.common.constants.id"
        val INTENT_PWD_DATA = "com.kcs.sampletodolist.common.constants.pwd"
        val INTENT_EMAIL_DATA = "com.kcs.sampletodolist.common.constants.email"
        val INTENT_AUTO_LOGIN_DATA = "com.kcs.sampletodolist.common.constants.auto.login"
        val INTENT_TAG = "com.kcs.sampletodolist.common.constants.tag"

        val ACTIVITY_REUSLT_ADD_TODO = 1

        val USER_TABLE_ID = "id"

        /** 정규식 */
        //패스워드 정규식
        // 대문자,소문자, 숫자 또는 특수문자
//    private val PASSWORD_RULS = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{6,20}\$"
        // 대문자, 소문자 숫자, 특수문자 최소 8자 - 최대 20자
        val PASSWORD_RULS = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}\$"
        //이메일 정규식
        val EMAIL_RULS = "^[a-z0-9_+.-]+@([a-z0-9-]+\\.)+[a-z0-9]{2,4}\$"
    }
}