package ch.tiim.sco.lenex.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "FEES")
public class Fees {
    @XmlElement(name = "FEE")
    public List<Fee> fees;
}
