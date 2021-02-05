package com.example.workshopcv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2 {
    private static final String TAG = "OCV 3.4.13";

    private CameraBridgeViewBase mOpenCVCameraView;

    private boolean mIsJavaCamera=true;
    private MenuItem mItemSwitchCamera=null;

    Mat mRgba;
    Mat mRgbaF;
    Mat mRgbaT;

    boolean startCanny = false;
    boolean startBlur = false;

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

        Button btnCanny = findViewById(R.id.btn_canny);
        Button btnBlur = findViewById(R.id.btn_blur);

        btnCanny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startCanny == false){
                    startCanny = true;
                }else{
                    startCanny = false;
                }
            }
        });

        btnCanny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startCanny == false){
                    startCanny = true;
                }else{
                    startCanny = false;
                }
            }
        });

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
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        Core.transpose(mRgba, mRgbaT);
        Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0,0, 0);
        Core.flip(mRgbaF, mRgba, 1 );

        if(startCanny==true){
            Imgproc.cvtColor(mRgba,mRgba,Imgproc.COLOR_RGBA2GRAY);
            Imgproc.Canny(mRgba,mRgba,100,80);
        }

        if(startBlur==true){
            Size size = new Size(45,45);
            Point point = new Point(20,30);
            Imgproc.blur(mRgba,mRgba,size,point,Core.BORDER_DEFAULT);
        }

        return mRgba;
    }
}