package com.example.SnapchatClone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateSnapActivity extends AppCompatActivity {

    ListView snapsListView;

    FirebaseAuth firebaseAuth;





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.createsnap, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()== R.id.createSnap){

            Intent intent = new Intent(CreateSnapActivity.this , MainActivity.class);

            startActivity(intent);

        }

       else if(item.getItemId()==R.id.logout){

            firebaseAuth.signOut();

            Intent intent = new Intent(CreateSnapActivity.this , LoginActivity.class);

            startActivity(intent);





        }
        return super.onOptionsItemSelected(item);
    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_snap);

        firebaseAuth = FirebaseAuth.getInstance();

        final ArrayList<String> emails = new ArrayList<>();

        final ArrayList<DataSnapshot> snaps = new ArrayList<>();

        snapsListView = (ListView) findViewById(R.id.snapListView);





        FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("Snaps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String From = dataSnapshot.child("From ").getValue(String.class);

                emails.add(From);

                snaps.add(dataSnapshot);

                final ArrayAdapter arrayAdapter = new ArrayAdapter(CreateSnapActivity.this , android.R.layout.simple_expandable_list_item_1 , emails);

                snapsListView.setAdapter(arrayAdapter);

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

        snapsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                Intent intent = new Intent(CreateSnapActivity.this , SnapsActivity.class);

                intent.putExtra("Snap URL" , snaps.get(i).child(" Image URL ").getValue(String.class));

                intent.putExtra("Snap key", snaps.get(i).getKey());



                intent.putExtra("Snap name" , snaps.get(i).child("Image Name").getValue(String.class));

                startActivity(intent);



            }
        });




    }
}