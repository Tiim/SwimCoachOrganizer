package ch.tiim.sco.util;

import javafx.util.Pair;

public class NameablePair<V, K> extends Pair<V, K> {

    private String name;

    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public NameablePair(V key, K value) {
        super(key, value);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
