package ch.tiim.sco.util;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertThat;

public class MailtoTest {

    @Test
    public void testAddRecipients() throws Exception {
        Mailto m = new Mailto("testos@test.ch");
        assertThat(m.build(), CoreMatchers.is("mailto:testos@test.ch"));
        m.addRecipients(Collections.singletonList("email2@example.ch"));
        assertThat(m.build(), CoreMatchers.is("mailto:testos@test.ch,email2@example.ch"));
    }

    @Test
    public void testAddCcRecipients() throws Exception {
        Mailto m = new Mailto();
        m.addCcRecipients(Collections.singletonList("testos@test.ch"));
        assertThat(m.build(), CoreMatchers.is("mailto:?cc=testos@test.ch"));
        m.addRecipients(Collections.singletonList("email2@example.ch"));
        assertThat(m.build(), CoreMatchers.is("mailto:email2@example.ch?cc=testos@test.ch"));
    }

    @Test
    public void testAddBccRecipients() throws Exception {
        Mailto m = new Mailto();
        m.addCcRecipients(Collections.singletonList("testos@test.ch"));
        m.addRecipients(Collections.singletonList("email2@example.ch"));
        m.addBccRecipients(Arrays.asList("m2@cad.com", "asda@dx.cj"));
        assertThat(m.build(), CoreMatchers.is("mailto:email2@example.ch?cc=testos@test.ch&bcc=m2@cad.com,asda@dx.cj"));
    }

    @Test
    public void testSetSubject() throws Exception {
        Mailto m = new Mailto();
        m.addRecipients(Collections.singletonList("email2@example.ch"));
        m.addBccRecipients(Arrays.asList("m2@cad.com", "asda@dx.cj"));
        m.setSubject("Subject");
        assertThat(m.build(), CoreMatchers.is("mailto:email2@example.ch?bcc=m2@cad.com,asda@dx.cj&subject=Subject"));
    }

    @Test
    public void testSetBody() throws Exception {
        Mailto m = new Mailto();
        m.addRecipients(Collections.singletonList("email2@example.ch"));
        m.setSubject("Subject");
        m.setBody("Hallo, It's me\n" +
                "I was wondering if you would like to meet?");
        assertThat(m.build(), CoreMatchers.is("mailto:email2@example.ch?subject=Subject&" +
                "body=Hallo%2C%20It%27s%20me%0AI%20was%20wondering%20if%20you%20would%20like%20to%20meet%3F"));
    }
}