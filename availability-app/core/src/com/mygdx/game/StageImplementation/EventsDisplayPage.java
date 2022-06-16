package com.mygdx.game.StageImplementation;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.Function;
import com.mygdx.game.HttpUtils;
import com.mygdx.game.JsonFactory;
import com.mygdx.game.MariaDB;
import com.mygdx.game.ScrollFactory;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.SerializableObjects.Group;
import com.mygdx.game.SerializableObjects.Event;
import com.mygdx.game.ServerResponse;
import com.mygdx.game.StageControls;
import com.mygdx.game.Stages.FifthStage;
import com.mygdx.game.Stages.SecondStage;
import com.mygdx.game.Stages.SixthStage;
import com.mygdx.game.Stages.ThirdStage;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class EventsDisplayPage extends CreateEvents {

    public EventsDisplayPage() { }

    public EventsDisplayPage(Stage stage, StageControls stgCtrls, Client client, Group group) {

        Table greetingContainer = new Table();
        Label greetingLabel = bf.createTextLabel(group.getGroupName());
        greetingContainer.add(greetingLabel);
        greetingContainer.setPosition(uis.getWidth()/2, uis.getMenuPosition().y);
        stage.addActor(greetingContainer);

        Table occasionsTable = new Table();

        int numOfOccasions = 0;
        for (int id : group.getEventIds()) {
            Map<String, String> params = new HashMap<>();
            params.put("eventId", Integer.toString(id));
            params.put("groupId", Integer.toString(group.getGroupId()));
            ServerResponse response = HttpUtils.Request("event/get", params);
            if (response.statusCode == 200) {
                Event myEvent = JsonFactory.classFromJsonString(response.message, Event.class);
                TextButton occButton = bf.createCellButton(myEvent.getEventName());
                occasionsTable.add(occButton).width(uis.getDisplayLength()).height(uis.getCellLength());
                occasionsTable.row();

                occButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        stgCtrls.switchStage(new SixthStage(stgCtrls, client, group, myEvent));
                    }
                });

                numOfOccasions++;
            }
            else {
                errorLabel.setText(response.message);
                errorLabel.setVisible(true);
            }
        }

        Table scrollTable = ScrollFactory.createScrollTable(occasionsTable, numOfOccasions);

        stage.addActor(scrollTable);

        Table menuContainer = new Table();
        ImageButton menuButton = bf.createMenuButton();
        menuContainer.add(menuButton).width(uis.getMenuSizing().x).height(uis.getMenuSizing().y);
        menuContainer.setPosition(uis.getMenuPosition().x, uis.getMenuPosition().y);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                Table extendedMenuTable = new Table();

                Table clearContainer = bf.clearButton(extendedMenuTable);
                stage.addActor(clearContainer);

                TextButton newEventButton = addNewEventButton(stage, stgCtrls, extendedMenuTable, client, group);
                TextButton groupNameButton = addGroupNameButton(stage, stgCtrls, extendedMenuTable, client, group);
                TextButton groupInfoButton = addInfoButton(stage, extendedMenuTable, "Group",
                        "Group Name:\n" + group.getGroupName() + "\n\nGroup ID:\n" + group.getGroupId());
                TextButton leaveGroupButton = addLeaveGroupButton(stage, stgCtrls, extendedMenuTable, client, group);
                TextButton deleteGroupButton = addDeleteGroupButton(stage, stgCtrls, extendedMenuTable, client, group);

                extendedMenuTable.add(newEventButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);
                extendedMenuTable.row();
                extendedMenuTable.add(groupNameButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);
                extendedMenuTable.row();
                extendedMenuTable.add(groupInfoButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);
                extendedMenuTable.row();
                extendedMenuTable.add(leaveGroupButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);
                extendedMenuTable.row();
                extendedMenuTable.add(deleteGroupButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);

                extendedMenuTable.setPosition(uis.getExtMenuPosition(5).x, uis.getExtMenuPosition(5).y);
                stage.addActor(extendedMenuTable);
            }
        });

        addBackButton(stage, stgCtrls, new ThirdStage(stgCtrls, client));
        stage.addActor(menuContainer);
    }

    private TextButton addLeaveGroupButton(Stage stage, StageControls stgCtrls, Table extendedMenuTable, Client client, Group group) {
        TextButton leaveGroupButton = bf.createDarkCellButton("Leave Group");

        leaveGroupButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                extendedMenuTable.remove();
                extendedMenuTable.pack();
                final Table leaveContainer = bf.AreYouSureButton(stage, new Function<Integer, Integer>() {
                            @Override
                            public Integer function(Integer i) {
                                Map<String, String> params = new HashMap<>();
                                params.put("clientId", Integer.toString(client.getClientId()));
                                params.put("groupId", Integer.toString(group.getGroupId()));
                                ServerResponse response = HttpUtils.Request("client/leave", params);
                                if (response.statusCode == 200) {
                                    client.removeGroupId(group.getGroupId());
                                    JsonFactory.classToJsonFile(JsonFactory.clientFile, client);
                                    if (client.getGroupIds().isEmpty()) {
                                        stgCtrls.switchStage(new SecondStage(stgCtrls, client));
                                    } else {
                                        stgCtrls.switchStage(new ThirdStage(stgCtrls, client));
                                    }
                                }
                                else {
                                    errorLabel.setText(response.message);
                                    errorLabel.setVisible(true);
                                }

                                return i;
                            }
                        });

                leaveContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
                stage.addActor(leaveContainer);
            }
        });

        return leaveGroupButton;
    }

    private TextButton addDeleteGroupButton(Stage stage, StageControls stgCtrls, Table extendedMenuTable, Client client, Group group) {
        TextButton deleteGroupButton = bf.createDarkCellButton("Delete Group");

        deleteGroupButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                extendedMenuTable.remove();
                extendedMenuTable.pack();

                final Table deletionContainer = bf.AreYouSureButton(stage, new Function<Integer, Integer>() {
                            @Override
                            public Integer function(Integer i) {
                                Map<String, String> params = new HashMap<>();
                                params.put("groupId", Integer.toString(group.getGroupId()));
                                ServerResponse response = HttpUtils.Request("group/delete", params);
                                if (response.statusCode == 200) {
                                    client.removeGroupId(group.getGroupId());
                                    JsonFactory.classToJsonFile(JsonFactory.clientFile, client);
                                    if (client.getGroupIds().isEmpty()) {
                                        stgCtrls.switchStage(new SecondStage(stgCtrls, client));
                                    } else {
                                        stgCtrls.switchStage(new ThirdStage(stgCtrls, client));
                                    }
                                }
                                else {
                                    errorLabel.setText(response.message);
                                    errorLabel.setVisible(true);
                                }

                                return i;
                            }
                        }
                );

                deletionContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
                stage.addActor(deletionContainer);
            }
        });

        return deleteGroupButton;
    }

    public TextButton addGroupNameButton(Stage stage, StageControls stgCtrls, Table extendedMenuTable, Client client, Group group) {
        TextButton groupNameButton = bf.createDarkCellButton("Change Group Name");

        groupNameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                extendedMenuTable.remove();
                extendedMenuTable.pack();

                final Table clientNameContainer = addClearToSubmittableButtons(stage, "Enter New Group Name:", "", "");

                checkmarkButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Map<String, String> params = new HashMap<>();
                        params.put("groupName", newName);
                        params.put("groupId", Integer.toString(group.getGroupId()));
                        ServerResponse response = HttpUtils.Request("group/name", params);
                        if (response.statusCode == 200) {
                            group.changeGroupName(newName);
                            stgCtrls.switchStage(new FifthStage(stgCtrls, client, group));
                        }
                        else {
                            errorLabel.setText(response.message);
                            errorLabel.setVisible(true);
                        }
                    }
                });

                clientNameContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
                stage.addActor(clientNameContainer);
            }
        });

        return groupNameButton;
    }

    private TextButton addNewEventButton(Stage stage, StageControls stgCtrls, Table extendedMenuTable, Client client, Group group) {
        TextButton newEventButton = bf.createDarkCellButton("Create New Event");

        newEventButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                extendedMenuTable.remove();
                extendedMenuTable.pack();

                addNewEventButtons(stage, stgCtrls, client, group);
            }
        });
        return newEventButton;
    }
}
