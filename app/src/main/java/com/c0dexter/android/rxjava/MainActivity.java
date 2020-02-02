package com.c0dexter.android.rxjava;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

//    Creating an Observable from a single object
//
//    1. Instantiate the object to become an Observable.
//    2. Create the Observable.
//    3. Subscribe to the Observable and get the emitted object.


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //    1. Instantiate the object to become an Observable.
        //final Task task = new Task("Walk the dog", false, 3);

        //    2. Create the Observable.
        Observable<Task> taskObservable = Observable
                .create(new ObservableOnSubscribe<Task>() {
                    @Override
                    public void subscribe(ObservableEmitter<Task> emitter) throws Exception {

                        for (Task task : DataSource.createTasksList()) {
                            if (!emitter.isDisposed()) {
                                emitter.onNext(task);
                            }
                        }

                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }

                    }
                })
                .subscribeOn(Schedulers.io())   // Background thread
                .observeOn(AndroidSchedulers.mainThread());

        //    3. Subscribe to the Observable and get the emitted object
        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Task task) {
                Log.d(TAG, "onNext:" + task.getDescription());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
