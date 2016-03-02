package ch.tiim.sco.database.model;


import java.time.LocalDate;
import java.util.Objects;

public class Training implements Model {

    private Integer id;
    private LocalDate date;
    private Team team;
    private ScheduleRule schedule;

    public Training(int id, LocalDate date, Team team, ScheduleRule schedule) {
        this(date, team, schedule);
        this.id = id;
    }

    public Training(LocalDate date, Team team, ScheduleRule schedule) {
        this.date = date;
        this.team = team;
        this.schedule = schedule;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training that = (Training) o;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.date, that.date);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", date.toString(), team == null ? "No Team" : team.getName());
    }

    @Override
    public String uiString() {
        return toString();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
