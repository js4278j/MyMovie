package com.example.mymovie.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mymovie.Manager.MovieManager;
import com.example.mymovie.R;

public class MySucFragment extends Fragment {

    public static MySucFragment newInstance(){
        return new MySucFragment();
    }

    View mView;
    TextView userId,userNickname;

    //test List
    ListView chat_list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_login_success ,container,false);

        MovieManager movieManager = MovieManager.getInstance();
        userId = mView.findViewById(R.id.userId);
        userNickname = mView.findViewById(R.id.userNickname);

        if(movieManager.getUserId() != null && movieManager.getNickName() != null){
            userId.setText(movieManager.getUserId() + " 님 환영합니다.");
            userNickname.setText(movieManager.getNickName());
        }


        //chat_list = mView.findViewById(R.id.chat_list);
        //
        movieManager.updateMovieList(movieManager.getUserId());
        return mView;
    }
}
