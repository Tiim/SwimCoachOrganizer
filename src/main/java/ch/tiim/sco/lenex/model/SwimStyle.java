package ch.tiim.sco.lenex.model;

import ch.tiim.sco.database.model.Model;
import ch.tiim.sco.util.lang.ResourceBundleEx;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "SWIMSTYLE")
public class SwimStyle implements Model {
    @XmlAttribute(name = "code")
    public String code;
    @XmlAttribute(name = "distance", required = true)
    public int distance;
    @XmlAttribute(name = "name")
    public String name;
    @XmlAttribute(name = "relaycount", required = true)
    public int relaycount;
    @XmlAttribute(name = "stroke", required = true)
    public Stroke stroke;
    @XmlAttribute(name = "swimstyleid")
    public int swimstyleid;
    @XmlAttribute(name = "technique")
    public Technique technique;

    @Override
    public String uiString(ResourceBundleEx lang) {
        StringBuilder b = new StringBuilder();
        if (name != null) {
            return name;
        }
        b.append(Integer.toString(distance)).append(lang.str("model.lenex.swimstyle.meter")).append(" ");
        if (stroke != Stroke.UNKNOWN) {
            b.append(stroke);
        } else {
            b.append(code);
        }
        if (relaycount == 1 && stroke == Stroke.MEDLEY) {
            b.append(" ").append(lang.str("model.lenex.swimstyle.individual"));
        } else if (relaycount > 1) {
            b.append(" ").append(lang.str("model.lenex.swimstyle.relay")).append("(").append(relaycount).append(") ");
        }
        if (technique != null) {
            b.append(technique).append(" ").append(lang.str("model.lenex.swimstyle.only"));
        }
        return b.toString();
    }

    @XmlType
    @XmlEnum
    public enum Technique {
        DIVE,
        GLIDE,
        KICK,
        PULL,
        START,
        TURN
    }
}
