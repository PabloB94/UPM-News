package es.upm.etsiinf.upmnews;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EditCreateForm extends AppCompatActivity {
    private static final int SELECT_PHOTO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcreate_form);
        Intent in = getIntent();
        //al darle a guardar tiene que salir mensaje de confirmación y meter obligacion * en codigo
        Button bpic = findViewById(R.id.buttonPhoto);
        bpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setType("image/*");
                in.setType("*/*");
                in.putExtra(Intent.EXTRA_MIME_TYPES,new String[]{"image/jpg","image/jpeg","image/png"});
                in.setAction(Intent.ACTION_GET_CONTENT);
                in.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(in,SELECT_PHOTO);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case SELECT_PHOTO:
                if(resultCode == Activity.RESULT_OK){
                    InputStream stream = null;
                    try {
                        stream = getContentResolver().openInputStream(data.getData());
                        Uri photoLink = data.getData();
                        Cursor retcursor = getContentResolver().query(photoLink, null, null, null, null);
                        int nameIndex = retcursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        TextView namephoto = findViewById(R.id.photoLabel);
                        String nombre = retcursor.getString(nameIndex);
                        namephoto.setText(nombre);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }else{}

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
