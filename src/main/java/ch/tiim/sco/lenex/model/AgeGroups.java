package ch.tiim.sco.lenex.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name = "AGEGROUPS")
public class AgeGroups {
    @XmlElement(name = "AGEGROUP")
    public List<AgeGroup> ageGroups;
}
