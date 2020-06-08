package com.example.mymovie.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseBooleanArray;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mymovie.Adapter.MovieListAdapter;
import com.example.mymovie.Data.MovieItem;
import com.example.mymovie.Data.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MovieManager {

    private static volatile MovieManager mMoleInstance;

    public static MovieManager getInstance() {
        if (mMoleInstance == null) { //if there is no instance available... create new one
            synchronized (MovieManager.class) {
                if (mMoleInstance == null) mMoleInstance = new MovieManager();
            }
        }
        return mMoleInstance;
    }

    private ArrayList<MovieItem> movieList = new ArrayList<>();

    public ArrayList<MovieItem> getMovieList() {
        return movieList;
    }

    private HashMap<String, Object> _objectMap = new HashMap<>();

    public void register(String key, Object value) {
        _objectMap.put(key, value);
    }

    public void unregister(String key) {
        if (_objectMap.containsKey(key)) {
            _objectMap.remove(key);
        }
    }

    public Object getValue(String key) {
        if (_objectMap.containsKey(key)) {
            return _objectMap.get(key);
        }
        return null;
    }

    private ArrayList<MovieItem> favoriteList = new ArrayList<>();

    public ArrayList<MovieItem> getFavoriteList() {
        return favoriteList;
    }

    public void setFavoriteList(ArrayList<MovieItem> favoriteList) {
        this.favoriteList = favoriteList;
    }


    //user Manager

    private boolean loginState = false;

    public boolean isLoginState() {
        return loginState;
    }

    public void setLoginState(boolean loginState) {

        this.loginState = loginState;
    }

    private HashMap<String, UserInfo> userMap = new HashMap<>();

    public HashMap<String, UserInfo> getUserMap() {
        return userMap;
    }

    public void putUserMap(String userId, UserInfo userInfo) {
        //혹시나 모른 중복 입력 방지
        if (!userMap.containsKey(userId)) {
            this.userMap.put(userId, userInfo);
        }
    }

    public UserInfo geUserInfo(String userId) {
        UserInfo userInfo = userMap.get(userId);
        return userInfo;
    }


    private String userId;
    private String nickName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    private ArrayList<LinearLayout> viewList = new ArrayList<>();

    public ArrayList<LinearLayout> getViewList() {
        return viewList;
    }


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    //로그인을 하는 순간  -> 비교후 로그인 한 정보를 Shared 에 저장
    public void loadDB() {
        databaseReference.child("info").child("user").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //DB값을 앱에다 넣을때 -> 로그인을 할 떄
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                putUserMap(userInfo.getId(), userInfo);

                /*if(userInfo.getId() != null){
                    userMap.put(userInfo.getId(),userInfo);
                }*/

                //getUserList().add(userInfo);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //회원가입을 할 때
    public void pushDB(String userId, String userPw, String userNickName) {
        UserInfo userInfo = new UserInfo(userId, userPw, userNickName); //ChatDTO를 이용하여 데이터를 묶는다.
        databaseReference.child("info").child("user").push().setValue(userInfo);
    }

    private SharedPreferences pref;

    public void saveLoginPref(Context context) {

        pref = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("loginState", loginState);
        editor.putString("loginId", userId);
        editor.apply();

        //saveFavorMovieList(userId);
        databaseReference = firebaseDatabase.getReference(userId);
        databaseReference.removeValue();

    }

    public void loadLoginPref() {

        loadDB();
        //비로그인 상태여도 pref는 들어간다. 그러므로 loginState을 같이 이용
        if (pref != null && isLoginState()) {
            loginState = pref.getBoolean("loginState", false);
            userId = pref.getString("loginId", null);
            //favoriteList.clear();
            updateMovieList(userId);
        }

        //contain 시켜서 해당 id 값에 맞는 movieList 불러와서 대조시킨다음에 favorlist에 넣어준다 .

        //setLoginState(pref.getBoolean("loginState", false));
        //setUserId(pref.getString("loginId", null));
    }

    //이건 HomeFragment가 그려진 후에
    public void loadFavorImage(MovieListAdapter movieListAdapter){

        if(movieListAdapter ==null)
            return;

        if(favoriteList.size() != 0){
            SparseBooleanArray selectedItems = new SparseBooleanArray(0);
            //MovieListAdapter movieListAdapter = (MovieListAdapter)getValue("MovieListAdapter");
            for(int i=0;i<movieList.size();i++){
                if(favoriteList.contains(movieList.get(i))){
                    selectedItems.put(i,true);
                }
            }

            // -> HomeFragment에 있는 어댑터를 가져와야한다.  MovieListAdapter movieListAdapter = new MovieListAdapter();
            movieListAdapter.setmSelectedItems(selectedItems);
        }



    }

    public void saveFavorMovieList(String userId) {

        //이미 들어 있는 값은 넣지 않거나 또는 모두 삭제후 다시 넣거나 어떤것이 효율적인가 부담을 덜가게하는가

        if (userId != null && isLoginState()) {
            for (int i = 0; i < favoriteList.size(); i++) {
                String movieName = favoriteList.get(i).getTitle();
                //databaseReference.child("info").child(userId).updateChildren(null);
                databaseReference.child("info").child(userId).push().setValue(movieName);
                //databaseReference.child("info").child(userId)

            }
        }

    }


    public void updateMovieList(String userId) {

        //favoriteList.clear(); 값이 들어가면
            /*databaseReference.child("info").child(userId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String movieName = dataSnapshot.getValue().toString();
                    containMovie(movieName);

                    //favoriteList.add()
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

            databaseReference.child("info").child(userId).removeEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String movieName = dataSnapshot.getValue().toString();
                    containMovie(movieName);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });






    }



    private void containMovie(String movieName) {

        for (int i = 0; i < movieList.size(); i++) {
            if (movieList.get(i).getTitle().contains(movieName)) {
                favoriteList.add(movieList.get(i));
            }
        }
    }

    //날짜 부여
    public String setModifyDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String modifyDate = sdf.format(date);

        return modifyDate;
    }

    public String setTransDate(String date) {

        String temp = date.substring(0, 4) + "년 ";
        temp += date.substring(4, 6) + "월 ";
        temp += date.substring(6, date.length()) + "일";

        return temp;
    }

    //end
}
