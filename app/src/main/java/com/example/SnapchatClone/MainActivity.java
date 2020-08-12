package com.example.SnapchatClone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    Intent intent = getIntent();

    FirebaseStorage firebaseStorage;

    StorageReference storageReference ;

    FirebaseAuth firebaseAuth;

    Uri selectedImage;

    EditText messageEditText;

    public void getPhoto(){

        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent , 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

         if(requestCode==1){

        if(grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

            getPhoto();

        }

       }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            selectedImage = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                imageView.setImageBitmap(bitmap);


            } catch (Exception e) {

                e.printStackTrace();
            }

        }}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.signout , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.logout){

            firebaseAuth.signOut();

            Intent intent = new Intent(MainActivity.this , LoginActivity.class);

            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    public void importPhoto(View view){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE} , 1);
                }

                else{

                    getPhoto();

                }
            }}

            public void upload(View view){


            if(firebaseAuth.getCurrentUser()!=null){

                String userID = firebaseAuth.getCurrentUser().getUid();

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

                progressDialog.setCancelable(false);

                progressDialog.setMessage("Uploading...");

                progressDialog.show();

                if(selectedImage!=null){

                    String filename = Calendar.getInstance().getTimeInMillis()+"";

                    firebaseStorage.getReference().child("Images").child(userID).child(filename).putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            final Task<Uri> downloadURL = taskSnapshot.getStorage().getDownloadUrl();

                            final String imageName = taskSnapshot.getStorage().getName();

                            progressDialog.dismiss();

                            Toast.makeText(MainActivity.this,"Image Uploaded",Toast.LENGTH_SHORT).show();

                            downloadURL.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){

                                        Intent intent = new Intent(MainActivity.this , userList.class);

                                        intent.putExtra("Image URL" ,  task.getResult().toString());

                                        intent.putExtra("Image Name" , imageName);

                                        intent.putExtra("Message" , messageEditText.getText().toString());

                                        startActivity(intent);
                                    }
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("MainActivity",e.getMessage());

                            progressDialog.dismiss();

                            Toast.makeText(MainActivity.this,"Image Upload Failed : "+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            int p  = (int)((taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount());

                            progressDialog.setMessage("Uploading...("+p+" %)");
                        }
                    });

                }
                else{

                    Toast.makeText(MainActivity.this,"Image Selection Error",Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                    return;
                }
            }
            else {

                Toast.makeText(MainActivity.this,"User is null",Toast.LENGTH_SHORT).show();

            }

        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);

        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference("Images");

        firebaseAuth= FirebaseAuth.getInstance();

        messageEditText = (EditText) findViewById(R.id.messageEditText);












































    }
}