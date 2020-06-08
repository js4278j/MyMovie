package com.example.mymovie.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymovie.Activity.CommentActivity;
import com.example.mymovie.Data.MovieItem;
import com.example.mymovie.R;
import com.example.mymovie.SwipeAndDragHelper;

import java.util.ArrayList;

public class FavorListAdapter extends RecyclerView.Adapter<FavorListAdapter.ViewHolder>
        implements SwipeAndDragHelper.ActionCompletionContract{

    //edit mode
    public static final int VIEWTYPE_NORMAL = 0;
    public static final int VIEWTYPE_EDIT = 1;
    private int mItemViewType;
    private ItemTouchHelper touchHelper;

    public void setItemViewType(int viewType) {
        mItemViewType = viewType;
        notifyDataSetChanged();
    }

    //중요 위에 setItemViewType에서 notifyDataSetChanged(); 를 해줌으로써 getItemViewType을 타게되고 position 값을 받아와 viewType을 다시 반환시켜 view 를 갱신시켜준다.
    // 그렇게 하지 않으면 편집을 눌렀을때 onCreateViewHolder 자체를 타지를 않는다 갱신은 하는데 getItemViewType에서 새로운 viewType을 갱신시켜주지 않기 때문이다
    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        return mItemViewType;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    private ArrayList<MovieItem> favorList;
    private Context mContext;

    public FavorListAdapter(Context context,ArrayList<MovieItem> favorList ) {
        this.mContext = context;
        this.favorList = favorList;
    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movImg;
        TextView movTitle, movRelease, movDirector;
        TextView commentTxt;
        LinearLayout mvMove;

        ViewHolder(View itemView) {
            super(itemView);

            movImg = itemView.findViewById(R.id.movImg);
            movTitle = itemView.findViewById(R.id.movTitle);
            movRelease = itemView.findViewById(R.id.movRelease);
            movDirector = itemView.findViewById(R.id.movDirector);
            commentTxt = itemView.findViewById(R.id.commentTxt);

            if (mItemViewType == 1){
                //mvOption = itemView.findViewById(R.id.mvOption);
                mvMove = itemView.findViewById(R.id.mvMove);
            }

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

        }
    }

    @NonNull
    @Override
    public FavorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == VIEWTYPE_NORMAL) {
            view = inflater.inflate(R.layout.item_favor_movie_adapter, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_favor_movie_editmode, parent, false);
        }

        //View view = inflater.inflate(R.layout.item_favor_movie_adapter, parent, false);
        FavorListAdapter.ViewHolder vh = new FavorListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final FavorListAdapter.ViewHolder holder, final int position) {

        holder.movTitle.setText(String.valueOf(favorList.get(position).getTitle()));
        holder.movRelease.setText(String.valueOf(favorList.get(position).getRelease()));
        holder.movDirector.setText(String.valueOf(favorList.get(position).getDirector()));

        holder.commentTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("title", favorList.get(position).getTitle());
                intent.putExtra("release", favorList.get(position).getRelease());
                intent.putExtra("director", favorList.get(position).getDirector());
                intent.putExtra("image", favorList.get(position).getImg_url());

                mContext.startActivity(intent);

            }
        });

        Glide.with(holder.itemView).load(favorList.get(position).getImg_url()).override(300, 400).into(holder.movImg);

        if(mItemViewType == 1){
            holder.mvMove.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
                        touchHelper.startDrag(holder);
                    }
                    return false;
                }
            });


            /*holder.mvOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Context context = v.getContext();
                    //Intent intent = new Intent(context, FolderEditActivity.class);
                    //intent.putExtra("title",title);
                   // intent.putExtra("position",position);
                    //(context).startActivity(intent);

                    *//*Context context = v.getContext();

                    ((MainActivity)v.getContext()).onClickItem(position);
                    ((MainActivity)v.getContext()).changeOptionType(position);*//*

                }
            });*/
        }




    }

    @Override
    public int getItemCount() {
        if(favorList == null){
            return 0;
        }
        return favorList.size();
    }


    @Override
    public void onViewMoved(int from_position, int to_position) {
        //이동할 객체 저장
        MovieItem movieItem = favorList.get(from_position);
        //이동할 객체 삭제
        favorList.remove(from_position);
        //이동하고 싶은 position에 추가
        favorList.add(to_position, movieItem);
        //Adapter에 데이터 이동알림
        notifyItemMoved(from_position, to_position);

    }

    @Override
    public void onViewSwiped(int position) {
        favorList.remove(position);
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.touchHelper = touchHelper;
    }

}
