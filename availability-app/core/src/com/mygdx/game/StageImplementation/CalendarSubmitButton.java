package com.mygdx.game.StageImplementation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.ButtonFactory;
import com.mygdx.game.HttpUtils;
import com.mygdx.game.JsonFactory;
import com.mygdx.game.SerializableObjects.AvailabilityData;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.SerializableObjects.Event;
import com.mygdx.game.ServerResponse;
import com.mygdx.game.Stages.FifthStage;
import com.mygdx.game.UISpacing;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CalendarSubmitButton {
    ImageButton checkmarkButton;
    UISpacing uis = new UISpacing();
    ButtonFactory bf = new ButtonFactory();

    public ImageButton createSubmitButton(final Stage stage, Actor actor, Client client, final Event myEvent, final AvailabilityData availData, final String date) {
        actor.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                availData.updateAvailabilityData(date);

                Table submitContainer = new Table();

                if (checkmarkButton == null) {
                    checkmarkButton = bf.createCheckmarkButton();
                    setButtonInvisible(client, myEvent, availData);
                    submitContainer.add(checkmarkButton).width(uis.getCellLength()).height(uis.getCellLength());
                    submitContainer.setPosition(uis.getWidth() * 0.92f, uis.getWidth() * (1.0f-0.92f));

                    stage.addActor(submitContainer);
                }
                else if (!checkmarkButton.isVisible()) {

                    checkmarkButton.setVisible(true);
                }
            }
        });
        return checkmarkButton;
    }

    public void setButtonInvisible(Client client, final Event myEvent, final AvailabilityData availData) {
        checkmarkButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                Map<String, String> params = new HashMap<>();
                params.put("clientId", Integer.toString(client.getClientId()));
                params.put("eventId", Integer.toString(myEvent.getEventId()));
                String[] datesTemp = new String[availData.getCheckedDates().size()];
                availData.getCheckedDates().toArray(datesTemp);
                System.out.println(JsonFactory.classToJsonString(datesTemp));
                params.put("checkedDates", JsonFactory.classToJsonString(datesTemp));

                ServerResponse response = HttpUtils.Request("availability/update", params);
                if (response.statusCode == 200) {
                    checkmarkButton.setVisible(false);
                }
            }
        });
    }

}
