package com.pocket.poktsales.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.pocket.poktsales.R;
import com.pocket.poktsales.adapters.SimpleCategoryAdapter;
import com.pocket.poktsales.adapters.SimpleSaleAdapter;
import com.pocket.poktsales.adapters.SimpleTicketAdapter;
import com.pocket.poktsales.interfaces.RequiredPresenterOps;
import com.pocket.poktsales.model.MDepartment;
import com.pocket.poktsales.model.MProduct;
import com.pocket.poktsales.model.MSale;
import com.pocket.poktsales.model.MTicket;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by MAV1GA on 04/09/2017.
 */

public class DialogBuilder {

    static Dialog instance;

    public static Dialog confirmDeleteTabDialog(final Context context, final MTicket ticketReference,
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
        tvTabName.setText(String.format("%s %s", ticketReference.ticketReference, context.getString(R.string.will_be_deleted)));
        image.setImageResource(R.drawable.ic_receipt_big);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.dismiss();
                callback.onDeleteTab(ticketReference.id);

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
                instance.dismiss();
                MDepartment department = new MDepartment();
                department.departmentName = etDepartmentName.getText().toString();
                callback.onNewDepartment(department);

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
        List<MDepartment> departments = presenter.getAllDepartments();
        for (MDepartment d : departments){
            d.productCount = presenter.getProductsInDepartment(d.id).size();
        }
        lvCategories.setAdapter(new SimpleCategoryAdapter(context, R.layout.row_department_item, departments));
        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                instance.dismiss();
                callback.onCategorySelected(id);

            }
        });
        btnRemoveFromCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.dismiss();
                callback.onCategorySelected(0L);
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;
    }

    public static Dialog saleSuccessDialog(final Context context, final DialogInteractionListener.OnSaleSuccessListener callback){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_sale_success, null);
        final ImageButton positiveButton = (ImageButton)dialogView.findViewById(R.id.btn_ok);
        //final ImageButton shareButton = (ImageButton)dialogView.findViewById(R.id.btn_share);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSuccess();
                instance.dismiss();
            }
        });
        /*
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onShareTicket();
            }
        });
        */


        builder.setView(dialogView);
        instance = builder.create();
        instance.setCancelable(false);
        return instance;
    }

    public static Dialog saleCanceledDialog(final Context context, final DialogInteractionListener.OnSaleCancelListener callback){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_canceled_sale, null);
        final ImageButton positiveButton = (ImageButton)dialogView.findViewById(R.id.btn_ok);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCancel();
                instance.dismiss();
            }
        });

        builder.setView(dialogView);
        instance = builder.create();
        instance.setCancelable(false);
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
        final ImageButton positiveButton = (ImageButton)dialogView.findViewById(R.id.btn_ok);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MProduct product = new MProduct();
                product.productName = etProductName.getText().toString();
                product.productMeasureUnit = spnProductMeasure.getSelectedItemPosition();
                String productSellPrice;
                if (etProductPrice.getText() != null) {
                    productSellPrice = etProductPrice.getText().toString();
                    if (productSellPrice.length() <= 0)
                        productSellPrice = "0";
                }else{
                    productSellPrice = "0";
                }
                product.productSellPrice = Float.parseFloat(productSellPrice);
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

    public static Dialog addToSaleDialog(final Context context, final DialogInteractionListener.OnAddToSale callback,final long productId){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_to_sale, null);
        final ImageButton btnMinus = (ImageButton) dialogView.findViewById(R.id.btn_minus);
        final ImageButton btnPlus = (ImageButton) dialogView.findViewById(R.id.btn_plus);
        final EditText etProductQty = (EditText) dialogView.findViewById(R.id.et_product_qty);
        final ImageButton positiveButton = (ImageButton)dialogView.findViewById(R.id.btn_ok);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int actual = Integer.valueOf(etProductQty.getText().toString());
                    if (actual > 0) {
                        actual--;
                        etProductQty.setText(String.valueOf(actual));
                    }
                }catch (Exception e){
                    etProductQty.setText("1");
                }
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int actual = Integer.valueOf(etProductQty.getText().toString());
                    actual ++;
                    etProductQty.setText(String.valueOf(actual));
                }catch (Exception e){
                    etProductQty.setText("1");
                }
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty;
                try{
                    qty = Integer.valueOf(etProductQty.getText().toString());
                }catch (Exception e){
                    qty = 1;
                }
                callback.onAddToSale(
                        productId, qty
                );
                instance.dismiss();
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;

    }
    /*
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
    }*/

    public static Dialog confirmDeleteProductDialog(final Context context, final MProduct product,
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
        tvProductName.setText(String.format("%s %s", product.productName, context.getString(R.string.will_be_deleted)));
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDeleteProduct(product.id);
                instance.dismiss();
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;
    }

    public static Dialog colorPicketDialog(final Context context, int currentColor, final DialogInteractionListener.OnColorSelectedListener callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_color_picker, null);
        final SeekBar sbRed = (SeekBar)dialogView.findViewById(R.id.pb_color_picker_red);
        final SeekBar sbGreen = (SeekBar)dialogView.findViewById(R.id.pb_color_picker_green);
        final SeekBar sbBlue = (SeekBar)dialogView.findViewById(R.id.pb_color_picker_blue);
        final ImageView imageColor = (ImageView)dialogView.findViewById(R.id.img_pallete);
        final ImageButton btnOk = (ImageButton)dialogView.findViewById(R.id.btn_ok);
        imageColor.setColorFilter(currentColor);
        sbRed.setProgress(Color.red(currentColor));
        sbGreen.setProgress(Color.green(currentColor));
        sbBlue.setProgress(Color.blue(currentColor));

        final int[] colors = new int[3];
        final int RED = 0;
        final int BLUE = 2;
        final int GREEN = 1;
        colors[RED] = Color.red(currentColor);
        colors[GREEN] = Color.green(currentColor);
        colors[BLUE] = Color.blue(currentColor);
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == R.id.pb_color_picker_red)
                    colors[RED] = progress;
                if (seekBar.getId() == R.id.pb_color_picker_green)
                    colors[GREEN] = progress;
                if (seekBar.getId() == R.id.pb_color_picker_blue)
                    colors[BLUE] = progress;
                imageColor.setColorFilter(Color.argb(255, colors[RED], colors[GREEN], colors[BLUE]));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        sbBlue.setOnSeekBarChangeListener(listener);
        sbRed.setOnSeekBarChangeListener(listener);
        sbGreen.setOnSeekBarChangeListener(listener);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onColorSelected(Color.argb(255, colors[RED], colors[GREEN], colors[BLUE]));
                instance.dismiss();
            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;
    }

    public static Dialog newDatePickerDialog(final Context context, final DialogInteractionListener.OnDateSelected callback){

        return new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                callback.onDateSelected(new DateTime(year, month+1, dayOfMonth, 0, 0));
            }
        }, DateTime.now().getYear(), DateTime.now().getMonthOfYear()-1, DateTime.now().getDayOfMonth());
    }

    public static Dialog newDatePickerDialogMonthOnly(final Context context, final DialogInteractionListener.OnDateSelected callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressWarnings("InflateParams")
        final String[] months = context.getResources().getStringArray(R.array.months);
        final String[] years = {"2018", "2019", "2020" };
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_no_year_date_picker, null);
        final Spinner spnMonth = (Spinner)dialogView.findViewById(R.id.spn_month);
        final Spinner spnYear = (Spinner)dialogView.findViewById(R.id.spn_year);
        final ImageButton btnOk = (ImageButton)dialogView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int month = spnMonth.getSelectedItemPosition();
                int year = 2018+(spnYear.getSelectedItemPosition());
                callback.onDateSelected(
                new DateTime(year, month + 1, 1, 0, 0));
                instance.dismiss();
            }
        });
        spnMonth.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item,
                months));
        spnYear.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, years));
        builder.setView(dialogView);
        instance = builder.create();
        return instance;


    }

    public static Dialog newTempDialog(final Context context, final DialogInteractionListener.OnNewTempDialogListener callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_new_temp_product, null);
        final EditText etProductName = (EditText)dialogView.findViewById(R.id.et_product_name);
        final EditText etProductPrice = (EditText)dialogView.findViewById(R.id.et_product_price);
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

    public static Dialog confirmDeleteCategoryDialog(final Context context, final MDepartment category,
                                                     final DialogInteractionListener.OnDeleteCategoryListener callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_delete, null);
        /*
        Init widgets
         */
        final TextView tvTabName = (TextView) dialogView.findViewById(R.id.tv_product_name);
        final ImageView image = (ImageView)dialogView.findViewById(R.id.img_reference);
        final ImageButton btnDelete = (ImageButton)dialogView.findViewById(R.id.btn_confirm_delete);
        tvTabName.setText(String.format("%s %s", category.departmentName, context.getString(R.string.will_be_deleted)));
        image.setImageResource(R.drawable.ic_modules_big);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.dismiss();
                callback.onDeleteCategory(category.id);

            }
        });
        builder.setView(dialogView);
        instance = builder.create();
        return instance;
    }

    public static Dialog seeTicketDialog(final Context context,
                                         long ticketId, List<MSale> sale, float ticketTotal,
                                         final DialogInteractionListener.OnShareTicketListener callback){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_ticket_details, null);
        /*
        Init widgets
         */
        final TextView tvTicketReference = (TextView) dialogView.findViewById(R.id.tv_ticket_reference);
        final TextView tvSaleTotal = (TextView) dialogView.findViewById(R.id.tv_ticket_total);
        final ListView lvSales = (ListView)dialogView.findViewById(R.id.lv_tickets);
        SimpleTicketAdapter adapter = new SimpleTicketAdapter(context, R.layout.simple_sale_row, sale);
        String ticket = String.format("%s %d", context.getString(R.string.ticket_no), ticketId);
        tvSaleTotal.setText(Conversor.asCurrency(ticketTotal));
        lvSales.setAdapter(adapter);
        tvTicketReference.setText(ticket);
        builder.setView(dialogView);
        instance = builder.create();
        return instance;
    }

    public static class DialogInteractionListener{
        public interface OnNewProductListener{
            void onNewProduct(MProduct product);
        }
        public interface OnDeleteProductListener{
            void onDeleteProduct(long productId);
        }
        public interface OnSortProductsListener{
            void onSortProducts(long departmentId);
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
            void onNewDepartment(MDepartment department);
        }
        public interface OnCategoryPickedListener{
            void onCategorySelected(long categoryId);
        }

        public interface OnDateSelected {
            void onDateSelected(DateTime date);
        }

        public interface OnAddToSale {
            void onAddToSale(long productId, int qty);
        }

        public interface OnSaleSuccessListener {
            void onSuccess();
            void onShareTicket();
        }

        public interface OnSaleCancelListener{
            void onCancel();
        }

        public interface OnDeleteCategoryListener {
            void onDeleteCategory(long categoryId);
        }

        public interface OnColorSelectedListener{
            void onColorSelected(int color);
        }

        public interface OnShareTicketListener {
            void onTicketShare();
        }
    }
}
