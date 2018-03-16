package com.manolatostech.photohistoryofgreece;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    File storageDir;
    TextView toastMessage;
    Toast toast;
    boolean onDelete = false, gpsEnabled, isConnected, firstTime = true;
    double latitude = 0, longitude = 0;
    int REQUEST_IMAGE_CAPTURE = 1, REQUEST_TAKE_PHOTO = 1, h, w, count;
    Helper helper;
    MyDbHelper myDbHelper;
    DBHelper dbHelper;
    GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;
    BottomNavigationView navigation;
    DecimalFormat decimalFormat = new DecimalFormat("#.00000000");
    RelativeLayout.LayoutParams relativeParams;
    RelativeLayout svrl;
    CheckBox[] checkBoxes;
    Button deletePhotos, checkAll;
    ImageView delete;
    ScrollView sv;
    MainActivity mainActivity;
    ArrayList<DataModel> dataModel = new ArrayList<DataModel>();
    ArrayList<InformationModel> informationModel = new ArrayList<InformationModel>();
    TimerTask timerTask;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_photos:

                    return true;
                case R.id.navigation_camera:
                    if (!isConnected) {
                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                        toastMessage.setText("No Internet Connection!");
                        toast.setView(toastMessage);
                        toast.show();
                    } else if (!gpsEnabled) {
                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                        toastMessage.setText("Please turn on the GPS!");
                        toast.setView(toastMessage);
                        toast.show();
                        locationInit();
                    } else if (latitude == 0 || longitude == 0) {
                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                        toastMessage.setText("Waiting for your location. Please try again!");
                        toast.setView(toastMessage);
                        toast.show();
                    } else
                        dispatchTakePictureIntent();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#A5A2A2"));
        mainActivity = this;
        Display display = getWindowManager().getDefaultDisplay();
        w = (display.getWidth());
        h = (display.getHeight());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        toastMessage = new TextView(this);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setTypeface(null, Typeface.BOLD);
        toastMessage.setPadding(w * 5 / 100, h * 2 / 100, w * 5 / 100, h * 2 / 100);
        toastMessage.setTextSize((float) (w * 1 / 100));
        toast = Toast.makeText(getApplicationContext(), null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        dbHelper = new DBHelper(this);
        helper = new Helper();
        informationModel = helper.getInformation();
        myDbHelper = new MyDbHelper(this);
        myDbHelper.create();
        dataModel = dbHelper.getData();
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists())
            storageDir.mkdir();
        svrl = (RelativeLayout) findViewById(R.id.svrl);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        relativeParams = (RelativeLayout.LayoutParams) rl.getLayoutParams();
        relativeParams.setMargins(w * 5 / 100, h * 2 / 100, 0, 0);
        relativeParams.height = h * 9 / 100;
        relativeParams.width = w * 90 / 100;
        rl.setLayoutParams(relativeParams);
        checkAll = new Button(this);
        rl.addView(checkAll);
        relativeParams = (RelativeLayout.LayoutParams) checkAll.getLayoutParams();
        relativeParams.setMargins(w * 4 / 100, h * 1 / 100, 0, 0);
        relativeParams.height = h * 7 / 100;
        relativeParams.width = w * 30 / 100;
        checkAll.setLayoutParams(relativeParams);
        checkAll.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        checkAll.setText("Check All");
        checkAll.setEnabled(false);
        checkAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!deletePhotos.isEnabled())
                    deletePhotos.setEnabled(true);
                for (int i = 0; i < count; i++) {
                    if (!checkBoxes[i].isChecked()) {
                        checkBoxes[i].setChecked(true);
                    }
                }
            }
        });

        deletePhotos = new Button(this);
        rl.addView(deletePhotos);
        relativeParams = (RelativeLayout.LayoutParams) deletePhotos.getLayoutParams();
        relativeParams.setMargins(w * 41 / 100, h * 1 / 100, 0, 0);
        relativeParams.height = h * 7 / 100;
        relativeParams.width = w * 30 / 100;
        deletePhotos.setLayoutParams(relativeParams);
        deletePhotos.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        deletePhotos.setText("Delete");
        deletePhotos.setEnabled(false);
        deletePhotos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");
                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        int index = 0;
                        for (int i = 0; i < count; i++) {
                            if (checkBoxes[i].isChecked()) {
                                File file = new File(storageDir, dataModel.get(i - index).imageName);
                                file.delete();
                                dbHelper.delete(dataModel.get(i - index).id);
                                dataModel.remove(i - index);
                                index++;
                            }
                        }
                        if (dataModel.size() == 0) {
                            checkAll.setEnabled(false);
                            delete.setBackgroundResource(R.drawable.ic_delete);
                            onDelete = false;
                        }
                        toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                        toastMessage.setText("Deleted Successfully!");
                        toast.setView(toastMessage);
                        toast.show();
                        deletePhotos.setEnabled(false);
                        svrl.removeAllViews();
                        getFiles();
                        createCheckBoxes();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });

        delete = new ImageView(this);
        rl.addView(delete);
        relativeParams = (RelativeLayout.LayoutParams) delete.getLayoutParams();
        relativeParams.setMargins(w * 78 / 100, h * 1 / 100, 0, 0);
        relativeParams.height = h * 6 / 100;
        relativeParams.width = w * 10 / 100;
        delete.setLayoutParams(relativeParams);
        delete.setBackgroundResource(R.drawable.ic_delete);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (dataModel.size() > 0) {
                    if (!onDelete) {
                        checkAll.setEnabled(true);
                        delete.setBackgroundResource(R.drawable.ic_cancel);
                        createCheckBoxes();
                    } else {
                        checkAll.setEnabled(false);
                        deletePhotos.setEnabled(false);
                        delete.setBackgroundResource(R.drawable.ic_delete);
                        destroyCheckBoxes();
                    }
                    onDelete = !onDelete;
                }
            }
        });

        sv = (ScrollView) findViewById(R.id.sv);
        relativeParams = (RelativeLayout.LayoutParams) sv.getLayoutParams();
        relativeParams.setMargins(0, h * 14 / 100, 0, 0);
        relativeParams.height = h * 74 / 100;
        relativeParams.width = w;
        sv.setLayoutParams(relativeParams);
        int MyVersion = android.os.Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermissions()) {
                requestForSpecificPermissions();
            } else {
                locationInit();
            }
        } else {
            locationInit();
        }
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getFiles();
    }

    private void getFiles() {
        ImageView[] photos = new ImageView[dataModel.size()];
        count = 0;
        int x = 5, y = 7;
        for (int i = 0; i < dataModel.size(); i++) {
            photos[count] = new ImageView(this);
            svrl.addView(photos[count]);
            relativeParams = (RelativeLayout.LayoutParams) photos[count].getLayoutParams();
            relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 5 / 100);
            relativeParams.height = h * 40 / 100;
            relativeParams.width = w * 40 / 100;
            photos[count].setLayoutParams(relativeParams);
            photos[count].setBackgroundColor(Color.BLACK);
            photos[count].setTag(count);
            String photoPath = storageDir + "/" + dataModel.get(i).imageName;
            Bitmap bitmap = helper.RotateBitmap(BitmapFactory.decodeFile(photoPath), 90);
            photos[count].setImageBitmap(bitmap);
            photos[count].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    long informationId = dataModel.get(tag).informationId;
                    String information = "";
                    if (!onDelete) {
                        for (int i = 0; i < informationModel.size(); i++) {
                            if (informationId == informationModel.get(i).id) {
                                information = informationModel.get(i).information;
                                break;
                            }
                        }
                        Intent myIntent = new Intent(mainActivity, HistoryActivity.class);
                        myIntent.putExtra("text", information);
                        startActivity(myIntent);
                    }
                }
            });
            count++;
            if ((count % 2) == 0) {
                y += 55;
                x = 5;
            } else {
                x += 50;
            }
        }
    }

    private void createCheckBoxes() {
        checkBoxes = new CheckBox[dataModel.size()];
        count = 0;
        int x = 5, y = 0;
        for (int i = 0; i < dataModel.size(); i++) {
            checkBoxes[count] = new CheckBox(this);
            svrl.addView(checkBoxes[count]);
            relativeParams = (RelativeLayout.LayoutParams) checkBoxes[count].getLayoutParams();
            relativeParams.setMargins(w * (x + 34) / 100, h * y / 100, 0, 0);
            checkBoxes[count].setLayoutParams(relativeParams);
            checkBoxes[count].setTag(count);
            if (Build.VERSION.SDK_INT < 21) {
                CompoundButtonCompat.setButtonTintList(checkBoxes[count], ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            } else {
                checkBoxes[count].setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
            }
            checkBoxes[count].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int tag = (int) view.getTag();
                    if (!checkBoxes[tag].isChecked()) {
                        int found = 0;
                        for (int i = 0; i < count; i++) {
                            if (checkBoxes[i].isChecked()) {
                                found = 1;
                                break;
                            }
                        }
                        if (found == 0)
                            deletePhotos.setEnabled(false);
                    } else if (!deletePhotos.isEnabled())
                        deletePhotos.setEnabled(true);
                }
            });
            count++;
            if ((count % 2) == 0) {
                y += 55;
                x = 5;
            } else {
                x += 50;
            }
        }
    }

    private void destroyCheckBoxes() {
        count = 0;
        for (int i = 0; i < dataModel.size(); i++) {
            svrl.removeView(checkBoxes[count]);
            count++;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                gpsEnabled = true;
                toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                toastMessage.setText("GPS enabled!");
                toast.setView(toastMessage);
                toast.show();
            } else {
                gpsEnabled = false;
                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                toastMessage.setText("GPS is not enabled!");
                toast.setView(toastMessage);
                toast.show();
            }
            locationInit();
        } else {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                if (!gpsEnabled) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("Please turn on the GPS!");
                    toast.setView(toastMessage);
                    toast.show();
                    locationInit();
                } else if (latitude == 0 || longitude == 0) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("Waiting for your location. Please try again!");
                    toast.setView(toastMessage);
                    toast.show();
                } else {
                    try {
                        String lastDCIMPic = helper.getLastDCIMPic(this);
                        File sourceFile = new File(lastDCIMPic);
                        helper.deleteDCIMFile(sourceFile);
                        helper.deleteDCIMThumbnail(this.getContentResolver(), sourceFile);
                        long informationId = -1;
                        Location center, test;
                        double tempDistance, distance = 999999;
                        for (int i = 0; i < informationModel.size(); i++) {
                            center = new Location("");
                            test = new Location("");
                            center.setLatitude(latitude);
                            center.setLongitude(longitude);
                            test.setLatitude(latitude);
                            test.setLongitude(longitude);
                            tempDistance = center.distanceTo(test);
                            boolean isWithin1km = tempDistance < 1000;
                            if (isWithin1km) {
                                if (tempDistance < distance) {
                                    distance = tempDistance;
                                    informationId = i;
                                }
                            }
                        }
                        if (informationId == -1) {
                            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                            toastMessage.setText("There is no museum near you where you took the photo.Please move closer!");
                            toast.setView(toastMessage);
                            toast.show();
                        } else {
                            informationId++;
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            String imageName = dataModel.size() + ".jpg";
                            imageName = helper.SaveImage(dataModel.size(), imageName, imageBitmap, storageDir);
                            long id = dbHelper.insert(imageName, latitude, longitude, informationId);
                            DataModel temp = new DataModel();
                            temp.id = id;
                            temp.imageName = imageName;
                            temp.latitude = latitude;
                            temp.longitude = longitude;
                            temp.informationId = informationId;
                            dataModel.add(0, temp);
                        }
                        dispatchTakePictureIntent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (onDelete) {
                    checkAll.setEnabled(false);
                    deletePhotos.setEnabled(false);
                    delete.setBackgroundResource(R.drawable.ic_delete);
                    onDelete = false;
                }
                navigation.setSelectedItemId(R.id.navigation_photos);
                sv.scrollTo(0, 0);
                svrl.removeAllViews();
                getFiles();
            }
        }
    }

    private boolean checkIfAlreadyhavePermissions() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int result3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    locationInit();
                } else if (Build.VERSION.SDK_INT >= 23 || !shouldShowRequestPermissionRationale(permissions[0]) || !shouldShowRequestPermissionRationale(permissions[1]) || !shouldShowRequestPermissionRationale(permissions[2])) {
                    // User selected the Never Ask Again Option Change settings in app settings manually
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Change Permissions in Settings");
                    alertDialogBuilder
                            .setMessage("" +
                                    "\nClick SETTINGS to manually set\n" + "Permissions")
                            .setCancelable(false)
                            .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, 1000);     // Comment 3.
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    // User selected Deny Dialog to EXIT App ==> OR <== RETRY to have a second chance to Allow Permissions
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("You have to set permissions to allow ");
                        alertDialogBuilder
                                .setMessage("Click ALLOW to restart the app\n\n" + "Click EXIT to close app")
                                .setCancelable(false)
                                .setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                    }
                                })
                                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            finishAndRemoveTask();
                                        } else {
                                            finish();
                                        }
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void locationInit() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                final Handler handler = new Handler();
                Timer t = new Timer();
                timerTask = new TimerTask() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                isConnected = helper.isOnline(getApplicationContext());
                                if (!isConnected) {
                                    latitude = 0;
                                    longitude = 0;
                                    if (firstTime) {
                                        firstTime = false;
                                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                        toastMessage.setText("No Internet Connection!");
                                        toast.setView(toastMessage);
                                        toast.show();
                                    }
                                }
                            }
                        });
                    }
                };

                // public void schedule (TimerTask task, long delay, long period)
                t.schedule(timerTask, 0, 1000);  //
                gpsEnabled = true;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                gpsEnabled = false;
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            latitude = Double.valueOf(decimalFormat.format(location.getLatitude()).replace(",", "."));
            longitude = Double.valueOf(decimalFormat.format(location.getLongitude()).replace(",", "."));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
            latitude = 0;
            longitude = 0;
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("Please turn on the GPS!");
            toast.setView(toastMessage);
            toast.show();
            locationInit();
        }
    };

    public class MyDbHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "PhotoHistory.db";

        public MyDbHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        public void create() {
            SQLiteDatabase db = this.getReadableDatabase();
            DBHelper mydb = new DBHelper(mainActivity);
            mydb.onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

    }
}