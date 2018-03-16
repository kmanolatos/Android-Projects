package com.manolatostech.photohistoryofgreece;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by kmanolatos on 22/2/2018.
 */

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#A5A2A2"));
        Display display = getWindowManager().getDefaultDisplay();
        int w = (display.getWidth());
        int h = (display.getHeight());
        String value = "";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            value = extras.getString("text");
        }
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        ScrollView sv = (ScrollView) findViewById(R.id.sv);
        TextView textView = (TextView) findViewById(R.id.textView);
        RelativeLayout.LayoutParams relativeParams;

        relativeParams = (RelativeLayout.LayoutParams) sv.getLayoutParams();
        relativeParams.setMargins(w * 5 / 100, h * 5 / 100, 0, 0);
        relativeParams.height = h * 86 / 100;
        relativeParams.width = w * 90 / 100;
        sv.setLayoutParams(relativeParams);

        relativeParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        relativeParams.setMargins(0, 0, 0, 0);
        relativeParams.height = h * 86 / 100;
        relativeParams.width = w * 90 / 100;
        textView.setLayoutParams(relativeParams);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText(value);
    }
}
