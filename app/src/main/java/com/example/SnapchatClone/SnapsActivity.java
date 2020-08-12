package com.example.SnapchatClone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SnapsActivity extends AppCompatActivity {

    Intent intent;


    ImageView imageView;

    TextView messageTextView;

    FirebaseDatabase firebaseDatabase;

    public class DownloadImage extends AsyncTask<String , Void , Bitmap>{




        @Override
        protected Bitmap doInBackground(String... urls) {

            try{

                URL url = new URL(urls[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;


            }



            catch (Exception e) {
                e.printStackTrace();
            }


            return null;

        }
    }











    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snaps);

         imageView = (ImageView) findViewById(R.id.snapImageView);

        messageTextView = (TextView) findViewById(R.id.messageTextView);

          intent = getIntent();

        messageTextView.setText( intent.getStringExtra("Snap message"));



        DownloadImage downloadImage = new DownloadImage();

        Bitmap myImage;

        try{

            intent = getIntent();

            myImage = downloadImage.execute(intent.getStringExtra("Snap URL")).get();

            imageView.setImageBitmap(myImage);

        }

        catch (Exception e){

            e.printStackTrace();
        }







}

    @Override
    public void onBackPressed() {
        super.onBackPressed();





        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Snaps").child(intent.getStringExtra("Snap key")).removeValue();
        String snapName = intent.getStringExtra("Snap name");
        FirebaseStorage.getInstance().getReference().child("Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapName).delete();


    }
}


