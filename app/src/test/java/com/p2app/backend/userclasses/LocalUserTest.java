package com.p2app.backend.userclasses;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class LocalUserTest {

    @Test
    public void calcBMITest01() {
        LocalUser user = new LocalUser();
        user.setHeight(180);
        user.setWeight(80);
        double BMI = (80 / (1.8 * 1.8));
        assertEquals(BMI, user.calcBMI(),0.001);
    }

    @Test
    public void calcBMITest02() {
        int height = 190;
        double weight = 65;

        double newHeight = (double) height / 100;
        double BMI = weight / (newHeight * newHeight);
        assertEquals(18.0055, BMI, 0.001);
    }
}