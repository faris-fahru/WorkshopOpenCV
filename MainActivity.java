package com.example.workshopcv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2 {
    private static final String TAG = "OCV 3.4.13";

    private CameraBridgeViewBase mOpenCVCameraView;

    private boolean mIsJavaCamera=true;
    private MenuItem mItemSwitchCamera=null;

    Mat mRgba;
    Mat mRgbaF;
    Mat mRgbaT;

    private BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                {
                    Log.i(TAG,"OpenCV Loaded Success");
                    mOpenCVCameraView.enableView();
                }break;
                default:
                {
                    super.onManagerConnected(status);
                }break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOpenCVCameraView =(CameraBridgeViewBase) findViewById(R.id.java_surface_view);

        mOpenCVCameraView.enableFpsMeter();
        mOpenCVCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCVCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mOpenCVCameraView !=null){
            mOpenCVCameraView.disableView();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG,"OpenCV internal tidak ditemukan. Gunakan OpenCV Manager untuk menginisialisasi");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,this,mLoaderCallBack);
        }else{
            Log.d(TAG,"OpenCV ada dalam package. Siap digunakan");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mOpenCVCameraView != null){
            mOpenCVCameraView.disableView();
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        return mRgba;
    }
}