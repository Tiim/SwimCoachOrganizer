/**
 * This class is generated by jOOQ
 */
package ch.tiim.sco.database.jooq.tables;


import ch.tiim.sco.database.jooq.DefaultSchema;
import ch.tiim.sco.database.jooq.Keys;
import ch.tiim.sco.database.jooq.tables.records.TeamMemberRecord;
import ch.tiim.sco.database.mapper.LocalDateConverter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.1"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TeamMember extends TableImpl<TeamMemberRecord> {

	private static final long serialVersionUID = 420806333;

	/**
	 * The reference instance of <code>team_member</code>
	 */
	public static final TeamMember TEAM_MEMBER = new TeamMember();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<TeamMemberRecord> getRecordType() {
		return TeamMemberRecord.class;
	}

	/**
	 * The column <code>team_member.member_id</code>.
	 */
	public final TableField<TeamMemberRecord, Integer> MEMBER_ID = createField("member_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>team_member.first_name</code>.
	 */
	public final TableField<TeamMemberRecord, String> FIRST_NAME = createField("first_name", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

	/**
	 * The column <code>team_member.last_name</code>.
	 */
	public final TableField<TeamMemberRecord, String> LAST_NAME = createField("last_name", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

	/**
	 * The column <code>team_member.birth_day</code>.
	 */
	public final TableField<TeamMemberRecord, LocalDate> BIRTH_DAY = createField("birth_day", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "", new LocalDateConverter());

	/**
	 * The column <code>team_member.address</code>.
	 */
	public final TableField<TeamMemberRecord, String> ADDRESS = createField("address", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>team_member.phone_private</code>.
	 */
	public final TableField<TeamMemberRecord, String> PHONE_PRIVATE = createField("phone_private", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>team_member.phone_work</code>.
	 */
	public final TableField<TeamMemberRecord, String> PHONE_WORK = createField("phone_work", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>team_member.phone_mobile</code>.
	 */
	public final TableField<TeamMemberRecord, String> PHONE_MOBILE = createField("phone_mobile", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>team_member.email</code>.
	 */
	public final TableField<TeamMemberRecord, String> EMAIL = createField("email", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>team_member.license</code>.
	 */
	public final TableField<TeamMemberRecord, String> LICENSE = createField("license", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>team_member.is_female</code>.
	 */
	public final TableField<TeamMemberRecord, Boolean> IS_FEMALE = createField("is_female", org.jooq.impl.SQLDataType.BOOLEAN, this, "");

	/**
	 * The column <code>team_member.notes</code>.
	 */
	public final TableField<TeamMemberRecord, String> NOTES = createField("notes", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * Create a <code>team_member</code> table reference
	 */
	public TeamMember() {
		this("team_member", null);
	}

	/**
	 * Create an aliased <code>team_member</code> table reference
	 */
	public TeamMember(String alias) {
		this(alias, TEAM_MEMBER);
	}

	private TeamMember(String alias, Table<TeamMemberRecord> aliased) {
		this(alias, aliased, null);
	}

	private TeamMember(String alias, Table<TeamMemberRecord> aliased, Field<?>[] parameters) {
		super(alias, DefaultSchema.DEFAULT_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<TeamMemberRecord> getPrimaryKey() {
		return Keys.PK_TEAM_MEMBER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<TeamMemberRecord>> getKeys() {
		return Arrays.<UniqueKey<TeamMemberRecord>>asList(Keys.PK_TEAM_MEMBER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TeamMember as(String alias) {
		return new TeamMember(alias, this);
	}

	/**
	 * Rename this table
	 */
	public TeamMember rename(String name) {
		return new TeamMember(name, null);
	}
}