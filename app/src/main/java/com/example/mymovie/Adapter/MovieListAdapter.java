package com.example.mymovie.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mymovie.Activity.CommentActivity;
import com.example.mymovie.Data.MovieItem;
import com.example.mymovie.Manager.MovieManager;
import com.example.mymovie.R;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder>
                implements Filterable {

    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    public SparseBooleanArray getmSelectedItems() {
        return mSelectedItems;
    }

    public void setmSelectedItems(SparseBooleanArray mSelectedItems) {
        this.mSelectedItems = mSelectedItems;
        notifyDataSetChanged();
    }

    private MovieManager movieManager = MovieManager.getInstance();

    private ArrayList<MovieItem> movieList = null;
    private ArrayList<MovieItem> filterList;
    private Context mContext;
    private String mType;



    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    public MovieListAdapter(){

    }

    public MovieListAdapter(Context context, ArrayList<MovieItem> list) {
        this.mContext = context;
        this.movieList = list;
        this.filterList = list;
    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movImg, favorImg;
        TextView movTitle, movRelease, movDirector;
        TextView commentTxt;


        ViewHolder(View itemView) {
            super(itemView);

            favorImg = (ImageView) itemView.findViewById(R.id.favorImg);
            movImg = (ImageView) itemView.findViewById(R.id.movImg);
            movTitle = itemView.findViewById(R.id.movTitle);
            movRelease = itemView.findViewById(R.id.movRelease);
            movDirector = itemView.findViewById(R.id.movDirector);
            commentTxt = itemView.findViewById(R.id.commentTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });


            favorImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(movieManager.isLoginState()){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            /*if(mListener != null){
                                mListener.onItemClick(v,position);
                            }*/
                            if (mSelectedItems.get(position, false)) {
                                mSelectedItems.put(position, false);
                                favorImg.setImageResource(R.drawable.ic_favorite_border_orange_24dp);

                                movieManager.getFavoriteList().remove(movieList.get(position));
                                Toast.makeText(mContext, movieList.get(position).getTitle() + "이 삭제됨", Toast.LENGTH_SHORT).show();


                            } else {
                                mSelectedItems.put(position, true);
                                favorImg.setImageResource(R.drawable.ic_favorite_orange_24dp);
                                //movieManager.getFavoriteList().add(movieList.get(position));

                                if (!movieManager.getFavoriteList().contains(movieList.get(position))) {
                                    //movieManager.getFavoriteList().add(movieList.get(i));
                                    movieManager.getFavoriteList().add(movieList.get(position));
                                    Toast.makeText(mContext, movieList.get(position).getTitle() + " 관심영화 생성", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }else{
                        Toast.makeText(mContext, "로그인 이후 이용해 주세요.", Toast.LENGTH_SHORT).show();
                    }



                }
            });

        }
    }

    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_movie_adapter, parent, false);
        MovieListAdapter.ViewHolder vh = new MovieListAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieListAdapter.ViewHolder holder, final int position) {

        holder.movTitle.setText(String.valueOf(movieList.get(position).getTitle()));
        holder.movRelease.setText(String.valueOf(movieList.get(position).getRelease()));
        holder.movDirector.setText(String.valueOf(movieList.get(position).getDirector()));

        holder.commentTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(movieManager.isLoginState()){
                    Context context = v.getContext();
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("title", movieList.get(position).getTitle());
                    intent.putExtra("release", movieList.get(position).getRelease());
                    intent.putExtra("director", movieList.get(position).getDirector());
                    intent.putExtra("image", movieList.get(position).getImg_url());

                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext,"로그인 후 이용 가능합니다",Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (mSelectedItems.get(position, false)) {
            holder.favorImg.setImageResource(R.drawable.ic_favorite_orange_24dp);
        } else {
            holder.favorImg.setImageResource(R.drawable.ic_favorite_border_orange_24dp);
        }

        Glide.with(holder.itemView).load(movieList.get(position).getImg_url()).override(300, 400).into(holder.movImg);

    }

    @Override
    public int getItemCount() {
        return movieList.size();

    }


    //filter
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    movieList = filterList;
                } else {
                    ArrayList<MovieItem> filteringList = new ArrayList<>();
                    for(MovieItem movieItem : filterList) {
                        if(movieItem.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(movieItem);
                        }
                    }
                    movieList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = movieList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                movieList = (ArrayList<MovieItem>)results.values;
                notifyDataSetChanged();
            }
        };
    }


}
