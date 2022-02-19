package com.example.pm1_e1_0090_0300;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1_e1_0090_0300.transactions.Transactions;

public class ActivityInsert extends AppCompatActivity {
    EditText nombre, telefono, nota;
    Spinner spaises;
    String pais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        spaises = findViewById(R.id.vePaises);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spaises.setAdapter(adapter);

        Button btnGuardar = findViewById(R.id.btnGuardarContacto);
        spaises = findViewById(R.id.vePaises);
        nombre = findViewById(R.id.etNombre);
        telefono = findViewById(R.id.etTelefono);
        nota = findViewById(R.id.etNota);
        pais = spaises.getSelectedItem().toString();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarContacto();
            }
        });

        Button verContacto = findViewById(R.id.btnVerContactos);
        verContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityListView.class);
                startActivity(intent);
            }
        });
    }

    private void agregarContacto() {
        SQLiteConexion connection = new SQLiteConexion(this, Transactions.nameDataBase, null, 1);
        SQLiteDatabase db = connection.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Transactions.country, pais = spaises.getSelectedItem().toString());
        nombre.setError(null);
        telefono.setError(null);
        nota.setError(null);
        String verifiedName = nombre.getText().toString();
        String verifiedPhone = telefono.getText().toString();
        String verifiedNote = nota.getText().toString();
//        String onlyLetterSpaces = "[a-zA-Z][a-zA-Z ]*"; Expresion regular para solo letras y espacios

        if(verifiedName.trim().isEmpty() || verifiedName.length() > 50)  {
            nombre.setError("Ingrese un Nombre de Contacto por favor (Maximo 50 caracteres)");
        } else if(verifiedPhone.trim().isEmpty() || verifiedPhone.length() > 15){
            telefono.setError("Ingrese un Numero de Telefono por favor (Maximo 15 caracteres)");
        } else if(verifiedNote.trim().isEmpty() || verifiedNote.length() > 50){
            nota.setError("Ingrese una Nota por favor (Maximo 50 caracteres)");
        } else {
            values.put(Transactions.name, verifiedName);
            values.put(Transactions.phone, verifiedPhone);
            values.put(Transactions.note, verifiedNote);

            Long result = db.insert(Transactions.tableContacts, Transactions.id, values);
            Toast.makeText(getApplicationContext(),"El Contacto se ha Guardado con Exito: "+ result.toString(), Toast.LENGTH_LONG).show();
            db.close();

            Limpiar();
        }
    }

    private void Limpiar() {
        nombre.setText("");
        telefono.setText("");
        nota.setText("");
        spaises.setSelection(0);
    }
}