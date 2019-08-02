package com.example.a170373.privatedatatest.dao

import android.content.Context
import com.example.a170373.privatedatatest.BaseApplication
import android.content.ContentValues
import android.database.Cursor


object MDbDao {
    val mSqliteHelper = MSqliteHelper(BaseApplication.instance,"test",null,1)

    fun insert(){
        val cv = ContentValues()
        cv.put("name", "小明")
        cv.put("age", 18)
        var writableDatabase = mSqliteHelper.writableDatabase
        writableDatabase.insert("person",null,cv)
        writableDatabase.close()
    }

    fun find():ArrayList<String>{
        val writableDatabase = mSqliteHelper.writableDatabase
        val query = writableDatabase.query("person", null, null, null, null, null, null, null)
        val arrayList : ArrayList<String> = ArrayList()
        while (query.moveToNext()){
            val name = query.getString(query.getColumnIndex("name"))
            val age = query.getInt(query.getColumnIndex("age"))
            arrayList.add("$name:$age")
        }
        writableDatabase.close()
        return arrayList
    }
}