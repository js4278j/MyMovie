package com.example.mymovie.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mymovie.Manager.MovieManager;
import com.example.mymovie.R;

//Login, SignUp, Suc 상위 Fragment
public class MyFragment extends Fragment {

    public static MyFragment newInstance() {
        return new MyFragment();
    }

    private String mTag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my,container,false);

        MovieManager movieManager = MovieManager.getInstance();
        //처음화면 세팅
        if (savedInstanceState == null) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            if(movieManager.isLoginState()){
                ft.add(R.id.child_fragment_container, MySucFragment.newInstance(), "success");
                mTag = "success";
            }else{
                ft.add(R.id.child_fragment_container, MyLoginFragment.newInstance(), "login");
                mTag = "login";
            }
            ft.commit();
        }
        movieManager.register("MyFragment",this);
        return view;
    }

    public void switchTag(String toTag){
        if (mTag.equals(toTag))
            return;

        FragmentManager fm = getChildFragmentManager();
        Fragment from = fm.findFragmentByTag(mTag);
        Fragment to = fm.findFragmentByTag(toTag);

        FragmentTransaction ft = fm.beginTransaction();
        if (to == null) {
            if ("login".equals(toTag)) {
                to = MyLoginFragment.newInstance();
            } else if ("signUp".equals(toTag)) {
                to = MySignUpFragment.newInstance();
            } else if ("success".equals(toTag)) {
                to = MySucFragment.newInstance();
            }
        }
        if (!to.isAdded()) {
            ft.hide(from).add(R.id.child_fragment_container, to, toTag);
        } else {
            ft.hide(from).show(to);
        }
        ft.commit();
        mTag = toTag;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void init(){

    }
}
