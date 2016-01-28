package ch.tiim.sco.util;

import com.google.common.collect.Iterables;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.List;

public class JSHelper {
    public static Iterable<Member> objInfo(Object o) {
        return Iterables.concat(getMembers(o), getFuctions(o));
    }

    public static Iterable<Member> getMembers(Object o) {
        Class<?> clazz = o.getClass();
        return Arrays.asList(clazz.getFields());
    }

    public static Iterable<Member> getFuctions(Object o) {
        Class<?> clazz = o.getClass();
        List<Member> m = Arrays.asList(clazz.getMethods());
        List<Member> c = Arrays.asList(clazz.getConstructors());
        return Iterables.concat(c, m);
    }
}
