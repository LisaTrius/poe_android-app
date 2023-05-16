package com.desperate.pez_android.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.desperate.pez_android.R;
import com.desperate.pez_android.other.User;
import com.desperate.pez_android.other.UserInfo;
import com.google.android.material.appbar.AppBarLayout;
import com.r0adkll.slidr.Slidr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends AppCompatActivity {

    private static final String ADD_FEEDBACK = "http://www.poe.pl.ua:8080/pezREST/abon/mail/send";

    private User user;
    private int spinnerPosition;
    private String filial;
    private String[] accounts;
    private UserInfo savedData;

    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.callcenter_layout) LinearLayout callCenterLayout;
    @BindView(R.id.cos_layout) LinearLayout cosLayout;
    @BindView(R.id.button) Button button;
    @BindView(R.id.cityNumberRow) TableRow cityNumberRow;
    @BindView(R.id.secondCellNumberRow) TableRow seconCellNumber;
    @BindView(R.id.cellNumber1) TextView cellNumber1View;
    @BindView(R.id.cellNumber2) TextView cellNumber2View;
    @BindView(R.id.adress) TextView adressView;
    @BindView(R.id.cityNumber) TextView cityNumberView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        savedData = new UserInfo(getApplicationContext());
        user = savedData.getSavedUser();
        accounts = savedData.getAccounts();
        spinnerPosition = savedData.getSelectedSpinner();
        filial = String.valueOf(user.getAccountList().get(spinnerPosition).getAccount()).substring(1,3);

        ButterKnife.bind(this);

        Slidr.attach(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        button.setOnClickListener(view -> {
            if (callCenterLayout.getVisibility() == View.VISIBLE) {
                callCenterLayout.setVisibility(View.GONE);
                cosLayout.setVisibility(View.VISIBLE);
                button.setText(R.string.callcenter);
                showFilialInfo(filial);
            } else {
                cosLayout.setVisibility(View.GONE);
                callCenterLayout.setVisibility(View.VISIBLE);
                button.setText(R.string.cos);
            }
        });

        onConfigurationChanged(getResources().getConfiguration());

    }

    private void showFilialInfo(String filial) {
        if(filial.equals("02")){
            seconCellNumber.setVisibility(View.VISIBLE);
            adressView.setText("39601, Полтавська область, м. Кременчук, проспект Свободи, 8");
            cellNumber1View.setText("(067) 571-66-97");
            cellNumber2View.setText("(095) 559-84-08");
            cityNumberView.setText("(0536) 74-72-17");
        }
        else {
            try {
                InputStream inputStream = getResources().openRawResource(R.raw.filials);
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                String lines;
                while ((lines = in.readLine()) != null) {
                    String[] words = lines.split(";");
                    if (filial.equals(words[0])) {
                        String adress = words[1];
                        adressView.setText(adress);
                        String cellNumber = words[2];
                        if (!cellNumber.contains(",")) {
                            cellNumber1View.setText(cellNumber);
                        } else {
                            seconCellNumber.setVisibility(View.VISIBLE);
                            String[] numbers = cellNumber.split(",");
                            cellNumber1View.setText(numbers[0]);
                            cellNumber2View.setText(numbers[1]);
                        }
                        String cityNumber = words[3];
                        if (!cityNumber.equals("пусто"))
                            cityNumberView.setText(cityNumber);
                        else cityNumberRow.setVisibility(View.GONE);
                    }
                }
                inputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
            // Checks the orientation of the screen
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                appbar.getLayoutParams().height = AppBarLayout.LayoutParams.WRAP_CONTENT;
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
                appbar.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
                //toolbarText.setGravity(View.TEXT_ALIGNMENT_CENTER);
            }
        }

    public boolean onOptionsItemSelected(MenuItem item){ finish(); return true; }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
