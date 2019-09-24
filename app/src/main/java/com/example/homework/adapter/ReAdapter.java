package com.example.homework.adapter;

import android.content.Context;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.homework.R;
import com.example.homework.bean.ViewPagerBean;
import com.example.homework.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class ReAdapter extends RecyclerView.Adapter {

    private List<ViewPagerBean.ShopListBeansBean> shopLists;
    private ViewPagerBean.WonderfulTimeBean wonderfulTime;
    private Context context;
    private OnItemClick onItemClick;
    private Handler handler;
    private int start_time;
    private long time;
    public void initData(ViewPagerBean.WonderfulTimeBean wonderfulTime) {
        this.wonderfulTime = wonderfulTime;
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public ReAdapter(List<ViewPagerBean.ShopListBeansBean> shopLists, Context context) {
        this.shopLists = shopLists;

        this.context = context;
    }

    private final static int VIEW0 = 0;
    private final static int VIEW1 = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == VIEW0) {
            view = LayoutInflater.from(context).inflate(R.layout.re_view0, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.re_view1, null);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == VIEW0) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            String url = wonderfulTime.getWonderful_url();
            Glide.with(context).load(url).into(viewHolder.iv_view0);
//            int start_time = wonderfulTime.getStart_time();
//            if (start_time != -1) {
//                initTimer(viewHolder);
//            }
            //获取系统的时间戳
            long nowTimer = TimeUtils.getNowTimer();

            //获取接口上的时间戳
            int start_time = wonderfulTime.getStart_time();

            //获取的时间戳 - 系统时间戳 = 相隔时间
            time=start_time-nowTimer;

            final Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            };
            timer.schedule(timerTask, 1000, 1000);

            handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    time--;
                    //调用方法转为时间
                    String remainTime = TimeUtils.getRemainTime(time);
                    viewHolder.tv_time_view0.setText(remainTime);
                }
            };
        } else {
            ViewHolder viewHolder1 = (ViewHolder) holder;
            ViewPagerBean.ShopListBeansBean beansBean = shopLists.get(position);
            Glide.with(context).load(beansBean.getShop_image_url()).into(viewHolder1.iv_view1);
            viewHolder1.tv_content_view1.setText(beansBean.getShop_introd());
            viewHolder1.tv_price_view1.setText(beansBean.getShop_pirce());
            viewHolder1.tv_title_view1.setText(beansBean.getShop_name());
            //上市时间的设置
            String shop_listingTime = beansBean.getShop_ListingTime();
            Long shopTime = Long.valueOf(shop_listingTime);
            //获取当前时间
            long nowTimer = TimeUtils.getNowTimer();
            //获得时间差
            long cha=nowTimer-shopTime;
            //换算成天
            long days = cha / (60 * 60 * 24);

            viewHolder1.tv_time_view1.setText(days+"天前");


        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onItemClick(position);
                }
            }
        });
    }


    public ViewPagerBean.ShopListBeansBean getBean(int position) {
        ViewPagerBean.ShopListBeansBean bean = shopLists.get(position);
        return bean;
    }

    @Override
    public int getItemCount() {
        return shopLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW0;
        } else {
            return VIEW1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time_view0;
        ImageView iv_view0;
        ImageView iv_view1;
        TextView tv_title_view1;
        TextView tv_content_view1;
        TextView tv_time_view1;
        TextView tv_price_view1;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if (viewType == VIEW0) {
                tv_time_view0 = itemView.findViewById(R.id.tv_time_view0);
                iv_view0 = itemView.findViewById(R.id.iv_view0);
            } else {
                iv_view1 = itemView.findViewById(R.id.iv_view1);
                tv_time_view1 = itemView.findViewById(R.id.tv_time_view1);
                tv_title_view1 = itemView.findViewById(R.id.tv_title_view1);
                tv_content_view1 = itemView.findViewById(R.id.tv_content_view1);
                tv_price_view1 = itemView.findViewById(R.id.tv_price_view1);
            }
        }
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }
}
