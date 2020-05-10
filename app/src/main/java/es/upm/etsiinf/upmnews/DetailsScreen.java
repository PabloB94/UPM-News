package es.upm.etsiinf.upmnews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Hashtable;
import java.util.List;

import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.model.Image;
import es.upm.etsiinf.upmnews.utils.SerializationUtils;
import es.upm.etsiinf.upmnews.utils.network.ModelManager;

public class DetailsScreen extends AppCompatActivity implements AsyncResponse {
    private String id;
    private Article current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen);
        Intent in = getIntent();
        id = in.getStringExtra("id");
        GetArticleDetails task = new GetArticleDetails(this,id);
        task.delegate = this;
        task.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String textazo="Las respuestas que te dieron son válidas. Lo que si me gustaría agregar es que quizás debas analizar utilizar fragmentos, son menos costosos y te dan una UI fluida. Por otra parte es a lo que Android apunta a con jetpack hoy, 1 actividad simple que solo inicia un navigator y después las vistas en fragmentos. Los pases entre vistas se hacen mucho más fluidos (cada actividad tiene un set de recursos asociados que deben cargarse al iniciar, el fragmento trabaja sobre una actividad)Las respuestas que te dieron son válidas. Lo que si me gustaría agregar es que quizás debas analizar utilizar fragmentos, son menos costosos y te dan una UI fluida. Por otra parte es a lo que Android apunta a con jetpack hoy, 1 actividad simple que solo inicia un navigator y después las vistas en fragmentos. Los pases entre vistas se hacen mucho más fluidos (cada actividad tiene un set de recursos asociados que deben cargarse al iniciar, el fragmento trabaja sobre una actividad)Las respuestas que te dieron son válidas. Lo que si me gustaría agregar es que quizás debas analizar utilizar fragmentos, son menos costosos y te dan una UI fluida. Por otra parte es a lo que Android apunta a con jetpack hoy, 1 actividad simple que solo inicia un navigator y después las vistas en fragmentos. Los pases entre vistas se hacen mucho más fluidos (cada actividad tiene un set de recursos asociados que deben cargarse al iniciar, el fragmento trabaja sobre una actividad)";
        Article ejemplo = new Article("Technology","Articulo de prueba del metodo load","Esto es el resumen del articulo que es muy interesante",textazo,"Subtitulo de la noticia de prueba bro","01");
        //loadElements(current);
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

            cat.setText(elems.get("category"));
            title.setText(elems.get("title"));
            subtitle.setText(Html.fromHtml(elems.get("subtitle"),Html.FROM_HTML_MODE_COMPACT));
            resume.setText(Html.fromHtml(elems.get("abstract"),Html.FROM_HTML_MODE_COMPACT));
            Image artImg = load.getImage();
            if(artImg!=null){
                String imgbase64 = artImg.getImage();
                byte[] decodedString = Base64.decode(imgbase64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                img.setImageBitmap(decodedByte);
            }else{
                img.setImageResource(R.drawable.ic_launcher_foreground);
            }
            body.setText(Html.fromHtml(elems.get("body"),Html.FROM_HTML_MODE_COMPACT));
            userUpdate.setText(Integer.toString(load.getIdUser()));
            dateUpdate.setText(elems.get("lastUpdate"));
            //////////////////////////////////////////la fecha de update no tiene get
        }catch(Exception e){
            Log.i("DetailsScreen",e.getMessage());
        }
    }

    @Override
    public void processFinish(List<Article> output) {
            current=output.get(0);
    }
}

