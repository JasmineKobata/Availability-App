package com.mygdx.game.StageImplementation;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.*;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.SerializableObjects.Group;
import com.mygdx.game.Stages.*;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class GroupsDisplayPage extends CreateGroups {

    public GroupsDisplayPage() {}

    public GroupsDisplayPage(final Stage stage, final StageControls stgCtrls, Client client) {

        Table greetingContainer = new Table();
        Label greetingLabel = bf.createTextLabel("Hi " + client.getClientName() + " :)");
        greetingContainer.add(greetingLabel);
        greetingContainer.setPosition(uis.getWidth()/2 + uis.getCellLength()*0.95f - uis.getDisplayLength()/2,
                uis.getMenuPosition().y);

        Table groupsTable = new Table();

        int numOfGroups = 0;

        for (int id : client.getGroupIds()) {

            Map<String, String> params = new HashMap<>();
            params.put("groupId", Integer.toString(id));
            ServerResponse response = HttpUtils.Request("group/get", params);
            if (response.statusCode == 200) {

                Group group = JsonFactory.classFromJsonString(response.message, Group.class);

                TextButton groupButton = bf.createCellButton(group.getGroupName());
                groupsTable.add(groupButton).width(uis.getDisplayLength()).height(uis.getCellLength());
                groupsTable.row();

                groupButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        //if event exists in clicked group go to FifthStage, else FourthStage
                        if (group.getEventIds().isEmpty()) {
                            stgCtrls.switchStage(new FourthStage(stgCtrls, client, group));
                        } else {
                            stgCtrls.switchStage(new FifthStage(stgCtrls, client, group));
                        }
                    }
                });

                numOfGroups++;
            }
            else {
                errorLabel.setText(response.message);
                errorLabel.setVisible(true);
            }
        }

        Table scrollTable = ScrollFactory.createScrollTable(groupsTable, numOfGroups);

        Table menuContainer = new Table();
        ImageButton menuButton = bf.createMenuButton();
        menuContainer.add(menuButton).width(uis.getMenuSizing().x).height(uis.getMenuSizing().y);
        menuContainer.setPosition(uis.getMenuPosition().x, uis.getMenuPosition().y);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                Table extendedMenuTable = new Table();

                new ClearButton(stage, extendedMenuTable);

                TextButton newGroupButton = addNewGroupButton(stage, stgCtrls, extendedMenuTable, client);
                TextButton joinGroupButton = addJoinGroupButton(stage, stgCtrls, extendedMenuTable, client);
                TextButton clientNameButton = addClientNameButton(stage, stgCtrls, extendedMenuTable, client);
                TextButton clientInfoButton = addInfoButton(stage, extendedMenuTable, "User",
                        "Username:\n" + client.getClientName() + "\n\nUser ID:\n" + client.getClientId());
                TextButton deleteClientButton = addDeleteClient(stage, stgCtrls, extendedMenuTable, client);

                extendedMenuTable.add(newGroupButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);
                extendedMenuTable.row();
                extendedMenuTable.add(joinGroupButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);
                extendedMenuTable.row();
                extendedMenuTable.add(clientNameButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);
                extendedMenuTable.row();
                extendedMenuTable.add(clientInfoButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);
                extendedMenuTable.row();
                extendedMenuTable.add(deleteClientButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);

                extendedMenuTable.setPosition(uis.getExtMenuPosition(5).x, uis.getExtMenuPosition(5).y);
                stage.addActor(extendedMenuTable);
            }
        });

        stage.addActor(greetingContainer);
        stage.addActor(scrollTable);
        stage.addActor(menuContainer);
    }

    private TextButton addJoinGroupButton(Stage stage, StageControls stgCtrls, Table extendedMenuTable, Client client) {
        TextButton joinGroupButton = bf.createDarkCellButton("Join Group");

        joinGroupButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                extendedMenuTable.remove();
                extendedMenuTable.pack();

                Table newGroupContainer = addClearToSubmittableButtons(stage, "Enter Group ID Number:", "","");
                addCheckmarkJoinGroupListener(stgCtrls, client);

                newGroupContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
                stage.addActor(newGroupContainer);
            }
        });
        return joinGroupButton;
    }

    public TextButton addNewGroupButton(final Stage stage, final StageControls stgCtrls, final Table extendedMenuTable, Client client) {
        TextButton newGroupButton = bf.createDarkCellButton("Create New Group");

        newGroupButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                extendedMenuTable.remove();
                extendedMenuTable.pack();

                Table newGroupContainer = addClearToSubmittableButtons(stage, "Enter New Group Name:", "", "");
                addCheckmarkNewGroupListener(stgCtrls, client);

                newGroupContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
                stage.addActor(newGroupContainer);
            }
        });

        return newGroupButton;
    }

    public TextButton addClientNameButton(final Stage stage, final StageControls stgCtrls, final Table extendedMenuTable, final Client client) {
        TextButton clientNameButton = bf.createDarkCellButton("Change Username");

        clientNameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                extendedMenuTable.remove();
                extendedMenuTable.pack();

                final Table clientNameContainer = addClearToSubmittableButtons(stage, "Enter New Username:", "", "");

                checkmarkButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Map<String, String> params = new HashMap<>();
                        params.put("clientName", newName);
                        params.put("clientId", Integer.toString(client.getClientId()));
                        ServerResponse response = HttpUtils.Request("client/name", params);
                        if (response.statusCode == 200) {
                            client.changeClientName(newName);
                            JsonFactory.classToJsonFile(JsonFactory.clientFile, client);
                            stgCtrls.switchStage(new ThirdStage(stgCtrls, client));
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

        return clientNameButton;
    }

    public TextButton addInfoButton(final Stage stage, final Table extendedMenuTable, String typeString, String infoString) {
        TextButton clientInfoButton = bf.createDarkCellButton(typeString + " Info");

        clientInfoButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                extendedMenuTable.remove();
                extendedMenuTable.pack();

                final Table clientInfoContainer = new Table();

                new ClearButton(stage, clientInfoContainer);

                TextButton newGroupLabel = bf.createDarkCellButton(infoString);
                clientInfoContainer.add(newGroupLabel).width(uis.getTabLength()).height(uis.getCellLength() * 3);
                clientInfoContainer.row();

                clientInfoContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
                stage.addActor(clientInfoContainer);
            }
        });

        return clientInfoButton;
    }

    private TextButton addDeleteClient(Stage stage, StageControls stgCtrls, Table extendedMenuTable, Client client) {
        TextButton deleteClientButton = bf.createDarkCellButton("Delete Client");

        deleteClientButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                extendedMenuTable.remove();
                extendedMenuTable.pack();

                final Table deletionContainer = bf.AreYouSureButton(stage, new Function<Integer, Integer>() {
                            @Override
                            public Integer function(Integer i) {
                                Map<String, String> params = new HashMap<>();
                                params.put("clientId", Integer.toString(client.getClientId()));
                                ServerResponse response = HttpUtils.Request("client/delete", params);
                                if (response.statusCode == 200) {
                                    client.deleteClient();
                                    stgCtrls.switchStage(new FirstStage(stgCtrls));
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

        return deleteClientButton;
    }
}
