package com.mygdx.game.StageImplementation;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.*;
import com.mygdx.game.SerializableObjects.AvailabilityData;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.SerializableObjects.Group;
import com.mygdx.game.SerializableObjects.Event;
import com.mygdx.game.Stages.FifthStage;

import java.text.ParseException;
import java.util.*;

public class EventTableGeneration {
    TextButton button;
    CalendarSubmitButton cf = new CalendarSubmitButton();
    UISpacing uis = new UISpacing();
    ButtonFactory bf = new ButtonFactory();

    public EventTableGeneration(Stage stage, StageControls stgCtrls, Client client, Group group, Event myEvent) {
        Table calendarTable = new Table();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Label yearButton = bf.createTextLabel("\n"+myEvent.getEventName()+"\n\n"+calendar.get(Calendar.YEAR));
        calendarTable.add(yearButton).width(uis.getCellLength()).height(uis.getCellLength());

        Map<String, String> params = new HashMap<>();
        params.put("groupId", Integer.toString(group.getGroupId()));
        params.put("clientId", Integer.toString(client.getClientId()));
        ServerResponse response = HttpUtils.Request("group/clients", params);
        List<Client> clientList = JsonFactory.listFromJsonString(response.message);

        params = new HashMap<>();
        params.put("groupId", Integer.toString(group.getGroupId()));
        params.put("eventId", Integer.toString(myEvent.getEventId()));
        response = HttpUtils.Request("event/availability", params);
        HashMap<Integer, AvailabilityData> availList = JsonFactory.mapFromJsonString(response.message);

        Table temp;
        for (Client otherClient : clientList) {
            button = bf.createUserTab(otherClient.getClientName());
            button.setTransform(true);
            button.setOrigin((int) (uis.getTabLength() / 2), uis.getCellLength() / 2);
            button.setRotation(90f);
            temp = new Table();
            temp.add(button);
            calendarTable.add(temp).width(uis.getCellLength()).height(uis.getTabLength());
        }
        calendarTable.row();

        Vector2f<String> rangeStr = myEvent.getDateRange();
        Vector2f<Date> dateRange = new Vector2f();

        try {
            dateRange.start = DateFactory.sixDigStringToDate(rangeStr.first);
            dateRange.end = DateFactory.sixDigStringToDate(rangeStr.second);
        }
        catch (ParseException e) {}

        int dateRangeLen = DateFactory.getDateRangeLength(dateRange);

        AvailabilityData myAvailData = AvailabilityData.getAvailabilityData(client.getClientId(), myEvent.getEventId());
        for (int i=0; i<dateRangeLen; i++) {
            String newDate = DateFactory.dateToString(DateFactory.offsetDate(dateRange.start, i));
            button = bf.createDateTab(newDate);
            calendarTable.add(button).width(uis.getTabLength()).height(uis.getCellLength());
            for (Client otherClient : clientList) {
                TextButton cellButton = bf.createCheckableButton("");

                if (otherClient.getClientId() == client.getClientId()) {
                    cf.createSubmitButton(stage, cellButton, client, myEvent, myAvailData, newDate);
                }
                else {
                    cellButton.setDisabled(true);
                }

                if (availList.get(otherClient.getClientId()).containsDate(newDate)){
                    cellButton.setChecked(true);
                } else {
                    cellButton.setChecked(false);
                }
                calendarTable.add(cellButton).width(uis.getCellLength()).height(uis.getCellLength());
            }
            calendarTable.row();
        }

        calendarTable.setTransform(true);
        calendarTable.pack();
        Table scrollTable = ScrollFactory.createScrollTable(calendarTable);

        stage.addActor(scrollTable);

        new Menu(stage, stgCtrls, client, group, myEvent);

        Table backContainer = createBackButton(stgCtrls, client, group);
        stage.addActor(backContainer);
    }

    public Table createBackButton(StageControls stgCtrls, Client client, Group group) {
        Table backContainer = new Table();
        Label backButton = bf.createTextLabel("<");
        backContainer.add(backButton).width(uis.getCellLength()).height(uis.getCellLength());;
        backContainer.setPosition(uis.getBackPosition().x, uis.getBackPosition().y);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y)  {
                stgCtrls.switchStage(new FifthStage(stgCtrls, client, group));
            }
        });

        return backContainer;
    }
}
