package com.app.tbd.ui.Model.Request;

import io.realm.RealmObject;

/**
 * Created by Dell on 3/7/2016.
 */
public class ManageFlightList extends RealmObject {

    private String cachedList;

    public String getCachedList() {
        return cachedList;
    }

    public void setCachedList(String cachedList) {
        this.cachedList = cachedList;
    }

}
