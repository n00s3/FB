package com.example.fb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        menu1 = findViewById(R.id.Sale1);
        menu2 = findViewById(R.id.Sale2);
        menu3 = findViewById(R.id.Sale3);

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



        // 값이 없다면 안보이게 설정
          menuSet();
//        if (item.getSale1().equals("")) {
//            findViewById(R.id.layout1).setVisibility(View.GONE);
//            txtSale1.setVisibility(View.GONE);
//            menu1.setVisibility(View.GONE);
//        }
//        if (item.getSale1().equals("")) {
//            findViewById(R.id.layout2).setVisibility(View.GONE);
//            txtSale2.setVisibility(View.GONE);
//            menu2.setVisibility(View.GONE);
//        }
//        if (item.getSale1().equals("")) {
//            findViewById(R.id.layout3).setVisibility(View.GONE);
//            txtSale1.setVisibility(View.GONE);
//            menu3.setVisibility(View.GONE);
//        }

//        Log.d(TAG, "Price : " + item.getPrice());
    }

    public String txtChange(String str) {
        if (str.equals(""))
            return "X";
        return str+"원";
    }

    public int txtChangePrice(String str) {
        if (str.equals(""))
            return 0;
        return Integer.parseInt(str.replace(",",""));
    }

    private void menuSet() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Sale").document("sale");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        menu1.setText(document.get("name1").toString());
                        menu2.setText(document.get("name2").toString());
                        menu3.setText(document.get("name3").toString());
//                        Log.d(TAG, document.get("name1").toString());
                        }
                    } else {
                        Log.d(TAG, "fail");
                    }
                }
        });
    }


}