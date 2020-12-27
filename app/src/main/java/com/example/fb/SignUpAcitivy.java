package com.example.fb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpAcitivy extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_acitivy);
        //초기화 Firebath Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.loginButton).setOnClickListener(onClickListener);
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signUpButton:
                    signUp();
                    Log.e("리스너", "회원가입");
                    break;
                case R.id.loginButton:
                    startLoginActivity();
                    break;
            }
        }
    };


    private void signUp() {
        String email = ((EditText)findViewById(R.id.emailText)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        String passwordConfirm = ((EditText)findViewById(R.id.passwordConfirm)).getText().toString();

        if (email.length()>0 && password.length()>0 && passwordConfirm.length() > 0) {
            if (password.equals(passwordConfirm)) {
                final RelativeLayout loderLayout = findViewById(R.id.loderLayout);
                loderLayout.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loderLayout.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("회원가입을 축하합니다.");
                                    startLoginActivity();
                                    //UI
                                } else {
                                    if (task.getException() != null)
                                        startToast(task.getException().toString());
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    //UI
                                }

                                // ...
                            }
                        });
            } else {
                startToast("패스워드가 일치하지 않습니다.");
            }
        }
        else {
            startToast("이메일 또는 패스워드를 입력해주세요.");
        }


    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}