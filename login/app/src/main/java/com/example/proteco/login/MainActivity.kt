package com.example.proteco.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.facebook.AccessToken
import com.facebook.login.LoginManager

class MainActivity : AppCompatActivity() {

    private lateinit var cerrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cerrar = findViewById(R.id.cerrarSesion)

        cerrar.setOnClickListener {
            if(AccessToken.getCurrentAccessToken()!=null){
                LoginManager.getInstance().logOut()
            }else {
                loginActivity.cambiaEstado(this, false)
            }
            startActivity(Intent(this,loginActivity::class.java))
            finish()
        }
    }
}
