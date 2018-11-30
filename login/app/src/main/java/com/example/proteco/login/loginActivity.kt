package com.example.proteco.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class loginActivity : AppCompatActivity() {

    private lateinit var txtUser: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var RBSesion: RadioButton
    private lateinit var logiButton: LoginButton
    private lateinit var callBackManager: CallbackManager

    private lateinit var auth: FirebaseAuth

    public var isActivateRB: Boolean = false

    private val STRING_PREFERENCES: String = "com.example.proteco.login"
    private val STRING_RB_SESION: String = "estado.button.sesion"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(obtenerEstado() || AccessToken.getCurrentAccessToken()!=null){
            action()
        }

        txtUser = findViewById(R.id.txtUser)
        txtPassword = findViewById(R.id.txtPassword)

        RBSesion = findViewById(R.id.RBSesion)
        progressBar = findViewById(R.id.progressBar)
        auth = FirebaseAuth.getInstance()

        logiButton = findViewById(R.id.login_button)
        callBackManager = CallbackManager.Factory.create()
        login_button.registerCallback(callBackManager,  object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                startActivity(Intent(this@loginActivity,MainActivity::class.java))
            }

            override fun onCancel() {
               Toast.makeText(this@loginActivity,"cancelada",Toast.LENGTH_SHORT)
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@loginActivity,"cancelada",Toast.LENGTH_SHORT)
            }

        } )

        isActivateRB = RBSesion.isChecked
        RBSesion.setOnClickListener {
            if(isActivateRB){
                RBSesion.isChecked = false
            }
            isActivateRB = RBSesion.isChecked
        }
    }

    fun forgotPassword(view: View){
        startActivity(Intent(this,forgotPassActivity::class.java))
    }

    fun register(view: View){
        startActivity(Intent(this,registerActivity::class.java))
    }

    fun login(view: View){

        loginUser()
        /*if(obtenerEstado()){
            Toast.makeText(this,"salio true",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"salio false",Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun loginUser(){
        val user:String = txtUser.text.toString().trim()
        val password:String = txtPassword.text.toString().trim()

        if(!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)){
            progressBar.visibility=View.VISIBLE

            auth.signInWithEmailAndPassword(user,password).addOnCompleteListener(this){
                task->

                if(task.isSuccessful){
                    guardaEstadoRB()
                    action()
                }else{
                    Toast.makeText(this,"Error en las autenticacion",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun action(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    public fun guardaEstadoRB(){
        var preferences: SharedPreferences
        preferences = getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().putBoolean(STRING_RB_SESION,RBSesion.isChecked).apply()

    }

    public fun obtenerEstado():Boolean{
        var preferences: SharedPreferences
        preferences = getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getBoolean(STRING_RB_SESION,false)

    }

    companion object {
        private val STRING_PREFERENCES: String = "com.example.proteco.login"
        private val STRING_RB_SESION: String = "estado.button.sesion"
        public fun cambiaEstado(c: Context,b: Boolean){
            var preferences: SharedPreferences
            preferences = c.getSharedPreferences(STRING_PREFERENCES, Context.MODE_PRIVATE)
            preferences.edit().putBoolean(STRING_RB_SESION,b).apply()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callBackManager?.onActivityResult(requestCode, resultCode, data)
    }

}
