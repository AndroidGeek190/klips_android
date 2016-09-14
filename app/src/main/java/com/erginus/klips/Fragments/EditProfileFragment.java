package com.erginus.klips.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.erginus.klips.Adapter.CountryAdapter;

import com.erginus.klips.Commons.MapAppConstant;
import com.erginus.klips.Commons.MultipartRequest;
import com.erginus.klips.Commons.Prefshelper;

import com.erginus.klips.Commons.VolleySingleton;
import com.erginus.klips.HomeActivity;
import com.erginus.klips.Model.CountryModel;

import com.erginus.klips.R;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditProfileFragment extends Fragment {
 EditText edt_first_name, edt_last_name, edt_email;
    Spinner spr_country;
    List<CountryModel> list;
    int country_id;
    Prefshelper prefHelper;
    File f;
    Uri imageUri;
    String user_email,user_phn,fname, lname;
    TextView btn_submit;
    private static final int PHOTO_PICKER_ID = 1;
    String filename;
    ProgressDialog pDialog;
    ImageView imageView_profile;
    LinearLayout ll_edt_image;
    Prefshelper prefsHelper;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2500;
    View view;
    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootview= inflater.inflate(R.layout.fragment_edit_profile, container, false);
        HomeActivity.txt_title.setText("Edit Profile");
        spr_country=(Spinner)rootview.findViewById(R.id.spinner_country);
        edt_first_name=(EditText)rootview.findViewById(R.id.edt_userFname);
        edt_last_name=(EditText)rootview.findViewById(R.id.edt_userLname);
        edt_email=(EditText)rootview.findViewById(R.id.edt_email);
       // edt_primary_contact=(EditText)rootview.findViewById(R.id.edt_contact);
        imageView_profile=(ImageView)rootview.findViewById(R.id.profilePic);
        ll_edt_image=(LinearLayout)rootview.findViewById(R.id.ll_edt_profile);
        btn_submit=(TextView)rootview.findViewById(R.id.button_sbmt);
        prefHelper=new Prefshelper(getActivity());
        edt_first_name.setText(prefHelper.getUserFNameFromPreference());
        edt_last_name.setText(prefHelper.getUserLNameFromPreference());
      //  edt_primary_contact.setText(prefHelper.getUserContactFromPreference());
        edt_email.setText(prefHelper.getUserEmailFromPreference());
        list = new ArrayList<CountryModel>();
        prefsHelper= new Prefshelper(getActivity());

            Picasso.with(getActivity()).load(prefHelper.getImageFromPreference()).into(imageView_profile);


        spr_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_id = Integer.parseInt(list.get(position).getId());
                Log.d("countryid", "" + country_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getCountry();

    ll_edt_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          dialog();
        }
    });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View focusView = null;
                boolean cancelLogin = false;
                fname = edt_first_name.getText().toString();
                lname = edt_last_name.getText().toString();
                user_email = edt_email.getText().toString();
            //    user_phn = edt_primary_contact.getText().toString();


                if (TextUtils.isEmpty(fname)) {
                    edt_first_name.setError(getString(R.string.userName_required));
                    focusView = edt_first_name;
                    cancelLogin = true;
                }
                if (TextUtils.isEmpty(lname)) {
                    edt_last_name.setError(getString(R.string.userName_required));
                    focusView = edt_last_name;
                    cancelLogin = true;
                }


                if (TextUtils.isEmpty(user_email)) {
                    edt_email.setError(getString(R.string.email_required));
                    focusView = edt_email;
                    cancelLogin = true;
                } else if (!isValidEmail(user_email)) {
                    edt_email.setError(getString(R.string.invalid_email));
                    focusView = edt_email;
                    cancelLogin = true;
                }
               /* if (TextUtils.isEmpty(user_phn) & !isValidPhone((user_phn))) {
                    edt_primary_contact.setError(getString(R.string.phone_required));
                    focusView = edt_primary_contact;
                    cancelLogin = true;
                }*/
                if (cancelLogin) {
                    // error in login
                    focusView.requestFocus();
                } else {
                    editProfile();
                }

            }});
                return  rootview;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhone(String pass) {
        return pass != null && pass.length() == 10;
    }
    public void dialog()
    {
        final Dialog dialog = new Dialog(getActivity(), R.style.cust_dialog);
        dialog.setTitle("Upload From");
        dialog.setContentView(R.layout.dialog_pop_up_gallery_camera);

        dialog.setTitle("Select an Option...");
        TextView txt_gallry=(TextView)dialog.findViewById(R.id.textView_gallery);
        TextView txt_camera=(TextView)dialog.findViewById(R.id.textView_camera);

        txt_gallry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                             Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            i.setType("image/*");
                            startActivityForResult(i, PHOTO_PICKER_ID);
            }
        });
        txt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();



                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                File fil = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fil));
                startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_IMAGE);
            }
        });
        dialog.show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE_CAPTURE_IMAGE:

                    if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK ) {

                        File fil = new File(Environment.getExternalStorageDirectory().toString());
                        for (File temp : fil.listFiles()) {
                            if (temp.getName().equals("temp.jpg")) {
                                fil = temp;
                                break;
                            }
                        }
                        Bitmap bitmap = null;

                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        Bitmap bmp = BitmapFactory.decodeFile(fil.getAbsolutePath(),bitmapOptions);
//
                        Log.e("edit", "new image path is " + fil.getAbsolutePath());
                        Log.e("bitmap",""+bmp);
                        Log.e("Result Ok",""+data);

                        compressImage(fil.getAbsolutePath());
                        f= new File(filename);
                        uploadImage();
//
                    }

                    break;
                case PHOTO_PICKER_ID:
                    if (requestCode == PHOTO_PICKER_ID && resultCode == Activity.RESULT_OK && null != data) {
                        Log.e("Result Ok",""+data);
                        Uri selectedImage = data.getData();
                        Log.e("selected image", "" + selectedImage);
                        Log.e("selected image", "" + getPath(selectedImage));
                        compressImage(getPath(selectedImage));
                        f= new File(filename);
                        uploadImage();

                    }

                    break;
            }
        } catch (Exception e)
        {
            Log.d("krvrrusbviuritiribtr", e.getMessage());
        }
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null)
            return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public void getCountry() {
        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "countries " + MapAppConstant.API + "countries");
            StringRequest sr = new StringRequest(Request.Method.POST, MapAppConstant.API + "countries", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                     //   Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONArray jsonArray = object.getJSONArray("data");
                                    String id = "", country_name = "";
                                    if(jsonArray.length()>0)
                                    {

                                        for(int i=0; i<jsonArray.length(); i++) {
                                            JSONObject object1 = jsonArray.getJSONObject(i);
                                            // id = object1.getString(MapAppConstants.COACH_ID);
                                            id = object1.getString("country_id");
                                            country_name = object1.getString("country_name");
                                            list.add(countryModel(id, country_name));
                                        }}
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            CountryAdapter countryAdapter=new CountryAdapter(getActivity(),list);
                            spr_country.setAdapter(countryAdapter);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getActivity(), "Timeout Error",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ServerError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof NetworkError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ParseError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    }
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("current_timestamp", "1");

                    return params;
                }
            };
            sr.setShouldCache(true);

            sr.setRetryPolicy(new DefaultRetryPolicy(50000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private CountryModel countryModel(String id,String name)
    {
        CountryModel coach = new CountryModel();
        coach.setId(id);
        coach.setCountry_name(name);

        return coach;
    }
    public void uploadImage()
    {
        try {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "LOGIN " + MapAppConstant.API + "profile_image");
            HashMap params = new HashMap<String, String>();
            params.put("user_id", prefHelper.getUserIdFromPreference());
            params.put("user_security_hash", prefHelper.getUserSecHashFromPreference());
            MultipartRequest sr = new MultipartRequest(MapAppConstant.API +"profile_image", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Log.d("file", f + "");
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject object1 = object.getJSONObject("data");
                                    String  img=object1.getString("user_profile_image_url");
                                    prefHelper.storeImageToPreference(img);

                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            Picasso.with(getActivity()).load(prefHelper.getImageFromPreference()).into(HomeActivity.profile_image);
                            Picasso.with(getActivity()).load(prefHelper.getImageFromPreference()).into(imageView_profile);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getActivity(), "Timeout Error",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ServerError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof NetworkError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ParseError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());

                    }
                }

            }, f, params);
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }



    @Override
    public void onPause() {
        super.onPause();

        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = null;
    }
    public void editProfile()
    {
        try {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Edit Profile" + MapAppConstant.API +"edit_profile");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"edit_profile", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    String fname, lname, email, contact, county_id;
                                    JSONObject jsonObject=object.getJSONObject("data");
                                    email=jsonObject.getString("user_email");
                                    fname=jsonObject.getString("user_first_name");
                                    lname=jsonObject.getString("user_last_name");
                                    county_id=jsonObject.getString("countries_id");
                                    prefsHelper.storeUserFirstNameToPreference(fname);
                                    prefsHelper.storeUserlastNameToPreference(lname);
                                    prefsHelper.storeEmailToPreference(email);
                                    prefHelper.storeCountryIdToPreference(county_id);

                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent=new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                            getActivity().finish();
                            edt_first_name.setText(prefsHelper.getUserFNameFromPreference());
                            edt_last_name.setText(prefsHelper.getUserLNameFromPreference());
                            edt_email.setText(prefsHelper.getUserEmailFromPreference());
                            HomeActivity.txt_email.setText(prefsHelper.getUserEmailFromPreference());
                            HomeActivity.txt_username.setText(prefsHelper.getUserFNameFromPreference()+" "+prefsHelper.getUserLNameFromPreference());

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getActivity(), "Timeout Error",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ServerError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof NetworkError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ParseError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    }
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id",prefsHelper.getUserIdFromPreference());
                    params.put("user_security_hash",prefsHelper.getUserSecHashFromPreference());
                    params.put("user_first_name", fname);
                    params.put("user_last_name", lname);
                    params.put("user_email",user_email);
                  //  params.put("user_primary_contact",user_phn);
                    params.put("countries_id",""+country_id);

                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if(getFragmentManager().getBackStackEntryCount() > 0) {

                        getFragmentManager().popBackStack();
                        HomeActivity.txt_title.setText("Home");
                    }

                    return true;

                }

                return false;
            }
        });
    }
}
