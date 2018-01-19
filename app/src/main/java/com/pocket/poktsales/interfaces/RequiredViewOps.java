package com.pocket.poktsales.interfaces;

import com.pocket.poktsales.model.MDepartment;
import com.pocket.poktsales.model.MProduct;
import com.pocket.poktsales.model.MTicket;

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
    String getResString(int resourceId);

    interface InventoryViewOps extends RequiredViewOps {
        void onProductAdded(MProduct product);
        void onProductUpdated(MProduct product);
        void onProductDeleted(long productId);
    }

    interface CategoryViewOps extends RequiredViewOps{
        void onDepartmentUpdate(MDepartment department);
        void onDepartmentAdded(MDepartment department);
    }

    interface TabViewOps extends RequiredViewOps{
        void onNewTab(MTicket ticket);
        void onTabApplied(long tabId);
        void onTabCancelled(long tabId);
    }

    interface SaleViewOps extends RequiredViewOps{
        void onProductAddToSale(MProduct product, String newTotal);
        void onDeleteFromSale(long productId, String newTotal);
        void onApplySale();
        void onCancelSale();
    }

    interface QuickSaleOps extends RequiredViewOps{
        void onApplySale();
    }

    interface DayReportOps {

    }

    interface BusinessReportViewOps extends RequiredViewOps{

    }
}
