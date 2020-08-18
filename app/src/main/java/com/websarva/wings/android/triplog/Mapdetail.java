package com.websarva.wings.android.triplog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Mapdetail extends AppCompatActivity {

    private double latitude;
    private double longtitude;
    private String name;
    private String memo;
    private String date;

    //public static final String LONGTITUDE = "com.websarva.wings.android.triplog.LONGTITUDE";
    //public static final String LATITUDE = "com.websarva.wings.android.triplog.LATITUDE";
    //public static final String EXTRA_NAME = "com.websarva.wings.android.triplog.EXTRA_NAME";
    //public static final String EXTRA_MEMO = "com.websarva.wings.android.triplog.EXTRA_MEMO";
    //public static final String EXTRA_DATE = "com.websarva.wings.android.triplog.EXTRA_DATE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapdetail);

        Intent intent = getIntent();
        //latitude = intent.getDoubleExtra("LATITUDE", 0);
        //longtitude = intent.getDoubleExtra("LONGTITUDE", 0);
        name = intent.getStringExtra("EXTRA_NAME");
        memo = intent.getStringExtra("EXTRA_MEMO");
        date = intent.getStringExtra("EXTRA_DATE");
        //Bundle b = intent.getExtras();
        //Bitmap bmp = (Bitmap) b.get("IMG");

        //TextView title = findViewById(R.id.set_title);
        //TextView day = findViewById(R.id.set_day);
        //TextView note = findViewById(R.id.set_memo);
        //ImageView imageView = findViewById(R.id.set_img);

        //title.setText((String)name);
        //day.setText(date);
        //note.setText(memo);
        //imageView.setImageBitmap(bmp);

        // データテスト
        Button button = findViewById(R.id.map_move);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Mapdetail.this, name,Toast.LENGTH_LONG).show();
                Toast.makeText(Mapdetail.this, date,Toast.LENGTH_LONG).show();
                Toast.makeText(Mapdetail.this, memo, Toast.LENGTH_LONG).show();
            }
        });
    }

}
