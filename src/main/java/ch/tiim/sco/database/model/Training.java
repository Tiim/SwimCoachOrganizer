package ch.tiim.sco.database.model;


import java.util.Objects;

public class Training implements Model {

    private Integer id;
    private String name;

    public Training(int id, String name) {
        this(name);
        this.id = id;
    }

    public Training(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training that = (Training) o;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String uiString() {
        return toString();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
