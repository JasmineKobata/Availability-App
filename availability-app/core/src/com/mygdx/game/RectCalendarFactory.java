package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.SerializableObjects.Group;

import org.lwjgl.Sys;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class RectCalendarFactory {
    UISpacing uis = new UISpacing();
    ButtonFactory bf = new ButtonFactory();
    int width = (int) uis.getTabLength() / 3;
    int height = (int) uis.getCellLength() / 3;

    public Table instantiateCalendar(Stage stage, Table clearButton, Function<Date, Date> callback) {
        return createCalendar(stage, Calendar.getInstance(), clearButton, callback);
    }

    public Table createCalendar(Stage stage, Calendar calendar, final Table clearButton, final Function<Date, Date> callback) {
        final Table calendarContainer = new Table();
        final Table outerCalendarContainer = new Table();
        TextButton weekButton, blankButton, dayButton, yearButton, monthButton;
        Label leftButton, rightButton;

        Date chosenDay = calendar.getTime();
        DateFactory.setCalendarMinimum(calendar);
        Date firstDayOfMonth = calendar.getTime();

        leftButton = bf.createTextLabel("<");
        leftButton.setAlignment(Align.center);
        Calendar currDay = Calendar.getInstance();
        if (currDay.get(Calendar.MONTH) >= calendar.get(Calendar.MONTH)
                && currDay.get(Calendar.YEAR) >= calendar.get(Calendar.YEAR)) {
            leftButton.setVisible(false);
        }
        rightButton = bf.createTextLabel(">");
        rightButton.setAlignment(Align.center);

        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                outerCalendarContainer.clear();
                calendar.add(Calendar.MONTH, -1);
                outerCalendarContainer.add(createCalendar(stage, calendar, clearButton, callback));
            }
        });

        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                outerCalendarContainer.clear();
                calendar.add(Calendar.MONTH,1);
                outerCalendarContainer.add(createCalendar(stage, calendar, clearButton, callback));
            }
        });

        yearButton = bf.createDarkCellButton(DateFactory.getYearStr(chosenDay));
        calendarContainer.add(yearButton).width(width).height(width);
        monthButton = bf.createDarkCellButton(DateFactory.getMonthStr(chosenDay));
        calendarContainer.add(monthButton).width(width*6).height(width).colspan(6);
        calendarContainer.row();

        yearButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addYearButtons(outerCalendarContainer, stage, calendar, clearButton, callback);
            }
        });

        monthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addMonthButtons(outerCalendarContainer, stage, calendar, clearButton, callback);
            }
        });

        weekButton = bf.createDarkCellButton("Sun");
        calendarContainer.add(weekButton).width(width).height(height);
        weekButton = bf.createDarkCellButton("Mon");
        calendarContainer.add(weekButton).width(width).height(height);
        weekButton = bf.createDarkCellButton("Tues");
        calendarContainer.add(weekButton).width(width).height(height);
        weekButton = bf.createDarkCellButton("Wed");
        calendarContainer.add(weekButton).width(width).height(height);
        weekButton = bf.createDarkCellButton("Thurs");
        calendarContainer.add(weekButton).width(width).height(height);
        weekButton = bf.createDarkCellButton("Fri");
        calendarContainer.add(weekButton).width(width).height(height);
        weekButton = bf.createDarkCellButton("Sat");
        calendarContainer.add(weekButton).width(width).height(height);
        calendarContainer.row();

        int dayOfWeek = 0;

        for (; dayOfWeek<calendar.get(Calendar.DAY_OF_WEEK)-1; dayOfWeek++) {
            blankButton = bf.createCellButton("");
            calendarContainer.add(blankButton).width(width).height(width);
        }

        for (int dayOfMonth=0; dayOfMonth<DateFactory.getMonthLength(calendar); dayOfMonth++) {
            dayButton = bf.createCellButton(Integer.toString(dayOfMonth+1));

            final Date date = DateFactory.offsetDate(firstDayOfMonth, dayOfMonth);

            if (date.compareTo(DateFactory.offsetDate(new Date(), -1)) >= 0) {
                dayButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        outerCalendarContainer.remove();
                        outerCalendarContainer.pack();
                        clearButton.remove();
                        clearButton.pack();

                        callback.function(date);
                    }
                });
            }
            calendarContainer.add(dayButton).width(width).height(width);

            if (++dayOfWeek == 7) {
                calendarContainer.row();
                dayOfWeek = 0;
            }
        }

        outerCalendarContainer.add(leftButton).width(width);
        outerCalendarContainer.add(calendarContainer);
        outerCalendarContainer.add(rightButton).width(width);

        return outerCalendarContainer;
    }

    public void addYearButtons(Table outerCalendarContainer, Stage stage, Calendar calendar, final Table clearButton, final Function<Date, Date> callback) {
        Table yearSelectContainer = new Table();
        TextButton yearButton = null;
        Calendar currentDay = Calendar.getInstance();

        Table clearContainer = bf.clearButton(yearSelectContainer);

        for (int i=0; i<9; i++) {
            int year = currentDay.get(Calendar.YEAR);
            yearButton = bf.createCellButton(Integer.toString(year));
            yearSelectContainer.add(yearButton).width(uis.getTabLength()/2).height(uis.getTabLength()/2);
            currentDay.add(Calendar.YEAR, 1);
            if (i%3 == 2) {
                yearSelectContainer.row();
            }

            yearButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    yearSelectContainer.remove();
                    yearSelectContainer.pack();
                    clearContainer.remove();
                    clearContainer.pack();

                    outerCalendarContainer.clear();
                    calendar.set(Calendar.YEAR, year);
                    outerCalendarContainer.add(createCalendar(stage, calendar, clearButton, callback));
                }
            });
        }

        stage.addActor(clearContainer);
        yearSelectContainer.setPosition(uis.getWidth()/2 - width*2.5f, uis.getHeight()/2 + width*2.5f);
        stage.addActor(yearSelectContainer);
    }

    public void addMonthButtons(Table outerCalendarContainer, Stage stage, Calendar calendar, final Table clearButton, final Function<Date, Date> callback) {
        Table monthSelectContainer = new Table();
        TextButton monthButton = null;
        Calendar currentDay = Calendar.getInstance();
        currentDay.set(Calendar.MONTH, 0);

        Table clearContainer = bf.clearButton(monthSelectContainer);

        for (int i=0; i<12; i++) {
            int monthInt = currentDay.get(Calendar.MONTH);
            String month = DateFactory.getMonthStr(currentDay);
            monthButton = bf.createCellButton(month);
            monthSelectContainer.add(monthButton).width(uis.getTabLength()/2).height(uis.getTabLength()/2);
            currentDay.add(Calendar.MONTH, 1);
            if (i%4 == 3) {
                monthSelectContainer.row();
            }

            monthButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    monthSelectContainer.remove();
                    monthSelectContainer.pack();
                    clearContainer.remove();
                    clearContainer.pack();

                    outerCalendarContainer.clear();
                    calendar.set(Calendar.MONTH, monthInt);
                    outerCalendarContainer.add(createCalendar(stage, calendar, clearButton, callback));
                }
            });
        }

        stage.addActor(clearContainer);
        monthSelectContainer.setPosition(uis.getWidth()/2 + width*0.5f, uis.getHeight()/2 + width*2.5f);
        stage.addActor(monthSelectContainer);
    }
}
