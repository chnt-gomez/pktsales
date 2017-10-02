package com.pocket.poktsales.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ListView;

import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.SimpleCategoryAdapter;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.presenter.SalesPresenter;
import com.pocket.poktsales.utils.DataLoader;
import com.pocket.poktsales.utils.DialogBuilder;

import butterknife.BindView;


public class CategoriesActivity extends BaseActivity {

    RequiredPresenterOps.DepartmentPresenterOps presenter;

    SimpleCategoryAdapter adapter;

    @BindView(R.id.lv_categories)
    ListView lvDepartments;

    @BindView(R.id.fab)
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.layoutResourceId = R.layout.activity_categories;
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSuccess() {
        super.onSuccess();
        start();
    }

    private void start() {
        DataLoader loader = new DataLoader(this);
        loader.execute();
    }

    @Override
    protected void init() {
        super.init();
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = SalesPresenter.getInstance(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.newDepartmentDialog(CategoriesActivity.this, new DialogBuilder.DialogInteractionListener.OnNewDepartmentListener() {
                    @Override
                    public void onNewDepartment(Department department) {
                        presenter.addNewDepartment(department);
                    }
                }).show();
            }
        });
        start();
    }

    @Override
    public void onLoading() {
        super.onLoading();
        adapter = new SimpleCategoryAdapter(getApplicationContext(), R.layout.row_department_item,
                presenter.getAllDepartments());
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        lvDepartments.setAdapter(adapter);

    }
}
