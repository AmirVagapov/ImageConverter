package com.vagapov.amir.android3h3.presenter;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.vagapov.amir.android3h3.view.InfoView;


public interface InfoPresenter extends MvpPresenter<InfoView> {

    void startToConvert();

    void unSubscribe();
}
