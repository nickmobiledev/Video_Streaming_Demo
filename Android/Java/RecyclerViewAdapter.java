package com.nick.mobile.dev.videostreaming;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.nick.mobile.dev.videostreaming.databinding.VideoLayoutBinding;

import java.util.ArrayList;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    MainActivity mainActivity;
    ArrayList<Map<String, String>> videoData;
    public static ArrayList<BitmapDrawable> BITMAPS = new ArrayList<>();
    VideoPlayer videoPlayer;

    public RecyclerViewAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        videoData = VideoDataRequest.getVideoData(mainActivity.getApplicationContext());
        videoPlayer = new VideoPlayer(mainActivity.getApplicationContext());
        for(int i=0; i<videoData.size(); i++){
            videoPlayer.mediaItems.add(i, null);
            BITMAPS.add(i, null);
        }
        for(int i=0; i<videoData.size(); i++){
            videoPlayer.getMediaItem(videoData.get(i).get("source"), i);
            VideoDataRequest.getImageFromUrl(videoData.get(i).get("img_source"), i);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VideoLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.video_layout,
                parent,
                false);
        return new RecyclerViewAdapter.ViewHolder(binding);
    }

    public void arrayListener(ArrayList array, final int index, Runnable runnable) {
        for (int i=0; i< array.size(); i++){
            Log.d("tag", "Array " + i + " = " + array.get(i));
        }
        Log.d("tag", "Listening for bitmap " + index);
        if (array.get(index) == null) {
            Log.d("tag", "Request Image Again");
            VideoDataRequest.getImageFromUrl(videoData.get(index).get("img_source"), index);
            Timer.timer(1000, new Runnable() {
                @Override
                public void run() {
                    arrayListener(array, index, runnable);
                }
            });
        } else {
            runnable.run();
        }
    }

    public void imageViewListener(ImageView imageView, Runnable runnable) {
        if (imageView.getHeight() <= 0) {
            Timer.timer(100, new Runnable() {
                @Override
                public void run() {
                    imageViewListener(imageView, runnable);
                }
            });
        } else {
            runnable.run();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapter.ViewHolder holder, final int position) {
        StyledPlayerView playerView = holder.binding.videoView;
        ImageView playerThumbNail = holder.binding.imageView;
        View progressWheel = holder.binding.progressWheel;
        TextView title = holder.binding.videoTitle;
        TextView subTitle = holder.binding.videoSubTitle;
        TextView description = holder.binding.description;
        ImageView descriptionButton = holder.binding.descriptionButton;


        arrayListener(BITMAPS, position, new Runnable() {
            @Override
            public void run() {
                Log.d("tag", "Set thumbnail " + position);
                progressWheel.setVisibility(View.INVISIBLE);
                playerThumbNail.setImageDrawable(BITMAPS.get(position));
           }
        });

        imageViewListener(playerThumbNail, new Runnable() {
            @Override
            public void run() {
                playerThumbNail.getLayoutParams().height = (int) ((int) playerThumbNail.getWidth() / 1.78);
                playerThumbNail.requestLayout();
            }
        });

        arrayListener(videoPlayer.mediaItems, position, new Runnable() {
            @Override
            public void run() {
                videoPlayer.setMediaItem(position);
            }
        });

        playerThumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.setMediaItem(position);
                progressWheel.setVisibility(View.VISIBLE);
                playerView.setPlayer(videoPlayer.player);
                videoPlayer.playerReadyListener(new Runnable() {
                    @Override
                    public void run() {
                        playerView.setVisibility(View.VISIBLE);
                        playerThumbNail.setVisibility(View.INVISIBLE);
                        progressWheel.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });


        title.setText(videoData.get(position).get("title"));
        subTitle.setText(videoData.get(position).get("subtitle"));

        descriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (description.getText().equals("")){
                    description.setText(videoData.get(position).get("description"));
                } else {
                    description.setText("");
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return videoData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VideoLayoutBinding binding;
        public ViewHolder(VideoLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



}