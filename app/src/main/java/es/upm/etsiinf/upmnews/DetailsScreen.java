package es.upm.etsiinf.upmnews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Hashtable;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.model.Image;
import es.upm.etsiinf.upmnews.utils.async.GetArticleDetails;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;

public class DetailsScreen extends AppCompatActivity implements AsyncResponse{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen);
        Toolbar myToolbar = findViewById(R.id.appbarDets);
        setSupportActionBar(myToolbar);
        Intent in = getIntent();
        String id = in.getStringExtra("id");
        GetArticleDetails task = new GetArticleDetails(this,id);
        task.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void loadElements(Article load){
        try{
            //load article elements on view
            TextView cat = findViewById(R.id.categoryShow);
            TextView title= findViewById(R.id.titleShow);
            TextView subtitle = findViewById(R.id.subtitleShow);
            TextView resume = findViewById(R.id.abstractShow);
            ImageView img = findViewById(R.id.imageShow);
            TextView body= findViewById(R.id.bodyShow);
            TextView userUpdate = findViewById(R.id.footnoteNameShow);
            TextView dateUpdate = findViewById(R.id.footnoteDateShow);

            Hashtable<String,String> elems= load.getAttributes();

            String category = elems.get("category");
            cat.setText(category);
            setColor(category, cat);

            title.setText(elems.get("title"));
            subtitle.setText(Html.fromHtml(elems.get("subtitle"),Html.FROM_HTML_MODE_COMPACT));
            resume.setText(Html.fromHtml(elems.get("abstract"),Html.FROM_HTML_MODE_COMPACT));
            Image artImg = load.getImage();
            if(artImg!=null){
                String imgbase64 = artImg.getImage();
                byte[] decodedString = Base64.decode(imgbase64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img.setImageBitmap(decodedByte);
            }
            body.setText(Html.fromHtml(elems.get("body"),Html.FROM_HTML_MODE_COMPACT));
            userUpdate.setText(Integer.toString(load.getIdUser()));
            dateUpdate.setText(elems.get("lastUpdate"));
        }catch(Exception e){
            Log.i("DetailsScreen",e.getMessage());
        }
    }

    @Override
    public void processData(Article output) {
        loadElements(output);
    }

    @Override
    public void processFinish(Boolean output) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.titlebar, menu);
        return true;
    }

    private void setColor(String category, TextView label){
        switch (category){
            case "Sports":
                label.setBackgroundColor(ContextCompat.getColor(this,R.color.Sports));
                break;
            case "National":
                label.setBackgroundColor(ContextCompat.getColor(this,R.color.National));
                break;
            case "Economy":
                label.setBackgroundColor(ContextCompat.getColor(this,R.color.Economy));
                break;
            case "Technology":
                label.setBackgroundColor(ContextCompat.getColor(this,R.color.Technology));
                break;
        }
    }
}

