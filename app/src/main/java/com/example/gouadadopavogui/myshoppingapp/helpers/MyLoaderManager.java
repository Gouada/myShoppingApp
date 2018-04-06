package com.example.gouadadopavogui.myshoppingapp.helpers;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;

import java.io.FileDescriptor;
import java.io.PrintWriter;

/**
 * Created by gouadadopavogui on 12.03.2017.
 */

//public  abstract class MyLoaderManager<M> extends LoaderManager implements LoaderManager.LoaderCallbacks{
public  abstract class MyLoaderManager implements LoaderManager.LoaderCallbacks {
    //private M product;

    public MyLoaderManager()
    {
       //this.product = product;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        onDataSouceUpdated(id, args);
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        onLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
/**
    @Override
    public <D> Loader<D> initLoader(int id, Bundle args, LoaderCallbacks<D> callback) {
        return null;
    }

    @Override
    public <D> Loader<D> restartLoader(int id, Bundle args, LoaderCallbacks<D> callback) {
        return null;
    }

    @Override
    public void destroyLoader(int id) {

    }

    @Override
    public <D> Loader<D> getLoader(int id) {
        return null;
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

    }
    */

    public abstract void onDataSouceUpdated(int id, Bundle args);

    public abstract void onLoadFinished(Object data);
}
