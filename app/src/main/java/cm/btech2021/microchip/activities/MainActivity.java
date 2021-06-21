package cm.btech2021.microchip.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import cm.btech2021.microchip.R;
import cm.btech2021.microchip.adapters.CustomerAdapter;
import cm.btech2021.microchip.repos.CustomerService;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.activityMainRecyclerView);
        registerBtn = findViewById(R.id.activityMainRegisterBtn);

        recyclerView.setAdapter(new CustomerAdapter(CustomerService.CUSTOMER_LIST));

        registerBtn.setOnClickListener(event -> {
            startActivity(new Intent(this, FirstStepActivity.class));
        });
    }
}