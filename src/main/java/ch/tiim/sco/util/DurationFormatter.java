package ch.tiim.sco.util;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This formatter is used to format a duration into a easy human readable form
 * and to parse such a string back into a duration.
 */
public class DurationFormatter {
    private static final Pattern PATTERN = Pattern.compile("(-?)(?:(?:(\\d+):)?(\\d{1,2}):)?(\\d{1,2})\\.(\\d{1,2})");
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final long NANOS_PER_SECOND = 1_000_000_000L;

    /**
     * Returns a string in the format "hh:mm:ss.nn"
     * but without leading zeroes and in the case of hours with additional digits.
     * the format is stripped to the smallest possible form.
     *
     * @param d Duration to format
     * @return String that represents d in human readable format
     */
    public static String format(Duration d) {
        if (d == null || d.isZero()) {
            return "0.00";
        }
        boolean neg = d.isNegative();
        d = d.abs();
        long hours = d.getSeconds() / SECONDS_PER_HOUR;
        int minutes = (int) ((d.getSeconds() % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
        int secs = (int) (d.getSeconds() % SECONDS_PER_MINUTE);
        int zenti = (int) ((d.getNano() % NANOS_PER_SECOND) / (NANOS_PER_SECOND / 100));

        StringBuilder b = new StringBuilder();

        if (neg) {
            b.append('-');
        }
        if (hours > 0) {
            b.append(hours).append(':');
        }
        if (minutes > 0) {
            if (hours > 0) {
                b.append(String.format("%02d", minutes));
            } else {
                b.append(minutes);
            }
            b.append(':').append(String.format("%02d", secs));
        } else if (hours > 0) {
            b.append("00:");
            b.append(String.format("%02d", secs));

        } else {
            b.append(secs);
        }
        b.append('.').append(String.format("%02d", zenti));
        return b.toString();
    }

    public static Duration parse(String d) {
        Matcher m = PATTERN.matcher(d);
        if (!m.matches()) {
            throw new IllegalArgumentException(d + " can't be parsed");
        }
        long duration = 0L;
        String s;
        boolean pos = m.group(1).isEmpty();
        if ((s = m.group(2)) != null) {
            duration += Long.parseLong(s) * 60L * 60L * 1000L;
        }
        if ((s = m.group(3)) != null) {
            duration += Long.parseLong(s) * 60L * 1000L;
        }
        duration += Long.parseLong(m.group(4)) * 1000L;
        duration += Long.parseLong(m.group(5)) * 10L;
        return Duration.ofMillis((pos ? 1 : -1) * duration);
    }
}
