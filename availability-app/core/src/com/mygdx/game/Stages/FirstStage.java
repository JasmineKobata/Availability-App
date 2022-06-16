package com.mygdx.game.Stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.MariaDB;
import com.mygdx.game.StageImplementation.CreateAccount;
import com.mygdx.game.StageControls;

import java.sql.Statement;

public class FirstStage extends Stage {

    public FirstStage(StageControls stgCtrls) {
        new CreateAccount(this, stgCtrls);
    }
}
