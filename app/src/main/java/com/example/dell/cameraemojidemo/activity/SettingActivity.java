package com.example.dell.cameraemojidemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.dell.cameraemojidemo.R;
import com.example.dell.cameraemojidemo.utils.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    private Spinner spinner_pic_resolution;
    private Button btn_ok;

    private ArrayAdapter<String> pic_resolution_adapter;

    private int picResolutionIndex = 0;

    private List<String> sizeNameList;
    private List<Size> sizeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        try {
            initViews();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void initViews() throws CameraAccessException {
        spinner_pic_resolution = (Spinner) findViewById(R.id.spinner_pic_resolution);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        getCameraSizeList();

        pic_resolution_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizeNameList);
        pic_resolution_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_pic_resolution.setAdapter(pic_resolution_adapter);

        //设置默认选项
        Map<String, Integer> settingMap = Constant.getSettingSelection(SettingActivity.this);
        spinner_pic_resolution.setSelection(settingMap.get("picResolutionPos"),true);
        picResolutionIndex = settingMap.get("picResolutionPos");

        spinner_pic_resolution.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                picResolutionIndex = arg2;
                Constant.saveSettingSelectionToFile(SettingActivity.this,arg2);
                //Toast.makeText(SettingActivity.this,picResolutionIndex+"",Toast.LENGTH_SHORT).show();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this,CameraActivity.class);
                intent.putExtra("picResolutionIndex",picResolutionIndex);
                startActivity(intent);
                finish();
            }
        });
    }

    //获取相机支持的分辨率列表
    public void getCameraSizeList() throws CameraAccessException {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        // 获取指定摄像头的特性
        CameraCharacteristics characteristics
                = manager.getCameraCharacteristics(Constant.mCameraId);
        // 获取摄像头支持的配置属性
        StreamConfigurationMap map = characteristics.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        sizeList = Arrays.asList(map.getOutputSizes(ImageFormat.JPEG));
        Collections.sort(sizeList, new CompareSizesByArea());
        sizeNameList = new ArrayList<>();
        for (Size size : sizeList) {
            String name = size.getWidth() + "×" + size.getHeight();
            sizeNameList.add(name);
        }
    }


    // 为Size定义一个比较器Comparator
    private static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // 强转为long保证不会发生溢出
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }
}
