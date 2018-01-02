package com.pocket.poktsales.presenter;

import com.orm.SugarRecord;

/**
 * Created by MAV1GA on 02/01/2018.
 */

 class Expense extends SugarRecord {

    private float expenseTotal;
    private String expenseConcept;
    private ExpenseCategory category;

}
