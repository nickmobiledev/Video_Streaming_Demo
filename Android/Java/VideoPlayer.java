package com.nick.mobile.dev.videostreaming;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.util.ArrayList;

public class VideoPlayer {

    SimpleExoPlayer player;
    ArrayList<MediaItem> mediaItems = new ArrayList<>();

    public VideoPlayer(Context context) {
        player = new SimpleExoPlayer.Builder(context).build();
    }

    public void playerReadyListener(Runnable runnable) {
        Log.d("tag", "loading = " + player.isLoading());
        if (player.isLoading()) {
            Timer.timer(100, new Runnable() {
                @Override
                public void run() {
                    playerReadyListener(runnable);
                }
            });
        } else {
            Timer.timer(100, new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            });
        }
    }

    public void getMediaItem(String url, int index) {
        mediaItems.add(index, MediaItem.fromUri(url));
    }

    public void setMediaItem(int index) {
        player.setMediaItem(mediaItems.get(index));
        player.prepare();
    }


}
