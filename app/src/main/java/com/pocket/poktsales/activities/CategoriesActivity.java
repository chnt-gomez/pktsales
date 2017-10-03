package com.pocket.poktsales.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.SimpleCategoryAdapter;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.presenter.SalesPresenter;
import com.pocket.poktsales.utils.DataLoader;
import com.pocket.poktsales.utils.DialogBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;


public class CategoriesActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    RequiredPresenterOps.DepartmentPresenterOps presenter;

    SimpleCategoryAdapter adapter;

    @BindView(R.id.lv_categories)
    ListView lvDepartments;

    @BindView(R.id.fab)
    FloatingActionButton btnAdd;

    @BindView(R.id.sliding_up_panel)
    SlidingUpPanelLayout panel;

    @BindView(R.id.et_department_name)
    EditText etDepartmentName;

    @BindView(R.id.tv_department_name)
    TextView tvDepartmentName;

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
        panel.setPanelHeight(0);
        panel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                btnAdd.setVisibility(View.GONE);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED || newState == SlidingUpPanelLayout.PanelState.HIDDEN)
                    btnAdd.setVisibility(View.VISIBLE);
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED)
                    btnAdd.setVisibility(View.GONE);
            }
        });
        panel.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        lvDepartments.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        seeDepartmentDetail(id);
    }

    private void seeDepartmentDetail(long id) {
        final Department department = presenter.getDepartment(id);
        if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED
                || panel.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)
            panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        etDepartmentName.setText(department.getDepartmentName());
        tvDepartmentName.setText(department.getDepartmentName());
    }

    @Override
    public void onBackPressed() {
        if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            panel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        else
            super.onBackPressed();
    }
}
