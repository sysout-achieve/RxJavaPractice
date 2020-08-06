package com.gun.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class DebounceSearchActivity extends AppCompatActivity {
    ListView mLogView;
    EditText mSearchBox;

    Disposable mDisposable;
    private LogAdapter mLogAdapter;
    private List<String> mLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debounce_search);
        mLogView = findViewById(R.id.dsf_lv_log);
        mSearchBox = findViewById(R.id.dsf_input_deb_search);
        setupLogger();

        mDisposable = getObservable()
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(s->!TextUtils.isEmpty(s))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    private Observable<CharSequence> getObservable() {
        return Observable.create(emitter-> mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emitter.onNext(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }));
    }
    private DisposableObserver<CharSequence> getObserver(){
        return new DisposableObserver<CharSequence>() {
            @Override
            public void onNext(@NonNull CharSequence charSequence) {
                log("search "+ charSequence.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
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
}