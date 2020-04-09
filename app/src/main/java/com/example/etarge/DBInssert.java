package com.example.etarge;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DBInssert {
    public static String [] Inphp(String[] i,String Wcook,String url) {
        String result = "";
        String r = "";

        try {
            HttpClient httpClient = new DefaultHttpClient(); //使用DefaultHttpClient類型實作HttpClient物件因為元件跟API版本不同所以選單會有一條中線
            HttpPost httpPost = new HttpPost();
            HttpGet request = new HttpGet();
            System.out.println("是否取得cookie="+Wcook);
            httpPost.addHeader("Cookie",Wcook+";expires=Thu,31-Dec-37 23:55:55 GMT; path=/");


                 ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(); //ArrayList是一個陣列表，NameValuePair是一個接口
                 params.add(new BasicNameValuePair("S1", i[0]));
                 params.add(new BasicNameValuePair("S2", i[1]));
                 params.add(new BasicNameValuePair("S3", i[2]));
            httpPost.setEntity(new UrlEncodedFormEntity(params, org.apache.http.protocol.HTTP.UTF_8)); //UrlEncodedFormEntity為指定編碼參數構造一個新的來發送Http Post請求(例UTF8)
            HttpResponse httpResponse = httpClient.execute(httpPost);// HttpResponse為接收傳回值。excute為執行=>用在GET跟POST
            //String respPHP = EntityUtils.toString(httpResponse.getEntity());
            //Log.e("log_tag",respPHP);
            //view_account.setText(httpResponse.getStatusLine().toString());
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            String response = new String( httpResponse.getEntity().toString());
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8); //BufferedReader為創建使用指定大小的輸入緩衝區的緩衝字符輸入流
            StringBuilder builder = new StringBuilder(); //StringBuilder是一個動態物件，可以指定它能容納的字元數上限值
            String line = null;

            while((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
                Log.e("log_tag_q", line+"*");
                r=line;
            }

            inputStream.close();
            result = builder.toString();
        } catch(Exception e) {

        }
        return new String[] {result,r};
    }


}

