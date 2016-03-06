package ch.tiim.sco.database.model;


import ch.tiim.sco.util.lang.ResourceBundleEx;

import java.util.Objects;

public class IndexedSet implements Model {
    private int index;
    private Set set;

    public IndexedSet(int index, Set set) {
        this.index = index;
        this.set = set;
    }

    @Override
    public String uiString(ResourceBundleEx lang) {
        return toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, set);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexedSet that = (IndexedSet) o;

        return Objects.equals(this.index, that.index) &&
                Objects.equals(this.set, that.set);

    }

    @Override
    public String toString() {
        return "IndexedSet{" +
                "index=" + index +
                ", set=" + set +
                '}';
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }
}
