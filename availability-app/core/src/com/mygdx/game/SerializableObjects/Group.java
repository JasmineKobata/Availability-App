package com.mygdx.game.SerializableObjects;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.ErrorFactory;
import com.mygdx.game.JsonFactory;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Random;

public class Group implements Serializable {
    int groupId;
    String groupName;
    HashSet<Integer> clientIds;
    HashSet<Integer> eventIds;

    public Group() {
        groupName = new String();
        clientIds = new HashSet<Integer>();
        eventIds = new HashSet<Integer>();
    }

    public void removeEventId(int id) {
        eventIds.remove(id);
    }

    public void removeClientId(int id) { clientIds.remove(id); }

    public String getGroupName() {
        return groupName;
    }

    public void changeGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public HashSet<Integer> getClientIds() {
        return clientIds;
    }

    public HashSet<Integer> getEventIds() {
        return eventIds;
    }

    public boolean containsEventId(int id) {
        return eventIds.contains(id);
    }

    public void addClientId(int id) {
        clientIds.add(id);
    }

    public void addEventId(int id) {
        eventIds.add(id);
    }
}
