package com.example.ayrtonfernandes.opencvtest;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class BlobDetection {
    // Lower and Upper bounds for range checking in HSV color space
    private Scalar mLowerBoundBall = new Scalar(0);
    private Scalar mUpperBoundBall = new Scalar(0);

    private Scalar mLowerBoundBlueGoal = new Scalar(0);
    private Scalar mUpperBoundBlueGoal = new Scalar(0);

    private Scalar mLowerBoundYellowGoal = new Scalar(0);
    private Scalar mUpperBoundYellowGoal = new Scalar(0);

    private Scalar mLowerBoundObstacle = new Scalar(0);
    private Scalar mUpperBoundObstacle = new Scalar(0);

    private Scalar mLowercarpet = new Scalar(0);
    private Scalar mUppercarpet = new Scalar(0);

    private List<MatOfPoint> mContoursBall = new ArrayList<MatOfPoint>();
    private List<MatOfPoint> mContoursBlueGoal = new ArrayList<MatOfPoint>();
    private List<MatOfPoint> mContoursYellowGoal = new ArrayList<MatOfPoint>();
    private List<MatOfPoint> mContoursCarpet = new ArrayList<MatOfPoint>();



    // Cache

    /*
        Ball
        Yellow goal
        Blue goal
        green mat
        obstacles
        white boundary

     */




    //0,30,130,255,130,255

    public void setHsvColorBall(Scalar min, Scalar max) { //selection screen for this?

        mLowerBoundBall.val[0] = min.val[0];
        mUpperBoundBall.val[0] = max.val[0];

        mLowerBoundBall.val[1] = min.val[1];
        mUpperBoundBall.val[1] = max.val[1];

        mLowerBoundBall.val[2] = min.val[2];
        mUpperBoundBall.val[2] = max.val[2];

    }

    public void setHsvColorBlueGoal (Scalar min, Scalar max) { //selection screen for this?

        mLowerBoundBlueGoal.val[0] = min.val[0];
        mUpperBoundBlueGoal.val[0] = max.val[0];

        mLowerBoundBlueGoal.val[1] = min.val[1];
        mUpperBoundBlueGoal.val[1] = max.val[1];

        mLowerBoundBlueGoal.val[2] = min.val[2];
        mUpperBoundBlueGoal.val[2] = max.val[2];

    }

    public void setHsvColorYellowGoal (Scalar min, Scalar max) { //selection screen for this?

        mLowerBoundYellowGoal.val[0] = min.val[0];
        mUpperBoundYellowGoal.val[0] = max.val[0];

        mLowerBoundYellowGoal.val[1] = min.val[1];
        mUpperBoundYellowGoal.val[1] = max.val[1];

        mLowerBoundYellowGoal.val[2] = min.val[2];
        mUpperBoundYellowGoal.val[2] = max.val[2];

    }

    public void setHsvColorobstacle (Scalar min, Scalar max) { //selection screen for this?

        mLowerBoundObstacle.val[0] = min.val[0];
        mUpperBoundObstacle.val[0] = max.val[0];

        mLowerBoundObstacle.val[1] = min.val[1];
        mUpperBoundObstacle.val[1] = max.val[1];

        mLowerBoundObstacle.val[2] = min.val[2];
        mUpperBoundObstacle.val[2] = max.val[2];

    }

    public void setHsvColorGreenCarpet (Scalar min, Scalar max) { //selection screen for this?

        mLowercarpet.val[0] = min.val[0];
        mUppercarpet.val[0] = max.val[0];

        mLowercarpet.val[1] = min.val[1];
        mUppercarpet.val[1] = max.val[1];

        mLowercarpet.val[2] = min.val[2];
        mUppercarpet.val[2] = max.val[2];

    }

    public void processBallCountours(Mat rgbaImage){

        Mat mHierarchy = new Mat();
        Mat mHsvMat = new Mat();
        Mat mMask= new Mat();
        Mat mDilatedMask = new Mat();

        Imgproc.cvtColor(rgbaImage, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        Core.inRange(mHsvMat, mLowerBoundBall, mUpperBoundBall, mMask);
        Imgproc.dilate(mMask, mDilatedMask, new Mat());

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Iterator<MatOfPoint> eachContour = contours.iterator();

        // Filter contours by area and resize to fit the original image size
        mContoursBall.clear();
        eachContour = contours.iterator();
        while (eachContour.hasNext()) {
            MatOfPoint contourBall = eachContour.next();
            if (Imgproc.contourArea(contourBall) > 350) {
                mContoursBall.add(contourBall);
            }
        }
    }

    public void processCarpetCountours(Mat rgbaImage){

        Mat mHierarchy = new Mat();
        Mat mHsvMat = new Mat();
        Mat mMask= new Mat();
        Mat mDilatedMask = new Mat();

        Imgproc.cvtColor(rgbaImage, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        Core.inRange(mHsvMat, mLowercarpet, mUppercarpet, mMask);
        Imgproc.dilate(mMask, mDilatedMask, new Mat());

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Iterator<MatOfPoint> eachContour = contours.iterator();

        // Filter contours by area and resize to fit the original image size
        mContoursCarpet.clear();
        eachContour = contours.iterator();
        while (eachContour.hasNext()) {
            MatOfPoint countourCountour = eachContour.next();
            if (Imgproc.contourArea(countourCountour) > 350) {
                mContoursCarpet.add(countourCountour);
            }
        }
    }

    public void processYellowGoalCountours(Mat rgbaImage){

        Mat mHierarchy = new Mat();
        Mat mHsvMat = new Mat();
        Mat mMask= new Mat();
        Mat mDilatedMask = new Mat();

        Imgproc.cvtColor(rgbaImage, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        Core.inRange(mHsvMat, mLowerBoundYellowGoal, mUpperBoundYellowGoal, mMask);
        Imgproc.dilate(mMask, mDilatedMask, new Mat());

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Iterator<MatOfPoint> eachContour = contours.iterator();

        // Filter contours by area and resize to fit the original image size
        mContoursYellowGoal.clear();
        eachContour = contours.iterator();
        while (eachContour.hasNext()) {
            MatOfPoint countourYellowGoal = eachContour.next();
            if (Imgproc.contourArea(countourYellowGoal) > 350) {
                mContoursYellowGoal.add(countourYellowGoal);
            }
        }
    }

    public void processBlueGoalCountours(Mat rgbaImage){

        Mat mHierarchy = new Mat();
        Mat mHsvMat = new Mat();
        Mat mMask= new Mat();
        Mat mDilatedMask = new Mat();

        Imgproc.cvtColor(rgbaImage, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        Core.inRange(mHsvMat, mLowerBoundBlueGoal, mUpperBoundBlueGoal, mMask);
        Imgproc.dilate(mMask, mDilatedMask, new Mat());

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Iterator<MatOfPoint> eachContour = contours.iterator();

        // Filter contours by area and resize to fit the original image size
        mContoursBlueGoal.clear();
        eachContour = contours.iterator();
        while (eachContour.hasNext()) {
            MatOfPoint contourBlueGoal = eachContour.next();
            if (Imgproc.contourArea(contourBlueGoal) > 350) {
                mContoursBlueGoal.add(contourBlueGoal);
            }
        }
    }


    public List<MatOfPoint> getBallCountours() {

        return mContoursBall;
    }

    public List<MatOfPoint> getYellowGoalCountours() {

        return mContoursYellowGoal;
    }

    public List<MatOfPoint> getCarpetCountours() {

        return mContoursCarpet;
    }

    public List<MatOfPoint> getBlueGoalCountours() {

        return mContoursBlueGoal;
    }


}