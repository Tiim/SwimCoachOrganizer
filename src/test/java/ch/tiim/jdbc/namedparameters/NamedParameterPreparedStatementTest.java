package ch.tiim.jdbc.namedparameters;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@SuppressWarnings("HardCodedStringLiteral")
public class NamedParameterPreparedStatementTest {
    @Test
    public void testParsing() {
        String testQuery = "SELECT * FROM table " +
                "WHERE afield = ':not me' " +
                "AND bfield = :param1 " +
                "AND cfield = :param2 " +
                "and dfield = :param2;";
        String expectedParsedQuery = "SELECT * FROM table " +
                "WHERE afield = ':not me' " +
                "AND bfield = ? " +
                "AND cfield = ? " +
                "and dfield = ?;";

        List<String> expectedParameterList = Lists.newArrayList("param1", "param2", "param2");
        NamedParameterPreparedStatement.ParseResult parseResult = NamedParameterPreparedStatement.parse(testQuery);
        assertEquals(expectedParsedQuery, parseResult.getSql());
        assertThat(expectedParameterList, is(parseResult.getOrderedParameters()));
    }

    @Test
    public void testParsing2() throws IOException {
        String testQuery = Resources.toString(
                NamedParameterPreparedStatementTest.class.getResource("test.sql"),
                Charsets.UTF_8
        );
        String expectedParsedQuery = Resources.toString(
                NamedParameterPreparedStatementTest.class.getResource("expected.sql"),
                Charsets.UTF_8
        );
        List<String> expectedParameterList = Lists.newArrayList("named_parameter1", "named_parameter2");
        NamedParameterPreparedStatement.ParseResult parseResult = NamedParameterPreparedStatement.parse(testQuery);
        assertEquals(expectedParsedQuery, parseResult.getSql());
        assertThat(expectedParameterList, is(parseResult.getOrderedParameters()));
    }
}