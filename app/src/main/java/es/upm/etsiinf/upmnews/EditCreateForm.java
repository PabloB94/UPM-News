package es.upm.etsiinf.upmnews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;

public class EditCreateForm extends AppCompatActivity {
    private static final int SELECT_PHOTO = 1;
    private String title;
    private String subtitle;
    private String resume;
    private String body;
    private String category;
    private String articleImage="";

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
                    saveArticle();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECT_PHOTO && resultCode == Activity.RESULT_OK){
            InputStream stream = null;
            try {
                stream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                articleImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                ((ImageView)findViewById(R.id.imageShow)).setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.i("EditCreateForm",e.getMessage());
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        Log.i("EditCreateForm",e.getMessage());
                    }
                }
            }
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

    private void saveArticle(){
       Article upload = new Article(category,title, resume,body,subtitle,ModelManager.getIdUser());
        if(!articleImage.isEmpty()){
            upload.addImage(articleImage,"Imagen chula del articulo");
        }
        UploadArticleTask task = new UploadArticleTask(this,upload);
        task.execute();
    }

    public void saveOk(){
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
    }

    public void saveFail(){
        AlertDialog.Builder ok = new AlertDialog.Builder(EditCreateForm.this);
        ok.setTitle("Error uploading your article");
        ok.setMessage("Sorry for the inconveniences");
        ok.setPositiveButton("OK",null);
        AlertDialog okMessage = ok.create();
        okMessage.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
