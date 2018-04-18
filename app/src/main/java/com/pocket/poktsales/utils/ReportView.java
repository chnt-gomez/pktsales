package com.pocket.poktsales.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pocket.poktsales.R;
import com.pocket.poktsales.model.MReport;

import org.joda.time.DateTime;

import java.text.DateFormat;

public class ReportView{

    private static Dialog instance;

    public static void showReport(Context context, MReport report){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_report_view, null);

        builder.setView(dialogView);

        final TextView tvBusinessName = (TextView) dialogView.findViewById(R.id.tv_business_name_placeholder);
        final TextView tvDate = (TextView) dialogView.findViewById(R.id.tv_date_place_holder);
        final TextView tvAddress = (TextView) dialogView.findViewById(R.id.tv_address_place_holder);
        final ImageButton btnShare = (ImageButton) dialogView.findViewById(R.id.btn_share);

        tvBusinessName.setText("Mi negocio");
        tvAddress.setText("Aqui va una direccion que por lo general son un poco grandes");
        tvDate.setText("Date goes Here");

        instance = builder.create();
        instance.show();
    }
}

interface ReportViewInteractionListener{

}
