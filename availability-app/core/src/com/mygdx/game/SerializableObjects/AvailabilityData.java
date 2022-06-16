package com.mygdx.game.SerializableObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.ButtonFactory;
import com.mygdx.game.HttpUtils;
import com.mygdx.game.JsonFactory;
import com.mygdx.game.ServerResponse;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class AvailabilityData implements Serializable {
    public int clientId;
    public int eventId;
    HashSet<String> checkedDates;

    public AvailabilityData() {
        checkedDates = new HashSet<String>();
    }

    public static AvailabilityData getAvailabilityData(int clientId, int eventId) {
        Map<String, String> params = new HashMap<>();
        params.put("clientId", Integer.toString(clientId));
        params.put("eventId", Integer.toString(eventId));
        ServerResponse response = HttpUtils.Request("availability/get", params);
        AvailabilityData myAvailData = new AvailabilityData();
        if (response.statusCode == 200) {
            myAvailData = JsonFactory.classFromJsonString(response.message, AvailabilityData.class);
        }
        return myAvailData;
    }

    public void updateAvailabilityData(String date) {
        if (!checkedDates.contains(date)) {
            checkedDates.add(date);
        }
        else {
            checkedDates.remove(date);
        }
    }

    public boolean containsDate(String date) {
        return checkedDates.contains(date);
    }

    public HashSet<String> getCheckedDates() {
        return checkedDates;
    }
}
