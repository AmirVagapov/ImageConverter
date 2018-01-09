package com.vagapov.amir.android3h3.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.vagapov.amir.android3h3.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import rx.Observable;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ImageConverter implements InfoModel{

    private static final String IMG_NAME = "newImage.png";

    private Context context;

    public  ImageConverter (@NonNull Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public Observable convertImage(){

        return Observable.create((Observable.OnSubscribe<Bitmap>) subscriber -> {

            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                try {
                    throw new Exception(context.getString(R.string.unavailable_storage));
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.test_img);
            subscriber.onNext(bitmap);

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            subscriber.onCompleted();

        }).subscribeOn(Schedulers.io()).map(bitmap -> {

            File file = new File(Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    , IMG_NAME);
            try {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);

                     bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                } finally {
                    if (fos != null) fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file;
        }).observeOn(AndroidSchedulers.mainThread());
    }
}
