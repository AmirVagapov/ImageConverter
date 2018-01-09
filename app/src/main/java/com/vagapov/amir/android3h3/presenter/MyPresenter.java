package com.vagapov.amir.android3h3.presenter;


import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.vagapov.amir.android3h3.model.ImageConverter;
import com.vagapov.amir.android3h3.view.InfoView;

import java.io.File;

import rx.Subscriber;
import rx.Subscription;


public class MyPresenter extends MvpBasePresenter<InfoView> implements InfoPresenter{

    private Subscription subscription;

    private ImageConverter converter;

    public MyPresenter(@NonNull ImageConverter converter){
        this.converter = converter;
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        unSubscribe();
    }

    @UiThread
    @Override
    public void startToConvert() {
        subscription = converter.convertImage().subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {
                        getView().conversionComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().conversionFailure(e);
                    }

                    @Override
                    public void onNext(Object b) {
                        getView().conversionRun();
                        getView().createFile((File) b);
                    }
                });
    }

    @Override
    public void unSubscribe() {
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
            subscription = null;
        }
    }


}
