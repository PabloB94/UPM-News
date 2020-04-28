package es.upm.etsiinf.upmnews;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import es.upm.etsiinf.upmnews.model.Article;

public class DetailsScreen extends AppCompatActivity {
    private Article current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_screen);
        Intent in = getIntent();
        //current =(Article) in.getSerializableExtra("Article");//pass the article in intent extras, check this out
    }

    @Override
    protected void onResume() {
        super.onResume();
        String rollo = "A ScrollView can have only one direct child, so you need to put all the other Views in a Layout, such as LinearLayout and put that layout in ScrollView\n" +
                "\n" +
                "Make your ScrollView's height as match_parent and the inner LinearLayout's height as wrap_content. The LinearLayout will stretch according to the number of children inside it and if the height exceeds the height of the ScrollView, the overflow can be seen by scrolling. If the ScrollView and inner Layout both have same height or if ScrollView have larger height than inner Layout, the scrolling won't happen for obvious reason.A ScrollView can have only one direct child, so you need to put all the other Views in a Layout, such as LinearLayout and put that layout in ScrollView\n" +
                "\n" +
                "Make your ScrollView's height as match_parent and the inner LinearLayout's height as wrap_content. The LinearLayout will stretch according to the number of children inside it and if the height exceeds the height of the ScrollView, the overflow can be seen by scrolling. If the ScrollView and inner Layout both have same height or if ScrollView have larger height than inner Layout, the scrolling won't happen for obvious reason.A ScrollView can have only one direct child, so you need to put all the other Views in a Layout, such as LinearLayout and put that layout in ScrollView\n" +
                "\n" +
                "Make your ScrollView's height as match_parent and the inner LinearLayout's height as wrap_content. The LinearLayout will stretch according to the number of children inside it and if the height exceeds the height of the ScrollView, the overflow can be seen by scrolling. If the ScrollView and inner Layout both have same height or if ScrollView have larger height than inner Layout, the scrolling won't happen for obvious reason.A ScrollView can have only one direct child, so you need to put all the other Views in a Layout, such as LinearLayout and put that layout in ScrollView\n" +
                "\n" +
                "Make your ScrollView's height as match_parent and the inner LinearLayout's height as wrap_content. The LinearLayout will stretch according to the number of children inside it and if the height exceeds the height of the ScrollView, the overflow can be seen by scrolling. If the ScrollView and inner Layout both have same height or if ScrollView have larger height than inner Layout, the scrolling won't happen for obvious reason.A ScrollView can have only one direct child, so you need to put all the other Views in a Layout, such as LinearLayout and put that layout in ScrollView\n" +
                "\n" +
                "Make your ScrollView's height as match_parent and the inner LinearLayout's height as wrap_content. The LinearLayout will stretch according to the number of children inside it and if the height exceeds the height of the ScrollView, the overflow can be seen by scrolling. If the ScrollView and inner Layout both have same height or if ScrollView have larger height than inner Layout, the scrolling won't happen for obvious reason.";
        //load article elements on view
        ImageView img = (ImageView) findViewById(R.id.imageShow);
        img.setImageResource(R.drawable.ic_launcher_foreground);
        TextView texto= findViewById(R.id.bodyShow);
        texto.setText(rollo);
    }
}

