package com.pocket.poktsales;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void seeMaxDaysInJan() throws Exception{
        DateTime date = new DateTime().withMonthOfYear(1);
        assertEquals(date.dayOfMonth().get(), 9);
        assertEquals(date.dayOfMonth().getMaximumValue(), 31);
        assertEquals(date.toString("MMM"), "Jan");
    }


}