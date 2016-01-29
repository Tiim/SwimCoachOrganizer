package ch.tiim.sco.event;

import ch.tiim.sco.database.model.Swimmer;
import ch.tiim.sco.util.Mailto;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmailEvent extends Task<String> {

    private static final String CREDIT = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n~Email made by SwimCoachOrganizer";
    private List<Swimmer> swimmers;

    public EmailEvent(List<Swimmer> swimmers) {
        this.swimmers = new ArrayList<>(swimmers.size());
        for (Swimmer s : swimmers) {
            this.swimmers.add(new Swimmer(s));
        }
    }

    @Override
    protected String call() throws Exception {
        Mailto mailto = new Mailto();
        mailto.addRecipients(swimmers.stream().map(Swimmer::getEmail).collect(Collectors.toList()));
        mailto.setBody(CREDIT);
        return mailto.build();
    }
}
