package com.pocket.poktsales;

import com.pocket.poktsales.presenter.SalesPresenter;
import com.pocket.poktsales.utils.Conversor;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Created by MAV1GA on 08/09/2017.
 */

public class IsConversorWorkingAsItsSupposed {

    @Test
    public void conversor_CorrectConversion_0_ReturnsTrue(){
        assertThat(Conversor.asCurrency(0).equals("$ 0.00"), is(true));
    }

    @Test
    public void conversor_CorrectConversion_100_02_ReturnsTrue(){
        assertThat(Conversor.asCurrency(100.02F).equals("$ 100.02"), is(true));
    }



}
