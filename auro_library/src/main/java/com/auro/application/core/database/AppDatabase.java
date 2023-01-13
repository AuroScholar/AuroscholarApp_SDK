package com.auro.application.core.database;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.auro.application.core.application.AuroApp;
import com.auro.application.home.data.model.CategoryQuestion;
import com.auro.application.teacher.data.datasource.database.TeacherModelDao;
import com.auro.application.teacher.data.model.common.DistrictDataModel;
import com.auro.application.teacher.data.model.common.StateDataModel;


@Database(entities = {CategoryQuestion.class, DistrictDataModel.class, StateDataModel.class}, version = 1,  exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    private static final String DB_NAME = "passengerhmi_database";
    public abstract TeacherModelDao teacherModelDao();

   // public abstract OutletModelDao outletModelDao();



    public static AppDatabase getAppDatabase() {
        if (instance == null) {
            instance = Room.databaseBuilder(AuroApp.getAppContext(), AppDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

}
