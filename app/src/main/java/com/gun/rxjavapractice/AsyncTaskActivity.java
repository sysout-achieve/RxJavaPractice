package com.gun.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class AsyncTaskActivity extends AppCompatActivity {

    MyAsyncTask myAsyncTask;
    TextView mAndroidTxtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        mAndroidTxtView = findViewById(R.id.mAndroidTxtView);
        initAndroidAsync();
    }

    private void initAndroidAsync(){
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("Hello","async", "world");
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