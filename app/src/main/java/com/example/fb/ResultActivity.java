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
    TextView notice1, notice2, notice3;
    String sel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_result_);

        Intent intent = getIntent();
        Item item = (Item) intent.getSerializableExtra("sendItem");

        int price, sale1, sale2, sale3;
        DecimalFormat formatter = new DecimalFormat("###,###");


        // 정수 변수에 받아옴
        price = txtChangePrice(item.getPrice());
        sale1 = txtChangePrice(item.getSale1());
        sale2 = txtChangePrice(item.getSale2());
        sale3 = txtChangePrice(item.getSale3());

        TextView txtCat = findViewById(R.id.textCat);
        TextView txtModel = findViewById(R.id.textModel);
        TextView txtPrice = findViewById(R.id.textPrice);
        TextView txtSale1 = findViewById(R.id.textSale1);
        TextView txtSale2 = findViewById(R.id.textSale2);
        TextView txtSale3 = findViewById(R.id.textSale3);
        TextView txtResult = findViewById(R.id.textResult);

        // 원가 텍스트 표시
        txtModel.setText(item.getModel());
        txtPrice.setText(txtChange(item.getPrice()));

        txtCat.setText(item.getSale_price());
        // 세일가 텍스트 표시
        txtSale1.setText(txtChange(item.getSale1()));
        txtSale2.setText(txtChange(item.getSale2()));
        txtSale3.setText(txtChange(item.getSale3()));
        txtResult.setText(formatter.format(price+sale1+sale2+sale3)+"원");

        menu1 = findViewById(R.id.txtViewSale1);
        menu2 = findViewById(R.id.txtViewSale2);
        menu3 = findViewById(R.id.txtViewSale3);

        findViewById(R.id.btn_card_policy).setOnClickListener(onClickListener);
        findViewById(R.id.btn_stock).setOnClickListener(onClickListener);
        findViewById(R.id.btn_calc).setOnClickListener(onClickListener);

        menuLoad();     // 할인 문구 불러오기


        noticeLoad(item.getSale_price());   // 공지사항 불러오기

        if (item.getSale1().equals("")) {
            txtSale1.setVisibility(View.INVISIBLE);
            menu1.setVisibility(View.INVISIBLE);
        }
        if (item.getSale2().equals("")) {
            txtSale2.setVisibility(View.INVISIBLE);
            menu2.setVisibility(View.INVISIBLE);
        }
        if (item.getSale3().equals("")) {
            txtSale3.setVisibility(View.INVISIBLE);
            menu3.setVisibility(View.INVISIBLE);
        }

////        Log.d(TAG, "Price : " + item.getPrice());
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
                case R.id.btn_calc:
                    Intent intent = getPackageManager().getLaunchIntentForPackage("com.sec.android.app.popupcalculator");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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
//                        Log.d(TAG, "fail");
                }
//                loderLayout.setVisibility(View.GONE);
            }
        });
    }

    private void noticeLoad(String sel_menu) {
        if (sel_menu.equals("정책할인"))
            sel = "notice_policy";
        else if (sel_menu.equals("딤채 (3개 지점)") || sel_menu.equals("딤채"))
            sel = "notice_dimchae";
        else if (sel_menu.equals("프라우드"))
            sel = "notice_praud";
        else if (sel_menu.equals("에어컨"))
            sel = "notice_air";
        else if (sel_menu.equals("딤채쿡"))
            sel = "notice_cook";
        else if (sel_menu.equals("냉장고, 냉동고"))
            sel = "notice_ref";
        else if (sel_menu.equals("세탁기"))
            sel = "notice_wash";
        else if (sel_menu.equals("기타소물"))
            sel = "notice_etc";
        else if (sel_menu.equals("전시품"))
            sel = "notice_exh";
        else if (sel_menu.equals("위니아전자"))
            sel = "notice_winia";
        else if (sel_menu.equals("20년형 딤채"))
            sel = "notice_20dim";


        notice1 = findViewById(R.id.txtNotice1);
        notice2 = findViewById(R.id.txtNotice2);
        notice3 = findViewById(R.id.txtNotice3);
        DocumentReference documentReference = db.collection("Management").document("Notice");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        notice1.setText(document.get(sel).toString());
                        notice2.setText(document.get(sel+'1').toString());
                        notice3.setText(document.get(sel+'2').toString());
                    }
                }
                else {
//                    Log.d(TAG, "fail");
                }
            }
        });
    }
}