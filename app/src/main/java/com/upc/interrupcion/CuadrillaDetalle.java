package com.upc.interrupcion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.upc.ingreso.R;

public class CuadrillaDetalle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuadrilla_detalle);
        if(getIntent().getExtras().containsKey("contenido")){
            String texto = getIntent().getExtras().getString("contenido");
            ((TextView)findViewById(R.id.textViewCuadrillaDetalle)).setText(texto);
        }
    }
/*
    public void regresar(View v){
        finish();
    }
*/
}
