package ch.tiim.sco.gui.events;

import ch.tiim.sco.database.model.ScheduleRule;
import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.dialog.ScheduleDialog;
import javafx.stage.Stage;

public class ScheduleEvent extends Event<ScheduleRule> {
    public ScheduleEvent(ScheduleRule obj) {
        super(obj);
    }


    public static class ScheduleOpenEvent extends ScheduleEvent implements OpenEvent {

        private final Stage parent;

        public ScheduleOpenEvent(ScheduleRule obj, Stage parent) {
            super(obj);
            this.parent = parent;
        }

        @Override
        public Class<? extends DialogView> getDialog() {
            return ScheduleDialog.class;
        }

        @Override
        public Stage getParent() {
            return parent;
        }
    }

    public static class ScheduleSaveEvent extends ScheduleEvent {

        public ScheduleSaveEvent(ScheduleRule obj) {
            super(obj);
        }
    }

    public static class ScheduleDeleteEvent extends ScheduleEvent {
        public ScheduleDeleteEvent(ScheduleRule item) {
            super(item);
        }
    }

}
