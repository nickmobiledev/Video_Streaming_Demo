package com.nick.mobile.dev.videostreaming;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VideoDataRequest {

    // This info can be delivered from a server

    public static Gson JSON = new Gson();



    public static ArrayList<Map<String, String>> getVideoData(Context context){
        String jsonString =  openRaw(R.raw.videodata, context);
        ArrayList<Map<String, String>> videoData = JSON.fromJson(jsonString, new TypeToken<ArrayList<Map<String, String>>>(){}.getType());
        return videoData;
    }

    public static String openRaw(int rawFile, Context context) {
        InputStream is = context.getResources().openRawResource(rawFile);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return "error";
            }
            int n;
            while (true) {
                try {
                    if (!((n = reader.read(buffer)) != -1)) break;
                } catch (IOException e) {
                    return "error";
                }
                try {
                    writer.write(buffer, 0, n);
                } catch (IOException e) {
                    return "error";
                }
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                return "error";
            }
        }
        String jsonString = writer.toString();
        return jsonString;
    }


    public static void getImageFromUrl(String url, int index) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "Response Failed");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Log.d("response", "Got bitmap " + index);
                    RecyclerViewAdapter.BITMAPS.add(index, new BitmapDrawable(bitmap));

                } else {
                    Log.d("response", "response unsuccessful");
                }
            }
        });
    }



    public static void runOnUIThread(Runnable runnable) {
        MainActivity.mainActivity.runOnUiThread(runnable);
    }


}
