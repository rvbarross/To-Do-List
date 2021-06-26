package br.unifor.todolist.activity

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import br.unifor.todolist.R
import br.unifor.todolist.database.TaskDAO
import br.unifor.todolist.model.Task
import br.unifor.todolist.model.User
import br.unifor.todolist.util.DatabaseUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskFormActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mTaskFormTitle :TextView
    private lateinit var mTaskFormName :EditText
    private lateinit var mTaskFormDescription :EditText
    private lateinit var mTaskFormIsDone :Switch
    private lateinit var mTaskFormSave :Button
    private lateinit var taskDAO: TaskDAO
    private val handler = Handler(Looper.getMainLooper())
    private var mUserId = -1
    private var mTaskId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)
        mTaskFormTitle = findViewById(R.id.task_form_title)
        mTaskFormName = findViewById(R.id.task_form_edittext_task_name)
        mTaskFormDescription = findViewById(R.id.task_form_edittext_task_description)
        mTaskFormIsDone = findViewById(R.id.task_form_switch_done)
        mTaskFormSave = findViewById(R.id.task_form_button_save)

        mTaskFormSave.setOnClickListener(this)
        taskDAO = DatabaseUtil.getDatabaseInstance(this).getTaskDAO()

        mUserId = intent.getIntExtra("userId", -1)
        mTaskId = intent.getIntExtra("taskId", -1)
        if(mTaskId != -1){

            GlobalScope.launch {
                val task = taskDAO.find(mTaskId)
                handler.post {
                    mTaskFormTitle.text = "Editar Tarefa"
                    mTaskFormName.text = Editable.Factory.getInstance().newEditable(task.name)
                    mTaskFormDescription.text = Editable.Factory.getInstance().newEditable(task.description)
                    mTaskFormIsDone.isChecked = task.isDone
                }
            }

        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.task_form_button_save ->{
                val name = mTaskFormName.text.toString()
                val description = mTaskFormDescription.text.toString()
                val isDone = mTaskFormIsDone.isChecked
                if(name.isEmpty()){
                    mTaskFormName.error = "Este campo não pode estar vazio"
                    return
                }
                if(mTaskId == -1){
                    val task = Task(name = name, description = description, isDone = isDone, userID = mUserId)

                    GlobalScope.launch {
                        taskDAO.insert(task)
                        handler.post {
                            Toast.makeText(applicationContext, "Task cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                } else {
                    //TODO: Editar Tarefa
                    val task = Task(mTaskId, name, description, isDone, mUserId)
                    GlobalScope.launch {
                        taskDAO.update(task)
                        handler.post {
                            Toast.makeText(applicationContext, "Task editada com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            }
        }
    }
}