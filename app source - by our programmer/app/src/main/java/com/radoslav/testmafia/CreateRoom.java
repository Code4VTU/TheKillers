package com.radoslav.testmafia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateRoom extends AppCompatActivity {

    EditText roomName;
    EditText userName;
    Button submit;

    long playerPositionInRoom;
    private String ROOM_EXTRA = "room name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        initializeUI();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase ref = new Firebase(Constants.firebaseUrl);


                        String group = roomName.getText().toString();

                        Map<String, Object> update = new HashMap<>();
                        update.put(group, "something");


                        ref.updateChildren(update);

                        setUpRoom(group);



                Toast.makeText(getApplicationContext(), "nopeee", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setUpRoom(final String group) {

        Firebase ref = new Firebase(Constants.firebaseUrl).child(group);

        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot snapshot) {
                                              playerPositionInRoom = snapshot.getChildrenCount()-2;



                                      }

                                      @Override
                                      public void onCancelled(FirebaseError firebaseError) {
                                          System.out.println("The read failed: " + firebaseError.getMessage());
                                      }

                                  }
        );

                Map<String, Object> update = new HashMap<>();
                update.put(userName.getText().toString(), Long.toString(playerPositionInRoom));
                update.put("roles", randomizeRoles());

                ref.updateChildren(update);

                Intent intent = new Intent();
                intent.putExtra(ROOM_EXTRA, group);
                setResult(RESULT_OK, intent);
                UserDetails.userName = userName.getText().toString();
                UserDetails.group = roomName.getText().toString();
                finish();


    }


    private String randomizeRoles() {
        List<Integer> solution = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            solution.add(i);
        }
        Collections.shuffle(solution);
        System.out.println(solution.toString());
        return solution.toString();
    }

    private void initializeUI() {
        roomName = (EditText) findViewById(R.id.name_et);
        userName = (EditText) findViewById(R.id.user_name_et);
        submit = (Button) findViewById(R.id.submit_btn);

    }

}
