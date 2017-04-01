package com.radoslav.testmafia;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class Night extends AppCompatActivity {

    String rolesInGame[];
    int playerId;


    ArrayList<String> names = new ArrayList<>();
    TextView timer;
    TextView firstTxt, secondTxt, thirdTxt, fourthTxt, fifthTxt, sixthTxt;

    LinearLayout firstLayout, secondLayout, thirdLayout, fourthLayout, fifthLayout, sixthLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night);

        Firebase.setAndroidContext(getApplicationContext());

        initializeUI();
        System.out.println("group " + UserDetails.group);
        Firebase ref = new Firebase(Constants.firebaseUrl).child(UserDetails.group);

        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot snapshot) {
                                          for (DataSnapshot child : snapshot.getChildren()) {

                                              if(!child.getKey().equals("roles")){
                                                  names.add(child.getKey());
                                              }

                                              if(child.getKey().equals(UserDetails.userName))
                                              {
                                                  playerId = Integer.parseInt(String.valueOf(child.getValue()));
                                                  System.out.println("player " + playerId);
                                              }
                                              else if(child.getKey().equals("roles")){
                                                  String roles = child.getValue().toString();
                                                  roles = roles.substring(1, roles.length());
                                                  rolesInGame = roles.split(", ");
                                                  System.out.println("roles " + roles);
                                                  GetRoleText getRoleText = new GetRoleText();
                                                  System.out.println("Your role is " + getRoleText.getRole((Integer.parseInt(rolesInGame[playerId]))));

                                              }
                                          }
                                      }

                                      @Override
                                      public void onCancelled(FirebaseError firebaseError) {
                                          System.out.println("The read failed: " + firebaseError.getMessage());
                                      }

                                  }
        );


        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                System.out.println("Night over");
                Intent intent = new Intent(Night.this, Day.class);
                startActivity(intent);
            }

    }.start();

        setUpUI();


        firstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p
            }
        });

        secondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        thirdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fourthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fifthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sixthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


}

    private void initializeUI() {
        timer = (TextView) findViewById(R.id.timer_txt);
        firstTxt = (TextView) findViewById(R.id.first_person_txt);
        secondTxt = (TextView) findViewById(R.id.second_person_txt);
        thirdTxt = (TextView) findViewById(R.id.third_person_txt);
        fourthTxt = (TextView) findViewById(R.id.fourth_person_txt);
        fifthTxt = (TextView) findViewById(R.id.fifth_person_txt);
        sixthTxt = (TextView) findViewById(R.id.sixth_person_txt);

        firstLayout = (LinearLayout) findViewById(R.id.first_lin_layout);
        secondLayout = (LinearLayout) findViewById(R.id.second_lin_layout);
        thirdLayout = (LinearLayout) findViewById(R.id.third_lin_layout);
        fourthLayout = (LinearLayout) findViewById(R.id.fourth_lin_layout);
        fifthLayout = (LinearLayout) findViewById(R.id.fifth_lin_layout);
        sixthLayout = (LinearLayout) findViewById(R.id.sixth_lin_layout);

    }

    private void setUpUI(){
        firstTxt.setText(names.get(0));
        secondTxt.setText(names.get(1));
        thirdTxt.setText(names.get(2));
        fourthTxt.setText(names.get(3));
        fifthTxt.setText(names.get(4));
        sixthTxt.setText(names.get(5));
    }
    }
