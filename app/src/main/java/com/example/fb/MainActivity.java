package com.example.fb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Item> item_arr = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private TextView nav_header_id_text;
    private Button btn;
    private View pre_v;
    private boolean bool = true;    // button check
    private boolean auth_check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // login check
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startLoginAcitivy();
        }

        btn = findViewById(R.id.button_dim3);


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
        findViewById(R.id.button_policy).setOnClickListener(onClickListener);
        findViewById(R.id.button_winia).setOnClickListener(onClickListener);


        // 네비게이션
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View nav_header_view = navigationView.getHeaderView(0);
        nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.nav_header);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
//                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.nav_home){
                    startToast("로그아웃");
                    FirebaseAuth.getInstance().signOut();
                    startLoginAcitivy();
                }
                return true;
            }
        });


        authChecker();  // uid check (name, readable)
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_dim:
                    loadCollection("dimchae", "capacity");
                    buttonChecker(v);
//                    startToast("dimchae");
                    break;
                case R.id.button_dim3:
                    loadCollection("dimchae_3", "capacity");
                    buttonChecker(v);
//                    startToast("dimchae_3");
                    break;
                case R.id.button_20dim:
                    loadCollection("20dimchae", "capacity");
                    buttonChecker(v);
//                    startToast("20_dimchae");
                    break;
                case R.id.button_dimCook:
                    loadCollection("dimchae_cook", "capacity");
                    buttonChecker(v);
//                    startToast("dimchae_cook");
                    break;
                case R.id.button_praud:
                    loadCollection("praud", "capacity");
                    buttonChecker(v);
//                    startToast("praud");
                    break;
                case R.id.button_air:
                    loadCollection("air", "capacity");
                    buttonChecker(v);
//                    startToast("air");
                    break;
                case R.id.button_ref:
                    loadCollection("ref", "category");
                    buttonChecker(v);
//                    startToast("refrigerator");
                    break;
                case R.id.button_wash:
                    loadCollection("wash", "capacity");
                    buttonChecker(v);
//                    startToast("washer");
                    break;
                case R.id.button_etc:
                    loadCollection("etc", "category");
                    buttonChecker(v);
//                    startToast("etc");
                    break;
                case R.id.button_exh:
                    loadCollection("exhibition", "category");
                    buttonChecker(v);
//                    startToast("exhibition");
                    break;
                case R.id.button_policy:
                    loadCollection("policy_discnt", "capacity");
                    buttonChecker(v);
//                    startToast("exhibition");
                    break;
                case R.id.button_winia:
                    loadCollection("winia", "category");
                    buttonChecker(v);
//                    startToast("exhibition");
                    break;
            }
        }
    };

    private void loadCollection(String name, String sort_field) {
        item_arr.clear();
        final RelativeLayout loderLayout = findViewById(R.id.loderLayout);
        loderLayout.setVisibility(View.VISIBLE);

        db.collection(name)// 오름차순
                .orderBy(sort_field, ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // loading start
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);

                                if (!item.getChannel().equals("stop"))
                                    item_arr.add(item);
//                                Log.d(TAG, item.getCapacity());

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        ////
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

                        loderLayout.setVisibility(View.GONE);
                    }
                });
    }

    private void authChecker() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("User").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        nav_header_id_text.setText(document.get("name").toString());
                        if (document.getBoolean("readable")) {
                            auth_check = true;
                        }
                        else
                            btn.setVisibility(View.GONE);
                    } else {
                        Log.d(TAG, "fail");
                    }
                }
                else {
                    Log.d(TAG, "fail");
                }
            }
        });
    }

    

    private void buttonChecker(View v) {
        if (bool) {
            v.setBackgroundColor(getResources().getColor(R.color.teal_200));
            pre_v = v;
            bool = false;
        }

        else {
            pre_v.setBackgroundColor(getResources().getColor(R.color.purple_700));
            pre_v = v;
            v.setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
    }


    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startSignUpAcitivy() {
        Intent intent = new Intent(this, SignUpAcitivy.class);
        startActivity(intent);
    }

    private void startLoginAcitivy() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void startResultAcitivy(Item item) {
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra("sendItem", item);
        startActivity(intent);
    }
}