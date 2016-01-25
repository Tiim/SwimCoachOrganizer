package ch.tiim.sco.gui.events;

public abstract class Event<T> {
    private final T obj;

    public Event(T obj) {
        this.obj = obj;
    }

    public T getObj() {
        return obj;
    }
}
