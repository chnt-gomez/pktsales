package com.pocket.poktsales.interfaces;

import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public interface RequiredViewOps {


    void onSuccess();
    void onSuccess(int messageRes);
    void onSuccess(String message);
    void onError();
    void onError(int messageRes);
    void onError(String message);

    interface InventoryViewOps extends RequiredViewOps {
        void onProductAdded(Product product);
        void onProductUpdated(Product product);
        void onProductDeleted(long productId);
    }

    interface CategoryViewOps extends RequiredViewOps{
        void onDepartmentUpdate(Department department);
        void onDepartmentAdded(Department department);
    }

}
