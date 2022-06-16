package com.mygdx.game.StageImplementation;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.ButtonFactory;
import com.mygdx.game.Colors;
import com.mygdx.game.DateFactory;
import com.mygdx.game.Function;
import com.mygdx.game.HttpUtils;
import com.mygdx.game.JsonFactory;
import com.mygdx.game.MariaDB;
import com.mygdx.game.RectCalendarFactory;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.SerializableObjects.Group;
import com.mygdx.game.SerializableObjects.Event;
import com.mygdx.game.ServerResponse;
import com.mygdx.game.StageControls;
import com.mygdx.game.Stages.FifthStage;
import com.mygdx.game.Stages.SecondStage;
import com.mygdx.game.Stages.ThirdStage;
import com.mygdx.game.UISpacing;
import com.mygdx.game.Vector2f;

import java.sql.Statement;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateEvents extends GroupsDisplayPage {
    Vector2f<String> dateRange = new Vector2f(new String(), new String());
    UISpacing uis = new UISpacing();
    ButtonFactory bf = new ButtonFactory();
    RectCalendarFactory rcf = new RectCalendarFactory();
    ImageButton blankButton = bf.createBlankButton();

    public CreateEvents() {}

    public CreateEvents(final Stage stage, final StageControls stgCtrls, Client client, final Group group) {

        dateRange.first = new String();
        dateRange.second = new String();
        addBackButton(stage, stgCtrls, new ThirdStage(stgCtrls, client));

        final Table table = new Table();

        TextButton newEventButton = bf.createCellButton("Create New Event");
        table.add(newEventButton).width(uis.getTabLength()).height(uis.getCellLength());
        table.row();

        newEventButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y)  {
                addNewEventButtons(stage, stgCtrls, client, group);
            }
        });

        table.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
        stage.addActor(table);

        Table deletionContainer = new Table();
        Label deleteGroupButton = bf.createTextLabel("Delete Group", Colors.midGreen);
        deletionContainer.add(deleteGroupButton);
        deletionContainer.setPosition(uis.getWidth()*6/7, uis.getHeight() / 10);
        stage.addActor(deletionContainer);

        deleteGroupButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y)  {
                Map<String, String> params = new HashMap<>();
                params.put("groupId", Integer.toString(group.getGroupId()));
                ServerResponse response = HttpUtils.Request("group/delete", params);
                if (response.statusCode == 200) {
                    client.removeGroupId(group.getGroupId());
                    JsonFactory.classToJsonFile(JsonFactory.clientFile, client);
                    if (client.getGroupIds().isEmpty()) {
                        stgCtrls.switchStage(new SecondStage(stgCtrls, client));
                    }
                    else {
                        stgCtrls.switchStage(new ThirdStage(stgCtrls, client));
                    }
                }
                else {
                    errorLabel.setText(response.message);
                    errorLabel.setVisible(true);
                }
            }
        });
    }

    public void addBackButton(Stage stage, StageControls stgCtrls, Stage newStage) {
        Table backContainer = new Table();
        Label backButton = bf.createTextLabel("<");
        backContainer.add(backButton).width(uis.getCellLength()).height(uis.getCellLength());;
        backContainer.setPosition(uis.getBackPosition().x, uis.getBackPosition().y);
        stage.addActor(backContainer);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y)  {
                stgCtrls.switchStage(newStage);
            }
        });
    }

    public void addNewEventButtons(final Stage stage, final StageControls stgCtrls, final Client client, final Group group) {
        Table newEventContainer = addClearToSubmittableButtons(stage, "Enter New Event Name:", newName, "");

        if (newName.isEmpty()) {
            checkmarkButton.setVisible(false);
        }
        else {
            checkmarkButton.setVisible(true);
        }

        checkmarkButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y)  {
                checkmarkButton = addDateButtons(stage);

                checkmarkButton.addListener(new ClickListener() {
                    @Override
                    public void clicked (InputEvent event, float x, float y)  {
                        Map<String, String> params = new HashMap<>();
                        params.put("eventName", newName);
                        params.put("groupId", Integer.toString(group.getGroupId()));
                        params.put("startDate", dateRange.first);
                        params.put("endDate", dateRange.second);
                        ServerResponse response = HttpUtils.Request("event/create", params);
                        if (response.statusCode == 200) {
                            Event myEvent = JsonFactory.classFromJsonString(response.message, Event.class);
                            group.addEventId(myEvent.getEventId());
                            stgCtrls.switchStage(new FifthStage(stgCtrls, client, group));
                        }
                        else {
                            errorLabel.setText(response.message);
                            errorLabel.setVisible(true);
                        }
                    }
                });
            }
        });

        newEventContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
        stage.addActor(newEventContainer);
    }

    public ImageButton addDateButtons(final Stage stage) {
        final Table datesContainer = new Table();

        Table clearContainer = bf.clearButton(datesContainer);
        stage.addActor(clearContainer);

        datesContainer.add(blankButton).width(uis.getCellLength()).height(uis.getCellLength());

        TextButton dateLabel = bf.createCellButton("Starting Date Range");
        datesContainer.add(dateLabel).width(uis.getTabLength()).height(uis.getCellLength());

        dateLabel = bf.createCellButton("Ending Date Range");
        datesContainer.add(dateLabel).width(uis.getTabLength()).height(uis.getCellLength());
        datesContainer.row();

        datesContainer.add(blankButton).width(uis.getCellLength()).height(uis.getCellLength());

        TextButton enterStartDate = bf.createDarkCellButton(dateRange.first);
        datesContainer.add(enterStartDate).width(uis.getTabLength()).height(uis.getCellLength());

        TextButton enterEndDate = bf.createDarkCellButton(dateRange.second);
        datesContainer.add(enterEndDate).width(uis.getTabLength()).height(uis.getCellLength());

        ImageButton checkmarkButton = bf.createCheckmarkButton();
        datesContainer.add(checkmarkButton).width(uis.getCellLength()).height(uis.getCellLength());
        checkmarkButton.setVisible(false);

        enterStartDate.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                Table calendarContainer = new Table();

                Table clearContainer = bf.clearButton(calendarContainer);
                stage.addActor(clearContainer);
                calendarContainer.add(
                        rcf.instantiateCalendar(stage, clearContainer, new Function<Date, Date>() {
                                    @Override
                                    public Date function(Date date) {
                                        dateRange.first = DateFactory.dateToSixDigString(date);
                                        enterStartDate.setText(dateRange.first);

                                        try {
                                            if (!dateRange.second.isEmpty() && DateFactory.sixDigStringToDate(dateRange.second).after(date)) {
                                                checkmarkButton.setVisible(true);
                                            }
                                            else {
                                                checkmarkButton.setVisible(false);
                                            }
                                        }
                                        catch (ParseException e) {}
                                        return date;
                                    }
                                }
                        )
                );

                calendarContainer.setPosition(uis.getWidth()/2, uis.getHeight()/2);
                stage.addActor(calendarContainer);
            }
        });

        enterEndDate.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                Table calendarContainer = new Table();

                Table clearContainer = bf.clearButton(calendarContainer);
                stage.addActor(clearContainer);
                calendarContainer.add(
                        rcf.instantiateCalendar(stage, clearContainer, new Function<Date, Date>() {
                                    @Override
                                    public Date function(Date date) {
                                        dateRange.second = DateFactory.dateToSixDigString(date);
                                        enterEndDate.setText(dateRange.second);

                                        try {
                                            if (!dateRange.first.isEmpty() && date.after(DateFactory.sixDigStringToDate(dateRange.first))) {
                                                checkmarkButton.setVisible(true);
                                            }
                                            else {
                                                checkmarkButton.setVisible(false);
                                            }
                                        }
                                        catch (ParseException e) {

                                        }
                                        return date;
                                    }
                                }
                        )
                );

                calendarContainer.setPosition(uis.getWidth()/2, uis.getHeight()/2);
                stage.addActor(calendarContainer);
            }
        });
        datesContainer.setPosition(uis.getWidth()/2, uis.getHeight()/2);
        stage.addActor(datesContainer);
        return checkmarkButton;
    }
}
