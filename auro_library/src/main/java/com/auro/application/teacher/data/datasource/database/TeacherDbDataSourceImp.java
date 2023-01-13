package com.auro.application.teacher.data.datasource.database;

import com.auro.application.core.database.DBHolder;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;
import com.auro.application.teacher.data.repository.TeacherRepo;

import java.util.List;

import io.reactivex.Single;

public class TeacherDbDataSourceImp implements TeacherRepo.TeacherDbData {

    @Override
    public Single<Long[]> insertStateList(List<StateDataModel> stateDataModelList) {
        Single<Long[]> single = Single.create(emitter -> {
            try {
                emitter.onSuccess(DBHolder.getDB().teacherModelDao().insertStateListData(stateDataModelList));
            } catch (Exception e) {

                emitter.onError(e);
            }
        });

        return single;
    }

    @Override
    public Single<Long[]> insertDistrictList(List<DistrictDataModel> stateDataModelList) {
        Single<Long[]> single = Single.create(emitter -> {
            try {
                emitter.onSuccess(DBHolder.getDB().teacherModelDao().insertDistrictListData(stateDataModelList));
            } catch (Exception e) {

                emitter.onError(e);
            }
        });

        return single;
    }

    @Override
    public Single<List<StateDataModel>> getStateList() {
        Single<List<StateDataModel>> single = Single.create(emitter -> {
            try {
                emitter.onSuccess(DBHolder.getDB().teacherModelDao().getAllStateData());
            } catch (Exception e) {

                emitter.onError(e);
            }
        });

        return single;
    }

    @Override
    public Single<List<DistrictDataModel>> getDistrictList() {
        Single<List<DistrictDataModel>> single = Single.create(emitter -> {
            try {
                emitter.onSuccess(DBHolder.getDB().teacherModelDao().getAllDistrictData());
            } catch (Exception e) {

                emitter.onError(e);
            }
        });

        return single;
    }

    @Override
    public Single<List<DistrictDataModel>> getStateDistrictList(String stateCode) {
        Single<List<DistrictDataModel>> single = Single.create(emitter -> {
            try {
                emitter.onSuccess(DBHolder.getDB().teacherModelDao().getStateDistrictData(stateCode));
            } catch (Exception e) {

                emitter.onError(e);
            }
        });

        return single;
    }
}
