package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;
import java.lang.Math;

public class DoggieCoin extends Item {
    private int value;
    private int amplitude;
    private int intercept;
    
    public DoggieCoin(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        setValue(500);
        setAmplitude(450);
        setIntercept(550);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(int amplitude) {
        this.amplitude = amplitude;
    }

    public int getIntercept() {
        return intercept;
    }

    public void setIntercept(int intercept) {
        this.intercept = intercept;
    }

    public void fluctuateValue(int cycles) {
        double v = Math.sin(cycles);
        v = getAmplitude()*v + getIntercept();
        setValue((int)v);
    }



}
