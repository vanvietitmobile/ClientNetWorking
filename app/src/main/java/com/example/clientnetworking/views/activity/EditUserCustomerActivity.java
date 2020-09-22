package com.example.clientnetworking.views.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.clientnetworking.MainActivity;
import com.example.clientnetworking.ProgressRequestBody;
import com.example.clientnetworking.R;
import com.example.clientnetworking.SharedPreferencesGetKey;
import com.example.clientnetworking.configs.APIServices;
import com.example.clientnetworking.configs.RetrofitClientServices;
import com.example.clientnetworking.models.UserInfor;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditUserCustomerActivity extends AppCompatActivity implements View.OnClickListener,ProgressRequestBody.UploadCallbacks {
    ImageView imgBack;
    EditText tvFullNameCustomer, tvBirthdayCustomer, tvAddressCustomer, tvPhoneCustomer;
    Button btnUpdate;
    ImageView imageCustomer;
    APIServices apiServices;
    List<UserInfor> userInforList = new ArrayList<>();

    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    Bitmap mBitmap;
    byte[] byteArray;
    String id ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_customer);
        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        tvFullNameCustomer = findViewById(R.id.tvFullNameCustomer);
        tvBirthdayCustomer = findViewById(R.id.tvBirthdayCustomer);
        tvAddressCustomer = findViewById(R.id.tvAddressCustomer);
        tvPhoneCustomer = findViewById(R.id.tvPhoneCustomer);
        btnUpdate = findViewById(R.id.btnUpdate);
        imageCustomer = findViewById(R.id.imageCustomer);

        btnUpdate.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imageCustomer.setOnClickListener(this);
        askPermissions();
        getSharedPreferencesGetKey(new SharedPreferencesGetKey(getApplicationContext()).isLogin());
    }

    private void getSharedPreferencesGetKey(String login) {
        apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
        apiServices.getKey(login).enqueue(new Callback<UserInfor>() {
            @Override
            public void onResponse(Call<UserInfor> call, Response<UserInfor> response) {
                 tvAddressCustomer.setText(response.body().getAddress());
                 tvPhoneCustomer.setText(response.body().getPhone());
                 tvBirthdayCustomer.setText(response.body().getBirthday());
                 tvFullNameCustomer.setText(response.body().getFullname());
                 userInforList.add(new UserInfor(response.body().getId()));
                 id = response.body().getId();
            }

            @Override
            public void onFailure(Call<UserInfor> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageCustomer:
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
                break;
            case R.id.imgBack:
                startActivity(new Intent(EditUserCustomerActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            case R.id.btnUpdate:
                if (mBitmap != null) {
                    multipartImageUpload(v);
                } else {
                    Toast.makeText(getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void multipartImageUpload(final View v) {
        try {
            if (byteArray != null) {
                File filesDir = getFilesDir();
                File file = new File(filesDir, "IMG-"+Calendar.getInstance().getTimeInMillis() + ".png");

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(byteArray);
                fos.flush();
                fos.close();
                ProgressRequestBody fileBody = new ProgressRequestBody(file,this);
                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), fileBody);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                RequestBody fullname = RequestBody.create(MediaType.parse("text/plain"), tvFullNameCustomer.getText().toString().trim());
                RequestBody _id = RequestBody.create(MediaType.parse("text/plain"), id );
                RequestBody birthday = RequestBody.create(MediaType.parse("text/plain"), tvBirthdayCustomer.getText().toString().trim());
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), tvAddressCustomer.getText().toString().trim());
                RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), tvPhoneCustomer.getText().toString().trim());
                apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
                apiServices.updateUser(_id,fullname,birthday,address,phone,body,name).enqueue(new Callback<UserInfor>() {
                    @Override
                    public void onResponse(Call<UserInfor> call, Response<UserInfor> response) {
                        Snackbar snackbar = Snackbar.make(v,"Update Success",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }

                    @Override
                    public void onFailure(Call<UserInfor> call, Throwable t) {
                        Log.d("TAG", "onFailure: "+ t.getMessage());
                    }
                });

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    public Intent getPickImageChooserIntent() {
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooserIntent;
    }
    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;
        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());
    }
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }
    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }
    private void getByteArrayInBackground() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byteArray = bos.toByteArray();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnUpdate.setVisibility(View.VISIBLE);
                    }
                });
            }

        };
        thread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ImageView imageView = findViewById(R.id.imageCustomer);
            if (requestCode == IMAGE_RESULT) {
                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    btnUpdate.setVisibility(View.GONE);
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    getByteArrayInBackground();
                        imageView.setImageBitmap(mBitmap);
                }
            }
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadStart() {

    }
}