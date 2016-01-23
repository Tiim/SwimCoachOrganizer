package ch.tiim.sco.gui.events;

import ch.tiim.sco.database.model.Team;
import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.dialog.TeamDialog;
import javafx.stage.Stage;

public class TeamEvent {
    private final Team team;

    public TeamEvent(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public static class TeamDeleteEvent extends TeamEvent {

        public TeamDeleteEvent(Team team) {
            super(team);
        }
    }

    public static class TeamOpenEvent extends TeamEvent implements OpenEvent {

        private final Stage stage;

        public TeamOpenEvent(Team team, Stage stage) {
            super(team);
            this.stage = stage;
        }

        @Override
        public Class<? extends DialogView> getDialog() {
            return TeamDialog.class;
        }

        @Override
        public Stage getParent() {
            return stage;
        }
    }

    public static class TeamSaveEvent extends TeamEvent {

        public TeamSaveEvent(Team team) {
            super(team);
        }
    }
}
