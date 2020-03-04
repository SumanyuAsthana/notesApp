package com.example.startup

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.media.projection.MediaProjection
import android.net.Uri
import android.widget.Toast

class DbManager{
    val dbName="MyNotesDb"
    val dbTable="MyNotesDbTable"
    val colID="ID"
    val colTitle="Title"
    val colDes="Description"
    val dbVersion=1
    //CREATE TABLE IF NOT EXISTS MyNotesDb(ID INTEGER PRIMARY KEY,Title TEXT,Description TEXT);
    val sqlCreateTable="CREATE TABLE IF NOT EXISTS "+dbTable+" ("+colID+" INTEGER PRIMARY KEY,"+
            colTitle+" TEXT, "+ colDes+" TEXT);"
    var sqlDb:SQLiteDatabase?=null
    constructor(context: Context){
        var db=DatabaseHelperForNotes(context)
        sqlDb=db.writableDatabase
    }

    inner class DatabaseHelperForNotes:SQLiteOpenHelper{
        var context:Context?=null
        constructor(context: Context):super(context,dbName,null,dbVersion){
            this.context=context
        }
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context," database is created",Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS  $dbTable")
        }

    }
    fun Insert(values: ContentValues):Long{
        val ID=sqlDb!!.insert(dbTable,"",values)
        return ID
    }
    fun Query(projection:Array<String>,selection:String,selectionArgs:Array<String>,SortOrder:String):Cursor{
        val qb=SQLiteQueryBuilder()
        qb.tables=dbTable
        var cursor=qb.query(sqlDb,projection,selection,selectionArgs,null,null,SortOrder)
        return cursor
    }
    fun delete(selection: String,selectionArgs: Array<String>):Int{
        val count=sqlDb!!.delete(dbTable,selection,selectionArgs)
        return count
    }
    fun update(values: ContentValues,selection: String,selectionArgs: Array<String>):Int{
        val count=sqlDb!!.update(dbTable,values,selection,selectionArgs)
        return count
    }

}