package ch.tiim.sco.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Debug {

    public static String rs(ResultSet rs) throws SQLException {
        StringBuilder b = new StringBuilder();
        ResultSetMetaData rsmd = rs.getMetaData();

        int cols = rsmd.getColumnCount();
        int widths[] = new int[cols];
        List<String[]> data = new ArrayList<>();
        String[] names = new String[cols];
        for (int i = 1; i <= cols; i++) {
            names[i - 1] = rsmd.getColumnName(i);
            widths[i - 1] = names[i - 1].length();
        }
        data.add(names);
        while (rs.next()) {
            String[] row = new String[cols];
            for (int i = 1; i <= cols; i++) {
                row[i - 1] = filter(rs.getString(i));
                widths[i - 1] = Math.max(widths[i - 1], row[i - 1].length());
            }
            data.add(row);
        }
        for (String[] strings : data) {
            b.append("\n");
            for (int i = 0; i < cols; i++) {
                b.append(strings[i]);
                for (int j = 0; j < widths[i] - strings[i].length(); j++) {
                    b.append(" ");
                }
                b.append("|");
            }
        }
        return b.toString();
    }

    private static String filter(String string) {
        if (string == null) return "null";
        string = string.replace("\n", "\\n"); //NON-NLS
        string = string.replace("\r", "\\r"); //NON-NLS
        string = string.replace("\t", "\\t"); //NON-NLS
        return string;
    }
}
