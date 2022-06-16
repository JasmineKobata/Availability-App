package com.mygdx.game.Stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.MariaDB;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.SerializableObjects.Group;
import com.mygdx.game.StageControls;
import com.mygdx.game.StageImplementation.EventsDisplayPage;

import java.sql.Statement;

public class FifthStage extends Stage {
    public FifthStage(StageControls stgCtrls, Client client, Group group) {
        new EventsDisplayPage(this, stgCtrls, client, group);
    }
}
