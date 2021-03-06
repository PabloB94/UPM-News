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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.widget.Toolbar;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.model.Image;
import es.upm.etsiinf.upmnews.utils.SerializationUtils;
import es.upm.etsiinf.upmnews.utils.async.GetArticleDetails;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;
import es.upm.etsiinf.upmnews.utils.async.UploadArticleTask;

public class EditCreateForm extends AppCompatActivity implements AsyncResponse{
    //variable os the photo selector
    private static final int SELECT_PHOTO = 1;
    //Elements of the form
    private String title;
    private String subtitle;
    private String resume;
    private String body;
    private String category;
    private String articleImage="";
    private int id;

    //creation of the class that discriminates if its edit or create and create the buttons listeners
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcreate_form);
        Toolbar myToolbar = findViewById(R.id.appbarDets);
        setSupportActionBar(myToolbar);
        Intent in = getIntent();
        String inId = in.getStringExtra("id");
        id = Integer.parseInt(inId);
        //if id>=0 load data to edit
        if(id>=0){ getData(inId);}
        //button to select a picture from the phone gallery
        Button bpic = findViewById(R.id.buttonPhoto);
        bpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.setType("*/*");
                in.putExtra(Intent.EXTRA_MIME_TYPES,new String[]{"image/jpg","image/jpeg","image/png"});
                in.setAction(Intent.ACTION_GET_CONTENT);
                in.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(in,SELECT_PHOTO);
            }
        });

        //button to cancel the action and go back to the previous screen
        Button bcancel = findViewById(R.id.buttonCancel);
        bcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //button to erase the selected image
        Button berase= findViewById(R.id.buttonPhotoErase);
        berase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanImage();
            }
        });

        //button that checks all the mandatory fields and uploads the article to the server
        Button bsave = findViewById(R.id.buttonSave);
        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkFields()){
                    AlertDialog.Builder mandatory = new AlertDialog.Builder(EditCreateForm.this);
                    mandatory.setTitle(R.string.mandatory_fields);
                    mandatory.setMessage(R.string.complete_fields);
                    mandatory.setPositiveButton(R.string.ok,null);
                    AlertDialog alert = mandatory.create();
                    alert.show();
                }else{
                    saveArticle();
                }
            }
        });
    }


    //photo selector activity on result executes this method
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

    //auxiliary method to check if all the mandatory fields are filled
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

    //method that creates the async task with the formed article
    private void saveArticle(){
            Article upload = new Article(category,title, resume,body,subtitle,ModelManager.getIdUser());
            if(!articleImage.isEmpty()){
                upload.addImage(articleImage,"Image associated to this article");
                upload.setThumbnail(SerializationUtils.createScaledStrImage(articleImage,160,90));
            }
            if(id>=0){
                upload.setId(id);
            }
            UploadArticleTask task = new UploadArticleTask(this,upload);
            task.execute();

    }

    //auxiliary method to save the photo
    private void loadImage(Bitmap photo){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        articleImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    //okay dialog
    public void saveOk(){
        AlertDialog.Builder ok = new AlertDialog.Builder(EditCreateForm.this);
        ok.setTitle(R.string.article_saved_title);
        ok.setMessage(R.string.article_saved);
        ok.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                onBackPressed();
            }
        });
        AlertDialog okMessage = ok.create();
        okMessage.show();
    }

    //error dialog
    public void saveFail(){
        AlertDialog.Builder ok = new AlertDialog.Builder(EditCreateForm.this);
        ok.setTitle(R.string.upload_error);
        ok.setMessage(R.string.sorry);
        ok.setPositiveButton(R.string.ok,null);
        AlertDialog okMessage = ok.create();
        okMessage.show();
    }

    //method to clean image selection
    public void cleanImage(){
        articleImage="";
        ImageView show = findViewById(R.id.imageShow);
        show.setImageDrawable(null);

    }

    //method to retrieve the specific data from the article selected to edit
    private void getData(String id){
        GetArticleDetails task = new GetArticleDetails(this,id);
        task.execute();
    }

    //method called by the async task on finish when details retrieved
    @Override
    public void processData(Article output) {
        EditText title =findViewById(R.id.newTitle);
        EditText subtitle =findViewById(R.id.newSubtitle);
        EditText resume =findViewById(R.id.newAbstract);
        EditText body =findViewById(R.id.newBody);
        Spinner cat = findViewById(R.id.selectCategory);
        ImageView photo =findViewById(R.id.imageShow);

        title.setText(output.getTitleText(), TextView.BufferType.EDITABLE);
        subtitle.setText(output.getSubtitleText(),TextView.BufferType.EDITABLE);
        resume.setText(output.getAbstractText(),TextView.BufferType.EDITABLE);
        body.setText(output.getBodyText(),TextView.BufferType.EDITABLE);
        cat.setSelection(getIndex(cat,output.getCategory()));
        Image img = output.getImage();
        if(img!=null && !img.getDescription().isEmpty()){
            Bitmap oldphoto= getPhoto(img);
            photo.setImageBitmap(oldphoto);
            loadImage(oldphoto);
        }
    }

    //auxiliary method to work with the category selector
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    //auxiliary method to generate the photo bitmap
    private Bitmap getPhoto(Image img){
        String imgbase64 = img.getImage();
        byte[] decodedString = Base64.decode(imgbase64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    //empty method to fulfill interface
    @Override
    public void processFinish(Boolean output) {  }

    //auxiliary method to configure the upper bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.titlebar, menu);
        return true;
    }

}
