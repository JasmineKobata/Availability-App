package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.Stages.FirstStage;
import com.mygdx.game.Stages.SecondStage;
import com.mygdx.game.Stages.ThirdStage;

import java.sql.Statement;

public class MyGdxGame extends StageControls {
    Color color = Colors.lightGreen;

    @Override
    public void create() {
        if (!JsonFactory.clientFile.exists()) {
            switchStage(new FirstStage(this));
        } else {
            Client client = JsonFactory.classFromJsonFile(JsonFactory.clientFile, Client.class);

            if (client.getGroupIds().isEmpty()) {
                switchStage(new SecondStage(this, client));
            } else {
                switchStage(new ThirdStage(this, client));
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updateStage();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(int width, int height) {

    }
}
