package com.nedvedd.projectq.controller;

import com.nedvedd.projectq.model.DataModel;

/**
 * Abstraktni trida pro controllery
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public abstract class AController {

    /** Atribut datoveho modelu */
    protected DataModel dataModel;

    /**
     * Metoda, ktera nacte jedinou instanci datoveho modelu.
     * Slouzi pro sdileni teto instance mezi vsemi controllery pro jednoduchou manipulaci s daty.
     *
     * @param dataModel instance datoveho modelu
     */
    public void initModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model already initialized");
        }
        this.dataModel = dataModel ;
    }
}
