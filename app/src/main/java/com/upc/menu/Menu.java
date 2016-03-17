package com.upc.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.upc.ingreso.R;
import com.upc.interrupcion.GenerarInforme;
import com.upc.interrupcion.Interrupcion;
import com.upc.interrupcion.OrdenesAtencion;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void irConsultarTicket(View v){
        Intent i = new Intent(this,Interrupcion.class);
        startActivity(i);
    }

    public void irConsultarOrdenAtencion(View v){
        Intent i = new Intent(this,OrdenesAtencion.class);
        startActivity(i);
    }

    public void irGenerarOrdenAtencion(View v){
        Intent i = new Intent(this,GenerarInforme.class);
        startActivity(i);
    }

    public void regresar(View v){
        finish();
    }
}
