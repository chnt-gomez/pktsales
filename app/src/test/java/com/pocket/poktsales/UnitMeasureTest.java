package com.pocket.poktsales;

import android.content.Context;
import android.content.res.Resources;

import com.pocket.poktsales.utils.MeasurePicker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by MAV1GA on 08/09/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class UnitMeasureTest {

    @Mock
    Resources mockContext;

    @Test
    public void readMeasureFromContext(){
        //Given
        when(MeasurePicker.getString(mockContext,0)).thenReturn("PIECE");
        //When
        String result = MeasurePicker.getString(mockContext, 0);
        //Then
        assertThat(result, is("PIECE"));
    }

}
