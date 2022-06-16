package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import com.badlogic.gdx.utils.Align;
import com.mygdx.game.SerializableObjects.Client;
import com.mygdx.game.Stages.SecondStage;
import com.mygdx.game.Stages.ThirdStage;
import javafx.util.Pair;
import org.w3c.dom.Text;

import java.sql.Statement;
import java.util.Date;

public class ButtonFactory {
    Texture cell;
    Texture darkCell;
    UISpacing uis = new UISpacing();

    public ButtonFactory() {
        cell = new Texture("clear.png");
        darkCell = new Texture("checked.png");
    }

    public Label createTextLabel(String s) {
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = Colors.darkGreen;
        new SetFontSize(labelStyle.font);

        return new Label(s, labelStyle);
    }

    public Label createTextLabel(String s, Color color) {
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont();
        labelStyle.fontColor = color;
        new SetFontSize(labelStyle.font);

        return new Label(s, labelStyle);
    }

    public TextButton createCellButton(String s) {
        TextButtonStyle cellStyle = setTextButtonStyle();
        cellStyle.up = new TextureRegionDrawable(cell);

        return new TextButton(s, cellStyle);
    }

    public TextButton createDarkCellButton(String s) {
        TextButtonStyle cellStyle = setTextButtonStyle();
        cellStyle.up = new TextureRegionDrawable(darkCell);

        return new TextButton(s, cellStyle);
    }

    public TextButton createCheckableButton(String s) {
        TextButtonStyle cellStyle = setTextButtonStyle();
        cellStyle.up = new TextureRegionDrawable(cell);
        cellStyle.checked = new TextureRegionDrawable(darkCell);

        return new TextButton(s, cellStyle);
    }

    private TextButtonStyle setTextButtonStyle() {
        TextButtonStyle cellStyle = new TextButtonStyle();
        cellStyle.font = new BitmapFont();
        cellStyle.fontColor = Colors.darkGreen;
        new SetFontSize(cellStyle.font);

        return cellStyle;
    }

    public ImageButton createCheckmarkButton() {
        Texture checkmark = new Texture(Gdx.files.internal("checkmark.png"));
        ImageButtonStyle checkmarkStyle = new ImageButtonStyle();
        checkmarkStyle.up = new TextureRegionDrawable(checkmark);

        return new ImageButton(checkmarkStyle);
    }

    public ImageButton createMenuButton() {
        Texture menuImg = new Texture(Gdx.files.internal("menu.png"));
        ImageButtonStyle menuButtonStyle = new ImageButtonStyle();
        menuButtonStyle.up = new TextureRegionDrawable(menuImg);

        return new ImageButton(menuButtonStyle);
    }

    public TextField createTextInputButton(String s) {
        TextFieldStyle inputButtonStyle = new TextFieldStyle();
        inputButtonStyle.font = new BitmapFont();
        inputButtonStyle.fontColor = Colors.darkGreen;
        new SetFontSize(inputButtonStyle.font);
        inputButtonStyle.background = new TextureRegionDrawable(darkCell);

        Label oneCharSizeCalibrationThrowAway = new Label("|", new LabelStyle(new BitmapFont(), Colors.darkGreen));
        Pixmap cursorColor = new Pixmap((int) oneCharSizeCalibrationThrowAway.getWidth(),
                (int) oneCharSizeCalibrationThrowAway.getHeight(),
                Pixmap.Format.RGB888);
        cursorColor.setColor(Colors.darkGreen);
        cursorColor.fill();
        inputButtonStyle.cursor = new Image(new Texture(cursorColor)).getDrawable();
        TextField textField = new TextField(s, inputButtonStyle);
        textField.setAlignment(Align.center);

        return textField;
    }

    public TextButton createDateTab(String s) {
        Texture dateTab = new Texture(Gdx.files.internal("datetab.png"));
        TextButtonStyle dateButtonStyle = new TextButton.TextButtonStyle();
        dateButtonStyle.font = new BitmapFont();
        new SetFontSize(dateButtonStyle.font);
        dateButtonStyle.fontColor = Colors.darkGreen;
        dateButtonStyle.up = new TextureRegionDrawable(dateTab);

        return new TextButton(s, dateButtonStyle);
    }

    public TextButton createUserTab(String s) {
        Texture userTab = new Texture(Gdx.files.internal("usertab.png"));
        TextButtonStyle userButtonStyle = new TextButton.TextButtonStyle();
        userButtonStyle.font = new BitmapFont();
        new SetFontSize(userButtonStyle.font);
        userButtonStyle.fontColor = Colors.darkGreen;
        userButtonStyle.up = new TextureRegionDrawable(userTab);
        userButtonStyle.up.setMinHeight(uis.getCellLength());
        userButtonStyle.up.setMinWidth(uis.getTabLength());

        return new TextButton(s, userButtonStyle);
    }

    public ImageButton createBlankButton() {
        ImageButtonStyle blankButtonStyle = new ImageButtonStyle();
        ImageButton button = new ImageButton(blankButtonStyle);
        button.setVisible(false);
        return button;
    }

    public ScrollPane createScrollPane(Actor actor) {
        ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
        return new ScrollPane(actor, scrollPaneStyle);
    }

    public Table clearButton(final Table newTable) {
        Table container = new Table();
        ImageButtonStyle clearButtonStyle = new ImageButtonStyle();
        ImageButton clearButton = new ImageButton(clearButtonStyle);

        clearButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                newTable.remove();
                newTable.pack();
                container.remove();
                container.pack();
                return true;
            }
        });
        container.add(clearButton).width(uis.getWidth()).height(uis.getHeight());
        container.setPosition(uis.getWidth() / 2, uis.getHeight() / 2);

        return container;
    }

    public Table AreYouSureButton(Stage stage, Function<Integer, Integer> callback) {
        final Table leaveContainer = new Table();

        Table clearContainer = clearButton(leaveContainer);
        stage.addActor(clearContainer);

        TextButton deletionLabel = createDarkCellButton("Are you sure?");
        leaveContainer.add(deletionLabel).width(uis.getTabLength()).height(uis.getCellLength()).colspan(2);
        leaveContainer.row();

        TextButton yesButton = createCellButton("Yes");
        leaveContainer.add(yesButton).width(uis.getTabLength()/2).height(uis.getCellLength());

        TextButton noButton = createCellButton("No");
        leaveContainer.add(noButton).width(uis.getTabLength()/2).height(uis.getCellLength());
        leaveContainer.row();

        noButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                leaveContainer.remove();
                leaveContainer.pack();
                clearContainer.remove();
                clearContainer.pack();
            }
        });

        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                leaveContainer.remove();
                leaveContainer.pack();
                clearContainer.remove();
                clearContainer.pack();

                callback.function(1);
            }
        });

        return leaveContainer;
    }
}
