package com.gun.rxjavapractice;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Scanner;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;

public class ReactiveSum {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void run() {
        ConnectableObservable<String> source = userInput();
        Observable<Integer> a = source
                .filter(str -> str.startsWith("a:"))
                .map(str -> str.replace("a:", ""))
                .map(Integer::parseInt);
        Observable<Integer> b = source
                .filter(str-> str.startsWith("b:"))
                .map(str -> str.replace("b:",""))
                .map(Integer::parseInt);
        Observable.combineLatest(
                a.startWith(0),
                b.startWith(0),
                Integer::sum).subscribe(res-> System.out.println("Result : "+ res));
        source.connect();
    }

    private ConnectableObservable<String> userInput() {
        return Observable.create((ObservableEmitter<String> emitter) -> {
            Scanner in = new Scanner(System.in);
            while (true) {
                System.out.println("Input : ");
                String line = in.nextLine();
                emitter.onNext(line);
                if (line.indexOf("exit") >= 0) {
                    in.close();
                    break;
                }
            }
        }).publish();
    }
}
