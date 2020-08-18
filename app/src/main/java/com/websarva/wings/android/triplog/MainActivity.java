package com.websarva.wings.android.triplog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //データベース格納用変数
    private String name;
    private String date;
    private double longtitude;
    private double latitude;
    private String memo;
    Bitmap bitmap;

    private ImageView imageView;

    // private TextView textView_name;
    // private TextView textView_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 画像を読み込む
        readSQL();

        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(path, name + ".jpg");

        // Log.d("テスト", "これはメッセージです「" + file + "」終わり");

        imageView = findViewById(R.id.rand_img);

        try(InputStream inputStream0 =
                    new FileInputStream(file) ) {
            bitmap = BitmapFactory.decodeStream(inputStream0);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
            //-------------------------------------------------------------
            // ListViewのインスタンスを生成
            //ListView listView = findViewById(R.id.listView);


            // BaseAdapter を継承したadapterのインスタンスを生成
            // レイアウトファイル list_items.xml を
            // activity_main.xml に inflate するためにadapterに引数として渡す
            //BaseAdapter adapter = new TestAdapter(this.getApplicationContext(),
            //        R.layout.list_item, name, date, photo);

            // ListViewにadapterをセット
            // listView.setAdapter(adapter);
            //---------------------------------------------------------------

        Button sendButton = findViewById(R.id.Bt_stat_reg);
        sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplication(), MapsActivity.class);
                    startActivity(intent);
                }
            });

        imageView.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this, "画像がクリックされました", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplication(), Mapdetail.class);
                        intent.putExtra("EXTRA_NAME", name);
                        intent.putExtra("EXTRA_DATE", date);
                        intent.putExtra("LATITUDE", latitude);
                        intent.putExtra("LONGTITUDE", longtitude);
                        intent.putExtra("EXTRA_MEMO", memo);
                        intent.putExtra("IMG", bitmap);
                        startActivity(intent);
                    }
                });

    }

    private void readSQL(){
        TestOpenHelper dbHelper = new TestOpenHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        /*
        SELECT文
         */
        String sql = "SELECT title, date, latitude, longtitude, memo FROM tripdb ORDER BY RANDOM()";

        // SQL文を実行してカーソルを取得
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();

        // データベースから取ってきたデータを変数にセット
        name = c.getString(c.getColumnIndex("title"));
        date = c.getString(c.getColumnIndex("date"));
        latitude = c.getDouble(c.getColumnIndex("latitude"));
        longtitude = c.getDouble(c.getColumnIndex("longtitude"));
        memo = c.getString(c.getColumnIndex("memo"));

        // データベースのクローズ処理
        c.close();
        db.close();

        Log.d("テスト", "これはメッセージです「" + name + "」終わり");
        // dataは取得できている
        }
    }



