package com.pocket.poktsales.presenter;

import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;

import java.util.List;

/**
 * Created by MAV1GA on 07/11/2017.
 */

public class CategoryPresenter extends BasePresenter implements RequiredPresenterOps.DepartmentPresenterOps {

    RequiredViewOps.CategoryViewOps view;

    public CategoryPresenter(RequiredViewOps.CategoryViewOps view){
        this.view = view;
    }

    @Override
    public void addNewDepartment(Department department) {
        if (!isDepartmentNameInUse(department.getDepartmentName())) {
            if (department.getDepartmentName().equals("")){
                view.onError();
                return;
            }
            department.save();
            view.onDepartmentAdded(department);
        }else{
            view.onError();
        }
    }

    @Override
    public List<Department> getAllDepartments() {
        return Department.listAll(Department.class);
    }

    @Override
    public void removeDepartment(long departmentId, long moveProductsToDepartmentId) {

    }

    @Override
    public Department getDepartment(long id) {
        return Department.findById(Department.class, id);
    }

    @Override
    public int getProductCountFromDepartment(long departmentId) {
        String args[] = {String.valueOf(departmentId), String.valueOf(Product.ACTIVE)};
        return (int) Product.count(Product.class, "department = ? and product_status = ?", args);
    }

    @Override
    public void updateDepartment(String newDepartmentArgs, long departmentId) {
        Department department = Department.findById(Department.class, departmentId);
        department.setDepartmentName(newDepartmentArgs);
        department.save();
        view.onDepartmentUpdate(department);
    }
}
