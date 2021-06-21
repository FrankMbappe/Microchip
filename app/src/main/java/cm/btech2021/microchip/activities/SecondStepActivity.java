package cm.btech2021.microchip.activities;

import static cm.btech2021.microchip.activities.FirstStepActivity.EXTRA_FIRST_STEP_CUSTOMER;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cm.btech2021.microchip.R;
import cm.btech2021.microchip.models.Customer;

public class SecondStepActivity extends AppCompatActivity {
    public static final String EXTRA_SECOND_STEP_CUSTOMER = "EXTRA_SECOND_STEP_CUSTOMER";

    TextView txtFullName;
    TextView txtBirthdate;
    Button btnNext;
    Button btnPrevious;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_step);

        final String DATE_FORMAT = getResources().getString(R.string.val_date_format);

        txtFullName = findViewById(R.id.activitySecondStepFullNameTxtInput);
        txtBirthdate = findViewById(R.id.activitySecondStepBirthdateTxtInput);
        btnNext = findViewById(R.id.activitySecondStepNextBtn);
        btnPrevious = findViewById(R.id.activitySecondStepPreviousBtn);

        btnPrevious.setOnClickListener(event -> {
            onBackPressed();
        });

        btnNext.setOnClickListener(event -> {
            String fullName = txtFullName.getText().toString().trim();
            String birthDate = txtBirthdate.getText().toString().trim();

            // Getting the extra from the first step activity
            String extra = getIntent().getExtras().getString(EXTRA_FIRST_STEP_CUSTOMER);
            if (extra != null){
                customer = new Gson().fromJson(extra, Customer.class);

                if (!fullName.isEmpty() && !birthDate.isEmpty()){
                    try {
                        customer.setFullName(fullName);
                        customer.setDateOfBirth(new SimpleDateFormat(
                                DATE_FORMAT, Locale.getDefault()).parse(birthDate));

                        Intent intent = new Intent(this, LastStepActivity.class);
                        intent.putExtra(EXTRA_SECOND_STEP_CUSTOMER, new Gson().toJson(customer));
                        startActivity(intent);

                    } catch (ParseException e) {
                        new AlertDialog.Builder(this)
                                .setTitle("Invalid date value")
                                .setMessage("An error occurred while parsing your birthdate value. " +
                                        "Please respect the format: " + DATE_FORMAT )
                                .create().show();
                    }
                }
            }
        });
    }
}