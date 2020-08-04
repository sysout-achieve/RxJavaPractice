package com.gun.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class HelloActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        textView = findViewById(R.id.textView);

        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext("Hello world");
            emitter.onComplete();
        }).subscribe(this::accept);

        Observable.just("Hello world")
                .subscribe(textView::setText);
    }

    Observer<String> observable = new DisposableObserver<String>() {
        @Override
        public void onNext(String s) {
            textView.setText(s);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };


    private void accept(String o) {
        textView.setText(o);
    }
}