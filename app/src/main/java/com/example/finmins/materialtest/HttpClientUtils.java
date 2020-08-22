package com.example.finmins.materialtest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientUtils {
    /**
     2 　　　* Psend服务请求
     3      *@param type  请求方式
     4      * @param requestUrl 请求地址
     5      * @param requestbody 请求参数
     6      * @return buffer.toString()
     7      */

    private String runable( ){

        return
    };
    public String send(String type, String requestUrl, String requestbody){
        final String types = type;
        final String url = requestUrl;
        final String body = requestbody;
        Runnable run = new Runnable() {
            @Override
            public void run() {
                if(types =="get"||types=="GET"){
                    return sendGet(url);
                }
                if(types=="post"||types=="POST"){
                    return sendPost(url,body);
                }
                else return "请求方式错误"; //说明是内部类的问题
            }
        }
        run.run();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        })
    }

    /**
     2 　　　* Post服务请求
     3      *
     4      * @param requestUrl 请求地址
     5      * @param requestbody 请求参数
     6      * @return buffer.toString()
     7      */


        public static String sendPost(String requestUrl, String requestbody){

         try {
                 //建立连接
                 URL url = new URL(requestUrl);
                 HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                 //设置连接属性
                 connection.setDoOutput(true); //使用URL连接进行输出
                 connection.setDoInput(true); //使用URL连接进行输入
                 connection.setUseCaches(false); //忽略缓存
                 connection.setRequestMethod("POST"); //设置URL请求方法
                 String requestString = requestbody;

                 //设置请求属性
                 byte[] requestStringBytes = requestString.getBytes(); //获取数据字节数据
                 connection.setRequestProperty("Content-length", "" + requestStringBytes.length);
                 connection.setRequestProperty("Content-Type", "application/octet-stream");
                 connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                 connection.setRequestProperty("Charset", "UTF-8");

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
            public static String sendGet(String requestUrl){
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
}
