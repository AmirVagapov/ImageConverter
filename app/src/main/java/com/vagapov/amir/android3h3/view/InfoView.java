package com.vagapov.amir.android3h3.view;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.io.File;


public interface InfoView extends MvpView {

    void conversionComplete();

    void conversionFailure(Throwable e);

    void conversionRun();

    void convertImg();

    void createFile(File file);

    void setButtonEnable(Boolean b);
}
