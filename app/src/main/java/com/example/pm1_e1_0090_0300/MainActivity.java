package com.example.pm1_e1_0090_0300;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAgregarContacto = findViewById(R.id.btnAgregarContacto);
        btnAgregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddContact = new Intent(getApplicationContext(), ActivityInsert.class);
                startActivity(intentAddContact);
            }
        });

        Button btnListaContactos = findViewById(R.id.btnListaContactos);
        btnListaContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentListContacts = new Intent(getApplicationContext(), ActivityListView.class);
                startActivity(intentListContacts);
            }
        });
    }
}