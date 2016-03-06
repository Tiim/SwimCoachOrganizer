package ch.tiim.sco.database.model;

import ch.tiim.sco.util.lang.ResourceBundleEx;

import java.util.Objects;

public class Club implements Model {
    private Integer id;
    private String name;
    private String nameShort;
    private String nameEn;
    private String nameShortEn;
    private String code;
    private String nationality;
    private int externId;

    public Club() {
    }

    public Club(int id, String name, String nameShort, String nameEn, String nameShortEn, String code, String nationality, int externId) {
        this(name, nameShort, nameEn, nameShortEn, code, nationality, externId);
        this.id = id;
    }

    public Club(String name, String nameShort, String nameEn, String nameShortEn, String code, String nationality, int externId) {
        this.name = name;
        this.nameShort = nameShort;
        this.nameEn = nameEn;
        this.nameShortEn = nameShortEn;
        this.code = code;
        this.nationality = nationality;
        this.externId = externId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Club that = (Club) o;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.nameShort, that.nameShort) &&
                Objects.equals(this.nameEn, that.nameEn) &&
                Objects.equals(this.nameShortEn, that.nameShortEn) &&
                Objects.equals(this.code, that.code) &&
                Objects.equals(this.nationality, that.nationality) &&
                Objects.equals(this.externId, that.externId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameShort, nameEn, nameShortEn, code, nationality, externId);
    }

    @Override
    public String uiString(ResourceBundleEx lang) {
        return name;
    }

    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameShort='" + nameShort + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", nameShortEn='" + nameShortEn + '\'' +
                ", code='" + code + '\'' +
                ", nationality='" + nationality + '\'' +
                ", externId=" + externId +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExternId() {
        return externId;
    }

    public void setExternId(int externId) {
        this.externId = externId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameShortEn() {
        return nameShortEn;
    }

    public void setNameShortEn(String nameShortEn) {
        this.nameShortEn = nameShortEn;
    }

    public String getNameShort() {
        return nameShort;
    }

    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
