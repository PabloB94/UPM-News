package es.upm.etsiinf.upmnews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import es.upm.etsiinf.upmnews.model.Article;
import es.upm.etsiinf.upmnews.utils.SerializationUtils;

public class DetailsScreen extends AppCompatActivity {
    private Article current;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen);
        Intent in = getIntent();
        id = in.getStringExtra("id");
        //current =(Article) in.getSerializableExtra("Article");//pass the article in intent extras, check this out
    }

    @Override
    protected void onResume() {
        super.onResume();
        String textazo="Las respuestas que te dieron son válidas. Lo que si me gustaría agregar es que quizás debas analizar utilizar fragmentos, son menos costosos y te dan una UI fluida. Por otra parte es a lo que Android apunta a con jetpack hoy, 1 actividad simple que solo inicia un navigator y después las vistas en fragmentos. Los pases entre vistas se hacen mucho más fluidos (cada actividad tiene un set de recursos asociados que deben cargarse al iniciar, el fragmento trabaja sobre una actividad)Las respuestas que te dieron son válidas. Lo que si me gustaría agregar es que quizás debas analizar utilizar fragmentos, son menos costosos y te dan una UI fluida. Por otra parte es a lo que Android apunta a con jetpack hoy, 1 actividad simple que solo inicia un navigator y después las vistas en fragmentos. Los pases entre vistas se hacen mucho más fluidos (cada actividad tiene un set de recursos asociados que deben cargarse al iniciar, el fragmento trabaja sobre una actividad)Las respuestas que te dieron son válidas. Lo que si me gustaría agregar es que quizás debas analizar utilizar fragmentos, son menos costosos y te dan una UI fluida. Por otra parte es a lo que Android apunta a con jetpack hoy, 1 actividad simple que solo inicia un navigator y después las vistas en fragmentos. Los pases entre vistas se hacen mucho más fluidos (cada actividad tiene un set de recursos asociados que deben cargarse al iniciar, el fragmento trabaja sobre una actividad)";
        Article ejemplo = new Article("Technology","Articulo de prueba del metodo load","Esto es el resumen del articulo que es muy interesante",textazo,"Subtitulo de la noticia de prueba bro","01");
        loadElements(ejemplo);
    }

    private void loadElements(Article load){
        try{
            //load article elements on view
            TextView cat = findViewById(R.id.categoryShow);
            TextView title= findViewById(R.id.titleShow);
            TextView subtitle = findViewById(R.id.subtitleShow);
            TextView resume = findViewById(R.id.abstractShow);
            ImageView img = (ImageView) findViewById(R.id.imageShow);
            TextView body= findViewById(R.id.bodyShow);
            TextView userUpdate = findViewById(R.id.footnoteNameShow);
            TextView dateUpdate = findViewById(R.id.footnoteDateShow);

            cat.setText(load.getCategory());
            title.setText(load.getTitleText());
            subtitle.setText(load.getSubtitleText());
            resume.setText(load.getAbstractText());
            if(load.getImage()!=null){
                img.setImageBitmap(SerializationUtils.base64StringToImg(load.getImage().getImage()));//comprobar esto
            }else{
                img.setImageResource(R.drawable.ic_launcher_foreground);
            }
            body.setText(load.getBodyText());
            userUpdate.setText(load.getIdUser());
            //////////////////////////////////////////la fecha de update no tiene get
        }catch(Exception e){
            Log.i("DetailsScreen",e.getMessage());
        }
    }
}

