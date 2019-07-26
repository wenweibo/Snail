package com.fxkj.publicframework.requestdata;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.fxkj.publicframework.beans.CallBackObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 管理所有请求类
 * Created by dongjunkun on 2016/2/1.
 */
public class RequestManager {
    private Handler handler = new Handler(Looper.getMainLooper());
    //网络连接超时时间
    private static final long TIME_OUT = 120000;
    protected void post(final int tag, String url, final HashMap params, final boolean isCache, final CallBack callBack, final Context _context) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Iterator map1it = params.entrySet().iterator();
        while (map1it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) map1it.next();
            //String newStr = new String(entry.getValue().getBytes("UTF-8"), "ISO-8859-1").trim();
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newBuilder().writeTimeout(TIME_OUT, TimeUnit.SECONDS).readTimeout(TIME_OUT, TimeUnit.SECONDS).build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String errorStr = e.getLocalizedMessage();
                        if (errorStr != null) {
                            if (errorStr.contains("502") || errorStr.contains("No address associated with hostname")) {
                                errorStr = "连接服务器失败";
                            }
                        }
                        OnFail(callBack, "连接服务器失败", tag);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    if (response == null) {
                        if (callBack != null) {
                            OnFail(callBack, "没有返回数据", tag);
                        }
                        return;
                    }
                    if (json.isEmpty()) {
                        if (callBack != null) {
                            onNoData(callBack, tag);
                        }
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(json);
                    String resultsCode = jsonObject.getString("retCode");
                    if (resultsCode.equals("0000")) {
                        if (callBack != null) {
                            onSuccess(callBack, tag, AnalyzeJson(jsonObject, tag, params));
                        }
                    } else {
                        if (callBack != null) {
                            String errorMsg = jsonObject.getString("retMsg");
                            if (resultsCode.equals("0099")) {

                            }
                            OnFail(callBack, errorMsg, tag);
                        }
                    }
                } catch (Exception e) {
                    Log.i("wxmijl", e.getMessage().toString());
                    if (callBack != null) {
                        OnFail(callBack, "格式化数据出错", tag);
                    }
                }
            }
        });
    }

    protected void get(final int tag, String url, final HashMap params, final boolean isCache, final CallBack callBack, final Context _context) {
        Log.e("test","abc");
        OkHttpUtils
                .get()
                .url(url)
                .params(params)
                .build()
                .connTimeOut(TIME_OUT)
                .readTimeOut(TIME_OUT)
                .writeTimeOut(TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, final Exception e, int id) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String errorStr = e.getLocalizedMessage();
                                if (errorStr != null) {
                                    if (errorStr.contains("502") || errorStr.contains("No address associated with hostname") || e instanceof TimeoutException) {
                                        errorStr = "连接服务器失败";
                                    }
                                }
                                OnFail(callBack, "连接服务器失败", tag);
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            String json = response.toString();
                            if (response == null) {
                                if (callBack != null) {
                                    OnFail(callBack, "没有返回数据", tag);
                                }
                                return;
                            }
                            if (json.isEmpty()) {
                                if (callBack != null) {
                                    onNoData(callBack, tag);
                                }
                                return;
                            }
                            JSONObject jsonObject = new JSONObject(json);
                            String resultsCode = jsonObject.getString("retCode");
                            if (resultsCode.equals("0000")) {
                                if (callBack != null) {
                                    onSuccess(callBack, tag, AnalyzeJson(jsonObject, tag, params));
                                }
                            } else {
                                if (callBack != null) {
                                    String errorMsg = jsonObject.getString("retMsg");
                                    if (resultsCode.equals("0099")) {

                                    }
                                    OnFail(callBack, errorMsg, tag);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("wxmijl", e.getMessage().toString());
                            if (callBack != null) {
                                OnFail(callBack, "格式化数据出错", tag);
                            }
                        }
                    }
                });
    }

    protected void postFile2(String url, File _file, CallBack callBack, final HashMap params) {
        url += "?fileName=" + _file.getName();
        final String url2 = url;
        RequestBody fileBody = RequestBody.create(MediaType.parse("video/mp4"), _file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileName", _file.getName(), fileBody).build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newBuilder().writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                String errorStr = e.getMessage();
                    OnFail(callBack, errorStr, -2);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json != null) {
                    try {
                        onSuccess(callBack, -2, AnalyzeJson(new JSONObject(json), -2, params));
                    } catch (JSONException e) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    protected void postFile(String url, File _file, CallBack callBack,int type,int index) {
        url += "?fileName=" + _file.getName();
        Log.d("url", type + "----" + url + "," + _file.getAbsolutePath());
        RequestBody fileBody = RequestBody.create(MediaType.parse("video/mp4"), _file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileName", _file.getName(), fileBody).build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newBuilder().writeTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                String errorStr = e.getMessage();
                OnFail(callBack, errorStr, -2);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json != null) {
                    try {
                        HashMap<String, Integer> params = new HashMap<>();
                        params.put("type", type);
                        params.put("index", index);
                        onSuccess(callBack, -2, AnalyzeJson(new JSONObject(json), -2, params));
                    } catch (JSONException e) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void OnFail(final CallBack callBack, final String error_text, final int flag) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onFailure(flag, error_text);
                }
            }
        });

    }

    private void onNoData(final CallBack callBack, final int flag) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onNoData(flag);
            }
        });
    }

    private void onSuccess(final CallBack callBack, final int flag, final CallBackObject _callBackObject) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    callBack.onSuccess(flag, _callBackObject);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    protected CallBackObject AnalyzeJson(JSONObject _json, final int flag, HashMap params) throws Exception {
        return null;
    }


}
