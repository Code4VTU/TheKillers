package com.radoslav.testmafia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ConnectScreen extends AppCompatActivity {
    Button createRoom, findRoom;
    private final int CONNECT_SCREEN_ID = 6;
    private final String ROOM_EXTRA = "room name";
    private final int PLAYERS_REQUIRED_TO_START = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_screen);
        Firebase.setAndroidContext(this);

        initializeUI();

        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ConnectScreen.this, CreateRoom.class);
                startActivityForResult(intent, CONNECT_SCREEN_ID);


            }
        });

        findRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectScreen.this, FindRoom.class);
                startActivityForResult(intent, CONNECT_SCREEN_ID);
            }
        });


    }

    private void initializeUI() {
        createRoom = (Button) findViewById(R.id.create_room_btn);
        findRoom = (Button) findViewById(R.id.find_room_btn);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CONNECT_SCREEN_ID && resultCode == RESULT_OK)
        {
            String roomName = data.getStringExtra(ROOM_EXTRA);
            Firebase ref = new Firebase(Constants.firebaseUrl).child(roomName);

            ref.addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(DataSnapshot snapshot) {
                                              if(snapshot.getChildrenCount()-1==PLAYERS_REQUIRED_TO_START)
                                              {
                                                  Intent intent = new Intent(ConnectScreen.this, Night.class);
                                                  startActivity(intent);
                                                  Toast.makeText(getApplicationContext(), "Let's start", Toast.LENGTH_LONG).show();
                                              }

                                          }

                                          @Override
                                          public void onCancelled(FirebaseError firebaseError) {
                                              System.out.println("The read failed: " + firebaseError.getMessage());
                                          }

                                      }
            );
        }
    }
}
