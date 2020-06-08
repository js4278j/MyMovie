package com.example.mymovie.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymovie.Manager.MovieManager;
import com.example.mymovie.R;

public class MySignUpFragment extends Fragment {

    public static MySignUpFragment newInstance(){
        return new MySignUpFragment();
    }

    MovieManager movieManager;

    View mView;

    TextView signUp_action,cancel_action;
    EditText sign_id, sign_pw, sign_nick;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sign_up ,container,false);

        movieManager = MovieManager.getInstance();

        signUp_action = mView.findViewById(R.id.signUp_action);
        cancel_action = mView.findViewById(R.id.cancel_action);
        sign_id = mView.findViewById(R.id.sign_id);
        sign_pw = mView.findViewById(R.id.sign_pw);
        sign_nick = mView.findViewById(R.id.sign_nick);

        signUp_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sign_id.getText().toString().length() > 0 && sign_pw.getText().toString().length() > 0 && sign_nick.getText().toString().length() > 0){
                    //UserInfo userInfo = new UserInfo(sign_id.getText().toString(), sign_pw.getText().toString(), sign_nick.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                    //databaseReference.child("info").child("userInfo").child(sign_id.getText().toString()).push().setValue(userInfo); // 데이터 푸쉬

                    if(movieManager.getUserMap().containsKey(sign_id.getText().toString())){
                        Toast.makeText(getActivity(),"아이디가 중복입니다. 다시 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }else {
                        movieManager.pushDB(sign_id.getText().toString(), sign_pw.getText().toString(), sign_nick.getText().toString());


                        sign_id.setText(""); //입력창 초기화
                        sign_pw.setText("");
                        sign_nick.setText("");

                        MyFragment myFragment = (MyFragment)movieManager.getValue("MyFragment");
                        myFragment.switchTag("login");
                        Toast.makeText(getActivity(),"가입성공 다시 로그인 해주세요.",Toast.LENGTH_SHORT).show();
                    }



                } else {
                    Toast.makeText(getActivity(),"빈칸을 채워주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragment myFragment = (MyFragment)movieManager.getValue("MyFragment");
                myFragment.switchTag("login");

            }
        });


        return mView;
    }


}
