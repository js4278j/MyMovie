package com.example.mymovie.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovie.Data.MapInfo;
import com.example.mymovie.R;

import java.util.ArrayList;

public class MapListAdapter extends RecyclerView.Adapter<MapListAdapter.ViewHolder>  {

    private ArrayList<MapInfo> mapInfoList = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public MapListAdapter(ArrayList<MapInfo> list) {
        this.mapInfoList = list;
    }

    //아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView theaterNm, theaterAddress, theaterDistance;


        ViewHolder(View itemView){
            super(itemView);

            theaterNm = itemView.findViewById(R.id.theaterNm);
            theaterAddress = itemView.findViewById(R.id.theaterAddress);
            theaterDistance = itemView.findViewById(R.id.theaterDistance);

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
    public MapListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_theater_info_adapter, parent, false) ;
        MapListAdapter.ViewHolder vh = new MapListAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull MapListAdapter.ViewHolder holder, int position) {

        //String distance = String.format("%,.1f",mapInfoList.get(position).getTheaterDistance());

        holder.theaterNm.setText(mapInfoList.get(position).getTheaterNm());
        holder.theaterAddress.setText(mapInfoList.get(position).getTheaterAddress());
        holder.theaterDistance.setText(mapInfoList.get(position).getTheaterDistance() + " M");

    }

    @Override
    public int getItemCount() {
        return mapInfoList.size();
    }
}
