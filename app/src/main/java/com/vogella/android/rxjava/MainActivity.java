package com.vogella.android.rxjava;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    //ui
    private TextView text;
    private String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);

//        This is a very basic example.
//        But it showcases the general structure of every Observable / Observer interaction.
//
//        1. Create an Observable
//        2. Apply an operator to the Observable
//        3. Designate what thread to do the work on and what thread to emit the results to
//        4. Subscribe an Observer to the Observable and view the results

        Observable<Task> taskObservable = Observable        // Create observable
                .fromIterable(DataSource.createTasksList()) // Apply operator to the Observable
                .subscribeOn(Schedulers.io())               // Subscribe on a tread
                .filter(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Exception {              // (another operator if we want)
                        Log.d(TAG, "Test: " + Thread.currentThread().getName());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return task.isComplete();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread()); // Describe where you observe on


        taskObservable.subscribe(new Observer<Task>() {     // Subscribe observers which will view the results on a the designated thread as observe thread
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: called.");
            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext: " + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: " + e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: called");
            }
        });
    }
}
