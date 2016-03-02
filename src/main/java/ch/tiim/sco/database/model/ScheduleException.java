package ch.tiim.sco.database.model;

import java.time.LocalDate;
import java.util.Objects;

public class ScheduleException {

    private Integer id;
    private ScheduleRule schedule;
    private LocalDate day;

    public ScheduleException(int id, ScheduleRule schedule, LocalDate day) {
        this(schedule, day);
    }

    public ScheduleException(ScheduleRule schedule, LocalDate day) {
        this.schedule = schedule;
        this.day = day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, schedule, day);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleException)) return false;
        ScheduleException that = (ScheduleException) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(schedule, that.schedule) &&
                Objects.equals(day, that.day);
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ScheduleRule getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleRule schedule) {
        this.schedule = schedule;
    }
}
