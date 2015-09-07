package ch.tiim.sco.database.model;


import java.util.Objects;

public class Team implements Model {
    private Integer id;
    private String name;

    public Team(int id, String name) {
        this(name);
        this.id = id;
    }

    public Team(String name) {
        this.name = name;
    }

    @Override
    public String uiString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team that = (Team) o;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name);

    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
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
}
