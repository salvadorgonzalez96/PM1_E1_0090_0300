package com.example.pm1_e1_0090_0300;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.pm1_e1_0090_0300.transactions.Transactions;

public class ActivityUpdate extends AppCompatActivity {
    SQLiteConexion conexion;
    Spinner spPais;
    String pais;
    Integer idRecibido;
    EditText actualizarNombre, actualizarTelefono, actualizarNota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        conexion = new SQLiteConexion(this, Transactions.nameDataBase, null, 1);

        spPais = findViewById(R.id.spPais);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPais.setAdapter(adapter);

        actualizarNombre = findViewById(R.id.etActualizarNombre);
        actualizarTelefono = findViewById(R.id.etActualizarTelefono);
        actualizarNota = findViewById(R.id.etActualizarNota);

        Bundle recoverValuesBundle = this.getIntent().getExtras();

        idRecibido = recoverValuesBundle.getInt("id");
        actualizarNombre.setText(recoverValuesBundle.getString("name"));
        actualizarTelefono.setText(recoverValuesBundle.getString("phone"));
        actualizarNota.setText(recoverValuesBundle.getString("note"));

        Button btnActualizar = findViewById(R.id.btnUpdateContact);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarContacto();
                Intent intentListView = new Intent(getApplicationContext(), ActivityListView.class);
                startActivity(intentListView);
            }
        });

    }

    private void actualizarContacto() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] params = {String.valueOf(idRecibido)};

        ContentValues values = new ContentValues();
        pais = spPais.getSelectedItem().toString();
        values.put(Transactions.country, pais);
        values.put(Transactions.name, actualizarNombre.getText().toString());
        values.put(Transactions.phone, actualizarTelefono.getText().toString());
        values.put(Transactions.note, actualizarNota.getText().toString());

        db.update(Transactions.tableContacts, values, Transactions.id + "=?", params);
        Toast.makeText(getApplicationContext(), "Contacto Actualizado", Toast.LENGTH_LONG).show();

    }
}