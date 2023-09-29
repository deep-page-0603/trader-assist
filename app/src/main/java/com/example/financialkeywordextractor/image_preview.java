package com.example.financialkeywordextractor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class image_preview extends AppCompatActivity {
    Uri uri;
    String ocrResult= "Loading...";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ImageView ImgPre = (ImageView)findViewById(R.id.imgpre);
        Button toText = (Button)findViewById(R.id.button);
        //setContentView(R.layout.image_preview);
        Bundle extra = getIntent().getExtras();
        if (extra != null && extra.containsKey("KEY")){
            String temp = extra.getString("KEY");

            uri = Uri.parse(temp);

            ImgPre.setImageURI(uri);
        }


        toText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ConvertImage2Text(uri);
            }
        });
    }
    private void ConvertImage2Text(Uri Image){
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage image;

        try{
            image = InputImage.fromFilePath(this,Image);
            Task<Text> result =
                    recognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    // Task completed successfully
                                    ocrResult = visionText.getText();


                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            //Toast.makeText(image_preview.this,e.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

            result.addOnCompleteListener(new OnCompleteListener<Text>() {
                @Override
                public void onComplete(@NonNull Task<Text> task) {
                    //Toast.makeText(image_preview.this,ocrResult, Toast.LENGTH_LONG).show();
                    //Send ocr Results to next activity
                    Intent intent = new Intent(image_preview.this,Covert_2_text.class);
                    intent.putExtra("KEY",ocrResult);
                    startActivity(intent);
                }
            });



        }
        catch (Exception e){
            Toast.makeText(this, Log.getStackTraceString(e), Toast.LENGTH_LONG).show();
        }

    }

}