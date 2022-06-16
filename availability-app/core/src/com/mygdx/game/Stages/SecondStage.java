package com.mygdx.game.Stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.MariaDB;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.StageImplementation.CreateGroups;
import com.mygdx.game.StageControls;

import java.sql.Statement;

public class SecondStage extends Stage {

    public SecondStage(StageControls stgCtrls, Client client) {
        new CreateGroups(this, stgCtrls, client);
    }
}
