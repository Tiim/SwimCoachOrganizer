package ch.tiim.sco.event;

public class ShowDocumentEvent {

    private final String document;

    public ShowDocumentEvent(String document) {
        this.document = document;
    }

    public String getDocument() {
        return document;
    }
}
