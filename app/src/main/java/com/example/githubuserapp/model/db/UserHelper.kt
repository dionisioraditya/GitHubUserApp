package com.example.githubuserapp.model.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import com.example.githubuserapp.model.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import java.sql.SQLException

class UserHelper(context: Context) {
    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: UserHelper? = null
        private lateinit var database: SQLiteDatabase
        fun getInstance(context: Context):UserHelper =
            INSTANCE ?: synchronized(this){
                INSTANCE?: UserHelper(context)
            }
    }
    init {
        databaseHelper = DatabaseHelper(context)
    }
    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    // close the database connection
    fun close() {
        databaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_NAME_USERNAME DESC")
    }
    fun queryById(id: String):Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$COLUMN_NAME_USERNAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }
    fun insert(values: ContentValues?):Long{
        return database.insert(DATABASE_TABLE, null, values)
    }
    fun deleteById(id: String):Int{
        return database.delete(DATABASE_TABLE, "$COLUMN_NAME_USERNAME = '$id'", null)
    }
}