package com.example.fb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Item> item_arr = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

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
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        findViewById(R.id.button_dim).setOnClickListener(onClickListener);
        findViewById(R.id.button_dim3).setOnClickListener(onClickListener);
        findViewById(R.id.button_20dim).setOnClickListener(onClickListener);
        findViewById(R.id.button_dimCook).setOnClickListener(onClickListener);
        findViewById(R.id.button_praud).setOnClickListener(onClickListener);
        findViewById(R.id.button_air).setOnClickListener(onClickListener);
        findViewById(R.id.button_ref).setOnClickListener(onClickListener);
        findViewById(R.id.button_wash).setOnClickListener(onClickListener);
        findViewById(R.id.button_etc).setOnClickListener(onClickListener);
        findViewById(R.id.button_exh).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.logoutButton:
//                    FirebaseAuth.getInstance().signOut();
//                    startSignUpAcitivy();
//                    break;
                case R.id.button_dim:
                    loadCollection("dimchae");
                    break;
                case R.id.button_dim3:
                    loadCollection("dimchae_3");
                    break;
                case R.id.button_20dim:
                    loadCollection("20dimchae");
                    break;
                case R.id.button_dimCook:
                    loadCollection("dimchae_cook");
                    break;
                case R.id.button_praud:
                    loadCollection("praud");
                    break;
                case R.id.button_air:
                    loadCollection("air");
                    break;
                case R.id.button_ref:
                    loadCollection("refrigerator");
                    break;
                case R.id.button_wash:
                    loadCollection("washer");
                    break;
                case R.id.button_etc:
                    loadCollection("etc");
                    break;
                case R.id.button_exh:
                    loadCollection("exhibition");
                    break;
            }
        }
    };

    private void loadCollection(String name) {
        item_arr.clear();
        final RelativeLayout loderLayout = findViewById(R.id.loderLayout);
        loderLayout.setVisibility(View.VISIBLE);

        db.collection(name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // loading start
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, "<"+name+">" + document.getId() + " => " + document.getData());
                                //문서 불러오기
                                DocumentReference docRef = db.collection(name).document(document.getId());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                        startToast("확인");
                                        Item item = documentSnapshot.toObject(Item.class);
                                        item_arr.add(item);
                                        //Log.d(TAG, "모델명 : " + item.getModel());

                                        adapter = new MyAdapter(getApplicationContext(), item_arr);
                                        ((MyAdapter) adapter).setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(MyAdapter.ViewHolder holder, View view, int position) {
                                            Item item = ((MyAdapter) adapter).getItem(position);
//                                            Toast.makeText(getApplicationContext(), item.getModel()+" clicked", Toast.LENGTH_SHORT).show();
                                            startResultAcitivy(item);
                                        }
                                        });
                                        recyclerView.setAdapter(adapter);
                                        // loading end
                                    }
                                });
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        loderLayout.setVisibility(View.GONE);
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

    private void startResultAcitivy(Item item) {
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra("sendItem", item);
        startActivity(intent);
    }
}