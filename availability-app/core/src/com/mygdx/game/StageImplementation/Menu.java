package com.mygdx.game.StageImplementation;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.ClearButton;
import com.mygdx.game.Function;
import com.mygdx.game.HttpUtils;
import com.mygdx.game.JsonFactory;
import com.mygdx.game.MariaDB;
import com.mygdx.game.SerializableObjects.AvailabilityData;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.SerializableObjects.Group;
import com.mygdx.game.SerializableObjects.Event;
import com.mygdx.game.ServerResponse;
import com.mygdx.game.StageControls;
import com.mygdx.game.Stages.FifthStage;
import com.mygdx.game.Stages.FourthStage;
import com.mygdx.game.Stages.SixthStage;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class Menu extends EventsDisplayPage {
    ImageButton blankButton;

    public Menu (final Stage stage, StageControls stgCtrls, Client client, Group group, Event occ) {
        blankButton = bf.createBlankButton();

        Table menuContainer = new Table();

        dateRange = occ.getDateRange();

        ImageButton menuButton = bf.createMenuButton();
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y)  {
                final Table extendedMenuTable = new Table();

                new ClearButton(stage, extendedMenuTable);

                TextButton eventNameButton = addEditEventNameButton(stage, stgCtrls, client, group, occ);
                TextButton eventRangeButton = addEditDatesButton(stage, stgCtrls, client, group, occ);
                TextButton deleteEventButton = addEventGroupButton(stage, stgCtrls, extendedMenuTable, client, group, occ);

                extendedMenuTable.add(eventNameButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);
                extendedMenuTable.row();
                extendedMenuTable.add(eventRangeButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);
                extendedMenuTable.row();
                extendedMenuTable.add(deleteEventButton).width(uis.getExtMenuSizing().x).height(uis.getExtMenuSizing().y);
                extendedMenuTable.setPosition(uis.getExtMenuPosition(3).x, uis.getExtMenuPosition(3).y);
                stage.addActor(extendedMenuTable);
            }
        });

        menuContainer.add(menuButton).width(uis.getWidth() / 10).height(uis.getWidth() / 10);
        menuContainer.setPosition(uis.getWidth() * 0.92f, uis.getHeight() - uis.getWidth() * 0.1f);

        stage.addActor(menuContainer);
    }

    private TextButton addEditEventNameButton(Stage stage, StageControls stgCtrls, Client client, Group group, Event occ) {
        TextButton eventNameButton = bf.createDarkCellButton("Edit Event Name");

        eventNameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                Table newNameContainer = addClearToSubmittableButtons(stage, "Rename Event", "", "");

                checkmarkButton.addListener(new ClickListener() {
                    @Override
                    public void clicked (InputEvent event, float x, float y)  {
                        Map<String, String> params = new HashMap<>();
                        params.put("eventName", newName);
                        params.put("eventId", Integer.toString(occ.getEventId()));
                        params.put("groupId", Integer.toString(group.getGroupId()));
                        ServerResponse response = HttpUtils.Request("event/name", params);
                        if (response.statusCode == 200) {
                            occ.editEventName(newName);
                            stgCtrls.switchStage(new SixthStage(stgCtrls, client, group, occ));
                        }
                        else {
                            errorLabel.setText(response.message);
                            errorLabel.setVisible(true);
                        }
                    }
                });

                newNameContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
                stage.addActor(newNameContainer);
            }
        });

        return eventNameButton;
    }

    private TextButton addEditDatesButton(final Stage stage, final StageControls stgCtrls, Client client, Group group, Event occ) {
        TextButton eventRangeButton = bf.createDarkCellButton("Edit Date Range");

        eventRangeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ImageButton checkmarkButton = addDateButtons(stage);

                checkmarkButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Map<String, String> params = new HashMap<>();
                        params.put("eventId", Integer.toString(occ.getEventId()));
                        params.put("groupId", Integer.toString(group.getGroupId()));
                        params.put("startDate", dateRange.first);
                        params.put("endDate", dateRange.second);
                        ServerResponse response = HttpUtils.Request("event/dates", params);
                        if (response.statusCode == 200) {
                            occ.editDateRange(dateRange);
                            stgCtrls.switchStage(new SixthStage(stgCtrls, client, group, occ));
                        }
                        else {
                            errorLabel.setText(response.message);
                            errorLabel.setVisible(true);
                        }
                    }
                });
            }
        });

        return eventRangeButton;
    }

    private TextButton addEventGroupButton(Stage stage, StageControls stgCtrls, Table extendedMenuTable, Client client, Group group, Event occ) {
        TextButton deleteGroupButton = bf.createDarkCellButton("Delete Event");

        deleteGroupButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                extendedMenuTable.remove();
                extendedMenuTable.pack();

                final Table deletionContainer = bf.AreYouSureButton(stage, new Function<Integer, Integer>() {
                    @Override
                    public Integer function(Integer i) {
                        Map<String, String> params = new HashMap<>();
                        params.put("eventId", Integer.toString(occ.getEventId()));
                        params.put("groupId", Integer.toString(group.getGroupId()));
                        ServerResponse response = HttpUtils.Request("event/delete", params);
                        if (response.statusCode == 200) {
                            group.removeEventId(occ.getEventId());

                            if (group.getEventIds().isEmpty()) {
                                stgCtrls.switchStage(new FourthStage(stgCtrls, client, group));
                            }
                            else {
                                stgCtrls.switchStage(new FifthStage(stgCtrls, client, group));
                            }
                        }
                        else {
                            errorLabel.setText(response.message);
                            errorLabel.setVisible(true);
                        }

                        return i;
                    }
                });

                deletionContainer.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);
                stage.addActor(deletionContainer);
            }
        });

        return deleteGroupButton;
    }
}
