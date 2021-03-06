package ch.tiim.sco.lenex.model;

import ch.tiim.sco.lenex.adapder.SwimTimeAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "ENTRY")
public class Entry {
    @XmlAttribute(name = "agegroupid")
    public int agegroupid;
    @XmlAttribute(name = "entrycourse")
    public Course entrycourse;
    @XmlJavaTypeAdapter(SwimTimeAdapter.class)
    @XmlAttribute(name = "entrytime")
    public SwimTime entrytime;
    @XmlAttribute(name = "eventid", required = true)
    public int eventid;
    @XmlAttribute(name = "heatid")
    public int heatid;
    @XmlAttribute(name = "lane")
    public int lane;
    @XmlElement(name = "MEETINFO")
    public MeetInfoEntry meetinfo;
    @XmlElement(name = "RELAYPOSITIONS")
    public RelayPositions relayPositions;
    @XmlAttribute(name = "status")
    public StatusEntry status;

    @XmlType
    @XmlEnum
    public enum StatusEntry {
        EXH,
        RJC,
        SICK,
        WDR
    }
}
