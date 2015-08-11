package ch.tiim.trainingmanager.lenex;

import ch.tiim.trainingmanager.lenex.adapder.SwimTimeAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "SPLIT")
public class Split {
    @XmlAttribute(name = "distance", required = true)
    private int distance;
    @XmlJavaTypeAdapter(SwimTimeAdapter.class)
    @XmlAttribute(name = "swimtime", required = true)
    private SwimTime swimTime;
}
