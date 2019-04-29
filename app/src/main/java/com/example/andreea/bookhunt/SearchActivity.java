package com.example.andreea.bookhunt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andreea.bookhunt.utils.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    private ImageView imageViewBook;
    private TextView textViewTitle;
    private TextView textViewBookName;

    private Bitmap rotatedBitmap;
    private String mCurrentPhotoPath;
    private File photoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        File file =(File) extras.get("file");
        mCurrentPhotoPath = (String) extras.get("currentPhotoPath");
    try {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(file));
        if (bitmap != null) {
            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
        }
    }
    catch (Exception error) {
        error.printStackTrace();
    }
        imageViewBook.setImageBitmap(rotatedBitmap);
//        String text = imageToText();
//        String text1 = textViewBookName.getText().toString();
//
//        String textNou = text1 + text;
//        textViewBookName.setText(textNou);

    }
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    public void initView() {
        textViewTitle = (TextView) findViewById(R.id.textView3);
        imageViewBook = (ImageView)  findViewById(R.id.imageViewBookPhoto);
        textViewBookName = (TextView) findViewById(R.id.textViewBookName);
    }


    public void btnRetakePic(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), "com.example.andreea.bookhunt.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, Constants.CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CAMERA_REQUEST && resultCode == RESULT_OK) {
           try {
               File file = new File(mCurrentPhotoPath);
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(file));
               if (bitmap != null) {
                   ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                   int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                   rotatedBitmap = null;
                   switch (orientation) {

                       case ExifInterface.ORIENTATION_ROTATE_90:
                           rotatedBitmap = rotateImage(bitmap, 90);
                           break;

                       case ExifInterface.ORIENTATION_ROTATE_180:
                           rotatedBitmap = rotateImage(bitmap, 180);
                           break;

                       case ExifInterface.ORIENTATION_ROTATE_270:
                           rotatedBitmap = rotateImage(bitmap, 270);
                           break;

                       case ExifInterface.ORIENTATION_NORMAL:
                       default:
                           rotatedBitmap = bitmap;
                   }
               }
               imageViewBook.setImageBitmap(rotatedBitmap);
           }  catch (Exception error) {
            error.printStackTrace();
        }
//            String text = imageToText();
//            String text1 = textViewBookName.getText().toString();
//
//            String textNou = text1 + text;
//            textViewBookName.setText(textNou);
        }
    }

//    public String imageToText() {
//        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
//        Bitmap bitmap = ((BitmapDrawable)imageViewBook.getDrawable()).getBitmap();
//        Frame imageFrame = new Frame.Builder()
//                .setBitmap(bitmap)                 // your image bitmap
//                .build();
//
//        StringBuilder imageText = new StringBuilder();
//
//
//        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
//
//        for (int i = 0; i < textBlocks.size(); i++) {
//            TextBlock textBlock = textBlocks.valueAt(i);
//            imageText.append(textBlock.getValue());
//        }
//        return imageText.toString();
//    }
}
