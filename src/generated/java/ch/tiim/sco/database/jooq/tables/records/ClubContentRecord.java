/**
 * This class is generated by jOOQ
 */
package ch.tiim.sco.database.jooq.tables.records;


import ch.tiim.sco.database.jooq.tables.ClubContent;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row;
import org.jooq.Row2;
import org.jooq.impl.TableRecordImpl;


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
public class ClubContentRecord extends TableRecordImpl<ClubContentRecord> implements Record2<Integer, Integer> {

	private static final long serialVersionUID = 1793941647;

	/**
	 * Setter for <code>club_content.club_id</code>.
	 */
	public void setClubId(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>club_content.club_id</code>.
	 */
	public Integer getClubId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>club_content.team_id</code>.
	 */
	public void setTeamId(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>club_content.team_id</code>.
	 */
	public Integer getTeamId() {
		return (Integer) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, Integer> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, Integer> valuesRow() {
		return (Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return ClubContent.CLUB_CONTENT.CLUB_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return ClubContent.CLUB_CONTENT.TEAM_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getClubId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getTeamId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClubContentRecord value1(Integer value) {
		setClubId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClubContentRecord value2(Integer value) {
		setTeamId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClubContentRecord values(Integer value1, Integer value2) {
		value1(value1);
		value2(value2);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ClubContentRecord
	 */
	public ClubContentRecord() {
		super(ClubContent.CLUB_CONTENT);
	}

	/**
	 * Create a detached, initialised ClubContentRecord
	 */
	public ClubContentRecord(Integer clubId, Integer teamId) {
		super(ClubContent.CLUB_CONTENT);

		setValue(0, clubId);
		setValue(1, teamId);
	}
}