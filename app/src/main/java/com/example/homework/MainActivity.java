package com.example.homework;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.homework.adapter.MyTextAdapter;
import com.example.homework.fragment.HomeFragment;
import com.example.homework.fragment.MineFragment;
import com.example.homework.fragment.TypeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTab;
    private ViewPager mVp;
    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;
    private MyTextAdapter myTextAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTab = (TabLayout) findViewById(R.id.tab);
        mVp = (ViewPager) findViewById(R.id.vp);
        //集合
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new TypeFragment());
        fragments.add(new MineFragment());
        //标题
        titles = new ArrayList<>();
        titles.add("首页");
        titles.add("分类");
        titles.add("个人中心");
        myTextAdapter = new MyTextAdapter(getSupportFragmentManager(), fragments, titles);
        mVp.setAdapter(myTextAdapter);
        mTab.setupWithViewPager(mVp);
    }
}
