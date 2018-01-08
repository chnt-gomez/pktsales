package com.pocket.poktsales.presenter;

import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;

/**
 * Created by MAV1GA on 08/01/2018.
 */

public class ReportPresenter extends BasePresenter implements RequiredPresenterOps.ReportsPresenterOps{

    private RequiredViewOps.BusinessReportViewOps view;

    public ReportPresenter(RequiredViewOps.BusinessReportViewOps view){
        this.view = view;
    }

}
