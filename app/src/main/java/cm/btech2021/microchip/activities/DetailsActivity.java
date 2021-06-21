package cm.btech2021.microchip.activities;

import static cm.btech2021.microchip.adapters.CustomerAdapter.EXTRA_CUSTOMER;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import cm.btech2021.microchip.R;
import cm.btech2021.microchip.models.Customer;

public class DetailsActivity extends AppCompatActivity {
    ImageView imageView;
    TextView txtFullName;
    TextView txtChipNumber;
    TextView txtBirthdate;
    Customer customer;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imageView = findViewById(R.id.detailsActivityImage);
        txtFullName = findViewById(R.id.detailsActivityTitle);
        txtChipNumber = findViewById(R.id.detailsActivityAddress);
        txtBirthdate = findViewById(R.id.detailsActivityBirthdate);
        backButton = findViewById(R.id.detailsActivityBackBtn);

        backButton.setOnClickListener(event -> {
            onBackPressed();
        });

        String extra = getIntent().getExtras().getString(EXTRA_CUSTOMER);
        if (extra != null){
            customer = new Gson().fromJson(extra, Customer.class);

            if (customer.getIdCardPhotoUri() != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(),
                            customer.getIdCardPhotoUri()
                    );
                    imageView.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            txtFullName.setText(customer.getFullName());
            txtChipNumber.setText(customer.getChipNumber());
            txtBirthdate.setText(customer.getDateOfBirth().toString());
        }

    }
}