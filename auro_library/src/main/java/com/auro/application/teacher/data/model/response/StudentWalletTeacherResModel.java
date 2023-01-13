package com.auro.application.teacher.data.model.response;

import android.graphics.drawable.Drawable;

public class StudentWalletTeacherResModel {

    private String totalValue;
    private String nameOfDocument;
    private Drawable drawable;


    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public String getNameOfDocument() {
        return nameOfDocument;
    }

    public void setNameOfDocument(String nameOfDocument) {
        this.nameOfDocument = nameOfDocument;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
