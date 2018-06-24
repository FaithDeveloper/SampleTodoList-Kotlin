package com.kcs.sampletodolist.module

import android.util.Log
import com.kcs.sampletodolist.common.Constants
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmResults
import io.realm.kotlin.deleteFromRealm

/**
 * Created by kcs on 2018. 5. 27..
 *
 * ref :
 * https://github.com/boarderYuki/AOS_study_0001/blob/master/app/src/main/java/io/indexpath/study_001/JoinActivity.kt
 * https://github.com/Developer-Leby/LoginSample-Kotlin/blob/master/app/src/main/java/com/example/lebyykim/realm/AccountRealmManager.kt
 * https://github.com/realm/realm-java/blob/master/examples/kotlinExample/src/main/kotlin/io/realm/examples/kotlin/KotlinExampleActivity.kt
 * https://realm.io/docs/java/latest/?_ga=2.172749477.769786165.1527428973-973285666.1523330369#realms
 */
open class RealmManager(val name: String) {


    //Realm 초기화
    val realm: Realm by lazy {
        val config = RealmConfiguration.Builder().name(name).build()
        Realm.getInstance(config)
    }

    /**
     * Realm에 저장된 모든 데이터 삭제
     * */
    open fun clear(){
        val config = RealmConfiguration.Builder().name(name).build()
        if (config != null) {
            realm.close()
            Realm.deleteRealm(config)
        }
    }

    /**
     * T로 받은 RealModel 데이터에서 key,value 값을 가진 데이터를 찾습니다
     */
    fun <T : RealmModel> find(value: String, key: String, targetDto: Class<T>): T? {
        Log.e(Constants.LOG_TEST, "realm address : " + realm.path)
        return realm.where(targetDto).equalTo(key, value).findFirst()
    }

    /**
     * T로 받은 RealModel 데이터에서 모든 값을 리턴
     */
    fun <T: RealmModel> findAll(targetDto: Class<T>): RealmResults<T>{
        Log.e(Constants.LOG_TEST, "realm address : " + realm.path)
        return realm.where(targetDto).findAll()
    }

    /**
     * T로 받은 RealModel 데이터에서 모든 값을 리턴
     */
    fun <T: RealmModel> findAll(value: String, key: String, targetDto: Class<T>): RealmResults<T>?{
        Log.e(Constants.LOG_TEST, "realm address : " + realm.path)
        if (realm.where(targetDto).equalTo(key, value).findAll().size == 0) {
            return null
        }
        return realm.where(targetDto).equalTo(key, value).findAll()
    }

    fun <T: RealmModel> removeAt(position:Int, dataList: RealmResults<T>): RealmResults<T>{
        if(position >= 0 && position < dataList.size){
            realm.beginTransaction()
            dataList[position]?.deleteFromRealm()
            realm.commitTransaction()
        }

        return dataList
    }

    fun <T: RealmModel> removeAt(value: String, key: String, position:Int, targetDto: Class<T>): RealmResults<T>{
        var dataList = realm.where(targetDto).equalTo(key, value).findAll()
        if(position >= 0 && position < dataList.size){
            realm.beginTransaction()
            dataList[position]?.deleteFromRealm()
            realm.commitTransaction()
        }

        return dataList
    }
}