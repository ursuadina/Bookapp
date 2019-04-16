package com.example.andreea.bookhunt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreea.bookhunt.utils.Constants;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private ImageView imageViewBook;
    private TextView textViewTitle;
    private TextView textViewBookName;
    private BitmapFactory.Options bitmapOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        byte[] image = (byte[]) extras.get("photo");
        //Bitmap imageBitmap = (Bitmap) extras.get("extras");
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        imageViewBook.setImageBitmap(bitmap);

        String text = imageToText();
        String text1 = textViewBookName.getText().toString();

        String textNou = text1 + text;
        textViewBookName.setText(textNou);

    }

    public void initView() {
        textViewTitle = (TextView) findViewById(R.id.textView3);
        imageViewBook = (ImageView)  findViewById(R.id.imageViewBookPhoto);
        textViewBookName = (TextView) findViewById(R.id.textViewBookName);
    }


    public void btnRetakePic(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, Constants.CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewBook.setImageBitmap(imageBitmap);

            String text = imageToText();
            String text1 = textViewBookName.getText().toString();

            String textNou = text1 + text;
            textViewBookName.setText(textNou);
        }
    }

    public String imageToText() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        Bitmap bitmap = ((BitmapDrawable)imageViewBook.getDrawable()).getBitmap();
        Frame imageFrame = new Frame.Builder()
                .setBitmap(bitmap)                 // your image bitmap
                .build();

        StringBuilder imageText = new StringBuilder();


        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.valueAt(i);
            imageText.append(textBlock.getValue());
        }
        return imageText.toString();
    }
}
