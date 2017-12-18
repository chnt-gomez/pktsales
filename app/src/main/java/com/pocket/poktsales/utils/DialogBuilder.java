package com.pocket.poktsales.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.DropDownDepartmentAdapter;
import com.pocket.poktsales.adapters.SimpleCategoryAdapter;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.model.Department;
import com.pocket.poktsales.model.Product;
import com.pocket.poktsales.model.Ticket;

import java.util.List;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class DialogBuilder {

    static Dialog instance;

    public static Dialog confirmDeleteTabDialog(final Context context, final Ticket ticketReference,
                                                final DialogInteractionListener.OnDeleteTabListener callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_delete, null);
        /*
        Init widgets
         */
        final TextView tvTabName = (TextView) dialogView.findViewById(R.id.tv_product_name);
        final ImageView image = (ImageView)dialogView.findViewById(R.id.img_reference);
        final ImageButton btnDelete = (ImageButton)dialogView.findViewById(R.id.btn_confirm_delete);
        tvTabName.setText(ticketReference.getTicketReference()+" "+context.getString(R.string.will_be_deleted));
        image.setImageResource(R.drawable.ic_receipt_big);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDeleteTab(ticketReference.getId());
                instance.dismiss();
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;
    }

    public static Dialog newDepartmentDialog(final Context context, final DialogInteractionListener.OnNewDepartmentListener
                                            callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_new_department, null);

        final EditText etDepartmentName = (EditText) dialogView.findViewById(R.id.et_department_name);
        final ImageButton btnOk = (ImageButton) dialogView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Department department = new Department();
                department.setDepartmentName(etDepartmentName.getText().toString());
                callback.onNewDepartment(department);
                instance.dismiss();
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;


    }

    public static Dialog addToCategoryDialog(final Context context,
                                             final DialogInteractionListener.OnCategoryPickedListener callback,
                                             RequiredPresenterOps.ProductPresenterOps presenter){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_pick_category, null);
        final ListView lvCategories = (ListView) dialogView.findViewById(R.id.lv_categories);
        final Button btnRemoveFromCategory = (Button) dialogView.findViewById(R.id.btn_remove_from_category);
        List<Department> departments = presenter.getAllDepartments();
        for (Department d : departments){
            d.setProductCount(presenter.getProductsInDepartment(d.getId()).size());
        }
        lvCategories.setAdapter(new SimpleCategoryAdapter(context, R.layout.row_department_item, departments));
        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.onCategorySelected(id);
                instance.dismiss();
            }
        });
        btnRemoveFromCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCategorySelected(0L);
                instance.dismiss();
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;
    }

    public static Dialog newProductDialog(final Context context,
                                          final DialogInteractionListener.OnNewProductListener callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_new_product, null);
        final Spinner spnProductMeasure = (Spinner)dialogView.findViewById(R.id.spn_product_measure);
        final EditText etProductName = (EditText)dialogView.findViewById(R.id.et_product_name);
        final EditText etProductPrice = (EditText)dialogView.findViewById(R.id.et_product_price);
        spnProductMeasure.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                MeasurePicker.getEntries(context.getResources())));
        ImageButton positiveButton = (ImageButton)dialogView.findViewById(R.id.btn_ok);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setProductName(etProductName.getText().toString());
                product.setProductMeasureUnit(spnProductMeasure.getSelectedItemPosition());
                product.setProductSellPrice(Conversor.toFloat(etProductPrice.getText().toString()));
                if (instance != null)
                    instance.dismiss();
                callback.onNewProduct(product);
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;

    }

    public static Dialog newTabDialog(final Context context, final DialogInteractionListener.OnNewTabListener callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_new_tab, null);
        /*
        Init widgets
         */
        final EditText etTabName = (EditText) dialogView.findViewById(R.id.et_tab_name);
        final ImageButton btnOk = (ImageButton) dialogView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onNewTab(etTabName.getText().toString());
                instance.dismiss();
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;
    }

    public static Dialog sortProductsDialog(final Context context, RequiredPresenterOps.ProductPresenterOps presenterOps,
                                            Product.Sorting sorting,
                                          final DialogInteractionListener.OnSortProductsListener callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_sort_products, null);
        final List<Department> allDepartments = presenterOps.getAllDepartments();
        final Spinner spnDepartments = (Spinner)dialogView.findViewById(R.id.spn_department);
        if (allDepartments != null && allDepartments.size() > 0)
            spnDepartments.setAdapter(new DropDownDepartmentAdapter(context, R.layout.dropdown_department_item,
                    allDepartments));
        else
            spnDepartments.setVisibility(View.GONE);
        final RadioButton rbNone = (RadioButton)dialogView.findViewById(R.id.rd_none);
        if (sorting == Product.Sorting.NONE)
            rbNone.setChecked(true);
        final RadioButton rbAlphabetically =(RadioButton)dialogView.findViewById(R.id.rd_alphabetically);
        if (sorting == Product.Sorting.ALPHABETICAL)
            rbAlphabetically.setChecked(true);
        final RadioButton rbPrice = (RadioButton)dialogView.findViewById(R.id.rd_price);
        if (sorting == Product.Sorting.PRICE)
            rbPrice.setChecked(true);
        ImageButton positiveButton = (ImageButton)dialogView.findViewById(R.id.btn_ok);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product.Sorting sorting = Product.Sorting.NONE;
                if (rbNone.isChecked())
                    sorting = Product.Sorting.NONE;
                if (rbPrice.isChecked())
                    sorting = Product.Sorting.PRICE;
                if (rbAlphabetically.isChecked())
                    sorting = Product.Sorting.ALPHABETICAL;
                if (instance != null)
                    instance.dismiss();
                callback.onSortProducts(-1, sorting);
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;
    }

    public static Dialog confirmDeleteProductDialog(final Context context, final Product product,
                                          final DialogInteractionListener.OnDeleteProductListener callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_delete, null);
        /*
        Init widgets
         */
        final TextView tvProductName = (TextView) dialogView.findViewById(R.id.tv_product_name);
        final ImageButton btnDelete = (ImageButton)dialogView.findViewById(R.id.btn_confirm_delete);
        final ImageView image = (ImageView)dialogView.findViewById(R.id.img_reference);
        image.setImageResource(R.drawable.ic_box_big);
        tvProductName.setText(product.getProductName()+" "+context.getString(R.string.will_be_deleted));
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDeleteProduct(product.getId());
                instance.dismiss();
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;
    }

    public static Dialog newTempDialog(final Context context, final DialogInteractionListener.OnNewTempDialogListener callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_new_temp_product, null);
        final Spinner spnProductMeasure = (Spinner)dialogView.findViewById(R.id.spn_product_measure);
        final EditText etProductName = (EditText)dialogView.findViewById(R.id.et_product_name);
        final EditText etProductPrice = (EditText)dialogView.findViewById(R.id.et_product_price);
        spnProductMeasure.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                MeasurePicker.getEntries(context.getResources())));
        ImageButton positiveButton = (ImageButton)dialogView.findViewById(R.id.btn_ok);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = etProductName.getText().toString();
                float productPrice = Conversor.toFloat(etProductPrice.getText().toString());
                if (instance != null)
                    instance.dismiss();
                callback.onNewTempProductDialog(productName, productPrice);
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;
    }

    public static class DialogInteractionListener{
        public interface OnNewProductListener{
            void onNewProduct(Product product);
        }
        public interface OnDeleteProductListener{
            void onDeleteProduct(long productId);
        }
        public interface OnSortProductsListener{
            void onSortProducts(long departmentId, Product.Sorting sorting);
        }
        public interface OnNewTabListener{
            void onNewTab(String ticketReference);
        }
        public interface OnDeleteTabListener{
            void onDeleteTab(long ticketId);
        }
        public interface OnNewTempDialogListener {
            void onNewTempProductDialog(String productName, float productPrice);
        }
        public interface OnNewDepartmentListener{
            void onNewDepartment(Department department);
        }
        public interface OnCategoryPickedListener{
            void onCategorySelected(long categoryId);
        }
    }
}
