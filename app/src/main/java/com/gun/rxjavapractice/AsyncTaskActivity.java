package com.gun.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.MaybeObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class AsyncTaskActivity extends AppCompatActivity {

    MyAsyncTask myAsyncTask;
    TextView mAndroidTxtView;
    TextView mRxTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        mAndroidTxtView = findViewById(R.id.mAndroidTxtView);
        mRxTextView = findViewById(R.id.mRxTextView);
        initAndroidAsync();
        initRxAsync();
    }

    private void initAndroidAsync(){
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("Hello","async", "world");
    }

    private void initRxAsync(){
        Observable.just("Hello", "rx", "world")
                .reduce((x,y)-> x+ " "+ y)
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(getObserver())
                .subscribe(
                        mRxTextView::setText,
                        Log::d,
                        ()->Log.d("TAG","DONE")
                );
    }

    private MaybeObserver<String> getObserver(){
        return new MaybeObserver<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull String s) {
              mRxTextView.setText(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("TAG", e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("TAG", "done");
            }
        };
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            StringBuilder word = new StringBuilder();
            for (String s : params){
                word.append(s).append(" ");
            }
            return word.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mAndroidTxtView.setText(s);
        }
    }
}