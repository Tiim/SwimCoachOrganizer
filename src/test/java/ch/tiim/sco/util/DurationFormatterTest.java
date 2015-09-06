package ch.tiim.sco.util;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

public class DurationFormatterTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFormatMinute() throws Exception {
        Duration duration = Duration.ofMinutes(1);
        String f = DurationFormatter.format(duration);
        Assert.assertThat(f, CoreMatchers.is("1:00.00"));
    }

    @Test
    public void testFormatHour() throws Exception {
        Duration duration = Duration.ofHours(1);
        String f = DurationFormatter.format(duration);
        Assert.assertThat(f, CoreMatchers.is("1:00:00.00"));
    }

    @Test
    public void testFormatSecond() throws Exception {
        Duration duration = Duration.ofSeconds(1);
        String f = DurationFormatter.format(duration);
        Assert.assertThat(f, CoreMatchers.is("1.00"));
    }

    @Test
    public void testFormatHsec() throws Exception {
        Duration duration = Duration.ofMillis(10);
        String f = DurationFormatter.format(duration);
        Assert.assertThat(f, CoreMatchers.is("0.01"));
    }

    @Test
    public void testFormatNegative() throws Exception {
        Duration duration = Duration.ofMinutes(-1);
        String f = DurationFormatter.format(duration);
        Assert.assertThat(f, CoreMatchers.is("-1:00.00"));
    }

    @Test
    public void testParseMinute() throws Exception {
        Duration d = DurationFormatter.parse("1:00.00");
        Assert.assertThat(d, CoreMatchers.is(Duration.ofMinutes(1)));
    }

    @Test
    public void testParseMinuteShort() throws Exception {
        Duration d = DurationFormatter.parse("1:0.0");
        Assert.assertThat(d, CoreMatchers.is(Duration.ofMinutes(1)));
    }

    @Test
    public void testParseNegative() throws Exception {
        Duration d = DurationFormatter.parse("-1:0.0");
        Assert.assertThat(d, CoreMatchers.is(Duration.ofMinutes(-1)));
    }

    @Test
    public void testParseHour() throws Exception {
        Duration d = DurationFormatter.parse("1:1:0.0");
        Assert.assertThat(d, CoreMatchers.is(Duration.ofMinutes(1).plus(Duration.ofHours(1))));
    }

    @Test
    public void testParseMinuteSecond() throws Exception {
        Duration d = DurationFormatter.parse("50.0");
        Assert.assertThat(d, CoreMatchers.is(Duration.ofSeconds(50)));
    }

    @Test
    public void testParseMinuteHsec() throws Exception {
        Duration d = DurationFormatter.parse("0.01");
        Assert.assertThat(d, CoreMatchers.is(Duration.ofMillis(10)));
    }
}