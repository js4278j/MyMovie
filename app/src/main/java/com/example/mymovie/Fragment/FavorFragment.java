package com.example.mymovie.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovie.Adapter.FavorListAdapter;
import com.example.mymovie.Data.MovieItem;
import com.example.mymovie.Manager.MovieManager;
import com.example.mymovie.R;
import com.example.mymovie.SwipeAndDragHelper;
import com.example.mymovie.Activity.WebViewActivity;

import java.util.ArrayList;

public class FavorFragment extends Fragment {

    public static FavorFragment newInstance() {
        return new FavorFragment();
    }

    //view
    private View mView;
    private TextView editTxt;
    private RelativeLayout addRout;
    private RecyclerView favorRcv;
    private FavorListAdapter favorListAdapter;


    private ArrayList<MovieItem> favorList;

    ItemTouchHelper helper;
    SwipeAndDragHelper swipeAndDragHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_favor, container, false);

        init();

        if(favorList.isEmpty()){
            addRout.setVisibility(View.VISIBLE);
            favorRcv.setVisibility(View.GONE);
        } else {
            favorRcv.setVisibility(View.VISIBLE);
            addRout.setVisibility(View.GONE);
        }

        editTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!favorList.isEmpty()){
                    if(editTxt.getText().toString().equals("편집")){

                        favorListAdapter.setItemViewType(FavorListAdapter.VIEWTYPE_EDIT);
                        editTxt.setText("완료");

                    } else if(editTxt.getText().toString().equals("완료")) {
                        favorListAdapter.setItemViewType(FavorListAdapter.VIEWTYPE_NORMAL);
                        editTxt.setText("편집");
                    }
                } else {
                    Toast.makeText(getActivity(),"먼저 관심영화를 추가해주세요",Toast.LENGTH_SHORT).show();
                }

            }
        });

        favorListAdapter.setOnItemClickListener(new FavorListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("movUrl", favorList.get(pos).getDetail_link());
                startActivity(intent);
            }
        });

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void init(){
        editTxt = mView.findViewById(R.id.editTxt);
        favorRcv = mView.findViewById(R.id.favorRcv);
        addRout = mView.findViewById(R.id.addRout);

        MovieManager movieManager = MovieManager.getInstance();
        favorList = movieManager.getFavoriteList();

        favorRcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        favorListAdapter = new FavorListAdapter(getActivity(),movieManager.getFavoriteList());
        swipeAndDragHelper = new SwipeAndDragHelper(favorListAdapter);
        helper = new ItemTouchHelper(swipeAndDragHelper);
        favorListAdapter.setTouchHelper(helper);
        favorRcv.setAdapter(favorListAdapter);
        helper.attachToRecyclerView(favorRcv);
        favorRcv.setNestedScrollingEnabled(false);
        favorListAdapter.notifyDataSetChanged();

    }

}
