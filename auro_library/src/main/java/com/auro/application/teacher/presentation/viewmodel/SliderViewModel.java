package com.auro.application.teacher.presentation.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.auro.application.core.common.ResponseApi;
import com.auro.application.teacher.domain.TeacherDbUseCase;
import com.auro.application.teacher.domain.TeacherRemoteUseCase;
import com.auro.application.teacher.domain.TeacherUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class SliderViewModel extends ViewModel {


    CompositeDisposable compositeDisposable;


    public TeacherUseCase teacherUseCase;
    TeacherRemoteUseCase teacherRemoteUseCase;
    TeacherDbUseCase teacherDbUseCase;

    public SliderViewModel(TeacherUseCase teacherUseCase, TeacherDbUseCase teacherDbUseCase, TeacherRemoteUseCase teacherRemoteUseCase) {
        this.teacherDbUseCase = teacherDbUseCase;
        this.teacherUseCase = teacherUseCase;
        this.teacherRemoteUseCase = teacherRemoteUseCase;
    }


    private final MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private final LiveData<Integer> mPagerIndex =
            Transformations.map(mIndex, new Function<Integer, Integer>() {
                @Override
                public Integer apply(Integer input) {
                    return input - 1;
                }
            });
    public void setIndex(int index) {
        mIndex.setValue(index);
    }
    public LiveData<Integer> getText() {
        return mPagerIndex;
    }
}
