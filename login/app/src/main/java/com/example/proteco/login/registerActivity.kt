package com.example.proteco.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class registerActivity : AppCompatActivity() {

    private lateinit var txtName:EditText
    private lateinit var txtLastName:EditText
    private lateinit var txtEmail:EditText
    private lateinit var txtPassword:EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var dbReference:DatabaseReference
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        txtName = findViewById(R.id.txtName)
        txtLastName = findViewById(R.id.txtLastName)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)

        progressBar = findViewById(R.id.progressBar)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("User")

    }
    fun register(view:View){
        createNewAccount()
    }

    private fun createNewAccount(){
        val name:String = txtName.text.toString().trim()
        val lastName:String = txtLastName.text.toString().trim()
        val email:String = txtEmail.text.toString().trim()
        val password:String = txtPassword.text.toString().trim()

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            progressBar.visibility=View.VISIBLE
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
                task ->

                if(task.isComplete){
                    val user:FirebaseUser?=auth.currentUser
                    verifyEmail(user)
                    val userDB = dbReference.child(user?.uid!!)
                    userDB.child("Name").setValue(name)
                    userDB.child("lastName").setValue(lastName)
                    action()
                }
            }
        }
    }

    private fun action(){
        startActivity(Intent(this,loginActivity::class.java))
    }

    private fun verifyEmail(user:FirebaseUser?){
        user?.sendEmailVerification()?.addOnCompleteListener(this){
            task ->

            if(task.isComplete){
                Toast.makeText(this,"Mail enviado",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Error al enviar mail",Toast.LENGTH_LONG).show()
            }
        }
    }
}
