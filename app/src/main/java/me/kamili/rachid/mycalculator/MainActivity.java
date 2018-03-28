package me.kamili.rachid.mycalculator;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.fathzer.soft.javaluator.DoubleEvaluator;

public class MainActivity extends AppCompatActivity {

    private DoubleEvaluator evaluator = new DoubleEvaluator();

    private Integer screenWidth = 0;
    private Integer screenHeight = 0;
    private GridLayout primaryGrid;
    private GridLayout sciGrid;
    private TextView tvValues;
    private TextView tvResults;
    private String values = "";
    private String results = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getScreenSizes();

        int orientation = this.getResources().getConfiguration().orientation;
        setContentView(R.layout.activity_main);
        startViews();
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setNormalSize();
        } else {
            setLandscapeSize();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setNormalSize();
        } else {
            setLandscapeSize();
        }
        setValues();
    }

    private void startViews() {
        tvValues = findViewById(R.id.values);
        tvResults = findViewById(R.id.results);
        primaryGrid = findViewById(R.id.primaryGrid);
        sciGrid = findViewById(R.id.sciGrid);
    }

    private void hideSciGrid() {
        sciGrid.setVisibility(GridLayout.INVISIBLE);
    }

    private void showSciGrid() {
        sciGrid.setVisibility(GridLayout.VISIBLE);
    }

    private void setLandscapeSize() {
        ViewGroup.LayoutParams layoutParams = primaryGrid.getLayoutParams();
        layoutParams.height = (int) (((double) screenWidth) * 0.7);

        layoutParams.width = screenHeight / 2;
        tvResults.getLayoutParams().height = (screenWidth - layoutParams.height - getStatusBarHeight()) / 2;
        tvValues.getLayoutParams().height = (screenWidth - layoutParams.height - getStatusBarHeight()) / 2;
        primaryGrid.setLayoutParams(layoutParams);
        sciGrid.setLayoutParams(layoutParams);
        //showSciGrid();
    }

    private void setNormalSize() {
        primaryGrid.getLayoutParams().height = screenWidth;
        primaryGrid.getLayoutParams().width = screenWidth;
        tvResults.getLayoutParams().height = (screenHeight - screenWidth) / 2;
        tvValues.getLayoutParams().height = (screenHeight - screenWidth) / 2;
        //hideSciGrid();
    }

    private void getScreenSizes() {
        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        int heightPixels = Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth = heightPixels > widthPixels ? widthPixels : heightPixels;
        screenHeight = heightPixels < widthPixels ? widthPixels : heightPixels;
    }

    private int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public void setValues() {
        tvValues.setText(values);
        tvResults.setText(results);
    }

    public void onClickHandler(View view) {
        if (results.equals("")) {
            values += ((Button) view).getText();
        } else {
            String text = ((Button) view).getText().toString();
            if (text.equals("/") || text.equals("*") || text.equals("+") || text.equals("-")) {
                values = results + text;

            } else {
                values = text;
            }
        }
        results = "";
        setValues();

    }

    public void onClickEqual(View view) {
        if (!values.equals(""))
            try {
                tvResults.setText(evaluator.evaluate(values) + "");
                results = "" + evaluator.evaluate(values);
            } catch (Exception e) {
                tvResults.setText("Invalid expression");
            }
    }

    public void onClickCancel(View view) {
        values = "";
        results = "";
        setValues();
    }

    public void onClickDelete(View view) {
        values = values.substring(0,values.length()-1);
        setValues();
    }
}
