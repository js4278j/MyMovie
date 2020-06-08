package com.example.mymovie;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mymovie.Manager.MovieManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NumberPickerDialog extends AppCompatActivity {

    private DatePicker datePicker;
    private Button mPickDate,cancel_button;

    MovieManager movieManager = MovieManager.getInstance();
    Intent intent;
    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_picker);

        init();

        String[] temp = createDate();
        int[] ymd = stringToInt(temp);
        createCalendar(ymd[0], ymd[1] , ymd[2]);


        mPickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String year = Integer.toString(datePicker.getYear());
                String month = trans("month",datePicker.getMonth());
                String day = trans("day",datePicker.getDayOfMonth());
                String date = year + month + day;
                movieManager.register("rankDate",date);

                finish();

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAction();
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelAction();
    }

    public String[] createDate(){
        final Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String modifyDate = sdf.format(date);
        String[] temp = modifyDate.split("-");

        return temp;
    }

    public int[] stringToInt(String[] date){
        int[] ymd = new int[3];

        //null check
        if(date != null){
            for(int i=0; i<ymd.length;i++){
                ymd[i] = Integer.parseInt(date[i]);
            }
        }
        return ymd;
    }

    public void createCalendar(int year, int month, int day){

        //null check
        if(year != 0 || month != 0 || day != 0){
            Calendar minDate = Calendar.getInstance();
            Calendar maxDate = Calendar.getInstance();
            minDate.set(2003, 11 - 1, 11);
            maxDate.set(year, month - 1, day-1);
            datePicker.setMinDate(minDate.getTime().getTime());
            datePicker.setMaxDate(maxDate.getTimeInMillis());
        }

    }

    public String trans(String type, int value){
        //type month 일때 ++ day이면 그냥 변환.
        String temp = "";

        if(type.equals("month")){
            value++;
        }

        if(value < 10){
            temp = String.format("%02d",value);
        } else {
            temp = Integer.toString(value);
        }

        return temp;
    }

    public void cancelAction(){
        if(intent.hasExtra("currentDate")){
            String date = intent.getExtras().getString("currentDate");
            movieManager.register("rankDate",date);
        }
    }

    public void init(){
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        mPickDate = (Button) findViewById(R.id.pickDate);
        cancel_button = (Button)findViewById(R.id.cancel_button);
        intent = getIntent();
    }


}
