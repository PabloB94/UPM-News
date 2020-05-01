package es.upm.etsiinf.upmnews;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EditCreateForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcreate_form);
        Intent in = getIntent();
        //falta configurar el boton de seleccionar foto con el filtro, mirar galley permission
        //al darle a guardar tiene que salir mensaje de confirmación y meter obligacion * en codigo
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
