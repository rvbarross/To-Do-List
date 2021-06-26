package br.unifor.todolist.adapter

import android.view.View

interface TaskItemListerner {
    fun onClick(v: View, position :Int)
    fun onLongClick(v: View, position :Int)
}
