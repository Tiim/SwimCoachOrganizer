package ch.tiim.sco.lenex.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "HANDICAP")
public class Handicap {
    @XmlAttribute(name = "breast", required = true)
    public HandicapClass breast;
    @XmlAttribute(name = "exception")
    public String exception;
    @XmlAttribute(name = "free", required = true)
    public HandicapClass free;
    @XmlAttribute(name = "medley", required = true)
    public HandicapClass medley;

    @XmlType
    @XmlEnum
    public enum HandicapClass {
        @XmlEnumValue("1")C1,
        @XmlEnumValue("2")C2,
        @XmlEnumValue("3")C3,
        @XmlEnumValue("4")C4,
        @XmlEnumValue("5")C5,
        @XmlEnumValue("6")C6,
        @XmlEnumValue("7")C7,
        @XmlEnumValue("8")C8,
        @XmlEnumValue("9")C9,
        @XmlEnumValue("10")C10,
        @XmlEnumValue("11")C11,
        @XmlEnumValue("12")C12,
        @XmlEnumValue("13")C13,
        @XmlEnumValue("14")C14,
        @XmlEnumValue("15")C15,
        @XmlEnumValue("GER.AB")GER_AB,
        @XmlEnumValue("GER.GB")GER_GB
    }
}
