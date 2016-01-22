package ch.tiim.sco.gui.events;

import ch.tiim.sco.database.model.Training;
import ch.tiim.sco.gui.dialog.DialogView;
import ch.tiim.sco.gui.dialog.TrainingDialog;
import javafx.stage.Stage;

public abstract class TrainingEvent {

    private final Training training;

    public TrainingEvent(Training training) {
        this.training = training;
    }

    public Training getTraining() {
        return training;
    }

    public static class TrainingOpenEvent extends TrainingEvent implements OpenEvent {
        private final Stage parent;

        public TrainingOpenEvent(Training training, Stage parent) {
            super(training);
            this.parent = parent;
        }

        @Override
        public Class<? extends DialogView> getDialog() {
            return TrainingDialog.class;
        }

        @Override
        public Stage getParent() {
            return parent;
        }
    }

    public static class TrainingSaveEvent extends TrainingEvent {
        public TrainingSaveEvent(Training training) {
            super(training);
        }
    }

    public static class TrainingDeleteEvent extends TrainingEvent {
        public TrainingDeleteEvent(Training training) {
            super(training);
        }
    }
}
