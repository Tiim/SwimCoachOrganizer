package ch.tiim.sco.database.model;

import java.util.Objects;

public class SetFocus implements Model {

    private Integer id;
    private String name;
    private String abbr;
    private String notes;

    public SetFocus(int id, String name, String abbr, String notes) {
        this(name, abbr, notes);
        this.id = id;
    }

    public SetFocus(String name, String abbr, String notes) {
        this.name = name;
        this.abbr = abbr;
        this.notes = notes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, abbr, notes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetFocus that = (SetFocus) o;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.abbr, that.abbr) &&
                Objects.equals(this.notes, that.notes);
    }

    @Override
    public String toString() {
        return name + " [" + abbr + "]";
    }

    @Override
    public String uiString() {
        return toString();
    }

    public String getAbbr() {
        return abbr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
