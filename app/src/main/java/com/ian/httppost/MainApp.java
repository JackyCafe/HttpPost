package com.ian.httppost;

import android.app.Application;
import android.content.SharedPreferences;

public class MainApp extends Application {
    SharedPreferences spf;
    SharedPreferences.Editor editor ;
    String token_url = "https://usr-test-304018.df.r.appspot.com/api/token/";
    String url = "https://usr-test-304018.df.r.appspot.com/api/reading/";

    @Override
    public void onCreate() {
        super.onCreate();
        spf  = getSharedPreferences("my_data.log",MODE_PRIVATE);
        editor = spf.edit();
    }


    public String getUserName(){
        return spf.getString("user","");
    }

    public void setUser(String user){
        editor.putString("user",user);
        editor.commit();
    }

    public String getPwdName()
    {
        return spf.getString("pwd","");
    }
    public void setPwd(String pwd){
        editor.putString("pwd",pwd);
        editor.commit();
    }

    public void setToken(String token)
    {
        editor.putString("token",token);
        editor.commit();
    }

    public String getToken()
    {
        return spf.getString("token","");
    }

    public  String getUrl() {return url;}
    public String getTokenURL(){return  token_url;}
}
