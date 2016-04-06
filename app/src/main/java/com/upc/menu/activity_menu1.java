package com.upc.menu;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.upc.ingreso.R;
import com.upc.interrupcion.GenerarInforme;
import com.upc.interrupcion.Interrupcion;
import com.upc.interrupcion.OrdenesAtencion;

public class activity_menu1 extends ActionBarActivity {

    private String[] opciones;
    private DrawerLayout drawerLayout;
    private ListView listView;

    private CharSequence tituloSec;
    private CharSequence tituloApp;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity_menu1);

        opciones = new String[] {"Consultar Ticket de Atencion","Consultar Orden de Atencion","Generar Orden de Atencion"};
        drawerLayout = (DrawerLayout) findViewById(R.id.contenedorPrincipald);
        listView = (ListView) findViewById(R.id.menuIzqd);
        listView.setAdapter(new ArrayAdapter<String>(getSupportActionBar().getThemedContext(),
                android.R.layout.simple_list_item_1,opciones));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    irConsultarTicket(view);
                }
                if(position==1){
                    irConsultarOrdenAtencion(view);
            }
                if(position==2){
                    irGenerarOrdenAtencion(view);
                }
                /*
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.contenedorFrame,consultarTicket)
                        .commit();
                */



                listView.setItemChecked(position,true);
                tituloSec = opciones[position];
                getSupportActionBar().setTitle(tituloSec);
                drawerLayout.closeDrawer(listView);

            }
        });

        tituloSec = getTitle();
        tituloApp = getTitle();

        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, R.mipmap.ic_launcher_reorder
                , R.string.abierto, R.string.cerrado){


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                ActivityCompat.invalidateOptionsMenu(activity_menu1.this);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ActivityCompat.invalidateOptionsMenu(activity_menu1.this);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        int id = item.getItemId();
/*
        if(id==R.id.action_settings){
            return true;
        }
  */
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);

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

}
