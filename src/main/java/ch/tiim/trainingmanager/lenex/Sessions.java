package ch.tiim.trainingmanager.lenex;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "SESSIONS")
public class Sessions {
    @XmlElement(name = "SESSION")
    private List<Session> sessions;
}
