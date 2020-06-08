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

import com.example.mymovie.Data.UserInfo;
import com.example.mymovie.Manager.MovieManager;
import com.example.mymovie.R;

import java.util.ArrayList;

public class MyLoginFragment extends Fragment {

    public static MyLoginFragment newInstance(){
        return new MyLoginFragment();
    }

    private MovieManager movieManager;
    private ArrayList<UserInfo> userList;
    private View mView;

    private TextView signInTxt,signUpTxt;
    private EditText edit_id,edit_pw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login ,container,false);

        init();

        signInTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo userInfo = movieManager.geUserInfo(edit_id.getText().toString());

                if(userInfo != null){

                    if(edit_pw.getText().toString().equals(userInfo.getPw())){
                        //로그인 성공
                        Toast.makeText(getActivity(),"로그인 성공.",Toast.LENGTH_SHORT).show();
                        movieManager.setLoginState(true);
                        movieManager.setUserId(userInfo.getId());
                        movieManager.setNickName(userInfo.getNickName());

                       // MySucFragment mySucFragment = new MySucFragment();
                        MyFragment myFragment = (MyFragment)movieManager.getValue("MyFragment");
                        myFragment.switchTag("success");

                        // -> 이후에 MyFragment 에서 MySucFragment로 이동 할 수 있게 한다. MainActivity에서 쓴거와 구글에서 사용한것을 참고한다.
                    }
                }
                /*for(int i=0; i< userList.size();i++){

                    if(userList.get(i).getId().equals(edit_id.getText().toString()) && userList.get(i).getPw().equals(edit_pw.getText().toString())){

                        //movieManager.setLoginState(true);
                        movieManager.saveLoginPref(getActivity(),true, userList.get(i).getId());
                        movieManager.setUserId(movieManager.getUserList().get(i).getId());
                        movieManager.setNickName(movieManager.getUserList().get(i).getNickName());

                        Toast.makeText(getActivity(),"로그인 성공.",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(),"아이디 또는 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                    }
                }*/

                /*if(movieManager.isLoginState()){
                    MyFragment myFragment = (MyFragment)movieManager.getValue("MyFragment");
                    //myFragType = MyFragType.SUCCESS;
                    myFragment.setChildFragment(self);
                }*/

            }
        });

        signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragment myFragment = (MyFragment)movieManager.getValue("MyFragment");
                myFragment.switchTag("signUp");
                //MySignUpFragment mySignUpFragment = MySignUpFragment.newInstance();
                /*MyFragment myFragment = MyFragment.newInstance();
                myFragment.setChildFragment(MySignUpFragment.newInstance());*/

                //MyFragment myFragment = (MyFragment)movieManager.getValue("MyFragment");

               // myFragType = MyFragType.SIGNUP;
                //myFragment.setChildFragment(self);
            }
        });

        return mView;
    }

    private void init(){

        movieManager = MovieManager.getInstance();
        //loadDB가 여러번 실행될경우 값이 중복되어 들어오거나 부하가 걸리거나 확인해야함
        /*userList = movieManager.getUserList();
        movieManager.getUserInfo();*/

        signInTxt = mView.findViewById(R.id.signInTxt);
        signUpTxt = mView.findViewById(R.id.signUpTxt);
        edit_id = mView.findViewById(R.id.edit_id);
        edit_pw = mView.findViewById(R.id.edit_pw);

    }

}
