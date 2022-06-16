package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ScrollFactory {
    //Create scroll table for groups & events
    public static Table createScrollTable(Table table, int numOfCells) {
        ButtonFactory bf = new ButtonFactory();
        UISpacing uis = new UISpacing();
        ScrollPane scrollPane = bf.createScrollPane(table);
        Table scrollTable = new Table();
        Vector2f displayScrollPositions = uis.getDisplayTableYPosition(numOfCells);
        float scrollTableHeight = displayScrollPositions.x;
        float tableYPos = displayScrollPositions.y;;

        scrollTable.add(scrollPane).width(uis.getWidth()).height(scrollTableHeight);
        scrollTable.row();
        scrollTable.setBounds(0, 0, uis.getWidth(), uis.getHeight());
        scrollTable.setPosition(0, tableYPos);

        return scrollTable;
    }

    //Create scroll table for event calendar
    public static Table createScrollTable(Table calendarTable) {
        ButtonFactory bf = new ButtonFactory();
        UISpacing uis = new UISpacing();
        ScrollPane scrollPane = bf.createScrollPane(calendarTable);
        Table scrollTable = new Table();

        scrollTable.add(scrollPane).width(uis.getWidth()).height(uis.getHeight());
        scrollTable.row();
        scrollTable.setBounds(0, 0, uis.getWidth(), uis.getHeight());

        return scrollTable;
    }
}
