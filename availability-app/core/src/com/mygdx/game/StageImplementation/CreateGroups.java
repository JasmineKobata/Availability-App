package com.mygdx.game.StageImplementation;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.*;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.SerializableObjects.Group;
import com.mygdx.game.Stages.SecondStage;
import com.mygdx.game.Stages.ThirdStage;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CreateGroups extends CreateAccount {

    public CreateGroups() {    }

    public CreateGroups(final Stage stage, final StageControls stgCtrls, Client client) {

        final Table table = new Table();

        TextButton newGroupButton = addNewGroupButtons(stage, stgCtrls, client);
        TextButton joinGroupButton = addJoinGroupButtons(stage, stgCtrls, client);
        
        table.add(newGroupButton).width(uis.getTabLength()).height(uis.getCellLength());
        table.row();
        table.add(joinGroupButton).width(uis.getTabLength()).height(uis.getCellLength());

        table.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);

        stage.addActor(table);
    }

    public Table addClearToSubmittableButtons(Stage stage, String s, String input, String errS) {
        Table newGroupContainer = new Table();
        new ClearButton(stage, newGroupContainer);
        addSubmittableButtons(newGroupContainer, s, input, errS);

        return newGroupContainer;
    }

    public TextButton addNewGroupButtons(Stage stage, final StageControls stgCtrls, Client client) {
        TextButton newGroupButton = bf.createCellButton("Create New Group");

        newGroupButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Table newGroupContainer = addClearToSubmittableButtons(stage,"Enter New Group Name:", "", "");
                addCheckmarkNewGroupListener(stgCtrls, client);

                newGroupContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
                stage.addActor(newGroupContainer);
            }
        });
        return newGroupButton;
    }

    public void addCheckmarkNewGroupListener(StageControls stgCtrls, Client client) {
        checkmarkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Map<String, String> params = new HashMap<>();
                params.put("groupName", newName);
                params.put("clientId", Integer.toString(client.getClientId()));
                ServerResponse response = HttpUtils.Request("group/create", params);
                if (response.statusCode == 200) {
                    Group group = JsonFactory.classFromJsonString(response.message, Group.class);
                    client.addGroupId(group.getGroupId());
                    JsonFactory.classToJsonFile(JsonFactory.clientFile, client);
                    stgCtrls.switchStage(new ThirdStage(stgCtrls, client));
                }
                else {
                    errorLabel.setText(response.message);
                    errorLabel.setVisible(true);
                }
            }
        });
    }

    public TextButton addJoinGroupButtons(Stage stage, final StageControls stgCtrls, Client client) {
        TextButton joinGroupButton = bf.createCellButton("Join Group");

        joinGroupButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                Table newGroupContainer = addClearToSubmittableButtons(stage, "Enter Group ID Number:", "", "");
                addCheckmarkJoinGroupListener(stgCtrls, client);

                newGroupContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
                stage.addActor(newGroupContainer);
            }
        });
        return joinGroupButton;
    }

    public void addCheckmarkJoinGroupListener(StageControls stgCtrls, Client client) {
        checkmarkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Map<String, String> params = new HashMap<>();
                params.put("groupId", newName);
                params.put("clientId", Integer.toString(client.getClientId()));
                ServerResponse response = HttpUtils.Request("group/join", params);
                if (response.statusCode == 200) {
                    client.addGroupId(Integer.parseInt(newName));
                    JsonFactory.classToJsonFile(JsonFactory.clientFile, client);
                    stgCtrls.switchStage(new ThirdStage(stgCtrls, client));
                }
                else {
                    errorLabel.setText(response.message);
                    errorLabel.setVisible(true);
                }
            }
        });
    }
}
