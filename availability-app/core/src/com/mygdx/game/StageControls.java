package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class StageControls extends ApplicationAdapter {
    private Stage stage;

    public StageControls() {
        stage = null;
    }

    public void switchStage(Stage newStage) {
        stage = newStage;
        Gdx.input.setInputProcessor(stage);
    }

    public void updateStage() {
        super.render();
        if (stage != null) {
            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            stage.draw();
        }
    }
}
