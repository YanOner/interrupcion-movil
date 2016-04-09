package com.upc.interrupcion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.upc.ingreso.MainActivity;
import com.upc.ingreso.R;
import com.upc.model.CuadrillaBean;
import com.upc.utility.services;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Cuadrillas extends AppCompatActivity {

    public ArrayList<CuadrillaBean> cuadrillas;
    public ArrayList<String> lista = new ArrayList<>();
    public static String seleccinado="";
    public String codigo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuadrillas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cuadrillas = new ArrayList<>();
        seleccinado="";
        new HttpRequestTask().execute();

        ListView listaViewCuadrillas = (ListView) findViewById(R.id.listViewCuadrillas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista);
        listaViewCuadrillas.setAdapter(adapter);
        listaViewCuadrillas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                seleccinado = ((TextView) view).getText().toString();
                Toast.makeText(getApplicationContext(),
                        seleccinado, Toast.LENGTH_SHORT).show();
                for (CuadrillaBean obj:cuadrillas) {
                    if(obj.getDescripcion().contains(seleccinado)){
                        codigo = obj.getCodigoCuadrilla();
                        Intent i = new Intent(getApplicationContext(), CuadrillaDetalle.class);
                        i.putExtra("contenido", obj.toString());
                        startActivity(i);
                        ((TextView)findViewById(R.id.textViewCuadrillas)).setText("SELECCIÓN: " + seleccinado);
                        break;
                    }
                }
            }
        });
    }

    public void asignarCuadrilla(View v){
        if(!seleccinado.equalsIgnoreCase("")){
            new HttpRequestTaskAsignar().execute();
        }else{
            Toast.makeText(getApplicationContext(),
                    "SELECCIONE UNA CUADRILLA", Toast.LENGTH_SHORT).show();
        }
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, List<CuadrillaBean>> {
        @Override
        protected List<CuadrillaBean> doInBackground(Void... params) {
            Log.i("doInBackground", "inicio");
            try {
                String url = services.WCuadrillaLista+ MainActivity.codigoUsuario +"/D";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Object objetos = restTemplate.getForObject(url, Object.class);
                Log.i("RESPUESTA",objetos.toString());
                List<HashMap<String,Object>> respuesta = (List<HashMap<String, Object>>) objetos;
                for (HashMap<String,Object> lhm : respuesta) {
                    Log.i("CUADRILLA", lhm.toString());
                    CuadrillaBean nuevo = new CuadrillaBean();
                    nuevo.setCodigoCuadrilla(String.valueOf(lhm.get("codigo")));
                    nuevo.setDescripcion(String.valueOf(lhm.get("descripcion")));
                    nuevo.setEstado(String.valueOf(lhm.get("estado")));
                    cuadrillas.add(nuevo);
                }
                return cuadrillas;
            } catch (Exception e) {
                Log.i("Exception", "ERROR");
                Log.e("HttpRequestTask", e.getMessage(), e);
            }
            Log.i("doInBackground", "fin");
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<CuadrillaBean> cuadrillas) {
            Log.i("onPostExecute", "inicio");
            Log.i("LISTO",cuadrillas.toString());
            lista = new ArrayList<>();
            for (CuadrillaBean obj:cuadrillas) {
                lista.add(obj.getDescripcion());
            }
            ListView listaViewCuadrillas = (ListView) findViewById(R.id.listViewCuadrillas);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,lista);
            listaViewCuadrillas.setAdapter(adapter);
            TextView tv = (TextView)findViewById(R.id.textViewCuadrillas);
            if(lista.isEmpty()){
                tv.setText("NO SE ENCONTRARÓN CUADRILLAS");
            }else{
                tv.setText("SELECCIONE");
            }
            Log.i("onPostExecute", "fin");
        }

    }

    //ASIGNAR
    private class HttpRequestTaskAsignar extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("doInBackground", "inicio");
            boolean exito = false;
            try {
                String url = services.WAsignarCuadrilla + Interrupcion.codigo + "/" + codigo;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Object objetos = restTemplate.getForObject(url, Object.class);
                Log.i("RESPUESTA",objetos.toString());
                LinkedHashMap<String,Object> respuesta = (LinkedHashMap<String,Object>) objetos;
                String valor = String.valueOf(respuesta.get("exito"));
                if(valor.equals("true")){
                    exito = true;
                }
            } catch (Exception e) {
                Log.i("Exception", "ERROR");
                Log.e("HttpRequestTask", e.getMessage(), e);
            }
            Log.i("doInBackground", "fin");
            return exito;
        }

        @Override
        protected void onPostExecute(Boolean exito) {
            Log.i("onPostExecute", "inicio");
            if(exito){
                Toast.makeText(getApplicationContext(), "Proceso terminado exitosamente.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Ocurrió un error al procesar la información.", Toast.LENGTH_LONG).show();
            }
            Log.i("onPostExecute", "fin");
        }

    }
}
