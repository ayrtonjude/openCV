package com.example.ayrtonfernandes.opencvtest;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
// OpenCV Libraries

// OpenCV Classes


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends Activity implements CvCameraViewListener2 {

    private static final String  TAG              = "OCVSample::Activity";

    private Mat                  mRgba;
    private Mat                  m_green_carpet;
    Mat mRgbaF;
    Mat mRgbaT;

    //boolean                     detectionEnabled = false;
    boolean                     calibrationEnabled = false;

    boolean  yellowGoal=false;
    boolean  blueGoal = false;
    boolean ball_detected=false;

    double ball_height=42.67; // in mm
    double Pixel_offset=0;
    double real_offset=0;
    double ball_angle=0;
    double distance_ball=0;
    private Scalar ballColourMin = new Scalar(0);
    private Scalar ballColourMax = new Scalar(0);

    private Scalar yellowGoalColourMin = new Scalar(0);
    private Scalar yellowGoalColourMax = new Scalar(0);

    private Scalar blueGoalColourMin = new Scalar(0);
    private Scalar blueGoalColourMax = new Scalar(0);

    private Scalar whiteBorderMin = new Scalar(0);
    private Scalar whiteBorderMax = new Scalar(0);

    private Scalar carpet_min=new Scalar(0);
    private Scalar carpet_max=new Scalar(0);

    private Scalar obstacle_min=new Scalar(0);
    private Scalar obstacle_max=new Scalar(0);


    double xCentroidBall = 0;
    double yCentroidBall = 0;
    double distBall = 0;

    double xCentroidBlueGoal = 0;
    double yCentroidBlueGoal = 0;
    double dist_blue_goal=0;

    double xCentroidYellowGoal = 0;
    double yCentroidYellowGoal = 0;
    double dist_yellow_goal = 0;


    private TextView x_yellow;
    private TextView y_yellow;
    private TextView yellow_dist;

    private TextView x_blue;
    private TextView y_blue;
    private TextView blue_dist;

    private TextView x_ball;
    private TextView y_ball;
    private TextView dist_ball;
    private TextView BallAngle;

    private BlobDetection   mDetection;


    private Scalar    		     CONTOUR_COLOR_ORANGE;
    private Scalar    		     CONTOUR_COLOR_RED;
    private Scalar    		     CONTOUR_COLOR_BLUE;
    private Scalar    		     CONTOUR_COLOR_GREEN;
    private Scalar    		     CONTOUR_COLOR_YELLOW;



    private CameraBridgeViewBase mOpenCvCameraView;

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };



    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {



//        // Ball
        ballColourMin.val[0] = 0;
        ballColourMax.val[0] = 40;

        ballColourMin.val[1] = 130;
        ballColourMax.val[1] = 255;

        ballColourMin.val[2] = 130;
        ballColourMax.val[2] = 255;
//
//        ballColourMin.val[0] = 20;
//        ballColourMax.val[0] = 228;
//
//        ballColourMin.val[1] = 97;
//        ballColourMax.val[1] = 255;
//
//        ballColourMin.val[2] = 164;
//        ballColourMax.val[2] = 255;

//

//      Blue Goal
        blueGoalColourMin.val[0] = 113;
        blueGoalColourMax.val[0] = 192;

        blueGoalColourMin.val[1] = 22;
        blueGoalColourMax.val[1] = 255;

        blueGoalColourMin.val[2] = 94;
        blueGoalColourMax.val[2] =216;

        //yellow
        yellowGoalColourMin.val[0] = 25;
        yellowGoalColourMax.val[0] = 51;

        yellowGoalColourMin.val[1] = 66;
        yellowGoalColourMax.val[1] = 255;

        yellowGoalColourMin.val[2] = 0;
        yellowGoalColourMax.val[2] = 255;



        CONTOUR_COLOR_ORANGE = new Scalar(255,165,0);
        CONTOUR_COLOR_RED = new Scalar(255,0,0);
        CONTOUR_COLOR_BLUE = new Scalar(0,0,255);
        CONTOUR_COLOR_GREEN = new Scalar(0,255,0);
        CONTOUR_COLOR_YELLOW = new Scalar(255,255,0);


        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.camera);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.show_camera);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        final Button startDetection = (Button) findViewById(R.id.startDetection);
        final Button reset = (Button) findViewById(R.id.reset);

        final Button yellow_goal = (Button) findViewById(R.id.yellow_goal);
        final Button blue_goal = (Button) findViewById(R.id.blue_goal);


//        x_yellow=(TextView) findViewById(R.id.x_yellow);
//        y_yellow=(TextView) findViewById(R.id.y_yellow);
//        yellow_dist=(TextView) findViewById(R.id.yellow_dist);
//
//        x_blue=(TextView) findViewById(R.id.x_blue);
//        y_blue=(TextView) findViewById(R.id.y_blue);
//        blue_dist=(TextView) findViewById(R.id.blue_dist);

        x_ball=(TextView) findViewById(R.id.x_ball);
        y_ball=(TextView) findViewById(R.id.y_ball);
        dist_ball=(TextView) findViewById(R.id.dist_ball);
        BallAngle=(TextView) findViewById(R.id.BallAngle);

        yellow_goal.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                yellowGoal = true;
                blueGoal=false;
                blue_goal.setVisibility(View.INVISIBLE);
                reset.setVisibility(View.VISIBLE);

            }
        });

        blue_goal.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                yellowGoal = false;
                blueGoal=true;
                yellow_goal.setVisibility(View.INVISIBLE);
                blue_goal.setVisibility(View.VISIBLE);
                reset.setVisibility(View.VISIBLE);

            }
        });

        reset.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                yellowGoal = false;
                blueGoal=false;

                yellow_goal.setVisibility(View.VISIBLE);
                blue_goal.setVisibility(View.VISIBLE);
                reset.setVisibility(View.INVISIBLE);

            }
        });


    }


    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null){
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null){
            mOpenCvCameraView.disableView();
        }
    }


    public void onCameraViewStarted(int width, int height) {

        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
        mRgba = new Mat(height, width, CvType.CV_8UC4);

        // create new blob instance
        mDetection= new BlobDetection();

        mDetection.setHsvColorBall(ballColourMin, ballColourMax);
        mDetection.setHsvColorBlueGoal(blueGoalColourMin, blueGoalColourMax);
        mDetection.setHsvColorYellowGoal(yellowGoalColourMin, yellowGoalColourMax);
        //mDetection.setHsvColorYellowGoal(yellowGoalColourMin, yellowGoalColourMax);

    }

    public void onCameraViewStopped() {
        mRgba.release();
    }


    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        Core.transpose(mRgba, mRgbaT);
        Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0, 0, 0);
        Core.flip(mRgbaF, mRgba, 1);


        // Blue is for orange ball
        // Green is for yellow ball
        //  YELLOW for blue goal
        double x_frame_size=480;
        mDetection.processBallCountours(mRgba);
        List<MatOfPoint> contoursBall = mDetection.getBallCountours();
        Imgproc.drawContours(mRgba, contoursBall, -1, CONTOUR_COLOR_BLUE);
        MatOfPoint2f approxBallCurve = new MatOfPoint2f();
        for (int i = 0; i < contoursBall.size(); i++) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contoursBall.get(i).toArray());
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxBallCurve, approxDistance, true);

            MatOfPoint points = new MatOfPoint(approxBallCurve.toArray());
            Rect rect = Imgproc.boundingRect(points);

            // this is the ball coordinates, you will need to tweak stuff to get ensure no false positives
            xCentroidBall = (rect.x + (rect.width / 2));
            yCentroidBall = (rect.y + (rect.height / 2));

            distance_ball=roundTwoDecimals((10*ball_height)/((rect.y+rect.height)-(rect.y)));
            Pixel_offset=roundTwoDecimals(x_frame_size/2-Math.abs(xCentroidBall));
            real_offset=roundTwoDecimals(ball_height*(Pixel_offset/((rect.y+rect.height)-(rect.y))));
            ball_angle=roundTwoDecimals((Math.asin(real_offset/distance_ball)));

//                Pixel_offset=x_frame_size/ 2 - Math.abs(mDetection.setHsvColorBall(ballColourMin, ballColourMax).uc);
//                real_offset=ball_height*(Pixeloffset/colour_brand_size);
//                ball_angle = Math.asin(Real_offset/distance);

            // Blobs_in_region
            // Pixel offset
            // colour_brand_size
            // Real_offset
            // distance
                /*
                uc is the horizontal cordinag
                colour band size is the height of the object in the pxiel frame

                 */

        }
        if (yellowGoal) {


            mDetection.processYellowGoalCountours(mRgba);

            List<MatOfPoint> contoursYellowGoal = mDetection.getYellowGoalCountours();

            Imgproc.drawContours(mRgba, contoursYellowGoal, -1, CONTOUR_COLOR_GREEN);
            Log.d(TAG, "Yellow goal detected");
            MatOfPoint2f approxYellowGoalCurve = new MatOfPoint2f();

            for (int i=0; i<contoursYellowGoal.size(); i++) {
                MatOfPoint2f contour2f = new MatOfPoint2f(contoursYellowGoal.get(i).toArray());
                double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
                Imgproc.approxPolyDP(contour2f, approxYellowGoalCurve, approxDistance, true);

                MatOfPoint points = new MatOfPoint(approxYellowGoalCurve.toArray());
                Rect rect = Imgproc.boundingRect(points);
                // this is the ball coordinates, you will need to tweak stuff to get ensure no false positives
                xCentroidYellowGoal = rect.x  + (rect.width/2);
                yCentroidYellowGoal = rect.y  + (rect.height/2);
                dist_yellow_goal=(Math.pow(xCentroidYellowGoal,2)) + (Math.pow(yCentroidYellowGoal,2));
//                dist_yellow_goal=Math.sqrt(dist_yellow_goal);
//                dist_yellow_goal=roundTwoDecimals(dist_yellow_goal);
                dist_yellow_goal=roundTwoDecimals((10*ball_height)/((rect.y+rect.height)-(rect.y)));
                Pixel_offset=roundTwoDecimals(x_frame_size/2-Math.abs(xCentroidYellowGoal));
                real_offset=roundTwoDecimals(ball_height*(Pixel_offset/((rect.y+rect.height)-(rect.y))));
                ball_angle=roundTwoDecimals((Math.asin(real_offset/distance_ball)));
            }



            runOnUiThread(new Runnable() {
                @Override
                public void run() {

//
//                    x_yellow.setText(String.valueOf((xCentroidYellowGoal)));
//                    y_yellow.setText(String.valueOf((yCentroidYellowGoal)));
//                    yellow_dist.setText(String.valueOf((dist_yellow_goal)));

                    x_ball.setText(String.valueOf((xCentroidBall)));
                    y_ball.setText(String.valueOf((yCentroidBall)));
                    dist_ball.setText(String.valueOf((distance_ball)));
                    BallAngle.setText(String.valueOf((ball_angle)));
                }
            });

            return mRgba;

        }

        else if(blueGoal){
            mDetection.processBallCountours(mRgba);
            mDetection.processBlueGoalCountours(mRgba);

            List<MatOfPoint> contoursBlueGoal = mDetection.getBlueGoalCountours();


            Imgproc.drawContours(mRgba, contoursBlueGoal, -1, CONTOUR_COLOR_YELLOW);

            Log.d(TAG, "Blue goal detected");

            MatOfPoint2f approxBlueGoalCurve = new MatOfPoint2f();


            for (int i=0; i<contoursBlueGoal.size(); i++) {
                MatOfPoint2f contour2f = new MatOfPoint2f(contoursBlueGoal.get(i).toArray());
                double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
                Imgproc.approxPolyDP(contour2f, approxBlueGoalCurve, approxDistance, true);

                MatOfPoint points = new MatOfPoint(approxBlueGoalCurve.toArray());
                Rect rect = Imgproc.boundingRect(points);
                // this is the ball coordinates, you will need to tweak stuff to get ensure no false positives
                xCentroidBlueGoal = rect.x  + (rect.width/2);
                yCentroidBlueGoal = rect.y  + (rect.height/2);
                dist_blue_goal=(Math.pow(xCentroidBlueGoal,2)) + (Math.pow(yCentroidBlueGoal,2));
                dist_blue_goal=Math.sqrt(dist_blue_goal);
                dist_blue_goal=roundTwoDecimals(dist_blue_goal);

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    x_blue.setText(String.valueOf((xCentroidBlueGoal)));
//                    y_blue.setText(String.valueOf((yCentroidBlueGoal)));
//                    blue_dist.setText(String.valueOf((dist_blue_goal)));

                    x_ball.setText(String.valueOf((xCentroidBall)));
                    y_ball.setText(String.valueOf((yCentroidBall)));
                    dist_ball.setText(String.valueOf((distBall)));

                }
            });

            return mRgba;
        }
        else {

            return mRgba;
        }
    }
    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("###.##");
        return Double.valueOf(twoDForm.format(d));
    }

}