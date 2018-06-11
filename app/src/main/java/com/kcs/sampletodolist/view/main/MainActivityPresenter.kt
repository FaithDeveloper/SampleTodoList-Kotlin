package com.kcs.sampletodolist.view.main

/**
 * Created by kcs on 2018. 6. 9..
 */
class MainActivityPresenter : MainActivityConstants.presenter{
    var userID = ""

    override fun setUserIDData(id: String) {
        userID = id
    }
}