package com.radoslav.testmafia;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Night extends AppCompatActivity {

    String rolesInGame[];
    int playerId;
    TextView[] textViews;

    private ArrayList<String> names;
    TextView timer;
    TextView firstTxt, secondTxt, thirdTxt, fourthTxt, fifthTxt, sixthTxt;
    TextView roleTxt;

    LinearLayout firstLayout, secondLayout, thirdLayout, fourthLayout, fifthLayout, sixthLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night);

        Firebase.setAndroidContext(getApplicationContext());

        initializeUI();

        names = new ArrayList<>();
        System.out.println("group " + UserDetails.group);
        Firebase ref = new Firebase(Constants.firebaseUrl).child(UserDetails.group);
        System.out.println(ref);
        System.out.println(ref.toString());

        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot snapshot) {
                                          for (DataSnapshot child : snapshot.getChildren()) {

                                              System.out.println("IS IS A NAME " + child.getKey().equals("roles"));

                                              if((child.getKey().equals("roles")) == false){
                                                  names.add(child.getKey());
                                                  System.out.println("name added   " + child.getKey());

                                                  for(TextView t : textViews) {
                                                      if(t.getText().toString().equals("")) {
                                                          t.setText(child.getKey());
                                                          break;
                                                      }
                                                  }
                                              }

                                              if(child.getKey().equals(UserDetails.userName))
                                              {
                                                  playerId = Integer.parseInt(String.valueOf(child.getValue()));
                                                  System.out.println("player " + playerId);
                                              }
                                              else if(child.getKey().equals("roles")){
                                                  String roles = child.getValue().toString();
                                                  roles = roles.substring(1, roles.length()-1);
                                                  rolesInGame = roles.split(", ");
                                                  System.out.println("roles " + roles);


                                                  GetRoleText getRoleText = new GetRoleText();
                                                  UserDetails.role = Integer.parseInt(rolesInGame[UserDetails.playerPositionInRoom]);

                                                  System.out.println("role " + Integer.parseInt(rolesInGame[UserDetails.playerPositionInRoom]));
                                                  System.out.println("Your role is " + getRoleText.getRole(Integer.parseInt(rolesInGame[UserDetails.playerPositionInRoom])));

                                                  roleTxt.setText("Your role is " + getRoleText.getRole(Integer.parseInt(rolesInGame[UserDetails.playerPositionInRoom])));
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
                UserDetails.night += 1;
                Intent intent = new Intent(Night.this, Day.class);
                startActivity(intent);
            }

    }.start();




      //  setUpUI();


        firstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) v.findViewById(R.id.first_person_txt);
                addTurn(text);
            }
        });

        secondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) v.findViewById(R.id.second_person_txt);
                addTurn(text);

            }
        });

        thirdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) v.findViewById(R.id.third_person_txt);
                addTurn(text);
            }
        });

        fourthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) v.findViewById(R.id.fourth_person_txt);
                addTurn(text);

            }
        });

        fifthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) v.findViewById(R.id.fifth_person_txt);
                addTurn(text);

            }
        });

        sixthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) findViewById(R.id.sixth_person_txt);
                addTurn(text);

            }
        });



}

    private void addTurn(TextView textView)
    {
        UserDetails userDetails = new UserDetails();
        GetRoleText getRoleText = new GetRoleText();



        //if policeman
        if(userDetails.role == 3)
        {
            chechIfKiller(textView.getText().toString());
        }


        else{
            Firebase ref = new Firebase(Constants.firebaseUrl).child(UserDetails.group +"/Night"+userDetails.night);


            Map<String, Object> update = new HashMap<>();

            System.out.println("userDetails.role" + userDetails.role);

            System.out.println("getRoleText.getRole(userDetails.role)" + getRoleText.getRole(userDetails.role));
            System.out.println("textView.getText().toString()" + textView.getText().toString());


            update.put(String.valueOf(getRoleText.getRole(userDetails.role)), textView.getText().toString());

            ref.updateChildren(update);
        }

    }

    private void chechIfKiller(final String name) {
        Firebase ref = new Firebase(Constants.firebaseUrl).child(UserDetails.group);

        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot snapshot) {
                                          for (DataSnapshot child : snapshot.getChildren()) {
                                              if(child.getKey().equals(name)){
                                              int roleOfSelected = Integer.parseInt(rolesInGame[Integer.parseInt(String.valueOf(child.getValue()))]);
                                              System.out.println("role   " + roleOfSelected);
                                                  if(roleOfSelected == 0 || roleOfSelected == 1)
                                                  {
                                                      Toast.makeText(getApplicationContext(), "S/HE IS A KILLER!", Toast.LENGTH_LONG).show();
                                                  }
                                                  else {
                                                      Toast.makeText(getApplicationContext(), "s/he is a good guy!", Toast.LENGTH_LONG).show();

                                                  }
                                              }
                                          }
                                      }
                                      @Override
                                      public void onCancelled(FirebaseError firebaseError) {
                                          System.out.println("The read failed: " + firebaseError.getMessage());
                                      }

                                  }
        );
    }

    private void initializeUI() {
        timer = (TextView) findViewById(R.id.timer_txt);



        firstTxt = (TextView) findViewById(R.id.first_person_txt);
        secondTxt = (TextView) findViewById(R.id.second_person_txt);
        thirdTxt = (TextView) findViewById(R.id.third_person_txt);
        fourthTxt = (TextView) findViewById(R.id.fourth_person_txt);
        fifthTxt = (TextView) findViewById(R.id.fifth_person_txt);
        sixthTxt = (TextView) findViewById(R.id.sixth_person_txt);

        textViews = new TextView[] {firstTxt, secondTxt, thirdTxt, fourthTxt, fifthTxt, sixthTxt};
        firstLayout = (LinearLayout) findViewById(R.id.first_lin_layout);
        secondLayout = (LinearLayout) findViewById(R.id.second_lin_layout);
        thirdLayout = (LinearLayout) findViewById(R.id.third_lin_layout);
        fourthLayout = (LinearLayout) findViewById(R.id.fourth_lin_layout);
        fifthLayout = (LinearLayout) findViewById(R.id.fifth_lin_layout);
        sixthLayout = (LinearLayout) findViewById(R.id.sixth_lin_layout);

        roleTxt = (TextView) findViewById(R.id.role_txt);
    }

    private void setUpUI(){

    }
    }
