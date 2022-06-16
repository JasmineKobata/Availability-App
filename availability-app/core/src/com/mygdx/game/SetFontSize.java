package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class SetFontSize extends UISpacing{
    public SetFontSize(BitmapFont font) {
        font.getData().setScale(cellLength / (font.getCapHeight() * 4));
    }
}
