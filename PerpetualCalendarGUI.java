package perpetualcalendar;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import com.ibm.icu.util.ChineseCalendar;
import com.ibm.icu.util.Calendar;

public class PerpetualCalendarGUI extends Application {

    private TextArea output;

    @Override
    public void start(Stage stage) {

        // ===== Input Fields =====
        TextField yearField = new TextField();
        TextField monthField = new TextField();
        TextField dayField = new TextField();
        TextField leapField = new TextField();

        yearField.setPromptText("Year");
        monthField.setPromptText("Month");
        dayField.setPromptText("Day");
        leapField.setPromptText("Leap? (1=Yes, 0=No)");

        // ===== Buttons =====
        Button gToLBtn = new Button("Gregorian → Lunar");
        Button lToGBtn = new Button("Lunar → Gregorian");
        Button monthBtn = new Button("View Month");

        // ===== Output Area =====
        output = new TextArea();
        output.setEditable(false);
        output.setPrefHeight(250);

        // ===== Button Actions =====
        gToLBtn.setOnAction(e ->
                gregorianToLunar(yearField, monthField, dayField));

        lToGBtn.setOnAction(e ->
                lunarToGregorian(yearField, monthField, dayField, leapField));

        monthBtn.setOnAction(e ->
                showMonth(yearField, monthField));

        // ===== Layout =====
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.add(yearField, 0, 0);
        inputGrid.add(monthField, 1, 0);
        inputGrid.add(dayField, 2, 0);
        inputGrid.add(leapField, 3, 0);

        HBox buttons = new HBox(10, gToLBtn, lToGBtn, monthBtn);

        VBox root = new VBox(15, inputGrid, buttons, output);
        root.setPadding(new Insets(15));

        stage.setTitle("Perpetual Calendar");
        stage.setScene(new Scene(root, 700, 400));
        stage.show();
    }

    /* =======================
       Gregorian → Lunar
       ======================= */
    private void gregorianToLunar(TextField y, TextField m, TextField d) {
        try {
            int year = Integer.parseInt(y.getText());
            int month = Integer.parseInt(m.getText());
            int day = Integer.parseInt(d.getText());

            LocalDate gDate = LocalDate.of(year, month, day);

            ChineseCalendar cc = new ChineseCalendar();
            cc.setTime(java.sql.Date.valueOf(gDate));

            int lunarYear = cc.get(Calendar.EXTENDED_YEAR) - 2637;
            int lunarMonth = cc.get(Calendar.MONTH) + 1;
            int lunarDay = cc.get(Calendar.DAY_OF_MONTH);
            boolean isLeap = cc.get(Calendar.IS_LEAP_MONTH) == 1;

            output.setText(
                    "Gregorian Date: " + gDate + "\n\n" +
                            "Lunar Year: " + lunarYear + "\n" +
                            "Lunar Month: " + lunarMonth +
                            (isLeap ? " (Leap Month)" : "") + "\n" +
                            "Lunar Day: " + lunarDay
            );

        } catch (Exception ex) {
            output.setText("Invalid Gregorian date.");
        }
    }

    /* =======================
       Lunar → Gregorian
       ======================= */
    private void lunarToGregorian(TextField y, TextField m, TextField d, TextField leap) {
        try {
            int year = Integer.parseInt(y.getText());
            int month = Integer.parseInt(m.getText());
            int day = Integer.parseInt(d.getText());
            boolean isLeap = leap.getText().equals("1");

            ChineseCalendar cc = new ChineseCalendar();
            cc.set(Calendar.EXTENDED_YEAR, year + 2637);
            cc.set(Calendar.MONTH, month - 1);
            cc.set(Calendar.IS_LEAP_MONTH, isLeap ? 1 : 0);
            cc.set(Calendar.DAY_OF_MONTH, day);

            Calendar gc = Calendar.getInstance();
            gc.setTime(cc.getTime());

            LocalDate gDate = LocalDate.of(
                    gc.get(Calendar.YEAR),
                    gc.get(Calendar.MONTH) + 1,
                    gc.get(Calendar.DAY_OF_MONTH)
            );

            output.setText(
                    "Lunar Date:\n" +
                            "Year: " + year + "\n" +
                            "Month: " + month +
                            (isLeap ? " (Leap)" : "") + "\n" +
                            "Day: " + day + "\n\n" +
                            "Gregorian Date:\n" +
                            gDate
            );

        } catch (Exception ex) {
            output.setText("Invalid Lunar date.");
        }
    }

    /* =======================
       Monthly Calendar
       ======================= */
    private void showMonth(TextField y, TextField m) {
        try {
            int year = Integer.parseInt(y.getText());
            int month = Integer.parseInt(m.getText());

            LocalDate firstDay = LocalDate.of(year, month, 1);

            StringBuilder sb = new StringBuilder();
            sb.append(firstDay.getMonth()).append(" ").append(year).append("\n");
            sb.append("S  M  T  W  T  F  S\n");

            int start = firstDay.getDayOfWeek().getValue() % 7;
            for (int i = 0; i < start; i++) sb.append("   ");

            for (int d = 1; d <= firstDay.lengthOfMonth(); d++) {
                sb.append(String.format("%2d ", d));
                if ((d + start) % 7 == 0) sb.append("\n");
            }

            output.setText(sb.toString());

        } catch (Exception ex) {
            output.setText("Invalid month or year.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}