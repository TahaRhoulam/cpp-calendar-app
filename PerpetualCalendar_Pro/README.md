# Chronos — Perpetual Calendar

A modern Java desktop calendar with an interactive month view and **Gregorian ↔ Chinese lunar** date conversion. Built with **JavaFX**, **ICU4J**, **Maven**, and **JUnit 5**.

> Version 2 is a redesigned successor to my original Perpetual Calendar project, with a new interface and separated conversion logic.

## Highlights

- **Interactive monthly calendar** — browse months, choose a day, and return to today.
- **Bidirectional date conversion** — convert Gregorian dates to Chinese lunar dates and back.
- **Leap lunar month support** — explicitly select the leap-month flag when converting from lunar dates.
- **Input validation** — clear feedback for invalid dates and nonnumeric input.
- **Modern JavaFX interface** — custom dark theme, responsive day grid, selected-day details.
- **Testable architecture** — conversion logic is isolated in `CalendarService`, with JUnit tests.

## Screenshots

Screenshots will be added after running the redesigned application. To add them, save real application screenshots to `docs/` and reference them here.

## Requirements

- JDK 17 or later (tested configuration targets Java 17 bytecode)
- Apache Maven 3.9+
- Internet access for the initial Maven dependency download

## Run

```bash
mvn clean test javafx:run
```

On Windows, you can also double-click `RUN_ON_WINDOWS.bat`. The script uses Maven from PATH, or the Maven location previously used on the developer's Windows machine. Adjust that path if yours differs.

## How to use

1. Select a day on the calendar, or browse months using the arrow buttons.
2. Enter the **Year**, **Month**, and **Day** in their labeled fields.
3. Choose **Gregorian → Lunar** or **Lunar → Gregorian**.
4. For lunar input, check **Leap lunar month** only if applicable.
5. Click **Convert date**. The result appears in the side panel and the calendar updates.

## Project structure

```text
src/main/java/perpetualcalendar/
  PerpetualCalendarGUI.java    # JavaFX user interface
  CalendarService.java        # ICU4J conversion and validation
src/main/resources/perpetualcalendar/
  theme.css                   # Application styling
src/test/java/perpetualcalendar/
  CalendarServiceTest.java    # Conversion tests
pom.xml                       # Maven dependencies and plugins
```

## Technology

| Technology | Purpose |
|---|---|
| Java 17+ | Application language |
| JavaFX | Desktop interface and event handling |
| ICU4J | Chinese lunar calendar calculations |
| Maven | Build and dependency management |
| JUnit 5 | Unit testing |

## Known considerations

- Chinese lunar dates have leap months; an invalid leap-month selection is rejected.
- The application uses the computer's local time zone for converting dates.
- This is a desktop app; it is not a web service or a mobile app.

## Future improvements

- Keyboard shortcuts and accessible navigation
- Calendar event storage and search
- Packaged installers for Windows/macOS/Linux
- CI builds and broader date-boundary test coverage

## Author

**Taha Rhoulam** · [GitHub](https://github.com/TahaRhoulam) · [LinkedIn](https://www.linkedin.com/in/taha-rhoulam-355893271/)
