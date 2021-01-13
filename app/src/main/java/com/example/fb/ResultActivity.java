package com.example.fb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView menu1, menu2, menu3;
    TextView notice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        Item item = (Item) intent.getSerializableExtra("sendItem");

        int price, sale1, sale2, sale3;
        DecimalFormat formatter = new DecimalFormat("###,###");


        // 정수 변수에 받아옴
        price = txtChangePrice(item.getPrice());
        sale1 = txtChangePrice(item.getSale1());
        sale2 = txtChangePrice(item.getSale2());
        sale3 = txtChangePrice(item.getSale3());

//        Log.d(TAG, price+" "+sale1+" "+sale2+" "+sale3);
//        Log.d(TAG, item.getPrice().replace(",", ""));
        
        TextView txtModel = findViewById(R.id.txtModel);
        TextView txtPrice = findViewById(R.id.txtPrice);
        TextView txtSale1 = findViewById(R.id.txtSale1);
        TextView txtSale2 = findViewById(R.id.txtSale2);
        TextView txtSale3 = findViewById(R.id.txtSale3);
        TextView txtResult = findViewById(R.id.txtResult);

        // 원가 텍스트 표시
        txtModel.setText(item.getModel());
        txtPrice.setText(txtChange(item.getPrice()));
        
        // 세일가 텍스트 표시
        txtSale1.setText(txtChange(item.getSale1()));
        txtSale2.setText(txtChange(item.getSale2()));
        txtSale3.setText(txtChange(item.getSale3()));
        txtResult.setText(formatter.format(price+sale1+sale2+sale3)+"원");

        menu1 = findViewById(R.id.Sale1);
        menu2 = findViewById(R.id.Sale2);
        menu3 = findViewById(R.id.Sale3);

        findViewById(R.id.btn_card_policy).setOnClickListener(onClickListener);
        findViewById(R.id.btn_stock).setOnClickListener(onClickListener);

        menuLoad();     // 할인 문구 불러오기
        noticeLoad();   // 공지사항 불러오기
        
        // 값이 없다면 안보이게 설정
//        Log.d(TAG, "하나: " + item.getSale1());
//        Log.d(TAG, "둘: " + item.getSale2());
//        Log.d(TAG, "셋: " + item.getSale3());

        if (item.getSale1().equals("")) {
//            Log.d(TAG, "조건 하나: " + item.getSale1());
//            findViewById(R.id.layout1).setVisibility(View.INVISIBLE);
            txtSale1.setVisibility(View.INVISIBLE);
            menu1.setVisibility(View.INVISIBLE);
        }
        if (item.getSale2().equals("")) {
//            Log.d(TAG, "조건 둘: " + item.getSale2());
//            findViewById(R.id.layout2).setVisibility(View.INVISIBLE);
            txtSale2.setVisibility(View.INVISIBLE);
            menu2.setVisibility(View.INVISIBLE);
        }
        if (item.getSale3().equals("")) {
//            Log.d(TAG, "조건 셋: " + item.getSale3());
//            Log.d(TAG, item.getSale3());
//            findViewById(R.id.layout3).setVisibility(View.INVISIBLE);
            txtSale3.setVisibility(View.INVISIBLE);
            menu3.setVisibility(View.INVISIBLE);
        }

//        Log.d(TAG, "Price : " + item.getPrice());
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_card_policy:
                    startImageViewAcitivy();
                    break;
                case R.id.btn_stock:
                    startToast("서비스 준비 중");
                    break;
            }
        }
    };

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startImageViewAcitivy() {
        Intent intent = new Intent(this, ImageViewActivity.class);
        startActivity(intent);
    }





    public String txtChange(String str) {
        if (str.equals(""))
            return "준비 중";
        return str+"원";
    }

    public int txtChangePrice(String str) {
        if (str.equals(""))
            return 0;
        return Integer.parseInt(str.replace(",",""));
    }

    private void menuLoad() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Management").document("Sale");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                final RelativeLayout loderLayout = findViewById(R.id.loderLayout);
//                loderLayout.setVisibility(View.VISIBLE);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        menu1.setText(document.get("sale1").toString());
                        menu2.setText(document.get("sale2").toString());
                        menu3.setText(document.get("sale3").toString());
//                        Log.d(TAG, document.get("name1").toString());
                        }
                    }
                else {
                        Log.d(TAG, "fail");
                }
//                loderLayout.setVisibility(View.GONE);
            }
        });
    }

    private void noticeLoad() {
        notice = findViewById(R.id.txtNotice);
        DocumentReference documentReference = db.collection("Management").document("Notice");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        notice.setText(document.get("notice").toString());
                    }
                }
                else {
                    Log.d(TAG, "fail");
                }
            }
        });
    }
}