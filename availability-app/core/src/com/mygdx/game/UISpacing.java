package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import java.util.Vector;

public class UISpacing {
    float width = Gdx.graphics.getWidth();
    float height = Gdx.graphics.getHeight();
//    float cellLength = width / 12;
    float cellLength = width / 9;
    float menuWidth = width * 2 / 5;
    float menuHeight = width / 10;

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getCellLength() {
        return cellLength;
    }

    public float getTabLength() {
        return cellLength * 3;
    }

    public float getDisplayLength() {
        return width * 0.9f;
    }

    public Vector2f getBackPosition() {
        Vector2f v = new Vector2f(width * 0.1f, height - width * 0.07f);
        return v;
    }

    public Vector2f getMenuSizing() {
        Vector2f v = new Vector2f(width / 10, width / 10);
        return v;
    }

    public Vector2f getMenuPosition() {
        Vector2f v = new Vector2f(width * 0.92f, height - width * 0.1f);
        return v;
    }

    public Vector2f getExtMenuSizing() {
        Vector2f v = new Vector2f(menuWidth, menuHeight);
        return v;
    }

    public Vector2f getExtMenuPosition(int menuSize) {
        Vector2f v = new Vector2f(width - menuWidth/2 - menuHeight/2,
                height - menuHeight*(menuSize + 1)/2);
        return v;
    }

    public Vector2f getDisplayTableYSizing() {
        float topMargin = cellLength * 2.25f;
        float scrollTableHeight = height - topMargin;
        return new Vector2f(topMargin, scrollTableHeight);
    }

    public Vector2f getDisplayTableYPosition(int numOfCells) {
        Vector2f ySizing = getDisplayTableYSizing();
        float topMargin = ySizing.x;
        float scrollTableHeight = ySizing.y;
        float groupTableHeight = cellLength * numOfCells;
        float scrollTableCenter = groupTableHeight < scrollTableHeight ? groupTableHeight/2.0f : scrollTableHeight/2.0f;
        float tableYPos = height/2.0f - ySizing.x - scrollTableCenter;
        return new Vector2f(scrollTableHeight, tableYPos);
    }
}
