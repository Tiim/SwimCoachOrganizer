package ch.tiim.sco.util;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Created by timba on 01.03.2016.
 */
public class ColorUtil {

    public static Color getPastelColorHash(Object o) {
        Random r = new Random(o.hashCode());
        double s = (r.nextInt(2000) + 1000) / 10000d;
        double b = (r.nextInt(1000) + 8500) / 10000d;
        return Color.hsb(r.nextDouble()*360,s, b);
    }
}
