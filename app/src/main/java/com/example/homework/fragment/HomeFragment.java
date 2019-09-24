package com.example.homework.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.homework.DetailsActivity;
import com.example.homework.R;
import com.example.homework.adapter.ReAdapter;
import com.example.homework.adapter.ViewPagerAdapter;
import com.example.homework.api.MyService;
import com.example.homework.bean.ViewPagerBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements ReAdapter.OnItemClick {
    private List<ViewPagerBean.BannerBeanBean> viewps = new ArrayList<>();
    private ArrayList<View> views = new ArrayList<>();
    private View view1;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager vp;
    private ArrayList<ImageView> imageViews=new ArrayList<>();
    private List<ViewPagerBean.ShopListBeansBean> shopLists=new ArrayList<>();//商品集合
    private ReAdapter reAdapter;
  //  private ViewPagerBean.WonderfulTimeBean wonderfulTime=new ViewPagerBean.WonderfulTimeBean();
    private Handler handler=new Handler(){
      @Override
      public void handleMessage(@NonNull Message msg) {
          super.handleMessage(msg);
           vp.setCurrentItem(countNext);//设置这次要显示的pager
          //切换选中的圆点
            setPointStaus(countNext);
      }
  };
    private int countNext=0;
    private String TAG="HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_home, container, false);
        initView(inflate);
        initData();
        return inflate;
    }


    private void initView(View inflate) {
        vp = inflate.findViewById(R.id.vp);
        RecyclerView re = inflate.findViewById(R.id.re);
        final ImageView point1 = inflate.findViewById(R.id.point1);
        ImageView point2 = inflate.findViewById(R.id.point2);
        ImageView point3 = inflate.findViewById(R.id.point3);
        ImageView point4 = inflate.findViewById(R.id.point4);
        //定义一个集合list去加point
        imageViews = new ArrayList<>();
        imageViews.add(point1);
        imageViews.add(point2);
        imageViews.add(point3);
        imageViews.add(point4);
        //默认第一个点选中
        point1.setSelected(true);
        //通过定时器，制定自动滑动效果
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                countNext = 1+vp.getCurrentItem();//下一页
                Log.e(TAG, "下一页为:"+countNext+"" );
                if (countNext==views.size()){
                    countNext=0;
                }
                handler.sendEmptyMessage(0x123);
            }
        };
        timer.schedule(timerTask,3000,2000);
        //对vp进行滑动的监听
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPointStaus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //rv
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        re.setLayoutManager(linearLayoutManager);
        reAdapter = new ReAdapter(shopLists, getContext());
        re.setAdapter(reAdapter);
        reAdapter.setOnItemClick(this);

    }

    private void setPointStaus(int position) {
        for (int i = 0; i < imageViews.size(); i++) {
            if (i == position) {
                imageViews.get(i).setSelected(true);
            }else {
                imageViews.get(i).setSelected(false);
            }
        }
    }

    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        MyService myService = retrofit.create(MyService.class);
        Observable<ViewPagerBean> observable = myService.getData();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ViewPagerBean>() {



                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ViewPagerBean viewPagerBean) {
                        List<ViewPagerBean.BannerBeanBean> bannerBean = viewPagerBean.getBannerBean();
                        viewps.addAll(bannerBean);
                        views = new ArrayList<>();

                        for (int i = 0; i < viewps.size(); i++) {
                            view1 = View.inflate(getContext(), R.layout.view1, null);
                            String url = viewps.get(i).getBannerIma_url();
                            ImageView iv = view1.findViewById(R.id.iv_view1);
                            Glide.with(getContext()).load(url).into(iv);
                            views.add(view1);
                        }
                        viewPagerAdapter = new ViewPagerAdapter(views, getContext());
                        vp.setAdapter(viewPagerAdapter);
                        viewPagerAdapter.notifyDataSetChanged();
                        //rv
                        List<ViewPagerBean.ShopListBeansBean> shopListBeans = viewPagerBean.getShopListBeans();
                        shopLists.addAll(shopListBeans);
                        reAdapter.notifyDataSetChanged();
                        //timebean
                        ViewPagerBean.WonderfulTimeBean time = viewPagerBean.getWonderfulTime();
                        reAdapter.initData(time);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        ViewPagerBean.ShopListBeansBean bean = reAdapter.getBean(position);
        String url = bean.getShop_image_url();
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
}
