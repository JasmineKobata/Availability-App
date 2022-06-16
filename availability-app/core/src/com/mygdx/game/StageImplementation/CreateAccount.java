package com.mygdx.game.StageImplementation;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.*;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.Stages.SecondStage;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CreateAccount {
    String newName = new String();
    Label errorLabel;
    UISpacing uis = new UISpacing();
    ButtonFactory bf = new ButtonFactory();
    ImageButton blankButton = bf.createBlankButton();
    ImageButton checkmarkButton = bf.createCheckmarkButton();

    public CreateAccount() {    }

    public CreateAccount(Stage stage, final StageControls stgCtrls) {
        Table newAccountContainer = new Table();
        addSubmittableButtons(newAccountContainer, "Enter New Username:", newName, "");

        checkmarkButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y)  {
                Map<String, String> params = new HashMap<>();
                params.put("clientName", newName);
                ServerResponse response = HttpUtils.Request("client/create", params);

                if (response.statusCode == 200) {
                    JsonFactory.stringToJsonFile(JsonFactory.clientFile, response.message);
                    Client newClient = JsonFactory.classFromJsonString(response.message, Client.class);
                    stgCtrls.switchStage(new SecondStage(stgCtrls, newClient));
                }
                else {
                    errorLabel.setText(response.message);
                    errorLabel.setVisible(true);
                }
            }
        });

        newAccountContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
        stage.addActor(newAccountContainer);
    }

    public void addSubmittableButtons(Table newGroupContainer, String s, String input, String errS) {
        newGroupContainer.add(blankButton).width(uis.getCellLength()).height(uis.getCellLength());
        newGroupContainer.row();
        newGroupContainer.add(blankButton).width(uis.getCellLength()).height(uis.getCellLength());

        TextButton newGroupLabel = bf.createCellButton(s);
        newGroupContainer.add(newGroupLabel).width(uis.getTabLength() + uis.getCellLength()).height(uis.getCellLength());
        newGroupContainer.row();

        newGroupContainer.add(blankButton).width(uis.getCellLength()).height(uis.getCellLength());

        TextField enterGroupName = bf.createTextInputButton(input);
        newGroupContainer.add(enterGroupName).width(uis.getTabLength() + uis.getCellLength()).height(uis.getCellLength());

        checkmarkButton = bf.createCheckmarkButton();
        checkmarkButton.setVisible(false);
        newGroupContainer.add(checkmarkButton).width(uis.getCellLength()).height(uis.getCellLength());
        newGroupContainer.row();

        errorLabel = bf.createTextLabel(errS);
//        errorLabel.setVisible(false);
        newGroupContainer.add(errorLabel).height(uis.getCellLength()).colspan(3);

        enterGroupName.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                newName = enterGroupName.getText().toString();

                if (newName.isEmpty()) {
                    checkmarkButton.setVisible(false);
                } else {
                    checkmarkButton.setVisible(true);
                }
            }
        });
    }
}
