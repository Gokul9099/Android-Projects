/*
Group 26
Name: Gokul Nithin Kumar Rajakumar 801082252
Name: Satish Kumar Balasubramanian 801114629
*/

package com.example.group26_inclass11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Bitmap bitmapUpload = null;
    ProgressBar progressBar;
    Button take_photo;
    TextView progress_value;
    static RecyclerView recyclerview;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String TAG = "demo";
    static RecyclerView.Adapter mAdapter;
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    static StorageReference storageRef = storage.getReference().child("images/");
    static ArrayList<Uri> uriList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        setTitle( "My Album" );
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        recyclerview = (RecyclerView)findViewById( R.id.recyclerview );
        progress_value = (TextView)findViewById( R.id.tv_progress_value );
        progressBar = (ProgressBar)findViewById( R.id.progress );
        take_photo = (Button)findViewById( R.id.b_take_button );

        get_images();
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();

            }
        });

    }

    void get_images (){
        uriList.clear();
        recyclerview.removeAllViews();
        mAdapter = new ImageAdapter(uriList);
        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        if(listResult.getItems().size() < 1){
                            mAdapter = new ImageAdapter(uriList);
                            recyclerview.setAdapter(mAdapter);
                        }

                        for (StorageReference item : listResult.getItems()) {
                            StorageReference gsReference = storage.getReferenceFromUrl(item+"");
                            gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    uriList.add(uri);
                                    mAdapter = new ImageAdapter(uriList);
                                    recyclerview.setAdapter(mAdapter);
                                    recyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }
    //    TAKE PHOTO USING CAMERA...
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private Bitmap getBitmapCamera() {
        //If photo not taken from camera...
//        if (bitmapUpload == null){
//            return ((BitmapDrawable) iv_TakePhoto.getDrawable()).getBitmap();
//        }
//        Photo taken from camera...
        return bitmapUpload;
    }

    //    Upload Camera Photo to Cloud Storage....
    private void uploadImage(Bitmap photoBitmap){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        final StorageReference imageRepo = storageReference.child("images/"+ UUID.randomUUID().toString()+".png");

//        Converting the Bitmap into a bytearrayOutputstream....
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRepo.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: "+e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: "+"Image Uploaded!!!");
            }
        });

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                return null;
                if (!task.isSuccessful()){
                    throw task.getException();
                }

                return imageRepo.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "Image Download URL"+ task.getResult());
                    String imageURL = task.getResult().toString();
                    Log.d(TAG,"Image URL: "+imageURL);
//                    Picasso.get().load(imageURL).into(iv_uploadedPhoto);
                }
            }
        });

//        ProgressBar......

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                progress_value.setText("Upload is " + progress + "% done"  );
                if(progress == 100){
                    progress_value.setText( "Upload Complete!!" );
                    get_images();
                }
//                System.out.println();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Camera Callback........
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            iv_TakePhoto.setImageBitmap(imageBitmap);
//            String a = (String) extras.get( "data" );
//            Log.d(TAG,"Get Camera Data"+a);
            bitmapUpload = imageBitmap;
            uploadImage(bitmapUpload);
        }
    }
}
