package cm.btech2021.microchip.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import cm.btech2021.microchip.R;
import cm.btech2021.microchip.models.Customer;

public class FirstStepActivity extends AppCompatActivity {
    public static final String EXTRA_FIRST_STEP_CUSTOMER = "EXTRA_FIRST_STEP_CUSTOMER";
    TextView txtChipNumber;
    TextView txtIdCardNumber;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_step);

        txtChipNumber = findViewById(R.id.activityFirstStepChipNumberTxtInput);
        txtIdCardNumber = findViewById(R.id.activityFirstStepIdNumberTxtInput);
        btnNext = findViewById(R.id.activityFirstStepNextBtn);

        btnNext.setOnClickListener(event -> {
            String chipNumber = txtChipNumber.getText().toString().trim();
            String idCardNumber = txtIdCardNumber.getText().toString().trim();

            if (!chipNumber.isEmpty() && !idCardNumber.isEmpty()){
                Customer customer = new Customer(chipNumber, idCardNumber);
                Intent intent = new Intent(this, SecondStepActivity.class);
                intent.putExtra(EXTRA_FIRST_STEP_CUSTOMER, new Gson().toJson(customer));
                startActivity(intent);
            }
            else new AlertDialog.Builder(this)
                    .setTitle("Verify your values")
                    .setMessage("Some values are missing, please verify your values.")
                    .create().show();
        });
    }
}