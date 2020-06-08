package com.example.mymovie.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymovie.Data.MovRank;
import com.example.mymovie.R;

import java.util.ArrayList;

public class MovieRankAdapter extends RecyclerView.Adapter<MovieRankAdapter.ViewHolder>  {

private ArrayList<MovRank> movieList = null;

public interface OnItemClickListener {
    void onItemClick(View v, int pos);
}

    private OnItemClickListener mListener = null;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public MovieRankAdapter(ArrayList<MovRank> list) {
        this.movieList = list;
    }

//아이템 뷰를 저장하는 뷰홀더 클래스
public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView stateImg;
    TextView rankTxt,movieNameTxt,audiAccTxt,rankIntenTxt;


    ViewHolder(View itemView){
        super(itemView);

        stateImg = itemView.findViewById(R.id.stateImg);
        rankTxt = itemView.findViewById(R.id.rankTxt);
        movieNameTxt = itemView.findViewById(R.id.movieNameTxt);
        audiAccTxt = itemView.findViewById(R.id.audiAccTxt);
        rankIntenTxt = itemView.findViewById(R.id.rankIntenTxt);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    if(mListener != null){
                        mListener.onItemClick(v,pos);
                    }
                }
            }
        });
    }
}


    @NonNull
    @Override
    public MovieRankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_movie_rank_adapter, parent, false) ;
        MovieRankAdapter.ViewHolder vh = new MovieRankAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRankAdapter.ViewHolder holder, int position) {

        holder.rankTxt.setText(String.valueOf(movieList.get(position).getRank()));
        holder.movieNameTxt.setText(String.valueOf(movieList.get(position).getMovieNm()));
        holder.audiAccTxt.setText(String.valueOf(movieList.get(position).getAudiAcc()));
        holder.rankIntenTxt.setText(String.valueOf(movieList.get(position).getRankInten()));

        int temp = Integer.parseInt(movieList.get(position).getRankInten());

        if(temp > 0){
            holder.stateImg.setImageResource(R.drawable.ic_arrow_drop_up_red_24dp);
        } else if(temp < 0){
            holder.stateImg.setImageResource(R.drawable.ic_arrow_drop_down_blue_24dp);
        } else {
            holder.stateImg.setImageResource(R.drawable.ic_equal_black_24dp);
        }


        //Glide.with(holder.itemView).load(movieList.get(position).getImg_url()).override(300,400).into(holder.movImg);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
