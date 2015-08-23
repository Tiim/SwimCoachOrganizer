/**
 * This class is generated by jOOQ
 */
package ch.tiim.sco.database.jooq;


import ch.tiim.sco.database.jooq.tables.Club;
import ch.tiim.sco.database.jooq.tables.ClubContent;
import ch.tiim.sco.database.jooq.tables.SetFocus;
import ch.tiim.sco.database.jooq.tables.SetForm;
import ch.tiim.sco.database.jooq.tables.Sets;
import ch.tiim.sco.database.jooq.tables.Team;
import ch.tiim.sco.database.jooq.tables.TeamContent;
import ch.tiim.sco.database.jooq.tables.TeamMember;
import ch.tiim.sco.database.jooq.tables.Training;
import ch.tiim.sco.database.jooq.tables.TrainingContent;
import ch.tiim.sco.database.jooq.tables.records.ClubContentRecord;
import ch.tiim.sco.database.jooq.tables.records.ClubRecord;
import ch.tiim.sco.database.jooq.tables.records.SetFocusRecord;
import ch.tiim.sco.database.jooq.tables.records.SetFormRecord;
import ch.tiim.sco.database.jooq.tables.records.SetsRecord;
import ch.tiim.sco.database.jooq.tables.records.TeamContentRecord;
import ch.tiim.sco.database.jooq.tables.records.TeamMemberRecord;
import ch.tiim.sco.database.jooq.tables.records.TeamRecord;
import ch.tiim.sco.database.jooq.tables.records.TrainingContentRecord;
import ch.tiim.sco.database.jooq.tables.records.TrainingRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code></code> 
 * schema
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.1"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------


	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<ClubRecord> PK_CLUB = UniqueKeys0.PK_CLUB;
	public static final UniqueKey<SetFocusRecord> PK_SET_FOCUS = UniqueKeys0.PK_SET_FOCUS;
	public static final UniqueKey<SetFormRecord> PK_SET_FORM = UniqueKeys0.PK_SET_FORM;
	public static final UniqueKey<SetsRecord> PK_SETS = UniqueKeys0.PK_SETS;
	public static final UniqueKey<TeamRecord> PK_TEAM = UniqueKeys0.PK_TEAM;
	public static final UniqueKey<TeamMemberRecord> PK_TEAM_MEMBER = UniqueKeys0.PK_TEAM_MEMBER;
	public static final UniqueKey<TrainingRecord> PK_TRAINING = UniqueKeys0.PK_TRAINING;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final ForeignKey<ClubContentRecord, ClubRecord> FK_CLUB_CONTENT_CLUB_1 = ForeignKeys0.FK_CLUB_CONTENT_CLUB_1;
	public static final ForeignKey<ClubContentRecord, TeamRecord> FK_CLUB_CONTENT_TEAM_1 = ForeignKeys0.FK_CLUB_CONTENT_TEAM_1;
	public static final ForeignKey<SetsRecord, SetFocusRecord> FK_SETS_SET_FOCUS_1 = ForeignKeys0.FK_SETS_SET_FOCUS_1;
	public static final ForeignKey<SetsRecord, SetFormRecord> FK_SETS_SET_FORM_1 = ForeignKeys0.FK_SETS_SET_FORM_1;
	public static final ForeignKey<TeamContentRecord, TeamMemberRecord> FK_TEAM_CONTENT_TEAM_MEMBER_1 = ForeignKeys0.FK_TEAM_CONTENT_TEAM_MEMBER_1;
	public static final ForeignKey<TeamContentRecord, TeamRecord> FK_TEAM_CONTENT_TEAM_1 = ForeignKeys0.FK_TEAM_CONTENT_TEAM_1;
	public static final ForeignKey<TrainingContentRecord, TrainingRecord> FK_TRAINING_CONTENT_TRAINING_1 = ForeignKeys0.FK_TRAINING_CONTENT_TRAINING_1;
	public static final ForeignKey<TrainingContentRecord, SetsRecord> FK_TRAINING_CONTENT_SETS_1 = ForeignKeys0.FK_TRAINING_CONTENT_SETS_1;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class UniqueKeys0 extends AbstractKeys {
		public static final UniqueKey<ClubRecord> PK_CLUB = createUniqueKey(Club.CLUB, Club.CLUB.CLUB_ID);
		public static final UniqueKey<SetFocusRecord> PK_SET_FOCUS = createUniqueKey(SetFocus.SET_FOCUS, SetFocus.SET_FOCUS.FOCUS_ID);
		public static final UniqueKey<SetFormRecord> PK_SET_FORM = createUniqueKey(SetForm.SET_FORM, SetForm.SET_FORM.FORM_ID);
		public static final UniqueKey<SetsRecord> PK_SETS = createUniqueKey(Sets.SETS, Sets.SETS.SET_ID);
		public static final UniqueKey<TeamRecord> PK_TEAM = createUniqueKey(Team.TEAM, Team.TEAM.TEAM_ID);
		public static final UniqueKey<TeamMemberRecord> PK_TEAM_MEMBER = createUniqueKey(TeamMember.TEAM_MEMBER, TeamMember.TEAM_MEMBER.MEMBER_ID);
		public static final UniqueKey<TrainingRecord> PK_TRAINING = createUniqueKey(Training.TRAINING, Training.TRAINING.TRAINING_ID);
	}

	private static class ForeignKeys0 extends AbstractKeys {
		public static final ForeignKey<ClubContentRecord, ClubRecord> FK_CLUB_CONTENT_CLUB_1 = createForeignKey(ch.tiim.sco.database.jooq.Keys.PK_CLUB, ClubContent.CLUB_CONTENT, ClubContent.CLUB_CONTENT.CLUB_ID);
		public static final ForeignKey<ClubContentRecord, TeamRecord> FK_CLUB_CONTENT_TEAM_1 = createForeignKey(ch.tiim.sco.database.jooq.Keys.PK_TEAM, ClubContent.CLUB_CONTENT, ClubContent.CLUB_CONTENT.TEAM_ID);
		public static final ForeignKey<SetsRecord, SetFocusRecord> FK_SETS_SET_FOCUS_1 = createForeignKey(ch.tiim.sco.database.jooq.Keys.PK_SET_FOCUS, Sets.SETS, Sets.SETS.FOCUS_ID);
		public static final ForeignKey<SetsRecord, SetFormRecord> FK_SETS_SET_FORM_1 = createForeignKey(ch.tiim.sco.database.jooq.Keys.PK_SET_FORM, Sets.SETS, Sets.SETS.FORM_ID);
		public static final ForeignKey<TeamContentRecord, TeamMemberRecord> FK_TEAM_CONTENT_TEAM_MEMBER_1 = createForeignKey(ch.tiim.sco.database.jooq.Keys.PK_TEAM_MEMBER, TeamContent.TEAM_CONTENT, TeamContent.TEAM_CONTENT.MEMBER_ID);
		public static final ForeignKey<TeamContentRecord, TeamRecord> FK_TEAM_CONTENT_TEAM_1 = createForeignKey(ch.tiim.sco.database.jooq.Keys.PK_TEAM, TeamContent.TEAM_CONTENT, TeamContent.TEAM_CONTENT.TEAM_ID);
		public static final ForeignKey<TrainingContentRecord, TrainingRecord> FK_TRAINING_CONTENT_TRAINING_1 = createForeignKey(ch.tiim.sco.database.jooq.Keys.PK_TRAINING, TrainingContent.TRAINING_CONTENT, TrainingContent.TRAINING_CONTENT.TRAINING_ID);
		public static final ForeignKey<TrainingContentRecord, SetsRecord> FK_TRAINING_CONTENT_SETS_1 = createForeignKey(ch.tiim.sco.database.jooq.Keys.PK_SETS, TrainingContent.TRAINING_CONTENT, TrainingContent.TRAINING_CONTENT.SET_ID);
	}
}