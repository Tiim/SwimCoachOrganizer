package ch.tiim.sco.lenex.model;

import ch.tiim.sco.database.model.Model;
import ch.tiim.sco.util.lang.ResourceBundleEx;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "AGEGROUP")
public class AgeGroup implements Model {

    @XmlAttribute(name = "agegroupid", required = true)
    public int agegroup;
    @XmlAttribute(name = "agemax", required = true)
    public int agemax;
    @XmlAttribute(name = "agemin", required = true)
    public int agemin;
    @XmlAttribute(name = "gender")
    public Gender gender;
    @XmlAttribute(name = "calculate")
    public Calculate calculate;
    /**
     * Only 1-15, 20,34,49 allowed
     */
    @XmlAttribute(name = "handicap")
    public int handicap;
    @XmlAttribute(name = "levelmax")
    public String levelmax;
    @XmlAttribute(name = "levelmin")
    public String levelmin;
    @XmlAttribute(name = "levels")
    public String levels;
    @XmlAttribute(name = "mame")
    public String name;
    @XmlElement(name = "RANKINGS")
    public Rankings rankings;

    @Override
    public String uiString(ResourceBundleEx lang) {
        StringBuilder b = new StringBuilder();
        String year = lang.str("model.lenex.year.short");
        if (agemin > 0) {
            b.append(agemin).append(year);
        }
        b.append(" - ");
        if (agemax > 0) {
            b.append(agemax).append(year);
        }
        if (name != null) {
            b.append(" ").append(name);
        }
        if (handicap != 0) {
            b.append(lang.str("model.lenex.handy_cap.short")).append(handicap);
        }
        return b.toString();
    }

    @XmlType
    @XmlEnum
    public enum Calculate {
        @XmlEnumValue("SINGLE")SINGLE,
        @XmlEnumValue("TOTAL")TOTAL
    }
}
