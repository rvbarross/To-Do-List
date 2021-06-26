package br.unifor.todolist.util

import android.content.Context
import androidx.room.Room
import br.unifor.todolist.database.ToDoDatabase

object DatabaseUtil {

    private var db :ToDoDatabase? = null

    fun getDatabaseInstance(context :Context) :ToDoDatabase{
        if(db == null){
            db = Room.databaseBuilder(
                context,
                ToDoDatabase::class.java,
                "todolist.db"
            )   .fallbackToDestructiveMigration()
                .build()
        }
        return db!!
    }
}