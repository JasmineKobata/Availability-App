package com.mygdx.game.SerializableObjects;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.ErrorFactory;
import com.mygdx.game.HttpUtils;
import com.mygdx.game.JsonFactory;
import com.mygdx.game.ServerResponse;
import com.mygdx.game.Vector2f;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class Event implements Serializable {
    int groupId;
    int eventId;
    String eventName;
    Vector2f<String> dateRange;

    public Event() {
        eventName = new String();
        dateRange =  new Vector2f<String>();
    }

    public int getEventId() {
        return eventId;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getEventName() {
        return eventName;
    }

    public Vector2f<String> getDateRange() {
        return dateRange;
    }

    public void editEventName(String newName) {
        eventName = newName;
    }

    public void editDateRange(Vector2f newDateRange) {
        dateRange = newDateRange;
    }
}
