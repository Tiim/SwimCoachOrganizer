package ch.tiim.sql_xml;

import ch.tiim.sco.util.OutOfCoffeeException;
import ch.tiim.sql_xml.model.Entry;
import ch.tiim.sql_xml.model.Module;
import ch.tiim.sql_xml.model.Modules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXB;
import java.io.InputStream;

/**
 * Utility class to load SQL statements from an xml document.
 * The xml has the following structure: <br />
 * <pre>{@code
 * <modules>
 *      <module name="name">
 *          <entry name="get_names>
 *              SQL code
 *          </entry>
 *          ...
 *      <&module>
 *      ...
 * </modules>
 * }</pre>
 */
public class SqlLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlLoader.class);
    private final Modules modules;

    /**
     * @param file the file to load values from
     */
    public SqlLoader(String file) {
        InputStream is = SqlLoader.class.getResourceAsStream(file);
        modules = JAXB.unmarshal(is, Modules.class);
    }

    /**
     * Returns the SQL statement that is inside the following xml:
     * <pre>{@code
     *     <modules>
     *         <module name="$module">
     *             <entry name="$name">
     *                 Returns this string here
     *             </entry>
     *         </module>
     *     </modules>
     * }</pre>
     *
     * @param module the name of the module
     * @param name   the name of the entry inside the module
     * @return the SQL statement
     */
    public String getValue(String module, String name) {
        for (Module m : modules.getModules()) {
            if (m.getName().equals(module)) {
                for (Entry e : m.getEntries()) {
                    if (e.getName().equals(name)) {
                        return e.getValue();
                    }
                }
            }
        }
        throw new OutOfCoffeeException(String.format("%s/%s does not exist", module, name)); //NON-NLS
    }
}
