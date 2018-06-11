package com.kcs.sampletodolist.common

import android.content.Context

/**
 * Created by kcs on 2018. 5. 19..
 */
class Preferences {
    companion object {
        fun setIDData(ctx: Context, value : String){
            val preferences = ctx.getSharedPreferences(Constants.INTENT_ID_DATA, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(Constants.INTENT_ID_DATA, value)
            editor.commit()
        }

        fun getIDData(ctx: Context) : String{
            val preferences = ctx.getSharedPreferences(Constants.INTENT_ID_DATA, Context.MODE_PRIVATE)
            return preferences.getString(Constants.INTENT_ID_DATA, "")
        }

        fun setPWDData(ctx: Context, value : String){
            val preferences = ctx.getSharedPreferences(Constants.INTENT_PWD_DATA, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(Constants.INTENT_PWD_DATA, value)
            editor.commit()
        }

        fun getPWDData(ctx: Context) : String{
            val preferences = ctx.getSharedPreferences(Constants.INTENT_PWD_DATA, Context.MODE_PRIVATE)
            return preferences.getString(Constants.INTENT_PWD_DATA, "")
        }

        fun setEMAILData(ctx: Context, value : String){
            val preferences = ctx.getSharedPreferences(Constants.INTENT_EMAIL_DATA, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(Constants.INTENT_EMAIL_DATA, value)
            editor.commit()
        }

        fun getEMAILData(ctx: Context) : String{
            val preferences = ctx.getSharedPreferences(Constants.INTENT_EMAIL_DATA, Context.MODE_PRIVATE)
            return preferences.getString(Constants.INTENT_EMAIL_DATA, "")
        }

        fun setAutoLogin(ctx: Context, value: Boolean){
            val preferences = ctx.getSharedPreferences(Constants.INTENT_AUTO_LOGIN_DATA, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putBoolean(Constants.INTENT_AUTO_LOGIN_DATA, value)
            editor.commit()
        }

        fun getAutoLogin(ctx: Context) : Boolean{
            val preferences = ctx.getSharedPreferences(Constants.INTENT_AUTO_LOGIN_DATA, Context.MODE_PRIVATE)
            return preferences.getBoolean(Constants.INTENT_AUTO_LOGIN_DATA, false)
        }
    }
}