package com.technoguff.mymusicplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.technoguff.mymusicplayer.R;
import com.technoguff.mymusicplayer.model.MusicFile;

import java.util.ArrayList;

/**
 * Created by darshanz on 7/14/15.
 */
public class MusicFileAdapter extends RecyclerView.Adapter<MusicFileAdapter.MyViewHolder>{

    ArrayList<MusicFile> mMusicFileList;

    public MusicFileAdapter(ArrayList<MusicFile> versionList) {
        mMusicFileList = versionList;
    }


    private OnMusicSelectedListener mListener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.layout_song_item, viewGroup, false);

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        MusicFile version = mMusicFileList.get(i);
        myViewHolder.title.setText(version.getTitle());
        myViewHolder.artist.setText(version.getArtist());

    }

    @Override
    public int getItemCount() {
        return mMusicFileList.size();
    }

    //ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView artist;


        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.songtitle);
            artist = (TextView)itemView.findViewById(R.id.artist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        mListener.onMusicSelected(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setMusicSelectedListener(OnMusicSelectedListener listneer){
       mListener = listneer;
    }


   public interface OnMusicSelectedListener {
       public void onMusicSelected(int position);
   }
}