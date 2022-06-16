package com.mygdx.game.Stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.MariaDB;
import com.mygdx.game.SerializableObjects.AvailabilityData;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.SerializableObjects.Group;
import com.mygdx.game.SerializableObjects.Event;
import com.mygdx.game.StageControls;
import com.mygdx.game.StageImplementation.EventTableGeneration;

import java.sql.Statement;

public class SixthStage extends Stage {
    public SixthStage(StageControls stgCtrls, Client client, Group group, Event occ) {
        //        Gdx.input.setInputProcessor(this);
        new EventTableGeneration(this, stgCtrls, client, group, occ);
    }
}
