package com.example.andreea.bookhunt;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreea.bookhunt.models.Book;
import com.example.andreea.bookhunt.utils.Constants;
import com.example.andreea.bookhunt.utils.Methods;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.snapshot.Index;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    private ImageView imageViewBook;
    private TextView textViewTitle;
    private TextView textViewBookName;
    private EditText mEditTextBookTitle;
    private EditText mEditTextAuthor;

    private Uri selectedImageUri;
    private String imageUri;

    private Bitmap rotatedBitmap;
    private String mCurrentPhotoPath;
    private File photoFile;

    private String mBookTitle;
    private String mAuthor;
    private String mImageName;
    private Book mBook;

    private StorageReference storageReference;
    private StorageReference imageRef;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if(Methods.checkPermissions(SearchActivity.this, SearchActivity.this)) {
            initView();

            Intent intent = getIntent();
            firebaseAuth = FirebaseAuth.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();
            databaseBooks = FirebaseDatabase.getInstance().getReference("Books");
        }
        //Methods.checkPermissions(SearchActivity.this, SearchActivity.this);

//        Bundle extras = intent.getExtras();
//        File file =(File) extras.get("file");
//        mCurrentPhotoPath = (String) extras.get("currentPhotoPath");
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(file));
//            if (bitmap != null) {
//                ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
//                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//                rotatedBitmap = null;
//                switch (orientation) {
//
//                    case ExifInterface.ORIENTATION_ROTATE_90:
//                        rotatedBitmap = rotateImage(bitmap, 90);
//                        break;
//
//                    case ExifInterface.ORIENTATION_ROTATE_180:
//                        rotatedBitmap = rotateImage(bitmap, 180);
//                        break;
//
//                    case ExifInterface.ORIENTATION_ROTATE_270:
//                        rotatedBitmap = rotateImage(bitmap, 270);
//                        break;
//
//                    case ExifInterface.ORIENTATION_NORMAL:
//                    default:
//                        rotatedBitmap = bitmap;
//                }
//            }
//        }
//        catch (Exception error) {
//            error.printStackTrace();
//        }
//        imageViewBook.setImageBitmap(rotatedBitmap);
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
        mEditTextBookTitle = (EditText) findViewById(R.id.etBookTitle);
        mEditTextAuthor = (EditText) findViewById(R.id.etAuthor);
    }


    public void btnTakePic(View view) {
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
               selectedImageUri = Uri.fromFile(file);
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
               selectedImageUri = getImageUri(SearchActivity.this,rotatedBitmap);
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
        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            String path = getPathFromURI(selectedImageUri);
            if (path != null) {
                File f = new File(path);
                selectedImageUri = Uri.fromFile(f);
            }
            // Set the image in ImageView
            imageViewBook.setImageURI(selectedImageUri);
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    public void btnSelectFromGallery(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE_REQUEST);
    }

    public void btnSearch(View view) {
        mBookTitle = mEditTextBookTitle.getText().toString();
        mAuthor = mEditTextAuthor.getText().toString();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();

        String imageName = mBookTitle + mAuthor + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        imageRef = storageReference.child("images/users/"+ userId + "/" + imageName );
        imageRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUri = uri.toString();
                        String id = databaseBooks.push().getKey();
                        mBook = new Book(id, mBookTitle, mAuthor, imageUri);
                        databaseBooks.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(id).setValue(mBook);
                    }
                });
            }
        });
        Intent intent = new Intent(SearchActivity.this, IndexActivity.class);
        startActivity(intent);
        finish();
    }

    public Uri getImageUri(Context context, Bitmap image) {
        if (Methods.checkPermissions(context, SearchActivity.this)) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "Title", null);
            return Uri.parse(path);
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(SearchActivity.this, "WRITE_EXTERNAL_STORAGE Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
//            case Constants.PERMISSION_REQUEST_CAMERA:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // do your stuff
//                } else {
//                    Toast.makeText(SearchActivity.this, "CAMERA Denied",
//                            Toast.LENGTH_SHORT).show();
//                }
//                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);

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
