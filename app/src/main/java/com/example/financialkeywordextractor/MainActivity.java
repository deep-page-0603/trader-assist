package com.example.financialkeywordextractor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img1 = (ImageView)findViewById(R.id.img1);
        img1.setBackgroundResource(R.drawable.logo);

        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setTypeface(null, Typeface.BOLD);


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Choose_Image();
            }
        });

        //Toast.makeText(MainActivity.this,"Crossed Onclick Listener",Toast.LENGTH_SHORT).show();
    }

    public void Send_Iamge(Uri img){
        Intent intent = new Intent(MainActivity.this,ImagePreview.class);
        intent.putExtra("KEY",img);
        startActivity(intent);
    }

    private void Choose_Image(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dial_logo,null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        ImageView imageViewCamera = dialogView.findViewById(R.id.imageViewCamera);
        ImageView imageViewPhotos = dialogView.findViewById(R.id.imageViewPhotos);

        AlertDialog alertDialog_prof = builder.create();
        alertDialog_prof.show();

        imageViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckPermission()){
                    takePictureFromCamera();
                    //Goto Next Page
                    alertDialog_prof.dismiss();
                }

            }
        });

        imageViewPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromGallery();
                //Goto text Page
                alertDialog_prof.dismiss();

            }
        });
    }

    private void takePictureFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto,1);
    }

    private void takePictureFromCamera(){
        Intent take_pic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(take_pic.resolveActivity(getPackageManager())!=null){
            startActivityForResult(take_pic,2);
        }
    }

    // This triggered when someone selects an option either to click an image or to select an image.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(MainActivity.this,image_preview.class);
        switch(requestCode){

            case 1: //Image from Gallery

                    Uri Image = data.getData();
                    String img_str = Image.toString();

                    intent.putExtra("KEY",img_str);
                    startActivity(intent);

                break;
            case 2: // Image from Camera
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmapImage, "Title", null);
                    //String img_cam = bitmapImage.toString();
                    intent.putExtra("KEY",path);
                    startActivity(intent);




        }
    }

    private boolean CheckPermission(){
        if(Build.VERSION.SDK_INT>=23){
            int camera_permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
            if(camera_permission== PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},20);
                return false;
            }

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==20 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            takePictureFromCamera();

        }
        else{
            Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }

}
