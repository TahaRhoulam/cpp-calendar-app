package perpetualcalendar;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class PerpetualCalendarGUI extends Application {
    private final DateTimeFormatter fullDate = DateTimeFormatter.ofPattern("EEEE, MMMM d, uuuu", Locale.ENGLISH);
    private YearMonth visibleMonth = YearMonth.now();
    private LocalDate selectedDate = LocalDate.now();
    private final Label monthTitle = new Label();
    private final Label selectedTitle = new Label();
    private final Label lunarSubtitle = new Label();
    private final Label status = new Label();
    private final GridPane calendar = new GridPane();
    private final TextField year = new TextField();
    private final TextField month = new TextField();
    private final TextField day = new TextField();
    private final CheckBox leap = new CheckBox("Leap lunar month");
    private final ToggleGroup direction = new ToggleGroup();
    private final Label conversionResult = new Label();

    @Override public void start(Stage stage) {
        Label logo = new Label("◈  CHRONOS"); logo.getStyleClass().add("brand");
        Label tagline = new Label("PERPETUAL CALENDAR  /  GREGORIAN + LUNAR"); tagline.getStyleClass().add("eyebrow");
        VBox brand = new VBox(5, logo, tagline);
        Button today = new Button("Today"); today.getStyleClass().add("ghost-button");
        today.setOnAction(e -> { selectedDate = LocalDate.now(); visibleMonth = YearMonth.from(selectedDate); refresh(); });
        HBox header = new HBox(20, brand, new Region(), today); HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        header.setAlignment(Pos.CENTER_LEFT);

        Label hero = new Label("Every date, beautifully connected."); hero.getStyleClass().add("hero");
        Label intro = new Label("Explore your month and convert between Gregorian and Chinese lunar dates."); intro.getStyleClass().add("muted");

        Button previous = new Button("‹"); previous.getStyleClass().add("nav-button");
        Button next = new Button("›"); next.getStyleClass().add("nav-button");
        previous.setOnAction(e -> { visibleMonth = visibleMonth.minusMonths(1); refresh(); });
        next.setOnAction(e -> { visibleMonth = visibleMonth.plusMonths(1); refresh(); });
        monthTitle.getStyleClass().add("section-title");
        HBox monthNav = new HBox(12, monthTitle, new Region(), previous, next);
        HBox.setHgrow(monthNav.getChildren().get(1), Priority.ALWAYS); monthNav.setAlignment(Pos.CENTER_LEFT);
        calendar.getStyleClass().add("calendar-grid");
        calendar.setHgap(7); calendar.setVgap(7);
        VBox calendarPanel = new VBox(18, monthNav, calendar); calendarPanel.getStyleClass().add("panel");

        Label detailEyebrow = new Label("SELECTED DATE"); detailEyebrow.getStyleClass().add("eyebrow");
        selectedTitle.getStyleClass().add("detail-title");
        lunarSubtitle.getStyleClass().add("muted"); lunarSubtitle.setWrapText(true);
        VBox selectedCard = new VBox(10, detailEyebrow, selectedTitle, lunarSubtitle); selectedCard.getStyleClass().add("panel");

        Label converterHeading = new Label("Date converter"); converterHeading.getStyleClass().add("section-title");
        RadioButton gregorian = new RadioButton("Gregorian → Lunar");
        RadioButton lunar = new RadioButton("Lunar → Gregorian");
        gregorian.setToggleGroup(direction); lunar.setToggleGroup(direction); gregorian.setSelected(true);
        HBox modes = new HBox(14, gregorian, lunar); modes.setAlignment(Pos.CENTER_LEFT);
        year.setPromptText("YYYY"); month.setPromptText("MM"); day.setPromptText("DD");
        year.setText(String.valueOf(selectedDate.getYear())); month.setText(String.valueOf(selectedDate.getMonthValue())); day.setText(String.valueOf(selectedDate.getDayOfMonth()));
        VBox yearBox = field("YEAR", year); VBox monthBox = field("MONTH", month); VBox dayBox = field("DAY", day);
        HBox fields = new HBox(9, yearBox, monthBox, dayBox);
        for (VBox v : new VBox[]{yearBox, monthBox, dayBox}) HBox.setHgrow(v, Priority.ALWAYS);
        leap.getStyleClass().add("muted"); leap.setVisible(false); leap.setManaged(false);
        direction.selectedToggleProperty().addListener((obs, old, current) -> {
            boolean lunarMode = current == lunar;
            leap.setVisible(lunarMode); leap.setManaged(lunarMode);
            conversionResult.setText("Enter a date and select Convert.");
        });
        Button convert = new Button("Convert date  →"); convert.getStyleClass().add("primary-button"); convert.setMaxWidth(Double.MAX_VALUE);
        convert.setOnAction(e -> convertDate(gregorian.isSelected()));
        conversionResult.getStyleClass().add("result"); conversionResult.setWrapText(true);
        conversionResult.setText("Enter a date and select Convert.");
        VBox converter = new VBox(17, converterHeading, modes, fields, leap, convert, conversionResult);
        converter.getStyleClass().add("panel");
        VBox sidebar = new VBox(18, selectedCard, converter); sidebar.setPrefWidth(345); sidebar.setMinWidth(325);
        HBox content = new HBox(20, calendarPanel, sidebar); HBox.setHgrow(calendarPanel, Priority.ALWAYS);
        status.getStyleClass().add("footer"); status.setText("Built with JavaFX + ICU4J   •   Dates shown in your local time zone");
        VBox root = new VBox(23, header, new VBox(7, hero, intro), content, status);
        root.getStyleClass().add("app"); root.setPadding(new Insets(32));
        Scene scene = new Scene(root, 1160, 810);
        scene.getStylesheets().add(getClass().getResource("/perpetualcalendar/theme.css").toExternalForm());
        stage.setTitle("Chronos — Perpetual Calendar"); stage.setScene(scene); stage.setMinWidth(1050); stage.setMinHeight(740);
        refresh(); stage.show();
    }

    private VBox field(String label, TextField input) {
        Label caption = new Label(label); caption.getStyleClass().add("field-caption");
        input.setMaxWidth(Double.MAX_VALUE);
        return new VBox(7, caption, input);
    }

    private void refresh() {
        monthTitle.setText(visibleMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + visibleMonth.getYear());
        selectedTitle.setText(selectedDate.format(fullDate));
        CalendarService.LunarDate ld = CalendarService.toLunar(selectedDate);
        lunarSubtitle.setText("Chinese lunar date  •  Year " + ld.year() + "  /  Month " + ld.month() +
                (ld.leapMonth() ? " (leap)" : "") + "  /  Day " + ld.day());
        calendar.getChildren().clear(); calendar.getColumnConstraints().clear();
        for (int c = 0; c < 7; c++) { ColumnConstraints constraint = new ColumnConstraints(); constraint.setPercentWidth(100.0 / 7); calendar.getColumnConstraints().add(constraint); }
        String[] weekdays = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        for (int c = 0; c < 7; c++) {
            Label heading = new Label(weekdays[c]); heading.getStyleClass().add("weekday"); heading.setMaxWidth(Double.MAX_VALUE); heading.setAlignment(Pos.CENTER);
            calendar.add(heading, c, 0);
        }
        int offset = visibleMonth.atDay(1).getDayOfWeek().getValue() - 1;
        for (int cell = 0; cell < 42; cell++) {
            int dayNumber = cell - offset + 1;
            Button tile = new Button(); tile.getStyleClass().add("day-tile"); tile.setMaxWidth(Double.MAX_VALUE); tile.setMinHeight(66);
            if (dayNumber >= 1 && dayNumber <= visibleMonth.lengthOfMonth()) {
                LocalDate date = visibleMonth.atDay(dayNumber);
                tile.setText(String.valueOf(dayNumber));
                if (date.equals(LocalDate.now())) tile.getStyleClass().add("today-tile");
                if (date.equals(selectedDate)) tile.getStyleClass().add("selected-tile");
                tile.setOnAction(e -> { selectedDate = date; year.setText(""+date.getYear()); month.setText(""+date.getMonthValue()); day.setText(""+date.getDayOfMonth()); refresh(); });
            } else { tile.setDisable(true); tile.getStyleClass().add("empty-tile"); }
            calendar.add(tile, cell % 7, 1 + cell / 7);
        }
    }

    private void convertDate(boolean gregorianToLunar) {
        try {
            int y = Integer.parseInt(year.getText().trim());
            int m = Integer.parseInt(month.getText().trim());
            int d = Integer.parseInt(day.getText().trim());
            if (gregorianToLunar) {
                LocalDate input = LocalDate.of(y, m, d);
                CalendarService.LunarDate converted = CalendarService.toLunar(input);
                conversionResult.setText("LUNAR DATE\nYear " + converted.year() + " · Month " + converted.month() +
                        (converted.leapMonth() ? " (leap)" : "") + " · Day " + converted.day());
                selectedDate = input; visibleMonth = YearMonth.from(input); refresh();
            } else {
                LocalDate converted = CalendarService.toGregorian(y, m, d, leap.isSelected());
                conversionResult.setText("GREGORIAN DATE\n" + converted.format(fullDate));
                selectedDate = converted; visibleMonth = YearMonth.from(converted); refresh();
            }
        } catch (NumberFormatException ex) {
            conversionResult.setText("Please enter numbers for year, month and day.");
        } catch (DateTimeException | IllegalArgumentException ex) {
            conversionResult.setText("Invalid date: " + ex.getMessage());
        }
    }

    public static void main(String[] args) { launch(args); }
}
