package ch.tiim.sco.gui.events;

import ch.tiim.sco.database.model.Club;
import ch.tiim.sco.gui.dialog.ClubDialog;
import ch.tiim.sco.gui.dialog.DialogView;
import javafx.stage.Stage;

public class ClubEvent extends Event<Club> {

    public ClubEvent(Club club) {
        super(club);
    }

    public static class ClubOpenEvent extends ClubEvent implements OpenEvent {

        private final Stage stage;

        public ClubOpenEvent(Club club, Stage stage) {
            super(club);
            this.stage = stage;
        }

        @Override
        public Class<? extends DialogView> getDialog() {
            return ClubDialog.class;
        }

        @Override
        public Stage getParent() {
            return stage;
        }
    }

    public static class ClubSaveEvent extends ClubEvent {
        public ClubSaveEvent(Club club) {
            super(club);
        }
    }
}
