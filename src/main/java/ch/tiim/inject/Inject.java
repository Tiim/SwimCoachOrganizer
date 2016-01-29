package ch.tiim.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a member as being available for injection.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {

    /**
     * Marks the member to be instantiated by the {@link Injector}
     *
     * @return true to receive new instance
     */
    boolean newInstance() default false;

    /**
     * Name of the injectable.
     * @return the name
     */
    String name() default "";
}
