package com.vagapov.amir.android3h3.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.vagapov.amir.android3h3.R;
import com.vagapov.amir.android3h3.model.ImageConverter;
import com.vagapov.amir.android3h3.presenter.InfoPresenter;
import com.vagapov.amir.android3h3.presenter.MyPresenter;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyActivity extends MvpViewStateActivity<InfoView, InfoPresenter> implements InfoView {

    private AlertDialog dialog;

    @BindView(R.id.btn_open_img)
    protected Button openImgBtn;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        createDialog();
    }

    @OnClick(R.id.btn_convert)
    void onClick() {
        convertImg();
    }

    @OnClick(R.id.btn_open_img)
    void OnClick(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setData(FileProvider.getUriForFile(this,
                this.getApplicationContext().getPackageName() + ".provider", file));
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private void createDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setCancelable(true).setTitle(R.string.dialogTitle).setView(R.layout.alert_dialog);
        dialog = dialogBuilder.setNegativeButton(R.string.cancel,
                (dialogInterface, i) ->{ presenter.unSubscribe();
                conversionFailure(null);
        }).create();
    }


    @NonNull
    @Override
    public InfoPresenter createPresenter() {
        return new MyPresenter(new ImageConverter(getApplicationContext()));
    }

    @Override
    public void conversionComplete() {
        MyActivityViewState vs = (MyActivityViewState) viewState;
        dialog.dismiss();
        Toast.makeText(MyActivity.this, R.string.conversion_succ, Toast.LENGTH_SHORT).show();
        vs.showingDialog(false);
        vs.showingButton(true);
        setButtonEnable(true);
        vs.showingFile(file);
    }

    @Override
    public void conversionFailure(Throwable e) {
        MyActivityViewState vs = (MyActivityViewState) viewState;
        vs.showingDialog(false);
        vs.showingButton(true);
        if(e != null) {
            Toast.makeText(MyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else {
            Toast.makeText(MyActivity.this, R.string.conversion_cancel, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void conversionRun() {
        MyActivityViewState vs = (MyActivityViewState) viewState;
        vs.showingDialog(true);
        vs.showingButton(true);
        Toast.makeText(MyActivity.this, R.string.conversion_run, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void convertImg() {
        dialog.show();
        presenter.startToConvert();
    }

    @Override
    public void createFile(File file) {
        this.file = file;
    }

    @Override
    public void setButtonEnable(Boolean b) {
        if(b){
            openImgBtn.setEnabled(true);
        }
    }

    @NonNull
    @Override
    public ViewState<InfoView> createViewState() {
        return new MyActivityViewState();
    }

    @Override
    public void onNewViewStateInstance() {
    }
}
