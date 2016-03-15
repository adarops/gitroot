package com.ilincar.drivingrecorder2;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.ilincar.utils.Data;

/**
 * Created by zoufeng on 2015/12/7 0007.
 */
public class DrivingRecorderSettings extends Activity{
    private RadioGroup mVideoClarityGroup;
    private RadioGroup mLengthVideoGroup; //group_length_video
    private RadioGroup mPhotoClarityGroup;
    private RadioGroup mAutoTakePictureGroup;

    private SparseIntArray mVideoClarityArray;
    private SparseIntArray mVideoDurationArray;
    private SparseIntArray mPhotoClarityArray;
    private SparseIntArray mAutoTakePhotoArray;

    private ImageButton mBtnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving_settings);
        Data.getInstance().load(getApplicationContext());
        initIdArray();
        setupViews();
    }

    private void initIdArray(){
        mVideoClarityArray = new SparseIntArray();
        mVideoClarityArray.put(0, R.id.video_clarity_720);
        mVideoClarityArray.put(1, R.id.video_clarity_1080);

        mVideoDurationArray = new SparseIntArray();
        mVideoDurationArray.put(0, R.id.video_each_one);
        mVideoDurationArray.put(1, R.id.video_each_two);
        mVideoDurationArray.put(2, R.id.video_each_three);

        mPhotoClarityArray = new SparseIntArray();
        mPhotoClarityArray.put(0, R.id.photo_clarity_low);
        mPhotoClarityArray.put(1, R.id.photo_clarity_mid);
        mPhotoClarityArray.put(2, R.id.photo_clarity_high);

        mAutoTakePhotoArray = new SparseIntArray();
        mAutoTakePhotoArray.put(0, R.id.auto_photo_close);
        mAutoTakePhotoArray.put(1, R.id.auto_photo_mid);
        mAutoTakePhotoArray.put(2, R.id.auto_photo_high);
    }

    private void setupViews(){
        mBtnBack = (ImageButton)findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(onClickListener);

        mVideoClarityGroup = (RadioGroup) findViewById(R.id.group_video_clarity);
        mLengthVideoGroup = (RadioGroup) findViewById(R.id.group_length_video);
        mPhotoClarityGroup = (RadioGroup) findViewById(R.id.group_photo_clarity);
        mAutoTakePictureGroup = (RadioGroup) findViewById(R.id.group_auto_take_picture);

        mVideoClarityGroup.check(mVideoClarityArray.get(Data.getInstance().getCurrentVideoSize()));
        mLengthVideoGroup.check(mVideoDurationArray.get(Data.getInstance().getCurrentVideoDuration()));
        mPhotoClarityGroup.check(mPhotoClarityArray.get(Data.getInstance().getCurrentPhotoSize()));
        mAutoTakePictureGroup.check(mAutoTakePhotoArray.get(Data.getInstance().getCurrentAutoLock()));

        mVideoClarityGroup.setOnCheckedChangeListener(listener);
        mLengthVideoGroup.setOnCheckedChangeListener(listener);
        mPhotoClarityGroup.setOnCheckedChangeListener(listener);
        mAutoTakePictureGroup.setOnCheckedChangeListener(listener);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Data.getInstance().save();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch(v.getId()){
                case R.id.btn_back:
                    finish();
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            switch(group.getId()){
                case R.id.group_video_clarity:
                    switch (checkedId) {
                        case R.id.video_clarity_720:
                            Data.getInstance().setCurrentVideoSize(0);
                            break;
                        case R.id.video_clarity_1080:
                            Data.getInstance().setCurrentVideoSize(1);
                            break;
                    }
                    break;
                case R.id.group_length_video:
                    switch(checkedId){
                        case R.id.video_each_one:
                            Data.getInstance().setCurrentVideoDuration(0);
                            break;
                        case R.id.video_each_two:
                            Data.getInstance().setCurrentVideoDuration(1);
                            break;
                        case R.id.video_each_three:
                            Data.getInstance().setCurrentVideoDuration(2);
                            break;
                    }
                    break;
                case R.id.group_photo_clarity:
                    switch(checkedId){
                        case R.id.photo_clarity_low:
                            Data.getInstance().setCreentPhotoSize(0);
                            break;
                        case R.id.photo_clarity_mid:
                            Data.getInstance().setCreentPhotoSize(1);
                            break;
                        case R.id.photo_clarity_high:
                            Data.getInstance().setCreentPhotoSize(2);
                            break;
                    }
                    break;
                case R.id.group_auto_take_picture:
                    switch(checkedId){
                        case R.id.auto_photo_close:
                            Data.getInstance().setCurrentAutoLock(0);
                            break;
                        case R.id.auto_photo_mid:
                            Data.getInstance().setCurrentAutoLock(1);
                            break;
                        case R.id.auto_photo_high:
                            Data.getInstance().setCurrentAutoLock(2);
                            break;
                    }
                    break;
            }
        }
    };
}
