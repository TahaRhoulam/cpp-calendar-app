package perpetualcalendar;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CalendarServiceTest {
    @Test void roundTripGregorianDate() {
        LocalDate original = LocalDate.of(2026, 11, 13);
        CalendarService.LunarDate lunar = CalendarService.toLunar(original);
        assertEquals(original, CalendarService.toGregorian(lunar.year(), lunar.month(), lunar.day(), lunar.leapMonth()));
    }
    @Test void invalidLunarMonthIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> CalendarService.toGregorian(2026, 13, 1, false));
    }
    @Test void invalidLunarDayIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> CalendarService.toGregorian(2026, 1, 31, false));
    }
}
