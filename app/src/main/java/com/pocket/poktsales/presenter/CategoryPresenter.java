package com.pocket.poktsales.presenter;

import com.pocket.poktsales.R;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MDepartment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAV1GA on 07/11/2017.
 */

public class CategoryPresenter extends BasePresenter implements RequiredPresenterOps.DepartmentPresenterOps {

    private RequiredViewOps.CategoryViewOps view;

    public CategoryPresenter(RequiredViewOps.CategoryViewOps view){
        this.view = view;
    }

    @Override
    public void addNewDepartment(MDepartment department) {
        if (!isDepartmentNameInUse(department.departmentName)) {
            if (department.departmentName.equals("")){
                view.onError();
                return;
            }
            Department ormDepartment = new Department(department);
            ormDepartment.setDepartmentStatus(Department.ACTIVE);
            department.id = ormDepartment.save();
            view.onDepartmentAdded(department);
        }else{
            view.onError();
        }
    }

    @Override
    public List<MDepartment> getAllDepartments() {
        return fromDepartmentList(Department.find(Department.class, "department_status = ?", String.valueOf(Department.ACTIVE)));
    }

    @Override
    public void removeDepartment(long departmentId, long moveProductsToDepartmentId) {
        List<Product> products = Product.find(Product.class, "department = ? ", String.valueOf(departmentId));
        for (Product p : products){
            p.setDepartment(null);
            p.save();
        }
        Department d = Department.findById(Department.class, departmentId);
        if (d != null) {
            d.setDepartmentStatus(Department.INACTIVE);
            d.save();
        }
        view.onDepartmentDeleted();
    }

    @Override
    public MDepartment getDepartment(long id) {
        return fromDepartment(Department.findById(Department.class, id));
    }

    @Override
    public int getProductCountFromDepartment(long departmentId) {
        String args[] = {String.valueOf(departmentId), String.valueOf(Product.ACTIVE)};
        return (int) Product.count(Product.class, "department = ? and product_status = ?", args);
    }

    @Override
    public void updateDepartment(String newDepartmentArgs, long departmentId) {
        if (newDepartmentArgs.length()<= 0){
            view.onError(R.string.err_invalid_department_name);
            return;
        }
        Department department = Department.findById(Department.class, departmentId);
        if (department.getDepartmentName().equals(newDepartmentArgs)){
            return;
        }
        if (isDepartmentNameInUse(newDepartmentArgs)){
            view.onError(R.string.err_department_name_already_in_use);
            return;
        }
        department.setDepartmentName(newDepartmentArgs);
        department.save();
        MDepartment modelDepartment = fromDepartment(department);
        view.onDepartmentUpdate(modelDepartment);
    }
}
