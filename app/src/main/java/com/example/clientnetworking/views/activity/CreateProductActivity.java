package com.example.clientnetworking.views.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.clientnetworking.MainActivity;
import com.example.clientnetworking.ProgressRequestBody;
import com.example.clientnetworking.R;
import com.example.clientnetworking.SharedPreferencesGetKey;
import com.example.clientnetworking.configs.APIServices;
import com.example.clientnetworking.configs.RetrofitClientServices;
import com.example.clientnetworking.models.PostusModel;
import com.example.clientnetworking.models.UserInfor;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProductActivity extends AppCompatActivity implements View.OnClickListener, ProgressRequestBody.UploadCallbacks {
    EasyImage easyImage;
    Bitmap mBitmap;
    byte[] byteArray;
    private static final int CAMERA_REQUEST_CODE = 7500;
    private static final int CHOOSER_PERMISSIONS_REQUEST_CODE = 7459;
    private static final int DOCUMENTS_REQUEST_CODE = 7501;
    String name;
    ImageView imgBack, imgProduct;
    EditText edtTitle, edtContent, edtAddress;
    Button btnPost;
    APIServices apiServices;
    String email, fullname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        initView();
        getSharedPreferencesGetKey(new SharedPreferencesGetKey(getApplicationContext()).isLogin());
    }

    private void getSharedPreferencesGetKey(String login) {
        apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
        apiServices.getKey(login).enqueue(new Callback<UserInfor>() {
            @Override
            public void onResponse(Call<UserInfor> call, Response<UserInfor> response) {
                email = response.body().getEmail();
                fullname = response.body().getFullname();
            }

            @Override
            public void onFailure(Call<UserInfor> call, Throwable t) {

            }
        });
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBack);
        imgProduct = findViewById(R.id.imgProduct);
        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        edtAddress = findViewById(R.id.edtAddress);
        btnPost = findViewById(R.id.btnPost);
        imgBack.setOnClickListener(this);
        imgProduct.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        setUpCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                startActivity(new Intent(CreateProductActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
            case R.id.imgProduct:
                chooseCamera();
                break;
            case R.id.btnPost:
                addProduct(v);
                break;
            default:
        }
    }

    private void addProduct(final View view) {
        try {
            if (checkADD(view, edtTitle.getText().toString(), edtContent.getText().toString(), edtAddress.getText().toString()) > 0) {
                if (byteArray != null) {
                    File filesDir = getFilesDir();
                    File file = new File(filesDir, "Product-" + Calendar.getInstance().getTimeInMillis() + ".png");

                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(byteArray);
                    fos.flush();
                    fos.close();
                    ProgressRequestBody fileBody = new ProgressRequestBody(file, this);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), fileBody);
                    RequestBody name = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                    RequestBody emails = RequestBody.create(MediaType.parse("text/plain"), email);
                    RequestBody fullnames = RequestBody.create(MediaType.parse("text/plain."), fullname);
                    RequestBody title = RequestBody.create(MediaType.parse("text/plain"), edtTitle.getText().toString().trim());
                    RequestBody content = RequestBody.create(MediaType.parse("text/plain"), edtContent.getText().toString().trim());
                    RequestBody address = RequestBody.create(MediaType.parse("text/plain"), edtAddress.getText().toString().trim());
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy hh:mm");
                    RequestBody date = RequestBody.create(MediaType.parse("text/plain"), formatter.format(Calendar.getInstance().getTime()));
                    apiServices = RetrofitClientServices.getInstance().create(APIServices.class);
                    apiServices.createProduct(emails, fullnames, title, content, address, date, body, name).enqueue(new Callback<PostusModel>() {
                        @Override
                        public void onResponse(Call<PostusModel> call, Response<PostusModel> response) {
                            Snackbar snackbar = Snackbar.make(view, "Succesfully", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }

                        @Override
                        public void onFailure(Call<PostusModel> call, Throwable t) {
                            Log.d("TAG", "onFailure: " + t.getMessage());
                        }
                    });

                } else {
                    Snackbar snackbar = Snackbar.make(view, "Please your enter picture", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int checkADD(View view, String title, String content, String address) {
        if (TextUtils.isEmpty(title)) {
            Snackbar snackbar = Snackbar.make(view, "Please your enter title", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        } else if (TextUtils.isEmpty(content)) {
            Snackbar snackbar = Snackbar.make(view, "Please your enter content", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        } else if (TextUtils.isEmpty(address)) {
            Snackbar snackbar = Snackbar.make(view, "Please your enter address", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return -1;
        }
        return 1;
    }


    private void setUpCamera() {
        easyImage = new EasyImage.Builder(this)
                .setChooserTitle("Pick media")
                .setCopyImagesToPublicGalleryFolder(false)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("Give And Take")
                .allowMultiple(true)
                .build();
    }

    private void chooseCamera() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chooser Type");
        builder.setMessage("Please choose photo by your camera or your documents");
        builder.setCancelable(false);
        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] necessaryPermissions = new String[]{Manifest.permission.CAMERA};
                if (arePermissionsGranted(necessaryPermissions)) {
                    easyImage.openCameraForImage(CreateProductActivity.this);
                } else {
                    requestPermissionsCompat(necessaryPermissions, CAMERA_REQUEST_CODE);
                }
            }
        });
        builder.setNegativeButton("DOCUMENTS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] necessaryPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (arePermissionsGranted(necessaryPermissions)) {
                    easyImage.openDocuments(CreateProductActivity.this);
                } else {
                    requestPermissionsCompat(necessaryPermissions, DOCUMENTS_REQUEST_CODE);
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean arePermissionsGranted(String[] necessaryPermissions) {
        for (String permission : necessaryPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    private void requestPermissionsCompat(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(CreateProductActivity.this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CHOOSER_PERMISSIONS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openChooser(CreateProductActivity.this);
        } else if (requestCode == CAMERA_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openCameraForImage(CreateProductActivity.this);
        } else if (requestCode == DOCUMENTS_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            easyImage.openDocuments(CreateProductActivity.this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        easyImage.handleActivityResult(requestCode, resultCode, data, CreateProductActivity.this, new DefaultCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                if (resultCode == Activity.RESULT_OK) {
                    String filePath = imageFiles[0].getFile().getPath();
                    name = imageFiles[0].getFile().getName();

                    btnPost.setVisibility(View.GONE);
                    if (filePath != null) {
                        mBitmap = BitmapFactory.decodeFile(filePath);
                        getByteArrayInBackground();
                        imgProduct.setImageBitmap(mBitmap);
                    }
                }
            }


            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {

            }
        });
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
                        btnPost.setVisibility(View.VISIBLE);
                    }
                });
            }
        };
        thread.start();
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