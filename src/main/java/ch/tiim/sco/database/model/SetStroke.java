package ch.tiim.sco.database.model;


import ch.tiim.sco.util.lang.ResourceBundleEx;

import java.util.Objects;

public class SetStroke implements Model {

    private Integer id;
    private String name;
    private String abbr;
    private String notes;

    public SetStroke(int id, String name, String abbr, String notes) {
        this(name, abbr, notes);
        this.id = id;
    }

    public SetStroke(String name, String abbr, String notes) {
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
        SetStroke that = (SetStroke) o;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.abbr, that.abbr) &&
                Objects.equals(this.notes, that.notes);
    }

    @Override
    public String toString() {
        return "SetStroke{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abbr='" + abbr + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    @Override
    public String uiString(ResourceBundleEx lang) {
        return String.format(lang.str("model.stroke.format"), name, abbr);
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
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

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
