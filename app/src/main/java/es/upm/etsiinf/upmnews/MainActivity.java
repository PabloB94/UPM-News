package es.upm.etsiinf.upmnews;

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

        //Test
        Button b = findViewById(R.id.btest);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent openSecondActivity = new Intent(MainActivity.this,DetailsScreen.class);
                startActivity(openSecondActivity);
            }
        });
    }
}
