package ch.tiim.sco.database.model;

import ch.tiim.sco.util.lang.ResourceBundleEx;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

public class Result implements Model {

    private Integer id;
    private String meet;
    private LocalDate meetDate;
    private Duration swimTime;
    private Duration reactionTime;
    private Stroke stroke;
    private int distance;
    private Course course;


    public Result() {

    }

    public Result(Integer id, String meet, LocalDate meetDate,
                  Duration swimTime, Duration reactionTime, Stroke stroke, int distance, Course course) {
        this(meet, meetDate, swimTime, reactionTime, stroke, distance, course);
        this.id = id;
    }

    public Result(String meet, LocalDate meetDate,
                  Duration swimTime, Duration reactionTime, Stroke stroke, int distance, Course course) {
        this.meet = meet;
        this.meetDate = meetDate;
        this.swimTime = swimTime;
        this.reactionTime = reactionTime;
        this.stroke = stroke;
        this.distance = distance;
        this.course = course;
    }

    @Override
    public String uiString(ResourceBundleEx lang) {
        return String.format(lang.str("model.result.format"), distance, stroke, swimTime, meetDate.toString(), meet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, meet, meetDate, swimTime, reactionTime, stroke, distance);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) return false;
        Result that = (Result) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.meet, that.meet) &&
                Objects.equals(this.meetDate, that.meetDate) &&
                Objects.equals(this.swimTime, that.swimTime) &&
                Objects.equals(this.reactionTime, that.reactionTime) &&
                Objects.equals(this.stroke, that.stroke) &&
                Objects.equals(this.distance, that.distance);
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", meet='" + meet + '\'' +
                ", meetDate=" + meetDate +
                ", swimTime=" + swimTime +
                ", reactionTime=" + reactionTime +
                ", stroke=" + stroke +
                ", distance=" + distance +
                '}';
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMeet() {
        return meet;
    }

    public void setMeet(String meet) {
        this.meet = meet;
    }

    public LocalDate getMeetDate() {
        return meetDate;
    }

    public void setMeetDate(LocalDate meetDate) {
        this.meetDate = meetDate;
    }

    public Duration getReactionTime() {
        return reactionTime;
    }

    public void setReactionTime(Duration reactionTime) {
        this.reactionTime = reactionTime;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public Duration getSwimTime() {
        return swimTime;
    }

    public void setSwimTime(Duration swimTime) {
        this.swimTime = swimTime;
    }
}
