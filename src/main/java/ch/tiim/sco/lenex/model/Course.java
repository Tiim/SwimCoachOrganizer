package ch.tiim.sco.lenex.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum
public enum Course {
    @XmlEnumValue("LCM")LCM,
    @XmlEnumValue("SCM")SCM,
    @XmlEnumValue("SCY")SCY,
    @XmlEnumValue("SCM16")SCM16,
    @XmlEnumValue("SCM20")SCM20,
    @XmlEnumValue("SCM33")SCM33,
    @XmlEnumValue("SCY20")SCY20,
    @XmlEnumValue("SCY27")SCY27,
    @XmlEnumValue("SCY33")SCY33,
    @XmlEnumValue("SCY36")SCY36,
    @XmlEnumValue("OPEN")OPEN;

    public ch.tiim.sco.database.model.Course toCourse() {
        switch (this) {
            case LCM:
                return ch.tiim.sco.database.model.Course.LCM;
            case SCM:
                return ch.tiim.sco.database.model.Course.SCM;
            case SCY:
                return ch.tiim.sco.database.model.Course.SCY;
            case SCM16:
                return ch.tiim.sco.database.model.Course.SCM16;
            case SCM20:
                return ch.tiim.sco.database.model.Course.SCM20;
            case SCM33:
                return ch.tiim.sco.database.model.Course.SCM33;
            case SCY20:
                return ch.tiim.sco.database.model.Course.SCY20;
            case SCY27:
                return ch.tiim.sco.database.model.Course.SCY27;
            case SCY33:
                return ch.tiim.sco.database.model.Course.SCY33;
            case SCY36:
                return ch.tiim.sco.database.model.Course.SCY36;
            case OPEN:
                return ch.tiim.sco.database.model.Course.OPEN;
            default:
                return ch.tiim.sco.database.model.Course.LCM;
        }
    }
}
