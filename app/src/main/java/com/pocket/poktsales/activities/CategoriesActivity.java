package com.pocket.poktsales.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.SimpleCategoryAdapter;
import com.pocket.poktsales.interfaces.RequiredViewOps;
import com.pocket.poktsales.model.MDepartment;
import com.pocket.poktsales.presenter.CategoryPresenter;
import com.pocket.poktsales.utils.DialogBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class CategoriesActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequiredViewOps.CategoryViewOps{

    CategoryPresenter presenter;

    SimpleCategoryAdapter adapter;

    ActivityAdapter activityAdapter;

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

    @BindView(R.id.btn_delete)
    ImageButton btnDelete;

    @BindView(R.id.btn_ok)
    ImageButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.layoutResourceId = R.layout.activity_categories;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        super.init();
        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new CategoryPresenter(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.newDepartmentDialog(CategoriesActivity.this, new DialogBuilder.DialogInteractionListener.OnNewDepartmentListener() {
                    @Override
                    public void onNewDepartment(MDepartment department) {
                        presenter.addNewDepartment(department);
                    }
                }).show();
            }
        });
        activityAdapter = new ActivityAdapter();
        adapter = new SimpleCategoryAdapter(getApplicationContext(), R.layout.row_department_item,
                new ArrayList<MDepartment>());
        lvDepartments.setAdapter(adapter);
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
    public void onLoadingPrepare() {
        super.onLoadingPrepare();
        adapter.clear();
    }

    @Override
    public void onLoading() {
        super.onLoading();
        activityAdapter.setDepartmentList(presenter.getAllDepartments());
    }

    @Override
    public void onLoadingComplete() {
        super.onLoadingComplete();
        if (adapter.getCount() > 0)
            adapter.clear();
        adapter.addAll(activityAdapter.getDepartmentList());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        seeDepartmentDetail(id);
    }

    private void seeDepartmentDetail(final long id) {
        final MDepartment department = presenter.getDepartment(id);
        if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED
                || panel.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)
            panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        etDepartmentName.setText(department.departmentName);
        tvDepartmentName.setText(department.departmentName);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.updateDepartment(etDepartmentName.getText().toString(), id );
                if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
                    panel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
            panel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        else
            super.onBackPressed();
    }

    @Override
    public void onDepartmentUpdate(MDepartment department) {
        activityAdapter.updateDepartment(department);
        adapter.clear();
        adapter.addAll(activityAdapter.getDepartmentList());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDepartmentAdded(MDepartment department) {
        activityAdapter.addDepartment(department);
        adapter.add(department);
        adapter.notifyDataSetChanged();
    }

    class ActivityAdapter {
         List<MDepartment> getDepartmentList() {
            return departmentList;
         }

        void setDepartmentList(List<MDepartment> departmentList) {
            this.departmentList = departmentList;
            for (MDepartment d : departmentList){
                d.productCount = presenter.getProductCountFromDepartment(d.id);
            }
        }

        void addDepartment(MDepartment department){
             if (departmentList == null)
                 departmentList = new ArrayList<>();
            departmentList.add(department);
        }

        void updateDepartment(MDepartment department){
            for (int i=0; i<departmentList.size(); i++){
                if (departmentList.get(i).id == department.id) {
                    departmentList.set(i, department);
                    return;
                }
            }
        }

        List<MDepartment> departmentList;
    }
}
