package com.example.pm1_e1_0090_0300;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pm1_e1_0090_0300.tables.Contacts;
import com.example.pm1_e1_0090_0300.transactions.Transactions;

import java.util.ArrayList;

import static android.Manifest.permission.CALL_PHONE;

public class ActivityListView extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView verListaContactos;
    ArrayList<Contacts> ListaContactos;
    ArrayList<String> ListaStringContactos;
    int posicionlista;
    String telefonoSeleccionado;
    String codigoRegion;
    String codigoRegionSeleccionado;
    String nombreSeleccionado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        conexion = new SQLiteConexion(this, Transactions.nameDataBase, null, 1);
        verListaContactos = findViewById(R.id.listContacts);

        getListContacts();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ListaStringContactos);
        verListaContactos.setAdapter(arrayAdapter);

        verListaContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicionlista = ListaContactos.get(position).getId();
                nombreSeleccionado = ListaContactos.get(position).getNombre();
                codigoRegion = ListaContactos.get(position).getPais();
                telefonoSeleccionado = ListaContactos.get(position).getTelefono();

                if(codigoRegion.contains("504")){
                    codigoRegionSeleccionado = "+504";
                } else if(codigoRegion.contains("506")){
                    codigoRegionSeleccionado = "+506";
                } else if(codigoRegion.contains("503")){
                    codigoRegionSeleccionado = "+503";
                } else if(codigoRegion.contains("502")){
                    codigoRegionSeleccionado = "+502";
                }
                String positionString = String.valueOf(posicionlista);
                Toast.makeText(getApplicationContext(), "Contacto: "+positionString+" Seleccionado", Toast.LENGTH_SHORT).show();


            }
        });


        Button btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posicionlista > 0){
                    AlertDialog.Builder alertDelete = new AlertDialog.Builder(ActivityListView.this);
                    alertDelete.setMessage("Desea Eliminar a "+ nombreSeleccionado)
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EliminarItem();
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), ActivityListView.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog tittle = alertDelete.create();
                    tittle.setTitle("ADVERTENCIA");
                    tittle.show();

                } else{
                    Toast.makeText(getApplicationContext(), "Seleccione un Contacto que desee Eliminar", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button btnActualizar = findViewById(R.id.btnActulizar);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posicionlista > 0){
                    finish();
                    getValuesToSendUpdateScreen();
                } else{
                    Toast.makeText(getApplicationContext(), "Seleccione un Contacto que desee Actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnLlamar = findViewById(R.id.btnLlamar);
        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posicionlista > 0){
                    AlertDialog.Builder alertCall = new AlertDialog.Builder(ActivityListView.this);
                    alertCall.setMessage("Llamar a "+ nombreSeleccionado)
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(Intent.ACTION_CALL);
                                    i.setData(Uri.parse("tel:"+ codigoRegionSeleccionado + telefonoSeleccionado));

                                    if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        startActivity(i);
                                    } else {
                                        requestPermissions(new String[]{CALL_PHONE}, 1);
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog tittle = alertCall.create();
                    tittle.setTitle("ACCION");
                    tittle.show();
                } else{
                    Toast.makeText(getApplicationContext(), "Seleccione un Contacto que desee Llamar", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void EliminarItem(){
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] parameterId = {String.valueOf(posicionlista)};

        db.delete(Transactions.tableContacts, Transactions.id + "=?", parameterId);
        Toast.makeText(getApplicationContext(), "Contacto Eliminado", Toast.LENGTH_LONG).show();
    }

    private void getValuesToSendUpdateScreen(){
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] parameterId = {String.valueOf(posicionlista)};
        String[] fields = {Transactions.id,
                Transactions.country,
                Transactions.name,
                Transactions.phone,
                Transactions.note};

        String whereCondition = Transactions.id + "=?";

        try {
            Cursor cursorQueryContact = db.query(Transactions.tableContacts, fields, whereCondition, parameterId,
                    null, null, null);

            cursorQueryContact.moveToFirst();

            Intent intentUpdate = new Intent(this, ActivityUpdate.class);
            Bundle sendValuesforUpdate = new Bundle();
            sendValuesforUpdate.putInt("id", cursorQueryContact.getInt(0));
            sendValuesforUpdate.putString("country", cursorQueryContact.getString(1));
            sendValuesforUpdate.putString("name", cursorQueryContact.getString(2));
            sendValuesforUpdate.putString("phone", cursorQueryContact.getString(3));
            sendValuesforUpdate.putString("note", cursorQueryContact.getString(4));

            intentUpdate.putExtras(sendValuesforUpdate);
            startActivity(intentUpdate);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Seleccione un Contacto Primero", Toast.LENGTH_SHORT).show();
        }
    }

    private void getListContacts() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contacts listContacts;
        ListaContactos = new ArrayList<>();

        Cursor cursorQueryContacts = db.rawQuery("SELECT * FROM " + Transactions.tableContacts, null);

        while (cursorQueryContacts.moveToNext()) {
            listContacts = new Contacts();
            listContacts.setId(cursorQueryContacts.getInt(0));
            listContacts.setPais(cursorQueryContacts.getString(1));
            listContacts.setNombre(cursorQueryContacts.getString(2));
            listContacts.setTelefono(cursorQueryContacts.getString(3));
            listContacts.setNota(cursorQueryContacts.getString(4));

            ListaContactos.add(listContacts);
        }

        cursorQueryContacts.close();
        fillList();
    }

    private void fillList() {
        ListaStringContactos = new ArrayList<>();
        for(int i = 0; i < ListaContactos.size(); i++){
            ListaStringContactos.add(ListaContactos.get(i).getId()+" | "
                    + ListaContactos.get(i).getNombre() + " | "
                    + ListaContactos.get(i).getTelefono());
        }
    }
}