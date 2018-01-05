package com.pocket.poktsales.utils;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.pocket.poktsales.R;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.text.DecimalFormat;

/**
 * Created by MAV1GA on 05/01/2018.
 */

public class DayPerformanceMarker extends MarkerView {

    TextView tvContent;
    private MPPointF mOffset;


    public DayPerformanceMarker(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tv_marker_content);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String hourTime = DateTime.now().withTime((int)e.getX(),0, 0,0 ).toString("HH:mm");
        tvContent.setText(String.format("%s, %s ",hourTime, Conversor.asCurrency(e.getY())));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }
        return mOffset;
    }
}
