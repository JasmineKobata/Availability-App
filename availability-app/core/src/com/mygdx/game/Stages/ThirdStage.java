package com.mygdx.game.Stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.MariaDB;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.StageImplementation.GroupsDisplayPage;
import com.mygdx.game.StageControls;

import java.sql.Statement;

public class ThirdStage extends Stage {
    public ThirdStage(StageControls stgCtrls, Client client) {
        new GroupsDisplayPage(this, stgCtrls, client);
    }
}
