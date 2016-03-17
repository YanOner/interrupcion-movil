package com.upc.ingreso;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void regresarLogin(View v){
        //Intent i = new Intent(this,MainActivity.class);
        //startActivity(i);
        finish();
    }

    public void enviarPassword(View v){
        String email = ((EditText)findViewById(R.id.editText3)).getText().toString();
        Toast.makeText(getApplicationContext(), "Correo: " + email, Toast.LENGTH_SHORT).show();
    }

}
