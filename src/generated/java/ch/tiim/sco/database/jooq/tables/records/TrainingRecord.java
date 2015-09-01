/**
 * This class is generated by jOOQ
 */
package ch.tiim.sco.database.jooq.tables.records;


import ch.tiim.sco.database.jooq.tables.Training;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


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
public class TrainingRecord extends UpdatableRecordImpl<TrainingRecord> implements Record2<Integer, String> {

	private static final long serialVersionUID = -547589668;

	/**
	 * Setter for <code>training.training_id</code>.
	 */
	public void setTrainingId(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>training.training_id</code>.
	 */
	public Integer getTrainingId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>training.name</code>.
	 */
	public void setName(String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>training.name</code>.
	 */
	public String getName() {
		return (String) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, String> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, String> valuesRow() {
		return (Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return Training.TRAINING.TRAINING_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return Training.TRAINING.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getTrainingId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value2() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TrainingRecord value1(Integer value) {
		setTrainingId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TrainingRecord value2(String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TrainingRecord values(Integer value1, String value2) {
		value1(value1);
		value2(value2);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TrainingRecord
	 */
	public TrainingRecord() {
		super(Training.TRAINING);
	}

	/**
	 * Create a detached, initialised TrainingRecord
	 */
	public TrainingRecord(Integer trainingId, String name) {
		super(Training.TRAINING);

		setValue(0, trainingId);
		setValue(1, name);
	}
}
