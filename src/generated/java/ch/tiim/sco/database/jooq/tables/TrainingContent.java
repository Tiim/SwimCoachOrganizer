/**
 * This class is generated by jOOQ
 */
package ch.tiim.sco.database.jooq.tables;


import ch.tiim.sco.database.jooq.DefaultSchema;
import ch.tiim.sco.database.jooq.Keys;
import ch.tiim.sco.database.jooq.tables.records.TrainingContentRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Table;
import org.jooq.TableField;
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
public class TrainingContent extends TableImpl<TrainingContentRecord> {

	private static final long serialVersionUID = -1431559822;

	/**
	 * The reference instance of <code>training_content</code>
	 */
	public static final TrainingContent TRAINING_CONTENT = new TrainingContent();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<TrainingContentRecord> getRecordType() {
		return TrainingContentRecord.class;
	}

	/**
	 * The column <code>training_content.training_id</code>.
	 */
	public final TableField<TrainingContentRecord, Integer> TRAINING_ID = createField("training_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>training_content.set_id</code>.
	 */
	public final TableField<TrainingContentRecord, Integer> SET_ID = createField("set_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>training_content.indx</code>.
	 */
	public final TableField<TrainingContentRecord, Integer> INDX = createField("indx", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>training_content</code> table reference
	 */
	public TrainingContent() {
		this("training_content", null);
	}

	/**
	 * Create an aliased <code>training_content</code> table reference
	 */
	public TrainingContent(String alias) {
		this(alias, TRAINING_CONTENT);
	}

	private TrainingContent(String alias, Table<TrainingContentRecord> aliased) {
		this(alias, aliased, null);
	}

	private TrainingContent(String alias, Table<TrainingContentRecord> aliased, Field<?>[] parameters) {
		super(alias, DefaultSchema.DEFAULT_SCHEMA, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<TrainingContentRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<TrainingContentRecord, ?>>asList(Keys.FK_TRAINING_CONTENT_TRAINING_1, Keys.FK_TRAINING_CONTENT_SETS_1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TrainingContent as(String alias) {
		return new TrainingContent(alias, this);
	}

	/**
	 * Rename this table
	 */
	public TrainingContent rename(String name) {
		return new TrainingContent(name, null);
	}
}
