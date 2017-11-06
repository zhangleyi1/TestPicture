package com.test.picture;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import static com.test.picture.R.layout.select_pop_window;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView mPictureIv;
    private Button mMagnify;
    private Button mShrink;
    private static final int REQUEST_FOR_RESULT_GET_PICTURE = 100;
    private static final int REQUEST_FOR_RESULT_SELECT_PICTURE = 101;
    private static final int REQUEST_FOR_RESULT_EDIT_PICTURE = 102;
    private String TAG = "MainActivity";
    private Uri mPhotoUri = null;
    private String mPhotoPath = null;
    private Button mFireBtn;
    private TextView cameraTv;
    private TextView selectPhotoTv;
    private TextView cancelTv;
    private PopupWindow popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
       /* String [] args = {"13262983503"};
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.haier.block.coolpad.provider/filter_call"),
                null, null, args, null);
        if (null != cursor) {
            Log.d(TAG, "zly --> cursor != null getCount: " + cursor.getCount());
//            if (cursor.getCount() > 0) {
//                //return true;
//            } else {
//           //     return false;
//            }
            if (cursor.moveToFirst()) {
                do {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    mPhotoPath = cursor.getString(columnIndex);
                } while(cursor.moveToNext());
                cursor.close();
            }
        }
//        cursor.close();
        Log.d(TAG, "zly --> cursor == null.");
        Integer a = 12;
        String ab = String.valueOf(a);*/
    }

    private void initView() {
        mFireBtn = (Button)findViewById(R.id.ib_fire);
        mFireBtn.setOnClickListener(this);

        mPictureIv = (ImageView)findViewById(R.id.iv_picture);

        mMagnify = (Button)findViewById(R.id.btn_magnify);
        mMagnify.setOnClickListener(MainActivity.this);

        mShrink = (Button)findViewById(R.id.btn_shrink);
        mShrink.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

       /* Cursor cursor = getContentResolver().query(Uri.parse("content://com.haier.block.coolpad.provider/filter_sms"), null, null, args, null);

        String [] args = {phoneNumber};
        Log.d(TAG, "zly --> uri: " + Uri.parse("content://com.haier.block.coolpad.provider/filter_sms"));
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://com.haier.block.coolpad.provider/filter_call"),
                null, null, args, null);
        if ((null != cursor) && cursor.moveToFirst()) {
            do {
                Log.d(TAG, "zly --> cursor != null getCount: " + cursor.getCount() + " getColumnCount:" + cursor.getColumnCount() + " result:" + cursor.getInt(cursor.getColumnIndexOrThrow("blockResult")));
                Log.d(TAG, "zly --> result:" + cursor.getColumnIndexOrThrow("blockResult"));
                Log.d(TAG, "zly --> c0:" + cursor.getColumnName(0) + " c1:" + cursor.getColumnName(1) + " c2:" + cursor.getColumnName(2));
                if ((cursor.getCount() > 0) && (1 == cursor.getInt(cursor.getColumnIndexOrThrow("blockResult")))) {
                    return true;
                } else {
                    return false;
                }
            } while(cursor.moveToNext());
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.ib_fire:
                selectOpenWays();
                //takePicture();
                break;
            case R.id.btn_magnify:
                break;
            case R.id.btn_shrink:
                break;
            case R.id.tv_camera:
                takePicture();
                popup.dismiss();
                break;
            case R.id.tv_select_photo:
                selectPhoto();
                popup.dismiss();
                break;
            case R.id.tv_cancel:
                popup.dismiss();
                break;
        }
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_FOR_RESULT_SELECT_PICTURE);
    }

    private void selectOpenWays() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）

        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.select_pop_window, null);
        view.setOnClickListener(this);
        popup = new PopupWindow(view, width, height*40/100);
        //popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
        //popup.showAsDropDown(findViewById(R.id.btn), 0,50);
        cameraTv = (TextView)view.findViewById(R.id.tv_camera);
        cameraTv.setOnClickListener(this);
        selectPhotoTv = (TextView)view.findViewById(R.id.tv_select_photo);
        selectPhotoTv.setOnClickListener(this);
        cancelTv = (TextView)view.findViewById(R.id.tv_cancel);
        cancelTv.setOnClickListener(this);
        popup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        mPhotoUri = this.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Log.d(TAG, "zly --> takePicture mPhotoUri:" + mPhotoUri.toString());
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mPhotoUri);
        startActivityForResult(intent, REQUEST_FOR_RESULT_GET_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_FOR_RESULT_GET_PICTURE:
                getPicture();
                break;
            case REQUEST_FOR_RESULT_SELECT_PICTURE:
//                mPictureIv.setImageBitmap(data.getData());
//                mPictureIv.setImageURI(data.getData());
                startPhotoZoom(data.getData());
                //mPictureIv.setVisibility(View.VISIBLE);
                break;
            case REQUEST_FOR_RESULT_EDIT_PICTURE:
                getZoomPhoto(data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getZoomPhoto(Intent intent) {
        if (null == intent) {
            return;
        }
        Bitmap bitmap = intent.getExtras().getParcelable("data");
        mPictureIv.setImageBitmap(bitmap);
        mPictureIv.setVisibility(View.VISIBLE);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 璁剧疆瑁佸壀
        intent.putExtra("crop", "true");
        // aspectX aspectY 鏄楂樼殑姣斾緥
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 鏄鍓浘鐗囧楂?
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_FOR_RESULT_EDIT_PICTURE);
    }

    private void getPicture() {
        String photoFile;
        Cursor cursor = getContentResolver().query(/*MediaStore.Images.Media.EXTERNAL_CONTENT_URI*/mPhotoUri, null, null, null, null);
        if (null != cursor) {
//            String[] proj = {MediaStore.Images.Media.DATA};
            if (cursor.moveToFirst()) {
               // do {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    mPhotoPath = cursor.getString(columnIndex);
                    Log.d(TAG, "zly --> mPhotoPath:" + mPhotoPath + " mPotoUri:" + mPhotoUri.toString());
               // } while(cursor.moveToNext());
                cursor.close();
            }
        }

        Bitmap bt = BitmapFactory.decodeFile(mPhotoPath);
        mPictureIv.setImageBitmap(bt);
        mPictureIv.setVisibility(View.VISIBLE);
    }
}

