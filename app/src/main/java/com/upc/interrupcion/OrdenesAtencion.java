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
import com.upc.model.InterrupcionBean;
import com.upc.model.OrdenAtencionBean;
import com.upc.utility.services;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class OrdenesAtencion extends AppCompatActivity {

    public List<OrdenAtencionBean> ordenes;
    public ArrayList<String> lista = new ArrayList<>();
    public String seleccinado="";
    public String codigo="";
    public String codigoInterrupcion="";
    public String codigoCuadrilla="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes_atencion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ordenes = new ArrayList<>();
        new HttpRequestTask().execute();

        ListView listaViewOrdenes = (ListView) findViewById(R.id.listViewOrdenes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lista);
        listaViewOrdenes.setAdapter(adapter);
        listaViewOrdenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView textToast.makeText(getApplicationContext(),
                seleccinado = ((TextView) view).getText().toString();
                Toast.makeText(getApplicationContext(),
                        seleccinado, Toast.LENGTH_SHORT).show();
                for (OrdenAtencionBean obj:ordenes) {
                    if((obj.getDescripcion()+" - "+obj.getComentarios()).contains(seleccinado)){
                        codigo = obj.getCodigoOrden();
                        codigoInterrupcion = obj.getCodigoInterrupcion();
                        codigoCuadrilla = obj.getCodigoCuadrilla();
                        Intent i = new Intent(getApplicationContext(),OrdenAtencionDetalle.class);
                        i.putExtra("contenido", obj.toString());
                        startActivity(i);
                        ((TextView)findViewById(R.id.textViewOrdenes)).setText("SELECCIÓN: " + seleccinado);
                        break;
                    }
                }
            }
        });
    }

    public void atenderOrdenAtencion(View v){
        if(!seleccinado.equalsIgnoreCase("")){
            new HttpRequestTaskAtender().execute();
        }else{
            Toast.makeText(getApplicationContext(),
                    "SELECCIONE UNA ORDEN", Toast.LENGTH_SHORT).show();
        }
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, List<OrdenAtencionBean>> {
        @Override
        protected List<OrdenAtencionBean> doInBackground(Void... params) {
            Log.i("doInBackground", "inicio");
            List<OrdenAtencionBean> lista = new ArrayList<>();
            try {
                String url = services.WOrdenAtencionLista + MainActivity.codigoUsuario +"/P";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Object objetos = restTemplate.getForObject(url, Object.class);
                Log.i("RESPUESTA",objetos.toString());
                List<HashMap<String,Object>> respuesta = (List<HashMap<String, Object>>) objetos;
                for (HashMap<String,Object> inter : respuesta) {
                    Log.i("ATENCION", inter.toString());
                    OrdenAtencionBean nuevo = new OrdenAtencionBean();
                    nuevo.setCodigoOrden(String.valueOf(inter.get("codigo")));
                    nuevo.setDescripcion(String.valueOf(inter.get("descripcion")));
                    nuevo.setComentarios(String.valueOf(inter.get("comentario")));
                    nuevo.setEstado(String.valueOf(inter.get("estado")));
                    nuevo.setFecha(String.valueOf(inter.get("fecha")));
                    nuevo.setCodigoInterrupcion(String.valueOf(inter.get("codigoInterrupcion")));
                    nuevo.setCodigoCuadrilla(String.valueOf(inter.get("codigoCuadrilla")));
                    nuevo.setCodigoUsuario(String.valueOf(inter.get("codigoUsuario")));
                    lista.add(nuevo);
                }
                return lista;
            } catch (Exception e) {
                Log.i("Exception", "ERROR");
                Log.e("HttpRequestTask", e.getMessage(), e);
            }
            Log.i("doInBackground", "fin");
            return lista;
        }

        @Override
        protected void onPostExecute(List<OrdenAtencionBean> ordenesTask) {
            Log.i("onPostExecute", "inicio");
            Log.i("LISTO",ordenesTask.toString());
            lista = new ArrayList<>();
            for (OrdenAtencionBean obj:ordenesTask) {
                lista.add(obj.getDescripcion()+" - "+obj.getComentarios());
            }
            ListView listaViewOrdenes = (ListView) findViewById(R.id.listViewOrdenes);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,lista);
            listaViewOrdenes.setAdapter(adapter);
            TextView tv = (TextView)findViewById(R.id.textViewOrdenes);
            if(lista.isEmpty()){
                tv.setText("NO SE ENCONTRARÓN ORDENES");
            }else{
                tv.setText("SELECCIONE");
            }
            ordenes = ordenesTask;
            Log.i("onPostExecute", "fin");
        }

    }
    //ATENDER
    private class HttpRequestTaskAtender extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("doInBackground", "inicio");
            boolean exito = false;
            try {
                String url = services.WAtenderOrden + codigo + "/" + codigoInterrupcion + "/" +codigoCuadrilla;
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
                finish();
                startActivity(getIntent());
            }else{
                Toast.makeText(getApplicationContext(), "Ocurrió un error al procesar la información.", Toast.LENGTH_LONG).show();
            }
            Log.i("onPostExecute", "fin");
        }

    }
}
