package com.example.fb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // login check
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startSignUpAcitivy();
        }

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        findViewById(R.id.buttonEx1).setOnClickListener(onClickListener);
        findViewById(R.id.buttonEx2).setOnClickListener(onClickListener);
        findViewById(R.id.buttonEx3).setOnClickListener(onClickListener);
        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
    }




    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    startSignUpAcitivy();
                    break;
                case R.id.buttonEx1:
                    loadCollection("Ex1");
                    MyAdapter adapter1 = new MyAdapter(getApplicationContext());
                    adapter1.addItem(new Item("Ex1", " A"));
                    adapter1.addItem(new Item("Ex1", " B"));
                    adapter1.addItem(new Item("Ex1", " C"));
                    adapter1.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MyAdapter.ViewHolder holder, View view, int position) {
                            Item item = adapter1.getItem(position);
                            Toast.makeText(getApplicationContext(), item.text1+item.text2 + " clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerView.setAdapter(adapter1);
                    break;
                case R.id.buttonEx2:
                    loadCollection("Ex2");
                    MyAdapter adapter2 = new MyAdapter(getApplicationContext());
                    adapter2.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MyAdapter.ViewHolder holder, View view, int position) {
                            Item item = adapter2.getItem(position);
                            Toast.makeText(getApplicationContext(), item.text1+item.text2 + " clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                    adapter2.addItem(new Item("Ex2", " A"));
                    adapter2.addItem(new Item("Ex2", " B"));
                    recyclerView.setAdapter(adapter2);
                    break;
                case R.id.buttonEx3:
                    loadCollection("Ex3");
                    MyAdapter adapter3 = new MyAdapter(getApplicationContext());
                    adapter3.addItem(new Item("Ex3", " C"));
                    adapter3.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MyAdapter.ViewHolder holder, View view, int position) {
                            Item item = adapter3.getItem(position);
                            Toast.makeText(getApplicationContext(), item.text1+item.text2 + " clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerView.setAdapter(adapter3);
                    break;

            }
        }
    };

    private void loadCollection(String name) {
        db.collection(name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "<"+name+">" + document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startSignUpAcitivy() {
        Intent intent = new Intent(this, SignUpAcitivy.class);
        startActivity(intent);
    }
}