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
import com.upc.model.OrdenAtencionBean;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OrdenesAtencion extends AppCompatActivity {

    public ArrayList<OrdenAtencionBean> ordenes;
    public ArrayList<String> lista = new ArrayList<>();
    public String seleccinado="";
    public String codigo="";

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
                    if(obj.getDescripcion().contains(seleccinado)){
                        codigo = obj.getCodigoOrden();
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
            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),
                    "SELECCIONE UNA ORDEN", Toast.LENGTH_SHORT).show();
        }
    }
    /*
    public void regresar(View v){
        finish();
    }
    */

    private class HttpRequestTask extends AsyncTask<Void, Void, List<OrdenAtencionBean>> {
        @Override
        protected List<OrdenAtencionBean> doInBackground(Void... params) {
            Log.i("doInBackground", "inicio");
            try {
                String url = "http://104.214.71.24:8080/RESTService/api/WOrdenAtencionLista/"+ MainActivity.codigoUsuario +"/P";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Object objetos = restTemplate.getForObject(url, Object.class);
                Log.i("RESPUESTA",objetos.toString());
                LinkedHashMap<String,Object> respuesta = (LinkedHashMap<String,Object>) objetos;
                Log.i("LinkedHashMap", respuesta.toString());
                ArrayList<LinkedHashMap<String,String>> lista =
                        (ArrayList<LinkedHashMap<String,String>>) respuesta.get("DTOOrdenAtencionList");
                Log.i("ORDENES", lista.toString());
                for (LinkedHashMap<String,String> lhm : lista) {
                    Log.i("ORDEN", lhm.toString());
                    OrdenAtencionBean nuevo = new OrdenAtencionBean();
                    nuevo.setCodigoOrden(String.valueOf(lhm.get("CodigoOrden")));
                    nuevo.setDescripcion(String.valueOf(lhm.get("Descripcion")));
                    nuevo.setEstado(String.valueOf(lhm.get("Estado")));
                    nuevo.setFecha(String.valueOf(lhm.get("Fecha")));
                    ordenes.add(nuevo);
                }
                return ordenes;
            } catch (Exception e) {
                Log.i("Exception", "ERROR");
                Log.e("HttpRequestTask", e.getMessage(), e);
            }
            Log.i("doInBackground", "fin");
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<OrdenAtencionBean> ordenes) {
            Log.i("onPostExecute", "inicio");
            Log.i("LISTO",ordenes.toString());
            lista = new ArrayList<>();
            for (OrdenAtencionBean obj:ordenes) {
                lista.add(obj.getDescripcion());
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
            Log.i("onPostExecute", "fin");
        }

    }

}
