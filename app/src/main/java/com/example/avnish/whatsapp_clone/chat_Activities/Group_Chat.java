package com.example.avnish.whatsapp_clone.chat_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.avnish.whatsapp_clone.R;
import com.example.avnish.whatsapp_clone.REcyclerView.adapter;
import com.example.avnish.whatsapp_clone.REcyclerView.databook;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Group_Chat extends AppCompatActivity {
    Intent i;
    String currentGroupName;
    Toolbar toolbar;
    EditText sendEdit;
    Button send_btn;
    DatabaseReference databaseRef;
    String currentUserId,username;
    RecyclerView recyclerView;
    HashMap<String,String> map;
    ArrayList<databook> arrayList=null;
    adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__chat);

        Initialize();

        if(i.getExtras()!=null){
            currentGroupName=i.getExtras().get("currentGroupName").toString();
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(currentGroupName);

          checkUserName();  //convert the data value from firebase into object
          writeData();
        }


    }

    private void checkUserName() {
        arrayList= new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Group").child(currentGroupName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                     for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())

                     arrayList.add(dataSnapshot1.getValue(databook.class));
                    // mAdapter.notifyDataSetChanged();
                }

               // arrayList.add(new databook("date","time","name","gfgcg"));
                mAdapter= new adapter(arrayList,name());
                recyclerView.setAdapter(mAdapter);
            }

            @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });



}

    private void writeData() {



            send_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(sendEdit.getText().toString()!=null) {

                        Calendar calender = Calendar.getInstance();
                        String date = new SimpleDateFormat("EEE,MMM d,yyyy").format(calender.getTime());
                        String time = new SimpleDateFormat("h:mm a").format(calender.getTime());
                        databaseRef = FirebaseDatabase.getInstance().getReference("Group").child(currentGroupName);
                        map = new HashMap<>();
                        map.put("Msg", sendEdit.getText().toString().trim());
                        map.put("Date", date);
                        map.put("Time", time);
                        map.put("Name",name());
                        map.put("user",currentUserId);}


                        databaseRef.push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            sendEdit.setText("");
                        }
                    });

                }
            });


        }

        private String name(){

            FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId).child("Name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        username = dataSnapshot.getValue().toString();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return username;
        }



    private void Initialize() {
        i= getIntent();
        toolbar=findViewById(R.id.toolbar);
        sendEdit=findViewById(R.id.hello);
        send_btn=findViewById(R.id.sendbtn);
        currentUserId=(FirebaseAuth.getInstance().getCurrentUser().getUid());
        recyclerView=findViewById(R.id.Recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

}