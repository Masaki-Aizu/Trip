package com.websarva.wings.android.triplog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

// 残っている処理
// タイトルのユニークの確認、　日付の確認、　

public class SaveInfo extends AppCompatActivity {
    private static final int RESULT_PICK_IMAGEFILE = 1001;
    private static final int REQUEST_PERMISSION = 1000;
    private static final int READ_REQUEST_CODE = 42;

    private TestOpenHelper helper;
    private SQLiteDatabase db;
    private double latitude;
    private double longtitude;
    public static final String EXTRA_LONGTITUDE = "com.websarva.wings.android.triplog.EXTRA_LONGTITUDE";
    public static final String EXTRA_LATITUDE = "com.websarva.wings.android.triplog.EXTRA_LATITUDE";
    private ImageView imageView;
    private Bitmap bmp;
    private Uri uri;

    private File path;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_save_location);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0);
        longtitude = intent.getDoubleExtra(EXTRA_LONGTITUDE, 0);

        final EditText editTextTitle = findViewById(R.id.reg_name);
        final EditText editTextDate = findViewById(R.id.reg_day);
        final EditText editTextMemo = findViewById(R.id.reg_memo);
        imageView = findViewById(R.id.select_img);

        path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        Button buttonSave = findViewById(R.id.save_img);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 23){
                    checkPermission();
                }
                else{
                    setUpWriteExternalStorage();
                }
            }
        });

        Button returnButton = findViewById(R.id.save_info);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helper == null){
                    helper = new TestOpenHelper(getApplicationContext());
                }

                if(db == null){
                    db = helper.getWritableDatabase();
                }

                String title = editTextTitle.getText().toString();
                String date = editTextDate.getText().toString();
                String memo = editTextMemo.getText().toString();

                if(title.length() == 0 || date.length() == 0 || memo.length() == 0 || bmp == null ){
                    Toast toast = Toast.makeText(getApplicationContext(), "フォームをすべて入力してください。", Toast.LENGTH_SHORT);
                    toast.show();

                } else {

                    file = new File(path, title + ".jpg");
                    saveImg(file);
                    saveData(db, title, date, memo);


                    finish();
                }
            }
        });
    }

    public void saveData(SQLiteDatabase db, String title, String date, String memo) {
        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("date", date);
        values.put("latitude", latitude);
        values.put("longtitude", longtitude);
        values.put("memo", memo);

        db.insert("tripdb", null, values);
    }

    private void saveImg(File file) {
        try(// 画像ファイルを取り出し
            InputStream inputStream =
                    getContentResolver().openInputStream(uri);

            // 外部ストレージに画像を保存
            FileOutputStream output =
                    new FileOutputStream(file)) {

            // バッファーを使って画像を書き出す
            int DEFAULT_BUFFER_SIZE = 1024 * 4;
            byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
            int len;

            while((len=inputStream.read(buf))!=-1){
                output.write(buf,0,len);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 画像の取得
    private void setUpWriteExternalStorage(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        super.onActivityResult(requestCode, resultCode, resultData);

        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            if(resultData.getData() != null){

                ParcelFileDescriptor pfDescriptor = null;
                try{
                    uri = resultData.getData();

                    pfDescriptor = getContentResolver().openFileDescriptor(uri, "r");
                    if(pfDescriptor != null){
                        FileDescriptor fileDescriptor = pfDescriptor.getFileDescriptor();
                        bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        pfDescriptor.close();
                        imageView.setImageBitmap(bmp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try{
                        if(pfDescriptor != null){
                            pfDescriptor.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    // permissionの確認
    public void checkPermission() {
        // 既に許可している
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            setUpWriteExternalStorage();
        }
        // 拒否していた場合
        else{
            requestLocationPermission();
        }
    }

    // 許可を求める
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(SaveInfo.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        } else {
            Toast toast = Toast.makeText(this, "許可してください", Toast.LENGTH_SHORT);
            toast.show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    REQUEST_PERMISSION);
        }
    }

    // 結果の受け取り
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 許可されたら画像選択に移行
                setUpWriteExternalStorage();
            } else {
                // それでも拒否された時の対応
                Toast toast = Toast.makeText(this, "何もできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
