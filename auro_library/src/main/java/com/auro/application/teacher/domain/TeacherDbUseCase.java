package com.auro.application.teacher.domain;

import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;
import com.auro.application.teacher.data.repository.TeacherRepo;

import java.util.List;

import io.reactivex.Single;

public class TeacherDbUseCase {

    TeacherRepo.TeacherDbData teacherDbData;

    public TeacherDbUseCase(TeacherRepo.TeacherDbData teacherDbData) {
        this.teacherDbData = teacherDbData;
    }


    public Single<Long[]> insertStateList(List<com.auro.application.teacher.data.model.common.StateDataModel> list) {
        return teacherDbData.insertStateList(list);
    }

    public Single<List<StateDataModel>> getStateList() {
        return teacherDbData.getStateList();
    }

    public Single<Long[]> insertDistrictList(List<com.auro.application.teacher.data.model.common.DistrictDataModel> list) {
        return teacherDbData.insertDistrictList(list);
    }

    public Single<List<com.auro.application.teacher.data.model.common.DistrictDataModel>> getDistrictList() {
        return teacherDbData.getDistrictList();
    }

    public Single<List<DistrictDataModel>> getDistrictList(String stateCode) {
        return teacherDbData.getStateDistrictList(stateCode);
    }

}
