package cm.btech2021.microchip.activities;

import static cm.btech2021.microchip.activities.FirstStepActivity.EXTRA_FIRST_STEP_CUSTOMER;
import static cm.btech2021.microchip.activities.SecondStepActivity.EXTRA_SECOND_STEP_CUSTOMER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import cm.btech2021.microchip.R;
import cm.btech2021.microchip.models.Customer;
import cm.btech2021.microchip.repos.CustomerService;

public class LastStepActivity extends AppCompatActivity {
    public static final int PERMISSION_FINE_LOCATION = 3309;
    public static final int PERMISSION_WRITE_EXTERNAL = 3310;
    public static final int REQUEST_PICK = 1900;
    public static final int REQUEST_IMAGE_CAPTURE = 1901;
    static String PICTURE_SAVE_LOCATION = "/Pictures/Microchip/";

    ImageView idCardImageView;
    TextView txtAddPicture;
    TextView txtLocation;
    Button btnGetLocation;
    Button btnSave;
    Button btnPrevious;

    Customer customer;
    FusedLocationProviderClient fusedLocationProviderClient;
    OutputStream imageOutputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_step);

        // Getting the extra from the second step activity
        String extra = getIntent().getExtras().getString(EXTRA_SECOND_STEP_CUSTOMER);
        if (extra != null) {
            customer = new Gson().fromJson(extra, Customer.class);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        idCardImageView = findViewById(R.id.activityLastStepIDCardImage);
        txtAddPicture = findViewById(R.id.activityLastStepTxtAddPicture);
        txtLocation = findViewById(R.id.activityLastStepTxtLocation);
        btnGetLocation = findViewById(R.id.activityLastStepGetLocationBtn);
        btnSave = findViewById(R.id.activityLastStepSaveBtn);
        btnPrevious = findViewById(R.id.activityLastStepPreviousBtn);

        btnPrevious.setOnClickListener(event -> onBackPressed());

        txtAddPicture.setOnClickListener(event -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                displayImagePicker();

            } else {
                requestStoragePermission();
            }
        });

        btnGetLocation.setOnClickListener(event -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                displayLocation();

            } else {
                requestLocationPermission();
            }
        });

        btnSave.setOnClickListener(event -> {
            if (customer != null && customer.isValid()) {
                CustomerService.CUSTOMER_LIST.add(customer);
                Toast.makeText(this, "The customer has been successfully registered."
                        , Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                Toast.makeText(this, "Unable to continue because all fields are required."
                        , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to get your current location")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, PERMISSION_FINE_LOCATION);
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSION_FINE_LOCATION);
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to save your pictures to local storage")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, PERMISSION_WRITE_EXTERNAL);
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_WRITE_EXTERNAL);
        }
    }

    @SuppressLint("MissingPermission")
    private void displayLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();

            if (location != null) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                txtLocation.setText(String.format(
                        Locale.getDefault(), "Long: %f, Lat: %f", longitude, latitude)
                );

                // Saving location to the current customer object
                if (customer != null) customer.setSellLocation(location);
            }
        })
        .addOnFailureListener(task -> {
            Toast.makeText(this, "Your last location is unknown"
                    , Toast.LENGTH_LONG).show();
        });
    }

    private void displayImagePicker() {
        String[] options = {"🌸   Gallery", "\uD83D\uDCF7   Camera", "❌   Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Take a photo");
        builder.setItems(options, (dialogInterface, i) -> {
            if (options[i].equals(options[0])) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK);
            } else if (options[i].equals(options[1])) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } else {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted."
                        , Toast.LENGTH_LONG).show();
                displayLocation();
            }
        } else if (requestCode == PERMISSION_WRITE_EXTERNAL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted."
                        , Toast.LENGTH_LONG).show();
                displayImagePicker();
            }
        } else {
            Toast.makeText(this, "Permission denied."
                    , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_PICK) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    idCardImageView.setImageURI(selectedImageUri);
                    if (customer != null)
                        customer.setIdCardPhotoUri(selectedImageUri);
                }
            } else {
                // Photo has been taken
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                idCardImageView.setImageBitmap(imageBitmap);

                // Save to the gallery
                File filePath = Environment.getExternalStorageDirectory();
                File directory = new File(filePath.getAbsolutePath() + PICTURE_SAVE_LOCATION);
                boolean directoryIsValid = directory.isDirectory();

                if (!directory.exists()) directoryIsValid = directory.mkdirs();

                if (directoryIsValid) {
                    String imageName = System.currentTimeMillis() + ".jpg";
                    File file = new File(directory, imageName);
                    try {
                        imageOutputStream = new FileOutputStream(file);
                        // Image is saved here
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageOutputStream);

                        // Then, customer ID card image uri is updated
                        customer.setIdCardPhotoUri(Uri.fromFile(file));

                        Toast.makeText(this, "Successfully saved image to "
                                + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        imageOutputStream.flush();
                        imageOutputStream.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}