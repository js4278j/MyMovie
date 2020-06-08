package com.example.mymovie.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mymovie.Adapter.MovieListAdapter;
import com.example.mymovie.Data.MovieItem;
import com.example.mymovie.Manager.MovieManager;
import com.example.mymovie.R;
import com.example.mymovie.Activity.WebViewActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements TextWatcher {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private MovieManager movieManager = MovieManager.getInstance();
    private MovieListAdapter movieListAdapter;
    private ArrayList<MovieItem> movieList;

    //view
    private View mView;
    private RecyclerView movieRcv;
    private EditText searchBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        init();

        searchBar.addTextChangedListener(this);
        movieListAdapter = new MovieListAdapter(getActivity(), movieList);
        movieRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        movieRcv.setLayoutManager(movieRcv.getLayoutManager());
        movieManager.loadFavorImage(movieListAdapter);

        movieListAdapter.setOnItemClickListener(new MovieListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("movUrl", movieList.get(pos).getDetail_link());
                startActivity(intent);
            }
        });



        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        new BackGround().execute();
    }

    private void init() {
        movieRcv = mView.findViewById(R.id.movieRcv);
        searchBar = mView.findViewById(R.id.searchBar);
        //movieList = new ArrayList<>();
        movieList = movieManager.getMovieList();

    }

    private class BackGround extends AsyncTask<Void, Void, Void> {
        //진행바표시
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //진행다일로그 시작
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                if (movieList.size() < 126) {
                    Document doc = Jsoup.connect("https://movie.naver.com/movie/running/current.nhn").get();
                    Elements elements = doc.select("ul[class=lst_detail_t1]").select("li");
                    int elementsSize = elements.size();

                    for (Element elem : elements) {
                        String my_title = elem.select("li dt[class=tit] a").text();
                        String my_link = elem.select("li div[class=thumb] a").attr("href");
                        String my_imgUrl = elem.select("li div[class=thumb] a img").attr("src");

                        Element rElem = elem.select("dl[class=info_txt1] dt").next().first();
                        String my_release = rElem.select("dd").text();
                        Element dElem = elem.select("dt[class=tit_t2]").next().first();
                        String my_director = "감독: " + dElem.select("a").text();
                        movieList.add(new MovieItem(my_title, my_imgUrl, my_link, my_release, my_director));

                    }
                    Log.d("debug :", "List " + elements);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //null 체크 하는 이유
            //Fragment에서 AsyncTask 실행 도중 액티비티를 종료하면 Fragment와 Activity가 분리되면서
            //getContext()나 getActivity() 메서드가 null을 리턴하고 이렇게 되면 onPostExecute() 부분에서 context를 사용할 때
            //nullException이 일어나므로 시작부분에서 null 체크를 해주어야한다.
            if (getContext() == null || getActivity() == null) {
                return;
            }
            progressDialog.dismiss();
            movieRcv.setAdapter(movieListAdapter);
            movieListAdapter.notifyDataSetChanged();
        }
    }


    //recyclerView filter
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        movieListAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}
