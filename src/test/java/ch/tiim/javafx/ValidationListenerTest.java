package ch.tiim.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class ValidationListenerTest {

    private Parent parent;

    private ValidationListener listener;

    @Before
    public void setUp() throws Exception {
        parent = new MockParent();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testChangedToNull() throws Exception {
        listener = new ValidationListener(".*", parent);
        listener.changed(new SimpleStringProperty("New Value"), "Old value", null);
        assertThat(parent.getPseudoClassStates(), IsNot.not(CoreMatchers.hasItems(ValidationListener.ERROR_CLASS)));
    }
}