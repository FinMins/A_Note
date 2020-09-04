package com.example.finmins.materialtest;

import android.util.Log;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpClientUtils {
  private String responseByOkhttp ;
    /**
     2 　　* send服务请求
     3      *@param type  请求方式
     4      * @param requestUrl 请求地址
     5      * @param requestbody 请求参数
     6      * @return responseText ;
     7      */


//    private Map<String, Object> JsonToMap(String s) {
//        return JSON.parseObject(s);
//    }

    /**
     2 　　* send服务请求
     3      *@param type  请求方式
     4      * @param requestUrl 请求地址
     5      * @param requestbody 请求参数
     6      * @return Map<string,object>
     7      */


    //返回json对象
//    public JSONObject send(String type, String requestUrl, String requestbody){
//        JSONObject jo = new JSONObject();
//                if(type =="get"||type=="GET"){
//
//                    try{   JSONObject jo1 = new JSONObject(sendGet(requestUrl));
//                        jo = jo1  ;
//                    }catch (Exception e){
//                    }
//                }
//                if(type=="post"||type=="POST"){
//                    try {
//                        JSONObject jo2 = new JSONObject(sendPost(requestUrl, requestbody));
//                        jo = jo2  ;
//                    }catch (Exception e){
//
//                    }
//
//                }
//                return  jo;
//        }
//    //返回string
//    public String send(String type, String requestUrl, String requestbody){
//
//               if(type =="get"||type=="GET") {
//                   return sendGet(requestUrl);
//               }
//               if(type=="post"||type=="POST"){
//                    return  sendPost(requestUrl, requestbody);
//               }
//               else return null;
//       }

    //返回string
    public String send(final String type,final String requestUrl,final String requestbody){


          Thread thread = new Thread(new Runnable() {
              @Override
              public void run() {
                  if(type =="get"||type=="GET") {
                      responseByOkhttp= sendGet(requestUrl);
                  }
                  if(type=="post"||type=="POST"){
                      responseByOkhttp =sendPost(requestUrl, requestbody);
                  }
              }
          });
          try { thread.start();
          thread.join();

          }catch (Exception e){
              Log.d("123123", "Http请求错误");
          }
        return responseByOkhttp;
       }


    /**
     2 　　　* Post服务请求
     3      *
     4      * @param requestUrl 请求地址
     5      * @param requestbody 请求参数
     6      * @return buffer.toString()
     7      */


        public  String sendPost(String requestUrl, String requestJsonBody){

         try {
                 //建立连接
                 URL url = new URL(requestUrl);
                 HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                 //设置连接属性
                 connection.setDoOutput(true); //使用URL连接进行输出
                 connection.setDoInput(true); //使用URL连接进行输入
                 connection.setUseCaches(false); //忽略缓存
                 connection.setRequestMethod("POST"); //设置URL请求方法
                 String requestString = requestJsonBody;

                 //设置请求属性
                 byte[] requestStringBytes = requestString.getBytes(); //获取数据字节数据
//                 connection.setRequestProperty("Content-length", "" + requestStringBytes.length);
//                 connection.setRequestProperty("Content-Type", "application/");
//                 connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
//                 connection.setRequestProperty("Charset", "UTF-8");

                 connection.setConnectTimeout(8000);
                 connection.setReadTimeout(8000);

                 //建立输出流,并写入数据
         OutputStream outputStream = connection.getOutputStream();
         outputStream.write(requestStringBytes);
         outputStream.close();

         //获取响应状态
         int responseCode = connection.getResponseCode();

         if (HttpURLConnection.HTTP_OK == responseCode) { //连接成功
                 //当正确响应时处理数据
                 StringBuffer buffer = new StringBuffer();
                 String readLine;
                 BufferedReader responseReader;
                 //处理响应流
                 responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             while ((readLine = responseReader.readLine()) != null) {
                     buffer.append(readLine).append("\n");
                 }
             responseReader.close();
             Log.d("HttpPOST", buffer.toString());
                 return buffer.toString();//成功
             }
             }catch (Exception e){
                 e.printStackTrace();
             }
         return "2";//失败

     }

      /**
  62      * Get服务请求
  63      *
  64      * @param requestUrl
  65      * @return buffer
  66      */
            public String sendGet(String requestUrl){
      try{
              //建立连接
              URL url = new URL(requestUrl);
              HttpURLConnection connection = (HttpURLConnection) url.openConnection();

              connection.setRequestMethod("GET");
              connection.setDoOutput(false);
              connection.setDoInput(true);

              connection.setConnectTimeout(8000);
              connection.setReadTimeout(8000);

              connection.connect();

              //获取响应状态
          int responseCode = connection.getResponseCode();

          if (HttpURLConnection.HTTP_OK == responseCode) { //连接成功
                  //当正确响应时处理数据
                  StringBuffer buffer = new StringBuffer();
                  String readLine;
                  BufferedReader responseReader;
                  //处理响应流
                  responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                  while ((readLine = responseReader.readLine()) != null) {
                          buffer.append(readLine).append("\n");
                      }
       responseReader.close();
            Log.d("HttpGET", buffer.toString());
            //JSONObject result = new JSONObject(buffer.toString());
            return buffer.toString();
        }
           }catch (Exception e){
               e.printStackTrace();
           }
       return null;
   }





/**
 2 　　　* OkhttprequestPost服务请求
 3      *
 4      * @param requestUrl 请求地址
 5      * @param requestbody 请求参数
 6      * @return buffer.toString()
 7      */


    public String sendPostByOkHttp(final String requestUrl, final String requestJsonBody){
        final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

     Thread a =   new Thread(new Runnable() {
           @Override
           public void run() {
               try{
                  OkHttpClient okHttpClient = new OkHttpClient();
                   Request request = new Request.Builder()
                           .url(requestUrl)
                           .post(RequestBody .create(requestJsonBody,mediaType)).build();
                   Response response = okHttpClient.newCall(request).execute();
                   responseByOkhttp = response.body().string();
                   Log.d("okhttp返回内容", responseByOkhttp);
               }catch (Exception e){
             responseByOkhttp = null;
               }
           }
       });
     try{
         a.start();
         a.join();
         return responseByOkhttp;
     }catch (Exception e){
         return "请求错误";
     }


    }
}
