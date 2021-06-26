package br.unifor.todolist.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import br.unifor.todolist.R
import br.unifor.todolist.database.UserDAO
import br.unifor.todolist.util.DatabaseUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mLoginEmail :EditText
    private lateinit var mLoginPassword :EditText
    private lateinit var mBtnLogin :Button
    private lateinit var mRegister :TextView
    private lateinit var userDAO :UserDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userDAO = DatabaseUtil.getDatabaseInstance(applicationContext).getUserDAO()

        mLoginEmail = findViewById(R.id.login_edittext_email)
        mLoginPassword = findViewById(R.id.login_edittext_senha)
        mBtnLogin = findViewById(R.id.login_button_login)
        mBtnLogin.setOnClickListener(this)

        mRegister = findViewById(R.id.login_textview_registro)
        mRegister.setOnClickListener(this)
    }

    override fun onClick(v :View?){

        when(v?.id){
            R.id.login_textview_registro ->{
                val it = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(it)
            }
            R.id.login_button_login -> {
                val email = mLoginEmail.text.toString()
                val password = mLoginPassword.text.toString()
                var isLoginFormFilled = true

                if(email.isEmpty()){ mLoginEmail.error = "Esse campo não pode estar vazio"; isLoginFormFilled = false }
                if(password.isEmpty()){ mLoginPassword.error = "Esse campo não pode estar vazio"; isLoginFormFilled = false }
                if(isLoginFormFilled){
                    GlobalScope.launch {

                        val user = userDAO.findByEmail(email)
                        if(user != null){
                            if(BCrypt.verifyer().verify(password.toCharArray(), user.password).verified){
                                val it = Intent(applicationContext, MainActivity::class.java)
                                it.putExtra("userId", user.id)
                                startActivity(it)
                            }else{
                                showToastError()
                            }
                        }else{
                            showToastError()
                        }
                    }
                }
            }
        }
    }
    fun showToastError(){
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(applicationContext, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show()
        }
    }
}