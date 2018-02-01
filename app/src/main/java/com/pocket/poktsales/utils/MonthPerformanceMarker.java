package com.pocket.poktsales.utils;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.pocket.poktsales.R;

import org.joda.time.DateTime;

import java.text.DecimalFormat;

/**
 * Created by MAV1GA on 03/01/2018.
 */

public class MonthPerformanceMarker extends MarkerView {

    protected TextView tvContent;
    private int year, month;
    public MonthPerformanceMarker(Context context, int layoutResource, int month, int year) {
        super(context, layoutResource);
        this. year = year;
        this.month = month;
        tvContent = (TextView) findViewById(R.id.tv_marker_content);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        DateTime date = new DateTime(year, month, (int)e.getX(), 0,0);

        tvContent.setText(String.format("%s, %s ",date.toString("dd MMM"), Conversor.asCurrency(e.getY())));

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}
