package com.mygdx.game.SerializableObjects;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.ErrorFactory;
import com.mygdx.game.JsonFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Random;

public class Client {
    int clientId;
    String clientName;
    HashSet<Integer> groupIds;

    public Client() {
        clientName = new String();
        groupIds = new HashSet<Integer>();
    }

    public void addGroupId(int id) {
        groupIds.add(id);
    }

    public void removeGroupId(int id) {
        groupIds.remove(id);
    }

    public void changeClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getClientId() {
        return clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public HashSet<Integer> getGroupIds() {
        return groupIds;
    }

    public boolean containsGroupId(int id) {
        return groupIds.contains(id);
    }

    public void deleteClient() {
        JsonFactory.clientFile.delete();
    }
}
