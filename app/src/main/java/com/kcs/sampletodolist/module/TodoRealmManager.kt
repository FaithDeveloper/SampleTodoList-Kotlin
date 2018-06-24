package com.kcs.sampletodolist.module

import com.kcs.sampletodolist.dto.TodoDTO
import com.kcs.sampletodolist.dto.UserDTO
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * Created by kcs on 2018. 5. 29..
 */
class TodoRealmManager : RealmManager("TodoDTO.realm") {
    fun <T: RealmModel, E: TodoDTO>insertTodo(targetDto: Class<T>, dto: E){

        realm.beginTransaction()

        //PrimaryKey 증가해서 넣어주는 것이 중요!!
        val account = realm.createObject(targetDto, dto.todoID)
        if(account is TodoDTO){
            account.userID = dto.userID
            account.content = dto.content
            account.isTodo = dto.isTodo
        }
        realm.commitTransaction()
    }


    fun <T: TodoDTO> updateCheckUseData(isCheck: Boolean, position:Int, dataList: RealmResults<T>): RealmResults<T>{
        if(position >= 0 && position < dataList.size){
            realm.beginTransaction()
            dataList?.get(position)?.isTodo = isCheck
            realm.commitTransaction()
        }

        return dataList
    }

    override fun clear(){
        val config = RealmConfiguration.Builder().name(name).build()
        if (config != null) {
            realm.beginTransaction()

//            realm.where(UserDTO::class.java).findAll().deleteAllFromRealm()
            realm.delete(UserDTO::class.java)

            realm.commitTransaction()
            realm.close()
        }
    }

}