package ch.tiim.sco.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BirthdayUtil {

    public static int daysUntilBirthday(LocalDate birthday) {
        LocalDate now = LocalDate.now();
        LocalDate next = LocalDate.of(now.getYear(), birthday.getMonth(), birthday.getDayOfMonth());
        long until = ChronoUnit.DAYS.between(now, next);
        if (until < 0) {
            next = LocalDate.of(now.getYear() + 1, birthday.getMonth(), birthday.getDayOfMonth());
        }
        return (int) ChronoUnit.DAYS.between(now, next);
    }

}
