package com.radoslav.testmafia;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class Day extends AppCompatActivity {

    String killed, cured;
    Button showResultsBtn;
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

    }
}
