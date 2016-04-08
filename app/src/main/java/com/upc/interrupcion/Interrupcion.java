package com.upc.interrupcion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.upc.ingreso.MainActivity;
import com.upc.ingreso.R;
import com.upc.model.InterrupcionBean;
import com.upc.utility.services;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Interrupcion extends AppCompatActivity {

    public List<InterrupcionBean> interrupciones;
    public ArrayList<String> lista = new ArrayList<>();
    public String seleccinado="";
    public String codigo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interrupcion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        interrupciones = new ArrayList<>();
        new HttpRequestTask().execute();

        ListView listaViewInterrupciones = (ListView) findViewById(R.id.listViewInterrupciones);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista);
        listaViewInterrupciones.setAdapter(adapter);
        listaViewInterrupciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                seleccinado = ((TextView) view).getText().toString();
                Toast.makeText(getApplicationContext(),
                        seleccinado, Toast.LENGTH_SHORT).show();
                for (InterrupcionBean obj : interrupciones) {
                    if (obj.getDescripcion().contains(seleccinado)) {
                        codigo = obj.getCodigoInterrupcion();
                        Intent i = new Intent(getApplicationContext(), InterrupcionDetalle.class);
                        i.putExtra("contenido", obj.toString());
                        startActivity(i);
                        ((TextView) findViewById(R.id.textViewInterrupciones)).setText("SELECCIÓN: " + seleccinado);
                        break;
                    }
                }
            }
        });

    }

    public void irConsultarCuadrilla(View v){

        seleccinado = " ";
        if(!seleccinado.equalsIgnoreCase("")){
            Intent i = new Intent(this,Cuadrillas.class);
            startActivity(i);
        }else{
            Toast.makeText(getApplicationContext(),
                    "SELECCIONE UNA INTERRUPCION", Toast.LENGTH_SHORT).show();
        }
    }
/*
    public void regresar(View v){
        finish();
    }
*/
    private class HttpRequestTask extends AsyncTask<Void, Void, List<InterrupcionBean>> {
        @Override
        protected List<InterrupcionBean> doInBackground(Void... params) {
            Log.i("doInBackground", "inicio");
            try {
                String url = services.WInterrupcionLista+ MainActivity.codigoUsuario +"/D";
                Log.i("URL",url);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Object objetos = restTemplate.getForObject(url, Object.class);
                Log.i("RESPUESTA",objetos.toString());
                LinkedHashMap<String,Object> respuesta = (LinkedHashMap<String,Object>) objetos;
                Log.i("LinkedHashMap", respuesta.toString());
                ArrayList<LinkedHashMap<String,String>> lista =
                        (ArrayList<LinkedHashMap<String,String>>) respuesta.get("DTOInterrupcionList");
                Log.i("INTERRUPCIONES", lista.toString());
                for (LinkedHashMap<String,String> inter : lista) {
                    Log.i("INTERRUPCION", inter.toString());
                    InterrupcionBean nuevo = new InterrupcionBean();
                    nuevo.setCodigoInterrupcion(String.valueOf(inter.get("CodigoInterrupcion")));
                    nuevo.setDescripcion(String.valueOf(inter.get("Descripcion")));
                    nuevo.setEstado(String.valueOf(inter.get("Estado")));
                    nuevo.setLatitud(String.valueOf(inter.get("Latitud")));
                    nuevo.setLongitud(String.valueOf(inter.get("Longitud")));
                    nuevo.setFecha(String.valueOf(inter.get("Fecha")));
                    interrupciones.add(nuevo);
                }
                return interrupciones;
            } catch (Exception e) {
                Log.i("Exception", "ERROR");
                Log.e("HttpRequestTask", e.getMessage(), e);
            }
            Log.i("doInBackground", "fin");
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<InterrupcionBean> interrupciones) {
            Log.i("onPostExecute", "inicio");
            Log.i("LISTO",interrupciones.toString());
            lista = new ArrayList<>();
            for (InterrupcionBean inter:interrupciones) {
                lista.add(inter.getDescripcion());
            }
            ListView listaViewInterrupciones = (ListView) findViewById(R.id.listViewInterrupciones);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,lista);
            listaViewInterrupciones.setAdapter(adapter);
            TextView tv = (TextView)findViewById(R.id.textViewInterrupciones);
            if(lista.isEmpty()){
                tv.setText("NO SE ENCONTRARÓN INTERRUPCIONES");
            }else{
                tv.setText("SELECCIONE");
            }
            Log.i("onPostExecute", "fin");
        }

    }
}
