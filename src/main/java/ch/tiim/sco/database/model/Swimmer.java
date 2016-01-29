package ch.tiim.sco.database.model;

import java.time.LocalDate;
import java.util.Objects;

public class Swimmer implements Model {
    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate birthDay;
    private String address;
    private String phonePrivate;
    private String phoneWork;
    private String phoneMobile;
    private String email;
    private String license;
    private boolean isFemale;
    private String notes;

    public Swimmer() {
    }

    public Swimmer(Swimmer s) {
        this(s.id,
                s.firstName,
                s.lastName,
                s.birthDay,
                s.address,
                s.phonePrivate,
                s.phoneWork,
                s.phoneMobile,
                s.email,
                s.license,
                s.isFemale,
                s.notes);
    }

    public Swimmer(int id, String firstName, String lastName, LocalDate birthDay, String address, String phonePrivate, String phoneWork, String phoneMobile, String email, String license, boolean isFemale, String notes) {
        this(firstName, lastName, birthDay, address, phonePrivate, phoneWork, phoneMobile, email, license, isFemale, notes);
        this.id = id;
    }

    public Swimmer(String firstName, String lastName, LocalDate birthDay, String address, String phonePrivate, String phoneWork, String phoneMobile, String email, String license, boolean isFemale, String notes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.address = address;
        this.phonePrivate = phonePrivate;
        this.phoneWork = phoneWork;
        this.phoneMobile = phoneMobile;
        this.email = email;
        this.license = license;
        this.isFemale = isFemale;
        this.notes = notes;
    }

    @Override
    public String uiString() {
        return firstName + " " + lastName + " [" + (isFemale ? "f" : "m") + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, birthDay, address, phonePrivate, phoneWork, phoneMobile, email,
                license, isFemale, notes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Swimmer that = (Swimmer) o;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.firstName, that.firstName) &&
                Objects.equals(this.lastName, that.lastName) &&
                Objects.equals(this.birthDay, that.birthDay) &&
                Objects.equals(this.address, that.address) &&
                Objects.equals(this.phonePrivate, that.phonePrivate) &&
                Objects.equals(this.phoneWork, that.phoneWork) &&
                Objects.equals(this.phoneMobile, that.phoneMobile) &&
                Objects.equals(this.email, that.email) &&
                Objects.equals(this.license, that.license) &&
                Objects.equals(this.isFemale, that.isFemale) &&
                Objects.equals(this.notes, that.notes);
    }

    @Override
    public String toString() {
        return "Swimmer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDay=" + birthDay +
                ", address='" + address + '\'' +
                ", phonePrivate='" + phonePrivate + '\'' +
                ", phoneWork='" + phoneWork + '\'' +
                ", phoneMobile='" + phoneMobile + '\'' +
                ", email='" + email + '\'' +
                ", license='" + license + '\'' +
                ", isFemale=" + isFemale +
                ", notes='" + notes + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getPhonePrivate() {
        return phonePrivate;
    }

    public void setPhonePrivate(String phonePrivate) {
        this.phonePrivate = phonePrivate;
    }

    public String getPhoneWork() {
        return phoneWork;
    }

    public void setPhoneWork(String phoneWork) {
        this.phoneWork = phoneWork;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public void setIsFemale(boolean isFemale) {
        this.isFemale = isFemale;
    }
}
