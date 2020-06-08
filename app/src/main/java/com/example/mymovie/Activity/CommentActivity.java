package com.example.mymovie.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mymovie.Data.ChatData;
import com.example.mymovie.Manager.MovieManager;
import com.example.mymovie.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    //ChatDTO chat;
    //ListView chat_list;
    ListView chat_view;
    ImageView chat_sent;
    EditText chat_edit;

    TextView movTitle,movRelease,movDirector;
    ImageView movImg;

    String mChatName;
    MovieManager movieManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        init();

        //child -> 하위 data를 참조한다.
        //databaseReference.child("chat").child(mChatName).push().setValue(chat); //databaseReference를 이용해 데이터 푸쉬
        //chat_edit.setText(""); //입력창 초기화
        //showChatList();

        openChat(mChatName);

        // 메시지 전송 버튼에 대한 클릭 리스너 지정
        chat_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat_edit.getText().toString().equals(""))
                    return;

                movieManager = MovieManager.getInstance();
                String nickName = movieManager.getNickName();

                ChatData chat = new ChatData(nickName, chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
                databaseReference.child("chat").child(mChatName).push().setValue(chat); // 데이터 푸쉬
                chat_edit.setText(""); //입력창 초기화

            }
        });

    }

    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatData chatData = dataSnapshot.getValue(ChatData.class);
        adapter.add(chatData.getUserName() + " : " + chatData.getMessage());
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        ChatData chatData = dataSnapshot.getValue(ChatData.class);
        adapter.remove(chatData.getUserName() + " : " + chatData.getMessage());
    }

    private void openChat(String chatName) {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_view.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);
                //dataSnapshot.getKey();
                Log.e("LOG", "s:"+s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //chat 확인용
    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        //chat_list.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                adapter.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void init(){

        //채팅방이름을 영화 제목으로 한다.
        //로그인 정보도 가져와야함.

        //chat
        chat_view = findViewById(R.id.chat_view);
        chat_sent = findViewById(R.id.chat_sent);
        chat_edit = findViewById(R.id.chat_edit);

        //chat = new ChatDTO();

        //info
        movTitle = findViewById(R.id.movTitle);
        movRelease = findViewById(R.id.movRelease);
        movDirector = findViewById(R.id.movDirector);
        movImg = findViewById(R.id.movImg);

        Intent intent = getIntent();
        if(intent.hasExtra("title") && intent.hasExtra("release") && intent.hasExtra("director") && intent.hasExtra("image")){
            mChatName = intent.getExtras().getString("title");
            movRelease.setText(intent.getExtras().getString("release"));
            movDirector.setText(intent.getExtras().getString("director"));

            movTitle.setText(mChatName);
            Glide.with(getApplicationContext()).load(intent.getExtras().getString("image")).override(300,400).into(movImg);
        }

        //hashmap으로 표현해준다.
        //mChatId = intent.getExtras().getString("id");


        //movImg.setText(intent.getExtras().getString("image"));


    }

}
