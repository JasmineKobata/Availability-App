package com.mygdx.game.Stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.MariaDB;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.SerializableObjects.Group;
import com.mygdx.game.StageControls;
import com.mygdx.game.StageImplementation.CreateEvents;

import java.sql.Statement;

public class FourthStage extends Stage {
    public FourthStage(StageControls stgCtrls, Client client, Group group) {
        new CreateEvents(this, stgCtrls, client, group);
    }
}
