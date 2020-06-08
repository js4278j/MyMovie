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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mymovie.Adapter.MovieRankAdapter;
import com.example.mymovie.Manager.MovieManager;
import com.example.mymovie.Data.MovRank;
import com.example.mymovie.NumberPickerDialog;
import com.example.mymovie.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;

public class RankFragment extends Fragment {

    public static RankFragment newInstance() {
        return new RankFragment();
    }

    // 발급키
    String key = "15929cbaffae2fbbc1f020167e9ed94d";

    View mView;
    LinearLayout searchLout;
    TextView dateTxt;

    String mDate;
    boolean defaultState;
    ArrayList<MovRank> rankList;
    RecyclerView rankRcv;
    MovieRankAdapter movieRankAdapter;
    MovieManager movieManager = MovieManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_rank, container, false);
        init();

        NumberPickerDialog numberPickDialog = new NumberPickerDialog();

        searchLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultState = false;
                Intent intent = new Intent(getActivity(), NumberPickerDialog.class);
                //취소를 눌렀을때 이전 정보를 다시 불러오기 위해
                intent.putExtra("currentDate",mDate);
                startActivity(intent);

            }
        });


        return mView;
    }

    private void init() {

        defaultState = true;

        dateTxt = mView.findViewById(R.id.dateTxt);
        searchLout = mView.findViewById(R.id.searchLout);
        rankRcv = mView.findViewById(R.id.rankRcv);
        rankList = new ArrayList<>();

        movieRankAdapter = new MovieRankAdapter(rankList);
        rankRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        rankRcv.setLayoutManager(rankRcv.getLayoutManager());
    }

    @Override
    public void onResume() {
        super.onResume();
        new BackGround().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
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
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                if (defaultState) {
                    mDate = movieManager.setModifyDate();
                    int temp = Integer.parseInt(mDate) - 1;
                    mDate = Integer.toString(temp);

                } else {
                    if (rankList.size() != 0){
                        rankList.clear();
                    }

                    mDate = movieManager.getValue("rankDate").toString();
                }

                if (rankList.size() < 10) {

                    KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);
                    String dailyResponse = "none";
                    dailyResponse = service.getDailyBoxOffice(true, mDate, "10", "", "", "");

                    JsonParser jsonParser = new JsonParser();
                    JsonElement jsonElement = jsonParser.parse(dailyResponse);

                    //manager로 따로 빼서 처리해준다.
                    //String 전체 리스트
                    String dailyBoxOfficeList = jsonElement.getAsJsonObject().get("boxOfficeResult").getAsJsonObject().get("dailyBoxOfficeList").toString();
                    JSONArray jsonArray = new JSONArray(dailyBoxOfficeList);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String rnum = jsonObject1.getString("rnum");
                        String rank = jsonObject1.getString("rank");
                        String rankInten = jsonObject1.getString("rankInten");
                        String movieCd = jsonObject1.getString("movieCd");
                        String movieNm = jsonObject1.getString("movieNm");
                        String openDt = jsonObject1.getString("openDt");
                        String audiCnt = jsonObject1.getString("audiCnt");
                        String audiAcc = jsonObject1.getString("audiCnt");
                        String scrnCnt = jsonObject1.getString("scrnCnt");

                        MovRank movRank = new MovRank(rnum, rank, rankInten, movieCd, movieNm, openDt, audiCnt, audiAcc, scrnCnt);
                        rankList.add(movRank);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            if (getContext() == null || getActivity() == null) {
                return;
            }

            String transDate = movieManager.setTransDate(mDate);
            dateTxt.setText(transDate);
            movieRankAdapter.notifyDataSetChanged();
            rankRcv.setAdapter(movieRankAdapter);

        }
    }

}
