package com.example.SnapchatClone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class userList extends AppCompatActivity {

    ListView listView;

    ArrayAdapter arrayAdapter;

    ArrayList<String> emails;

    ArrayList<String> keys;

    FirebaseDatabase firebaseDatabase;

    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;

    Intent intent = getIntent();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listView = (ListView) findViewById(R.id.userListView);

        emails = new ArrayList<>();

        keys = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(userList.this , android.R.layout.simple_expandable_list_item_1 , emails);

        listView.setAdapter(arrayAdapter);

         firebaseDatabase = FirebaseDatabase.getInstance();

         firebaseAuth = FirebaseAuth.getInstance();

         databaseReference = firebaseDatabase.getReference("Users");

         databaseReference.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



                   String emailFromDatabase = dataSnapshot.child("Email").getValue(String.class);

                   emails.add(emailFromDatabase);

                   keys.add(dataSnapshot.getKey());

                   arrayAdapter.notifyDataSetChanged();



           }



           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
           }
           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
           }
           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
           }
       });




       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


//               String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

               Intent intent= getIntent();
//
//
//
//
//
//               String filename = Calendar.getInstance().getTimeInMillis() + "";
//
//               Task<Uri> imageURL = FirebaseStorage.getInstance().getReference("Images").child(userID).child(filename).getDownloadUrl();

               Map<String , String> dataToShare = new HashMap<String, String>();

               dataToShare.put("From " , FirebaseAuth.getInstance().getCurrentUser().getEmail());
               dataToShare.put(" Image URL " ,  intent.getStringExtra("Image URL"));
               dataToShare.put("Image Name" , intent.getStringExtra("Image Name" ) );

              // String imageName = FirebaseStorage.getInstance().getReference("Images").child(userID).



               firebaseDatabase.getReference().child("Users").child(keys.get(i)).child("Snaps").push().setValue(dataToShare);

           }
       });











































    }
}