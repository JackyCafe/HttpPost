package com.ian.httppost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class MainActivity extends AppCompatActivity {
    private AppCompatEditText ed_user, ed_pwd;
    private String usr,pwd;
    private MainApp app;
//    private String surl = "https://usr-test-304018.df.r.appspot.com/api/reading/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_user = findViewById(R.id.user);
        ed_pwd = findViewById(R.id.pwd);
        app = (MainApp) getApplication();
        if (!app.getUserName().equals(""))    {
            ed_user.setText(app.getUserName());
        }
        if (!app.getPwdName().equals(""))    {
            ed_pwd.setText(app.getPwdName());
        }



    }



    public void doLogin(View view) {
        if (!ed_user.getText().equals("")){
            usr = ed_user.getText().toString();
            app.setUser(usr);
        }
        if (!ed_pwd.getText().equals("")){
            pwd = ed_pwd.getText().toString();
            app.setPwd(pwd);
        }
       doAuthentication();


    }

    private void doAuthentication() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL(app.getTokenURL());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(3000);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//設定訊息的型別
                    conn.setRequestProperty("Accept-Charset", "UTF-8");
                    JSONObject json = new JSONObject();//建立json物件

                    json.put("username", usr);//使用URLEncoder.encode對特殊和不可見字元進行編碼
                    json.put("password",pwd);

                    conn.connect();
                    Log.v("Jacky","json:"+json.toString());

                    OutputStream out = conn.getOutputStream();//輸出流，用來發送請求，http請求實際上直到這個函式裡面才正式傳送出去
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));//建立字元流物件並用高效緩衝流包裝它，便獲得最高的效率,傳送的是字串推薦用字元流，其它資料就用位元組流
                    out.write(json.toString().getBytes());//把json字串寫入緩衝區中
                    out.flush();//重新整理緩衝區，把資料傳送出去，這步很重要
                    out.close();
                    bw.close();//使用完關閉
                    int responseCode = conn.getResponseCode();
                    Log.v("Jacky","responseCode:"+responseCode);
                    InputStream in =  conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    BufferedReader br = new BufferedReader(reader);
                    String line = br.readLine();
                    Gson gson = new Gson();
                    Token token = gson.fromJson(line, new TypeToken<Token>(){}.getType());
                    app.setToken(token.getAccess());


                }catch (Exception e){
                    for (StackTraceElement se: e.getStackTrace())
                        Log.v("Jacky",se.toString());
                    Log.v("Jacky",e.toString());
                }

            }
        }).start();
        Log.v("Jacky",app.getToken());
        Intent it = new Intent(this, ReadingActivity.class);
        startActivity(it);
    }
}