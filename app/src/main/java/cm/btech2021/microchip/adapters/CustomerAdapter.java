package cm.btech2021.microchip.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cm.btech2021.microchip.R;
import cm.btech2021.microchip.activities.DetailsActivity;
import cm.btech2021.microchip.models.Customer;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    public static final String EXTRA_CUSTOMER = "EXTRA_CUSTOMER";
    final ArrayList<Customer> customers;

    public CustomerAdapter(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.card_customer,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer customer = customers.get(position);
        Context context = holder.itemView.getContext();

        holder.itemView.setOnClickListener(event -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(EXTRA_CUSTOMER, new Gson().toJson(customer));
            context.startActivity(intent);
        });

        if (customer.getIdCardPhotoUriPath() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        context.getContentResolver(),
                        Uri.fromFile(new File(customer.getIdCardPhotoUriPath()))
                );
                holder.imageCustomerView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        holder.txtFullName.setText(customer.getFullName());

        holder.txtChipNumber.setText(customer.getChipNumber());

    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCustomerView;
        TextView txtFullName;
        TextView txtChipNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageCustomerView = itemView.findViewById(R.id.cardCustomerImageView);
            txtFullName = itemView.findViewById(R.id.cardCustomerTitle);
            txtChipNumber = itemView.findViewById(R.id.cardCustomerSubTitle);
        }
    }
}
