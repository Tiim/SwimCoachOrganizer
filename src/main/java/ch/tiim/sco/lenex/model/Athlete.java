package ch.tiim.sco.lenex.model;

import ch.tiim.sco.lenex.adapder.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlRootElement(name = "ATHLETE")
public class Athlete {
    @XmlAttribute(name = "athleteid", required = true)
    public int athleteid;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlAttribute(name = "birthdate", required = true)
    public LocalDate birthdate;
    @XmlElement(name = "CLUB")
    public Club club;
    @XmlElement(name = "ENTRIES")
    public Entries entries;
    @XmlAttribute(name = "firstname", required = true)
    public String firstname;
    @XmlAttribute(name = "firstname.en")
    public String firstnameEn;
    @XmlAttribute(name = "gender", required = true)
    public Gender gender;
    @XmlElement(name = "HANDICAP")
    public Handicap handicap;
    @XmlAttribute(name = "lastname", required = true)
    public String lastname;
    @XmlAttribute(name = "lastname.en")
    public String lastnameEn;
    @XmlAttribute(name = "level")
    public String level;
    @XmlAttribute(name = "license")
    public String license;
    @XmlAttribute(name = "nameprefix")
    public String nameprefix;
    @XmlAttribute(name = "nation")
    public Nation nation;
    @XmlAttribute(name = "passport")
    public String passport;
    @XmlElement(name = "RESULTS")
    public Results results;
    @XmlAttribute(name = "swrid")
    public int swrid;
}
