package com.vagapov.amir.android3h3.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby.mvp.viewstate.RestorableViewState;
import java.io.File;


public class MyActivityViewState implements RestorableViewState<InfoView> {

    private final String STATE_LOADING = "MyActivityViewStateLoading";
    private final String STATE_OPEN_BUTTON = "MyActivityViewStateButton";
    private final String STATE_FILE = "MyActivityViewStateFile";
    private Boolean showDialog = false;
    private Boolean isFileCreate = false;
    private File file;


    @Override
    public void saveInstanceState(@NonNull Bundle out) {
        out.putBoolean(STATE_LOADING, showDialog);
        out.putBoolean(STATE_OPEN_BUTTON, isFileCreate);
        out.putSerializable(STATE_FILE, file);
    }

    @Override
    public RestorableViewState<InfoView> restoreInstanceState(Bundle in) {

        if(in == null) {
            return null;
        }
        showDialog = in.getBoolean(STATE_LOADING);
        isFileCreate = in.getBoolean(STATE_OPEN_BUTTON);
        file = (File) in.getSerializable(STATE_FILE);
        return MyActivityViewState.this;
    }

    @Override
    public void apply(InfoView view, boolean retained) {
        if (showDialog){
            view.convertImg();
        }
        if(isFileCreate){
            view.setButtonEnable(true);
            view.createFile(file);
        }

    }

    void showingDialog(boolean b){
        showDialog = b;
    }

    void showingButton(boolean b){
        isFileCreate = b;
    }

    void showingFile(File file){
        this.file = file;
    }


}
