package ch.tiim.sco.database.model;


import java.util.Objects;

public class Set implements Model {
    private Integer id;
    private String name;
    private String content;
    private int distance1;
    private int distance2;
    private int distance3;
    private int intensity;
    private SetFocus focus;
    private SetStroke stroke;
    private String notes;
    private int interval;
    private boolean isPause;

    public Set() {

    }

    public Set(int id, String name, String content, int distance1, int distance2, int distance3, int intensity, SetFocus focus, SetStroke stroke, String notes, int interval, boolean isPause) {
        this(name, content, distance1, distance2, distance3, intensity, focus, stroke, notes, interval, isPause);
        this.id = id;
    }

    public Set(String name, String content, int distance1, int distance2, int distance3, int intensity, SetFocus focus, SetStroke stroke, String notes, int interval, boolean isPause) {
        this.name = name;
        this.content = content;
        this.distance1 = normalize(distance1);
        this.distance2 = normalize(distance2);
        this.distance3 = normalize(distance3);
        this.intensity = intensity;
        this.focus = focus;
        this.stroke = stroke;
        this.notes = notes;
        this.interval = interval;
        this.isPause = isPause;
    }

    private static int normalize(int i) {
        if (i <= 0) {
            return 1;
        }
        return i;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, content, distance1, distance2,
                distance3, intensity, focus, stroke, notes, interval, isPause);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Set that = (Set) o;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.content, that.content) &&
                Objects.equals(this.distance1, that.distance1) &&
                Objects.equals(this.distance2, that.distance2) &&
                Objects.equals(this.distance3, that.distance3) &&
                Objects.equals(this.intensity, that.intensity) &&
                Objects.equals(this.focus, that.focus) &&
                Objects.equals(this.stroke, that.stroke) &&
                Objects.equals(this.notes, that.notes) &&
                Objects.equals(this.interval, that.interval) &&
                Objects.equals(this.isPause, that.isPause);
    }

    @Override
    public String toString() {
        return "Set{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", distance1=" + distance1 +
                ", distance2=" + distance2 +
                ", distance3=" + distance3 +
                ", intensity=" + intensity +
                ", focus=" + focus +
                ", stroke=" + stroke +
                ", notes='" + notes + '\'' +
                ", interval=" + interval +
                ", isPause=" + isPause +
                '}';
    }

    @Override
    public String uiString() {
        return name + " [" + getDistance() + "]";
    }

    public String getDistance() {
        StringBuilder b = new StringBuilder();
        if (getDistance1() > 1) {
            b.append(getDistance1()).append("x");
        }
        if (getDistance2() > 1) {
            b.append(getDistance2()).append("x");
        }
        b.append(getDistance3()).append("m");
        return b.toString();
    }

    public int getDistance1() {
        return distance1;
    }

    public void setDistance1(int distance1) {
        this.distance1 = normalize(distance1);
    }

    public int getDistance2() {
        return distance2;
    }

    public void setDistance2(int distance2) {
        this.distance2 = normalize(distance2);
    }

    public int getDistance3() {
        return distance3;
    }

    public void setDistance3(int distance3) {
        this.distance3 = normalize(distance3);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SetFocus getFocus() {
        return focus;
    }

    public void setFocus(SetFocus focus) {
        this.focus = focus;
    }

    public SetStroke getStroke() {
        return stroke;
    }

    public void setStroke(SetStroke stroke) {
        this.stroke = stroke;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public String getIntervalString() {
        if (isPause) {
            return "P: " + getInterval();
        } else {
            return "@: " + getInterval();
        }
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean getIsPause() {
        return isPause;
    }

    public void setIsPause(boolean isPause) {
        this.isPause = isPause;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getTotalDistance() {
        return distance1 * distance2 * distance3;
    }

    public boolean isPause() {
        return isPause;
    }
}
