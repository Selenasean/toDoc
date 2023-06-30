package com.cleanup.todoc.ui.utils.injection;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.AppApplication;
import com.cleanup.todoc.data.AppDatabase;
import com.cleanup.todoc.data.repository.Repository;
import com.cleanup.todoc.ui.MainViewModel;

/**
 * class who will create a new ViewModel for a new activity/fragment
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private static volatile ViewModelFactory factory;

    /**
     * Get an instance of ViewModelFactory
     *
     * @return factory which is a ViewModelFactory
     */
    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory(new Repository(AppDatabase.getInstance(AppApplication.contextApp)));
                }
            }
        }
        return factory;
    }

     @NonNull
     private final Repository mRepository;
    /**
     * Constructor
     */
    private ViewModelFactory(Repository repository) {
        this.mRepository = repository;
    }

    /**
     * Creation of viewModels
     *
     * @param modelClass model of classes ViewModel
     * @param <T>        type of Class, here of ViewModel
     * @return new viewModel
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        // if modelClass - model of classes ViewModel - is the same as new ViewModel created = MainViewModel
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(mRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
