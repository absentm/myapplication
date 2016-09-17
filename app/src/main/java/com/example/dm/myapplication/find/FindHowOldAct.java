package com.example.dm.myapplication.find;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dm.myapplication.R;
import com.example.dm.myapplication.utiltools.SystemUtils;
import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * how old
 * Created by dm on 16-8-7.
 */
public class FindHowOldAct extends Activity {
    final private static String TAG = "MainActivity";
    final private int PICTURE_CHOOSE = 1;

    private ImageButton titleBackImv;
    private TextView titleCenterTv;

    private ImageView imageView = null;
    private Bitmap img = null;
    private Button buttonDetect = null;
    private TextView textView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_howold);

        initViews();
        SystemUtils.checkNetWork(FindHowOldAct.this);

        Button button = (Button) this.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // get a picture form your phone
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, PICTURE_CHOOSE);
            }
        });

        textView = (TextView) this.findViewById(R.id.textView1);
        buttonDetect = (Button) this.findViewById(R.id.button2);
        buttonDetect.setVisibility(View.INVISIBLE);
        buttonDetect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                textView.setText("Waiting ...");

                FaceppDetect faceppDetect = new FaceppDetect();
                faceppDetect.setDetectCallback(new DetectCallback() {
                    public void detectResult(JSONObject rst) {
                        // Log.v(TAG, rst.toString());

                        // use the red paint
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        paint.setStrokeWidth(Math.max(img.getWidth(),
                                img.getHeight()) / 300f);

                        // create a new canvas
                        Bitmap bitmap = Bitmap.createBitmap(img.getWidth(),
                                img.getHeight(), img.getConfig());
                        Canvas canvas = new Canvas(bitmap);
                        canvas.drawBitmap(img, new Matrix(), null);

                        try {
                            // find out all faces
                            final int count = rst.getJSONArray("face").length();
                            for (int i = 0; i < count; ++i) {
                                float x, y, w, h;
                                int age, range, realAge;
                                String gender;

                                // get the center point
                                x = (float) rst.getJSONArray("face")
                                        .getJSONObject(i)
                                        .getJSONObject("position")
                                        .getJSONObject("center").getDouble("x");
                                y = (float) rst.getJSONArray("face")
                                        .getJSONObject(i)
                                        .getJSONObject("position")
                                        .getJSONObject("center").getDouble("y");

                                // get face size
                                w = (float) rst.getJSONArray("face")
                                        .getJSONObject(i)
                                        .getJSONObject("position")
                                        .getDouble("width");
                                h = (float) rst.getJSONArray("face")
                                        .getJSONObject(i)
                                        .getJSONObject("position")
                                        .getDouble("height");

                                // get person age
                                age = rst.getJSONArray("face").getJSONObject(i)
                                        .getJSONObject("attribute")
                                        .getJSONObject("age").getInt("value");
                                range = rst.getJSONArray("face")
                                        .getJSONObject(i)
                                        .getJSONObject("attribute")
                                        .getJSONObject("age").getInt("range");
                                gender = rst.getJSONArray("face")
                                        .getJSONObject(i)
                                        .getJSONObject("attribute")
                                        .getJSONObject("gender")
                                        .getString("value");

                                // change percent value to the real size
                                x = x / 100 * img.getWidth();
                                w = w / 100 * img.getWidth() * 0.7f;
                                y = y / 100 * img.getHeight();
                                h = h / 100 * img.getHeight() * 0.7f;

                                if (gender.equals("Male")) {
                                    gender = "男";
                                } else if (gender.equals("Female")) {
                                    gender = "女";
                                }

                                paint.setTextSize(25);
                                canvas.drawText(String.valueOf(age) + "岁", x - 5, y
                                        - h - 15, paint);
                                canvas.drawText(String.valueOf(gender) + " ", x - 35, y
                                        - h - 15, paint);

                                // draw the box to mark it out
                                canvas.drawLine(x - w, y - h, x - w, y + h,
                                        paint);
                                canvas.drawLine(x - w, y - h, x + w, y - h,
                                        paint);
                                canvas.drawLine(x + w, y + h, x - w, y + h,
                                        paint);
                                canvas.drawLine(x + w, y + h, x + w, y - h,
                                        paint);
                            }

                            // save new image
                            img = bitmap;

                            FindHowOldAct.this.runOnUiThread(new Runnable() {

                                public void run() {
                                    // show the image
                                    imageView.setImageBitmap(img);
                                    textView.setText("Finished, " + count + " faces.");
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FindHowOldAct.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    textView.setText("Error.");
                                }
                            });
                        }

                    }
                });
                faceppDetect.detect(img);
            }
        });

        imageView = (ImageView) this.findViewById(R.id.imageView1);
        imageView.setImageBitmap(img);
    }

    private void initViews() {
        titleBackImv = (ImageButton) findViewById(R.id.title_imv);
        titleCenterTv = (TextView) findViewById(R.id.title_center_text_tv);

        titleBackImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindHowOldAct.this.finish();
            }
        });

        titleCenterTv.setText("How old are you?");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // the image picker callback
        if (requestCode == PICTURE_CHOOSE) {
            if (intent != null) {
                // The Android api ~~~
                // Log.d(TAG, "idButSelPic Photopicker: " +
                // intent.getDataString());
                Cursor cursor = getContentResolver().query(intent.getData(),
                        null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                String fileSrc = cursor.getString(idx);

//                String fileSrc = intent.getDataString();
                Log.d(TAG, "Picture:" + fileSrc);

                // just read size
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                img = BitmapFactory.decodeFile(fileSrc, options);

                // scale size to read
                options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                        (double) options.outWidth / 1024f,
                        (double) options.outHeight / 1024f)));
                options.inJustDecodeBounds = false;
                img = BitmapFactory.decodeFile(fileSrc, options);
                textView.setText("Clik Detect. ==>");

                imageView.setImageBitmap(img);
                buttonDetect.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "idButSelPic Photopicker canceled");
            }
        }
    }

    private class FaceppDetect {
        DetectCallback callback = null;

        public void setDetectCallback(DetectCallback detectCallback) {
            callback = detectCallback;
        }

        public void detect(final Bitmap image) {

            new Thread(new Runnable() {

                public void run() {
                    HttpRequests httpRequests = new HttpRequests(
                            "8a6935b89012418fad0e9b3328bbeba9",
                            "WQN_MfSnt5Frtmy0XYeKJ8fh1xIVAmo_", true, false);
                    // Log.v(TAG, "image size : " + img.getWidth() + " " +
                    // img.getHeight());

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    float scale = Math.min(
                            1,
                            Math.min(600f / img.getWidth(),
                                    600f / img.getHeight()));
                    Matrix matrix = new Matrix();
                    matrix.postScale(scale, scale);

                    Bitmap imgSmall = Bitmap.createBitmap(img, 0, 0,
                            img.getWidth(), img.getHeight(), matrix, false);
                    // Log.v(TAG, "imgSmall size : " + imgSmall.getWidth() + " "
                    // + imgSmall.getHeight());

                    imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] array = stream.toByteArray();

                    try {
                        // detect
                        JSONObject result = httpRequests
                                .detectionDetect(new PostParameters()
                                        .setImg(array));
                        // finished , then call the callback function
                        if (callback != null) {
                            callback.detectResult(result);
                        }
                    } catch (FaceppParseException e) {
                        e.printStackTrace();
                        FindHowOldAct.this.runOnUiThread(new Runnable() {
                            public void run() {
                                SystemUtils.checkNetWork(FindHowOldAct.this);
                                textView.setText("error.");
                            }
                        });
                    }

                }
            }).start();
        }
    }

    interface DetectCallback {
        void detectResult(JSONObject rst);
    }
}