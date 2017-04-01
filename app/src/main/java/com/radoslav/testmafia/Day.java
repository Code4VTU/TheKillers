package com.radoslav.testmafia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
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

public class Day extends AppCompatActivity {

    String killed, cured;
    TextView[] textViews;
    private ArrayList<String> names;
    TextView timer;


    TextView firstTxt, secondTxt, thirdTxt, fourthTxt, fifthTxt, sixthTxt;
    LinearLayout firstLayout, secondLayout, thirdLayout, fourthLayout, fifthLayout, sixthLayout;

    LinearLayout[] linearLayouts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);



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

        textViews = new TextView[] {firstTxt, secondTxt, thirdTxt, fourthTxt, fifthTxt, sixthTxt};
        linearLayouts = new LinearLayout[] {firstLayout, secondLayout, thirdLayout, fourthLayout, fifthLayout, sixthLayout};
        int i = 0;
        for(TextView t : textViews) {
            if(t.getText().toString().equals("")) {
                t.setText(UserDetails.names.get(i).toString());
                i++;

            }
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(Day.this);

        alert.setTitle("User Name");
        alert.setMessage("Enter your user name");

// Set an EditText view to get user input

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(!(killed.equals(cured))){
                    Toast.makeText(getApplicationContext(), killed + " dies!", Toast.LENGTH_LONG).show();
                    int i =0;
                    for(TextView t : textViews) {
                        if(t.getText().toString().equals(killed)) {
                            t.setVisibility(View.INVISIBLE);
                            linearLayouts[i].setVisibility(View.INVISIBLE);
                            i++;
                            break;
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Nothing really happens!", Toast.LENGTH_LONG).show();

                }

            }
        });


        alert.show();


        Firebase ref = new Firebase(Constants.firebaseUrl).child(UserDetails.group+"/Night"+ String.valueOf(UserDetails.night-1));
        System.out.println(ref);
        System.out.println(ref.toString());

        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot snapshot) {
                                          for (DataSnapshot child : snapshot.getChildren()) {
                                              if("Killer".equals(child.getKey())){
                                                  killed= String.valueOf(child.getValue());
                                              }
                                              if("Doctor".equals(child.getKey())){
                                                  cured= String.valueOf(child.getValue());
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
                Intent intent = new Intent(Day.this, Night.class);
                if(UserDetails.names == null){
                    UserDetails.names = new ArrayList<>(names);}
                startActivity(intent);
            }

        }.start();




        firstLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) v.findViewById(R.id.first_person_txt);
                addTurn(text.getText().toString());
            }
        });

        secondLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) v.findViewById(R.id.second_person_txt);
                addTurn(text.getText().toString());

            }
        });

        thirdLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) v.findViewById(R.id.third_person_txt);
                addTurn(text.getText().toString());
            }
        });

        fourthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) v.findViewById(R.id.fourth_person_txt);
                addTurn(text.getText().toString());

            }
        });

        fifthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) v.findViewById(R.id.fifth_person_txt);
                addTurn(text.getText().toString());

            }
        });

        sixthLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v);
                TextView text = (TextView) findViewById(R.id.sixth_person_txt);
                addTurn(text.getText().toString());

            }
        });




    }

    private void addTurn(String killed) {
        Firebase ref = new Firebase(Constants.firebaseUrl).child(UserDetails.group +"/Day"+UserDetails.night);

        Map<String, Object> update = new HashMap<>();

        update.put(UserDetails.userName, killed);

        ref.updateChildren(update);

    }
}
