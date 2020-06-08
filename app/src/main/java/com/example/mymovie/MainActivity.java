package com.example.mymovie;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mymovie.Data.ViewInfo;
import com.example.mymovie.Fragment.FavorFragment;
import com.example.mymovie.Fragment.HomeFragment;
import com.example.mymovie.Fragment.MapFragment;
import com.example.mymovie.Fragment.MyFragment;
import com.example.mymovie.Fragment.RankFragment;
import com.example.mymovie.Manager.MovieManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionCheck();
        setContentView(R.layout.activity_main);
        init();
        movieManager.loadLoginPref();

        //처음화면
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.mainContainer, HomeFragment.newInstance(), "home");
            ft.commit();
            mTag = "home";
        }

        changeMainFrame();



    }

    public void changeMainFrame() {

        mainHomeLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchContent("home");
                selectPos(0);
            }
        });
        mainFavoriteLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchContent("favorite");
                selectPos(1);
            }
        });
        mainRankLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchContent("rank");
                selectPos(2);

            }
        });
        mainMapLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchContent("map");
                selectPos(3);
            }
        });
        mainMyLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchContent("my");
                selectPos(4);
            }
        });

    }

    public void switchContent(String toTag) {
        if (mTag.equals(toTag))
            return;

        FragmentManager fm = getSupportFragmentManager();
        Fragment from = fm.findFragmentByTag(mTag);
        Fragment to = fm.findFragmentByTag(toTag);

        FragmentTransaction ft = fm.beginTransaction();
        if (to == null) {
            if ("home".equals(toTag)) {
                to = HomeFragment.newInstance();
            } else if ("rank".equals(toTag)) {
                to = RankFragment.newInstance();
            } else if ("map".equals(toTag)) {
                to = MapFragment.newInstance();
            } else if ("favorite".equals(toTag)) {
                to = FavorFragment.newInstance();
            } else {
                to = MyFragment.newInstance();
            }
        }
        if (!to.isAdded()) {
            ft.hide(from).add(R.id.mainContainer, to, toTag);
        } else {
            ft.hide(from).show(to);
        }

        if (toTag.equals("favorite")) {
            ft.detach(to);
            ft.attach(to);
        }
        ft.commit();

        mTag = toTag;
    }

    public void selectPos(int pos) {

        movieManager.getViewList().get(pos).setBackgroundResource(R.color.color_select);
        for (int i = 0; i < movieManager.getViewList().size(); i++) {
            if (i != pos) {
                movieManager.getViewList().get(i).setBackgroundResource(R.color.design_default_color_background);
            }
        }

    }

    public void init() {
        movieManager = MovieManager.getInstance();
        backPressCloseHandler = new BackPressCloseHandler(this);
        mainHomeLout = findViewById(R.id.mainHomeLout);
        mainFavoriteLout = findViewById(R.id.mainFavoriteLout);
        mainRankLout = findViewById(R.id.mainRankLout);
        mainMapLout = findViewById(R.id.mainMapLout);
        mainMyLout = findViewById(R.id.mainMyLout);

        homeImg = findViewById(R.id.homeImg);
        favorImg = findViewById(R.id.favorImg);
        rankImg = findViewById(R.id.rankImg);
        mapImg = findViewById(R.id.mapImg);
        myImg = findViewById(R.id.myImg);

        homeTxt = findViewById(R.id.homeTxt);
        favorTxt = findViewById(R.id.favorTxt);
        rankTxt = findViewById(R.id.rankTxt);
        mapTxt = findViewById(R.id.mapTxt);
        myTxt = findViewById(R.id.myTxt);

        movieManager.getViewList().clear();
        movieManager.getViewList().add(mainHomeLout);
        movieManager.getViewList().add(mainFavoriteLout);
        movieManager.getViewList().add(mainRankLout);
        movieManager.getViewList().add(mainMapLout);
        movieManager.getViewList().add(mainMyLout);

    }

    private void permissionCheck(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1001);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieManager.saveLoginPref(this);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    String mTag;
    LinearLayout mainHomeLout, mainRankLout, mainMapLout, mainFavoriteLout, mainMyLout;
    TextView homeTxt, rankTxt, mapTxt, favorTxt, myTxt;
    ImageView homeImg, rankImg, mapImg, favorImg, myImg;

    MovieManager movieManager;
    BackPressCloseHandler backPressCloseHandler;

}
