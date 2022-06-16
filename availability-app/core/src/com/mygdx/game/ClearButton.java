package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.Stages.FirstStage;
import com.mygdx.game.Stages.SecondStage;
import com.mygdx.game.Stages.ThirdStage;

public class ClearButton {
    Table clearContainer = new Table();
    static UISpacing uis = new UISpacing();

    public ClearButton(Stage stage, final Table newTable) {
        ImageButtonStyle clearButtonStyle = new ImageButtonStyle();
        ImageButton clearButton = new ImageButton(clearButtonStyle);

        clearButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                newTable.clear();
                newTable.remove();
                newTable.pack();
                clearContainer.remove();
                clearContainer.pack();
                return true;
            }
        });
        clearContainer.add(clearButton).width(uis.getWidth()).height(uis.getHeight());
        clearContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);

        stage.addActor(clearContainer);
    }

    public void removeClearContainer() {
        clearContainer.remove();
        clearContainer.pack();
    }

}
