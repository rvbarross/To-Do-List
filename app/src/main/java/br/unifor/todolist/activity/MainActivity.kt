package br.unifor.todolist.activity

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import br.unifor.todolist.R
import br.unifor.todolist.adapter.TaskAdapter
import br.unifor.todolist.adapter.TaskItemListerner
import br.unifor.todolist.database.TaskDAO
import br.unifor.todolist.database.ToDoDatabase
import br.unifor.todolist.database.UserDAO
import br.unifor.todolist.model.Task
import br.unifor.todolist.model.UserWithTasks
import br.unifor.todolist.util.DatabaseUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener, TaskItemListerner {
    private lateinit var userDAO :UserDAO
    private lateinit var taskDAO :TaskDAO
    private lateinit var mRecyclerView :RecyclerView
    private lateinit var mUserWithTasks :UserWithTasks
    private lateinit var mTaskAdd :FloatingActionButton
    private lateinit var taskAdapter :TaskAdapter
    private var mTaskList = mutableListOf<Task>()
    private var userId = -1
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = findViewById(R.id.main_recyclerview_tasks)
        mTaskAdd = findViewById(R.id.main_floatingbutton_add_task)
        mTaskAdd.setOnClickListener(this)

        GlobalScope.launch {
            taskDAO = DatabaseUtil.getDatabaseInstance(applicationContext).getTaskDAO()
            userDAO = DatabaseUtil.getDatabaseInstance(applicationContext).getUserDAO()
            if(intent != null) {
                userId = intent.getIntExtra("userId", -1)
                if (userId != -1) {
                    mUserWithTasks = userDAO.findUserWithTasks(userId)
                }
            }
            taskAdapter = TaskAdapter(mUserWithTasks.tasks)
            val llm = LinearLayoutManager(applicationContext)

            mRecyclerView.apply {
                adapter = taskAdapter
                layoutManager = llm
            }


        }

    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            if(userId != -1) {
                mUserWithTasks = userDAO.findUserWithTasks(userId)
                mTaskList.clear()
                mTaskList.addAll(mUserWithTasks.tasks)

                handler.post {
                    taskAdapter = TaskAdapter(mTaskList)
                    val llm = LinearLayoutManager(applicationContext)
                    taskAdapter.setTaskItemListener(this@MainActivity)

                    mRecyclerView.apply {
                        adapter = taskAdapter
                        layoutManager = llm
                    }

                }
            }

        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.main_floatingbutton_add_task ->{
                val intent = Intent(applicationContext, TaskFormActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }

        }
    }

    override fun onClick(v: View, position: Int) {
        val it = Intent(applicationContext, TaskFormActivity::class.java)
        it.putExtra("userId", userId)
        it.putExtra("taskId",mUserWithTasks.tasks[position].id)
        startActivity(it)
    }

    override fun onLongClick(v: View, position: Int) {
//        TODO("Not yet implemented")
        val dialog = AlertDialog.Builder(this)
            .setTitle("ToDo List")
            .setMessage("Você deseja excluir a tarefa ${mUserWithTasks.tasks[position].name}?")
            .setPositiveButton("Sim") {dialog, _ ->
                GlobalScope.launch {
                    val taskId = mUserWithTasks.tasks[position].id
                    taskDAO.delete(taskDAO.find(taskId!!))

                    mUserWithTasks = userDAO.findUserWithTasks(userId)
                    mTaskList.removeAt(position)
                    handler.post {
                        taskAdapter.notifyItemRemoved(position)

                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Não") {dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }
}