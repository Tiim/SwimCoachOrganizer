package ch.tiim.sco.database.model;


import ch.tiim.sco.util.lang.ResourceBundleEx;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ScheduleRule implements Model {

    private Integer id;
    private LocalDate start;
    private LocalTime time;
    private int interval;
    private int duration;
    private Team team;

    public ScheduleRule(Integer id, LocalDate start, LocalTime time, int interval, int duration, Team team) {
        this(start, time, interval, duration, team);
        this.id = id;
    }

    public ScheduleRule(LocalDate start, LocalTime time, int interval, int duration, Team team) {
        this.start = start;
        this.time = time;
        this.interval = interval;
        this.duration = duration;
        this.team = team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, time, interval, duration, team);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ScheduleRule)) return false;
        ScheduleRule o = (ScheduleRule) obj;
        return Objects.equals(start, o.start) &&
                Objects.equals(time, o.time) &&
                Objects.equals(interval, o.interval) &&
                Objects.equals(duration, o.duration) &&
                Objects.equals(team, o.team);
    }

    @Override
    public String uiString(ResourceBundleEx lang) {
        if (interval == 1) {
            return String.format(lang.str("model.schedule.format.day"), time,
                    duration / 60, duration % 60);
        } else if (interval == 7) {
            return String.format(lang.str("model.schedule.format.week"), start.getDayOfWeek(), time,
                    duration / 60, duration % 60);
        } else if (interval % 7 == 0) {
            return String.format(lang.str("model.schedule.format.week.mul"), interval / 7, start.getDayOfWeek(), time,
                    duration / 60, duration % 60);
        } else {
            return String.format(lang.str("model.schedule.format.day.mul"),
                    interval, time, duration / 60, duration % 60, start);
        }
    }

    @Override
    public String toString() {
        return "ScheduleRule{" +
                "id=" + id +
                ", start=" + start +
                ", time=" + time +
                ", interval=" + interval +
                ", duration=" + duration +
                ", team=" + team +
                '}';
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
