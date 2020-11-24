package com.airi.presentation

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Saved : RealmObject(){
    @PrimaryKey
    var id : String = UUID.randomUUID().toString()
    @Required
    var title : String = ""
    var bunshou : String = ""
    var time :Int = 0
    var date :String? = ""
}