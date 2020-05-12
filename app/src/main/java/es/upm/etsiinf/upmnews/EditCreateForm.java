package es.upm.etsiinf.upmnews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.model.Image;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.network.exceptions.ServerCommunicationError;

public class EditCreateForm extends AppCompatActivity {
    private static final int SELECT_PHOTO = 1;
    private Article upload;
    private Image artImg=null;
    private String title;
    private String subtitle;
    private String resume;
    private String body;
    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcreate_form);
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

        Button bcancel = findViewById(R.id.buttonCancel);
        bcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button bsave = findViewById(R.id.buttonSave);
        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkFields()){
                    AlertDialog.Builder mandatory = new AlertDialog.Builder(EditCreateForm.this);
                    mandatory.setTitle("All fields marked with * are mandatory");
                    mandatory.setMessage("Please complete all the required fields to proceed");
                    mandatory.setPositiveButton("OK",null);
                    AlertDialog alert = mandatory.create();
                    alert.show();
                }else{
                    if(true){
                        AlertDialog.Builder ok = new AlertDialog.Builder(EditCreateForm.this);
                        ok.setTitle("Your article was saved successfully");
                        ok.setMessage("Your article has been saved and uploaded to our servers");
                        ok.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                onBackPressed();
                            }
                        });
                        AlertDialog okMessage = ok.create();
                        okMessage.show();
                    }else{
                        AlertDialog.Builder fail = new AlertDialog.Builder(EditCreateForm.this);
                        fail.setTitle("Error communicating with the server, operation failed");
                        fail.setMessage("Due to an internal error the operation was cancelled");
                        fail.setPositiveButton("Ok",null);
                        AlertDialog alert = fail.create();
                        alert.show();
                    }
                }
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

    private boolean checkFields(){
        boolean res =true;
        title = ((EditText)findViewById(R.id.newTitle)).getText().toString();
        subtitle = ((EditText)findViewById(R.id.newSubtitle)).getText().toString();
        resume = ((EditText)findViewById(R.id.newAbstract)).getText().toString();
        body = ((EditText)findViewById(R.id.newBody)).getText().toString();
        Spinner cat = findViewById(R.id.selectCategory);
        category = (String)cat.getItemAtPosition(cat.getSelectedItemPosition());
        if(title.isEmpty() || subtitle.isEmpty() || resume.isEmpty() || body.isEmpty() || category.isEmpty()){
            res = false;
        }
        return res;
    }

    //al volver a la pantalla principal hay que refrescar la lista de articulos
    private boolean saveArticle(){
        boolean res=true;
            upload = new Article(category,title, resume,body,subtitle,ModelManager.getIdUser());
            UploadArticleTask task = new UploadArticleTask(this,upload);
            //debemos setId del articulo creado,
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
