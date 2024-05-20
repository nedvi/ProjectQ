package com.nedvedd.projectq.controller;

import com.nedvedd.projectq.data.DataModel;

public abstract class AController {

    protected DataModel dataModel;

    public void initModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model already initialized");
        }
        this.dataModel = dataModel ;
    }
}
