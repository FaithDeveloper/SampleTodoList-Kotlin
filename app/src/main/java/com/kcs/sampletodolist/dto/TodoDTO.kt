package com.kcs.sampletodolist.dto

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by kcs on 2018. 6. 8..
 */
@RealmClass
open class TodoDTO : RealmObject() {
    @PrimaryKey
    open var todoID:Long = 0
    open var userID:String? = null
    open var content:String? = null
    open var isTodo:Boolean = false
}