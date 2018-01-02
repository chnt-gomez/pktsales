package com.pocket.poktsales.model;

import com.orm.SugarRecord;

/**
 * Created by MAV1GA on 02/01/2018.
 */

public class Expense extends SugarRecord {

    private float expenseTotal;
    private String expenseConcept;
    private ExpenseCategory category;

}
