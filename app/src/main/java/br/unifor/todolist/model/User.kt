package br.unifor.todolist.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["email"], unique = true)] )
data class User (
        @PrimaryKey
        val id :Int? = null,
        @ColumnInfo(name = "first_name")
        val firstName :String,
        @ColumnInfo(name = "last_name")
        val lastName :String,
        val email :String,
        val password :String
)