package com.upc.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.upc.ingreso.R;
import com.upc.interrupcion.ConsultarTicket;

public class activity_menu1 extends AppCompatActivity {

    private String[] opciones;
    private DrawerLayout drawerLayout;
    private ListView listView;

    private CharSequence tituloSec;
    private CharSequence tituloApp;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu1);

        //opciones = new String[] {"Opc.Menu1","Opc.Menu2","Opc.Menu3","Opc.Menu4"};
        opciones = new String[] {"Opc.Menu1"};
        drawerLayout = (DrawerLayout) findViewById(R.id.contenedorPrincipal);
        listView = (ListView) findViewById(R.id.menuIzq);
        listView.setAdapter(new ArrayAdapter<String>(getSupportActionBar().getThemedContext(),
                android.R.layout.simple_list_item_1,opciones));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConsultarTicket consultarTicket = null;
                if(position==0){
                    consultarTicket = new ConsultarTicket();
                }
                /*if(position==1){
                    fragment = new Fragmento2();
                }
                if(position==2){
                    fragment = new Fragmento3();
                }
                if(position==3){
                    fragment = new Fragmento4();
                }*/

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.contenedorFrame,consultarTicket)
                        .commit();

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


}
