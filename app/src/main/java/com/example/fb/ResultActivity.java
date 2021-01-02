package com.example.fb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        Item item = (Item) intent.getSerializableExtra("sendItem");

        int price, sale1, sale2, sale3;
        DecimalFormat formatter = new DecimalFormat("###,###");

        price = txtChangePrice(item.getPrice());
        sale1 = txtChangePrice(item.getSale1());
        sale2 = txtChangePrice(item.getSale2());
        sale3 = txtChangePrice(item.getSale3());

        Log.d(TAG, price+" "+sale1+" "+sale2+" "+sale3);
//        Log.d(TAG, item.getPrice().replace(",", ""));

        TextView txtModel = findViewById(R.id.txtModel);
        TextView txtPrice = findViewById(R.id.txtPrice);
        TextView txtSale1 = findViewById(R.id.txtSale1);
        TextView txtSale2 = findViewById(R.id.txtSale2);
        TextView txtSale3 = findViewById(R.id.txtSale3);
        TextView txtResult = findViewById(R.id.txtResult);

        txtModel.setText(txtChange(item.getModel()));
        txtPrice.setText(txtChange(item.getPrice()));
        txtSale1.setText(txtChange(item.getSale1()));
        txtSale2.setText(txtChange(item.getSale2()));
        txtSale3.setText(txtChange(item.getSale3()));
        txtResult.setText(formatter.format(price+sale1+sale2+sale3));


//        Log.d(TAG, "Price : " + item.getPrice());
    }

    public String txtChange(String str) {
        if (str.equals(""))
            return "X";
        return str;
    }

    public int txtChangePrice(String str) {
        if (str.equals(""))
            return 0;
        return Integer.parseInt(str.replace(",",""));
    }

}