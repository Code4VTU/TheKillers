package com.radoslav.testmafia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindRoom extends AppCompatActivity {
    ListView lv;
    long playerPositionInRoom;
    ArrayList<String> groups = new ArrayList<>();
    private String ROOM_EXTRA = "room name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room);
        initializeUI();


        Firebase ref = new Firebase(Constants.firebaseUrl);

        ref.addValueEventListener(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot snapshot) {
                                          for (DataSnapshot child : snapshot.getChildren()) {
                                              groups.add(child.getKey());
                                              System.out.println(child.getKey());
                                          }
                                      }
                                      @Override
                                      public void onCancelled(FirebaseError firebaseError) {
                                          System.out.println("The read failed: " + firebaseError.getMessage());
                                      }

                                  }
        );

        lv.setAdapter(new VersionAdapter(FindRoom.this));
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                final String roomName = ((TextView) view.findViewById(R.id.room_txt)).getText()
                        .toString();

                Firebase ref = new Firebase(Constants.firebaseUrl).child(roomName);

                ref.addValueEventListener(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(DataSnapshot snapshot) {
                                                  playerPositionInRoom = snapshot.getChildrenCount()-1;

                                              }

                                              @Override
                                              public void onCancelled(FirebaseError firebaseError) {
                                                  System.out.println("The read failed: " + firebaseError.getMessage());
                                              }

                                          }
                );


                AlertDialog.Builder alert = new AlertDialog.Builder(FindRoom.this);

                alert.setTitle("User Name");
                alert.setMessage("Enter your user name");

// Set an EditText view to get user input
                final EditText input = new EditText(FindRoom.this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        addToRoom(roomName, input.getText().toString());
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                alert.show();






            }
        });
    }

    private void addToRoom(final String group, final String user) {

        Firebase ref = new Firebase(Constants.firebaseUrl).child(group);

        Map<String, Object> update = new HashMap<>();
        update.put(user, ""+playerPositionInRoom);
        ref.updateChildren(update);

        Intent intent = new Intent();
        intent.putExtra(ROOM_EXTRA, group);
        setResult(RESULT_OK, intent);
        UserDetails.userName = user;
        UserDetails.group = group;
        finish();


    }

    private void initializeUI()
    {
        lv = (ListView) findViewById(R.id.list_view);

    }

    class VersionAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;

        public VersionAdapter(FindRoom activity) {
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return groups.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View listItem = convertView;
            int pos = position;
            if (listItem == null) {
                listItem = layoutInflater.inflate(R.layout.list_item_appointments, null);
            }

            ImageView iv = (ImageView) listItem.findViewById(R.id.thumb);

            TextView room = (TextView) listItem.findViewById(R.id.room_txt);
            String letter = "A";

            letter = String.valueOf(groups.get(pos).charAt(0)).toUpperCase();


            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRoundRect(letter, color, 20);



            room.setText(groups.get(pos));
            iv.setImageDrawable(drawable);

            return listItem;
        }

    }
}
