package com.pocket.poktsales;

import com.pocket.poktsales.model.Product;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by MAV1GA on 05/10/2017.
 */




public class IsCategoryNullOrZero {

    @Mock
    Product product;

    @Test
    public void isCategoryNUllOrZero(){
        assertEquals (null, product.getDepartment());
    }

}
