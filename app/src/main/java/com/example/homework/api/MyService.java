package com.example.homework.api;

import com.example.homework.bean.ViewPagerBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MyService {
    //http://106.13.63.54:8080/sys/home.json
    String baseUrl="http://106.13.63.54:8080/sys/";

    @GET("home.json")
    Observable<ViewPagerBean> getData();
}
