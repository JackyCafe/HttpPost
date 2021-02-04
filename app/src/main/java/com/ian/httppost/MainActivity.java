package com.ian.httppost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doPost();
            }
        }).start();
    }

    private void doPost()  {

        try {

            URL url = new URL("http://120.110.115.81:8000/api/blog/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//設定訊息的型別
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            JSONObject json = new JSONObject();//建立json物件
            json.put("title", "test1");//使用URLEncoder.encode對特殊和不可見字元進行編碼
            json.put("category","新聞");
            json.put("content","新聞");
            json.put("user", 1);
            conn.connect();

            Log.v("Jacky",json.toString());
            OutputStream out = conn.getOutputStream();//輸出流，用來發送請求，http請求實際上直到這個函式裡面才正式傳送出去
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//建立字元流物件並用高效緩衝流包裝它，便獲得最高的效率,傳送的是字串推薦用字元流，其它資料就用位元組流
            bw.write(json.toString());//把json字串寫入緩衝區中
            bw.flush();//重新整理緩衝區，把資料傳送出去，這步很重要
            out.close();
            bw.close();//使用完關閉

            int responseCode = conn.getResponseCode();
            Log.v("Jacky","responseCode:"+responseCode);
            if (responseCode >= 400 ) {
                InputStream in =  conn.getErrorStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);
                Log.v("br",""+responseCode);

            }

        }catch (Exception e){
            for (StackTraceElement st :e.getStackTrace())
                Log.v("Jacky",""+st.toString());
        }

    }
}