Perpetual Calendar — Gregorian ↔ Chinese Lunar Converter
A desktop calendar application built with JavaFX that converts dates between the Gregorian and Chinese lunar calendars and displays a monthly Gregorian calendar.
Tech stack: Java · JavaFX · ICU4J (com.ibm.icu.util.ChineseCalendar) · java.time

Features
- Gregorian → Chinese lunar: Enter a Gregorian date to display the corresponding lunar year, month, and day, including leap-month information.
- Chinese lunar → Gregorian: Convert a lunar date to a Gregorian date, with an input for whether the lunar month is a leap month.
- Monthly calendar: Display a text-based monthly Gregorian calendar inside the desktop interface.
- Input feedback: Show a message when a date or month cannot be parsed or converted.
Interface
The application provides fields for Year, Month, Day, and Leap? (1=Yes, 0=No), plus three buttons:
Button	Action
Gregorian → Lunar	Convert a Gregorian date to a Chinese lunar date
Lunar → Gregorian	Convert a Chinese lunar date to a Gregorian date
View Month	Display the selected Gregorian month


Results appear in a read-only text area.
Requirements
- Java Development Kit (JDK) compatible with your JavaFX version
- JavaFX SDK or JavaFX dependencies configured in your IDE/build environment
- ICU4J library (provides com.ibm.icu.util.ChineseCalendar)
Note: The repository currently contains a Java source file but no dependency-management configuration. JavaFX and ICU4J must be installed/configured separately. Exact launch commands depend on your operating system and library locations.
Run locally
1. Clone the repository:
   git clone https://github.com/TahaRhoulam/cpp-calendar-app.git
   cd cpp-calendar-app
2. Open the project in a Java IDE (for example, IntelliJ IDEA or Eclipse).
3. Configure JavaFX and ICU4J as project dependencies.
4. Ensure the source file PerpetualCalendarGUI.java is in the perpetualcalendar package (for example, under src/perpetualcalendar/).
5. Run the perpetualcalendar.PerpetualCalendarGUI main class with the JavaFX runtime configured.
Example usage
Gregorian to lunar
1. Enter a valid Gregorian year, month, and day.
2. Select Gregorian → Lunar.
3. Read the lunar date and leap-month indicator in the output area.
Lunar to Gregorian
1. Enter the lunar year, month, and day.
2. Set Leap? to 1 for a leap month, or 0 otherwise.
3. Select Lunar → Gregorian.
Monthly view
1. Enter a Gregorian year and month.
2. Select View Month to see the month's dates.
Project structure
cpp-calendar-app/
├── PerpetualCalendarGUI.java   # JavaFX UI, conversion logic, monthly view
└── README.md                   # Project documentation
Implementation notes
- java.time.LocalDate is used for Gregorian date creation and month calculations.
- ICU4J's ChineseCalendar handles Chinese lunar calendar conversion and leap-month information.
- JavaFX provides the input fields, action buttons, and results panel.
- The monthly view is generated with StringBuilder.
Current limitations and possible improvements
- Add Maven or Gradle configuration for reproducible dependency installation and builds.
- Add automated tests for date conversion, leap months, and invalid input.
- Improve lunar-date validation and provide more specific error messages.
- Add a visual calendar grid and optional date selection.
- Add screenshots or a short demo GIF.
Author
Taha Rhoulam
GitHub · LinkedIn
