package com.example.dell.cameraemojidemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static String IMG_PATH
            = Environment.getExternalStorageDirectory() + "/CameraEmojiDemo/images/";

    public static final int TAKE_PHOTO = 1;
    private Uri imageUri;

    private Button takePhoto;
    private Button detectEmotion;
    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takePhoto = (Button) findViewById(R.id.btn_take_photo);
        detectEmotion = (Button) findViewById(R.id.btn_detect_emotion);
        picture = (ImageView) findViewById(R.id.iv_picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    openCamera();
                }
            }
        });

        detectEmotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectEmotion();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    toast("You denied the permission!");
                }
                break;
            default:
        }
    }

    public void openCamera(){
        if (!new File(IMG_PATH).exists()) {
            new File(IMG_PATH).mkdirs();
        }
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(IMG_PATH, "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    //TODO 读取拍摄照片进行表情识别
    public void detectEmotion(){
        //未拍照前不可以进行表情识别
        File outputImage = new File(IMG_PATH, "output_image.jpg");
        if(!outputImage.exists()){
            toast("You haven't take a picture!");
        }
        //真正的逻辑

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void toast(String content){
        Toast.makeText(MainActivity.this,content,Toast.LENGTH_SHORT).show();
    }
}
