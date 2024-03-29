package com.cqkj.publicframework.requestdata;

import com.cqkj.publicframework.beans.CallBackObject;

import java.text.ParseException;

/**
 * by:wxmijl
 */
public interface CallBack {

    void onSuccess(int flag, CallBackObject obj) throws ParseException;

    void onFailure(int flag, String message);

    void onNoData(int flag);
}
