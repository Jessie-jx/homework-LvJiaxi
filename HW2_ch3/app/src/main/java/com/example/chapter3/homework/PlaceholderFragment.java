package com.example.chapter3.homework;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;

import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment extends Fragment {
    private AnimatorSet animatorSet2;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件

//        // 实现一：静态ListView
//        List<String> dataList = new ArrayList<String>();
//        for (int i = 1; i <= 10; i++) {
//            dataList.add("好友" + Integer.toString(i));
//        }
//        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
//        ListView friendList = view.findViewById(R.id.friend_list);
//        ArrayAdapter adapter = new ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dataList);
//        friendList.setAdapter(adapter);

        // 实现二：动态RecyclerView
        List<String> myListData=new ArrayList<>();

        for(int i = 0; i < 100; i++) {
           myListData.add("Hello");
        }

        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        recyclerView = view.findViewById(R.id.friend_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(myListData));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                ObjectAnimator lottie_alpha = ObjectAnimator.ofFloat(getView().findViewById(R.id.loading_lottie), "alpha", 1f, 0f);
                lottie_alpha.setDuration(1000);
                lottie_alpha.setRepeatCount(0);

                ObjectAnimator friendList_alpha = ObjectAnimator.ofFloat(getView().findViewById(R.id.friend_list), "alpha", 0f, 1f);
                friendList_alpha.setDuration(1000);
                friendList_alpha.setRepeatCount(0);

                animatorSet2 = new AnimatorSet();
                animatorSet2.playSequentially(lottie_alpha, friendList_alpha);
                animatorSet2.start();
            }
        }, 5000);
    }
}
