package ch.tiim.sco.util;

import javax.annotation.Nonnull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Mailto {

    private static final String MAILTO = "mailto:";

    private List<String> rec = new ArrayList<>();
    private List<String> cc = new ArrayList<>();
    private List<String> bcc = new ArrayList<>();

    private String subject = "";
    private String body = "";

    public Mailto() {

    }

    public Mailto(@Nonnull String recipient) {
        rec.add(recipient);
    }

    public void addRecipients(@Nonnull Collection<String> emails) {
        rec.addAll(emails);
    }

    public void addCcRecipients(@Nonnull Collection<String> emails) {
        cc.addAll(emails);
    }

    public void addBccRecipients(@Nonnull Collection<String> emails) {
        bcc.addAll(emails);
    }

    @Nonnull
    public String build() {
        StringBuilder b = new StringBuilder();
        int nr = 0;
        b.append(MAILTO);
        b.append(joinString(rec));
        if (!cc.isEmpty()) {
            b.append(getDelmiter(nr++)).append("cc=");
        }
        b.append(joinString(cc));
        if (!bcc.isEmpty()) {
            b.append(getDelmiter(nr++)).append("bcc=");
        }
        b.append(joinString(bcc));
        if (!subject.isEmpty()) {
            b.append(getDelmiter(nr++)).append("subject=").append(enc(subject));
        }
        if (!body.isEmpty()) {
            b.append(getDelmiter(nr)).append("body=").append(enc(body));
        }
        return b.toString();
    }

    @Nonnull
    private String joinString(@Nonnull List<String> s) {
        return s.stream().collect(Collectors.joining(","));
    }

    private char getDelmiter(int nr) {
        return nr == 0 ? '?' : '&';
    }

    @Nonnull
    private String enc(@Nonnull String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new OutOfCoffeeException(e);
        }
    }

    public void setBody(@Nonnull String body) {
        this.body = body;
    }

    public void setSubject(@Nonnull String subject) {
        this.subject = subject;
    }
}
