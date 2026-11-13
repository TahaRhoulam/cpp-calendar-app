package perpetualcalendar;

import com.ibm.icu.util.ChineseCalendar;
import com.ibm.icu.util.Calendar;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/** Calendar conversions backed by ICU4J; UI-independent and testable. */
public final class CalendarService {
    private CalendarService() {}
    public record LunarDate(int year, int month, int day, boolean leapMonth) {}

    public static LunarDate toLunar(LocalDate date) {
        ChineseCalendar cc = new ChineseCalendar();
        cc.setTime(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return new LunarDate(cc.get(Calendar.EXTENDED_YEAR) - 2637,
                cc.get(Calendar.MONTH) + 1, cc.get(Calendar.DAY_OF_MONTH),
                cc.get(Calendar.IS_LEAP_MONTH) == 1);
    }

    public static LocalDate toGregorian(int year, int month, int day, boolean leap) {
        if (year < 1 || year > 9999 || month < 1 || month > 12 || day < 1 || day > 30)
            throw new IllegalArgumentException("Enter a valid lunar year, month and day.");
        ChineseCalendar cc = new ChineseCalendar();
        cc.clear();
        cc.setLenient(false);
        cc.set(Calendar.EXTENDED_YEAR, year + 2637);
        cc.set(Calendar.MONTH, month - 1);
        cc.set(Calendar.IS_LEAP_MONTH, leap ? 1 : 0);
        cc.set(Calendar.DAY_OF_MONTH, day);
        Date converted;
        try { converted = cc.getTime(); }
        catch (IllegalArgumentException ex) { throw new IllegalArgumentException("That lunar date does not exist."); }
        LocalDate result = converted.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LunarDate check = toLunar(result);
        if (check.year() != year || check.month() != month || check.day() != day || check.leapMonth() != leap)
            throw new IllegalArgumentException("That lunar date does not exist (check leap month and day).");
        return result;
    }
}
