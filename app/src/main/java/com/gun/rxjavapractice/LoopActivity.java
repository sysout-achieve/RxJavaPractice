package com.gun.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class LoopActivity extends AppCompatActivity {
    public static final String TAG = LoopActivity.class.getSimpleName();

    Iterable<String> samples  = Arrays.asList("banana", "orange", "apple", "apple mango",
            "melon", "watermelon");

    private LogAdapter mLogAdapter;
    private List<String> mLogs;

    ListView mLogView;
    TextView mTitle;
    Button btn_loop;
    Button btn_loop2;
    Button btn_loop3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop);
        btn_loop = findViewById(R.id.btn_loop);
        btn_loop2 = findViewById(R.id.btn_loop2);
        btn_loop3 = findViewById(R.id.btn_loop3);
        mLogView = findViewById(R.id.lv_log);
        mTitle = findViewById(R.id.tv_title);
        setupLogger();
        btn_loop.setOnClickListener(view -> {
            onClickBtn1();
        });

        btn_loop3.setOnClickListener(view -> {
            onClickBtn3();
        });
    }

    private void onClickBtn1(){
        log(">>>>> get an apple :: java");
        for (String s :samples){
            if (s.contains("apple")){
                log(s);
                return;
            }
        }
    }

    private void onClickBtn3(){
        log(">>>>> get an apple :: rx 2.x");
        Observable.fromIterable(samples)
                .filter(s ->s.contains("apple"))
                .first("Not found")
                .subscribe(this::log);
    }

    private void log(String log) {
        mLogs.add(log);
        mLogAdapter.clear();
        mLogAdapter.addAll(mLogs);
    }
    private void setupLogger() {
        mLogs = new ArrayList<>();
        mLogAdapter = new LogAdapter(this, new ArrayList<>());
        mLogView.setAdapter(mLogAdapter);
    }

    private class LogAdapter extends ArrayAdapter<String> {
        public LogAdapter(Context context, List<String> logs) {
            super(context, R.layout.textview_log, R.id.tv_log, logs);
        }
    }

}