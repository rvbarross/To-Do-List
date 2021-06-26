package br.unifor.todolist.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import br.unifor.todolist.R
import br.unifor.todolist.database.UserDAO
import br.unifor.todolist.model.User
import br.unifor.todolist.util.DatabaseUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userDAO :UserDAO

    private lateinit var mFirstName :EditText
    private lateinit var mSecondName :EditText
    private lateinit var mEmail :EditText
    private lateinit var mPassword :EditText
    private lateinit var mPasswordConfirmation :EditText
    private lateinit var mRegister :Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userDAO = DatabaseUtil.getDatabaseInstance(applicationContext).getUserDAO()

        mFirstName = findViewById(R.id.register_edittext_nome)
        mSecondName = findViewById(R.id.register_edittext_sobrenome)
        mEmail = findViewById(R.id.register_edittext_email)
        mPassword = findViewById(R.id.register_edittext_senha)
        mPasswordConfirmation = findViewById(R.id.register_edittext_confirmasenha)

        mRegister = findViewById(R.id.register_button_registrar)
        mRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.register_button_registrar -> {
                val firstName = mFirstName.text.toString()
                val secondName = mSecondName.text.toString()
                val email = mEmail.text.toString()
                val password = mPassword.text.toString()
                val passwordConfirmation = mPasswordConfirmation.text.toString()

                var isFormFilled = true

                if(firstName.isEmpty()){ mFirstName.error = "Este campo não pode ficar vazio"; isFormFilled = false }
                if(secondName.isEmpty()){ mSecondName.error = "Este campo não pode ficar vazio"; isFormFilled = false }
                if(email.isEmpty()){ mEmail.error = "Este campo não pode ficar vazio"; isFormFilled = false }
                if(password.isEmpty()){ mPassword.error = "Este campo não pode ficar vazio"; isFormFilled = false }
                if(passwordConfirmation.isEmpty()){ mPasswordConfirmation.error = "Este campo não pode ficar vazio"; isFormFilled = false }

                if(isFormFilled){
                    if(password != passwordConfirmation){
                        mPasswordConfirmation.error = "As senhas não coincidem"
                        return
                    }

                    GlobalScope.launch {
                        val handler = Handler(Looper.getMainLooper())
                        val user = userDAO.findByEmail(email)
                        if(user == null) {
                            val newUser = User(
                                firstName = firstName,
                                lastName = secondName,
                                email = email,
                                password = BCrypt.withDefaults()
                                    .hashToString(12, password.toCharArray())
                            )
                            userDAO.insert(newUser)
                            handler.post {
                                Toast.makeText(applicationContext, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }else{
                            handler.post {
                                mEmail.error ="Ja existe um usuário cadastrado com esse email"
                            }
                        }
                    }
                }
            }
        }
    }
}