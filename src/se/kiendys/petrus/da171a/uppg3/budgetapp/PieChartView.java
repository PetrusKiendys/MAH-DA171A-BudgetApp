package se.kiendys.petrus.da171a.uppg3.budgetapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class PieChartView extends View {
	
	private Paint piechartItemPaint1;
	private Paint piechartItemPaint2;
	private Paint piechartBorderPaint;
	private Paint infoBorderPaint1;
	private Paint infoBackgroundPaint1;
	private Paint infoTextPaint1;
	private Paint infoBorderPaint2;
	private Paint infoBackgroundPaint2;
	private Paint infoTextPaint2;
	private RectF oval;
	
	private int mWidth;
	private int mHeight;
	private int mMarginLeft;
	private int mMarginTop;
	private int mMarginInfoTop;
	private int mMarginInfoY;
	private float mStartAngle;
	private float mSweepAngle;
	
	private double percentage;

	public PieChartView(Context context) {
		super(context);
		init();
	}
	
	public PieChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init() {
		piechartItemPaint1 = new Paint();					// the piechart items color
		piechartItemPaint2 = new Paint();					// the piechart items color
		piechartBorderPaint = new Paint();					// the piechart border color
		infoTextPaint1 = new Paint();						// the info text color
		infoBackgroundPaint1 = new Paint();					// the info background color
		infoBorderPaint1 = new Paint();						// the info border color
		infoTextPaint2 = new Paint();
		infoBackgroundPaint2 = new Paint();
		infoBorderPaint2 = new Paint();
		
		mWidth = 300;								// circle width
		mHeight = 300;								// circle height
		mMarginLeft = 10;							// circle and info margin
		mMarginTop = 10;							// circle and info margin
		mMarginInfoTop = mHeight+(mMarginTop*3);	// info margin
		mMarginInfoY = 35;							// distance between info boxes
		mStartAngle = 0;							// the starting angle of the arc, begins at the rightmost part of the circle (an angle of 0 degrees correspond to the geometric angle of 0 degrees, or 3 o'clock on a watch)
													// and through incrementation moves clockwise, the units are degrees
		mSweepAngle = 0;							// sweep angle initialized to 0
		
		oval = new RectF(mMarginLeft+85, mMarginTop, mWidth+85, mHeight);	// sets the shape and size (boundaries) of the drawn circle
		
		infoBackgroundPaint1.setAntiAlias(true);
		infoBackgroundPaint1.setStyle(Style.FILL);
		infoBackgroundPaint1.setColor(0xCC24C932);
		
		infoBorderPaint1.setAntiAlias(true);
		infoBorderPaint1.setStyle(Style.STROKE);
		infoBorderPaint1.setColor(0xCC2BAD47); 
		infoBorderPaint1.setStrokeWidth(1.5f);
		
		infoTextPaint1.setTextSize(20);
		infoTextPaint1.setStyle(Style.FILL_AND_STROKE);
		infoTextPaint1.setColor(Color.BLACK);
		infoTextPaint1.setStrokeWidth(1.0f);
		
		infoBackgroundPaint2.setAntiAlias(true);
		infoBackgroundPaint2.setStyle(Style.FILL);
		infoBackgroundPaint2.setColor(0xCC3425D9);
		
		infoBorderPaint2.setAntiAlias(true);
		infoBorderPaint2.setStyle(Style.STROKE);
		infoBorderPaint2.setColor(0xCC2C219E); 
		infoBorderPaint2.setStrokeWidth(1.5f);
		
		infoTextPaint2.setTextSize(20);
		infoTextPaint2.setStyle(Style.FILL_AND_STROKE);
		infoTextPaint2.setColor(Color.BLACK);
		infoTextPaint2.setStrokeWidth(1.0f);
		
		
		piechartItemPaint1.setAntiAlias(true);
		piechartItemPaint1.setStyle(Style.FILL);
		piechartItemPaint1.setStrokeWidth(0.5f);
		piechartItemPaint1.setColor(0xCC3425D9);	// blue color
		
		piechartItemPaint2.setAntiAlias(true);
		piechartItemPaint2.setStyle(Style.FILL);
		piechartItemPaint2.setStrokeWidth(0.5f);
		piechartItemPaint2.setColor(0xCC24C932);	// green color
		
		piechartBorderPaint.setAntiAlias(true);
		piechartBorderPaint.setStyle(Style.STROKE);
		piechartBorderPaint.setColor(Color.BLACK);
		piechartBorderPaint.setStrokeWidth(2f);
	}
	
	public void setDistributionPercentage(double percentage) {
		this.percentage = percentage;
		Log.d(BudgetConstants.DEBUG_TAG, "percentage: "+percentage);
		
		initDynamics();
	}
	
	private void initDynamics() {
		mSweepAngle = (float)(360*percentage);
		Log.d(BudgetConstants.DEBUG_TAG, "init percentage: "+percentage+", mSweepAngle: "+mSweepAngle);
		
		invalidate();
	}

	protected void onDraw(Canvas canvas) {
		Log.d(BudgetConstants.DEBUG_TAG, "onDraw fired!");
		
		canvas.drawArc(oval, mStartAngle, mSweepAngle, true, piechartItemPaint1);
		canvas.drawArc(oval, mStartAngle, mSweepAngle, true, piechartBorderPaint);
		canvas.drawArc(oval, mSweepAngle, (360-mSweepAngle), true, piechartItemPaint2);
		canvas.drawArc(oval, mSweepAngle, (360-mSweepAngle), true, piechartBorderPaint);
		
		canvas.drawRect(mMarginLeft, (mMarginInfoTop-15), mWidth, (mMarginInfoTop+15), infoBackgroundPaint1);
		canvas.drawRect(mMarginLeft, (mMarginInfoTop-15), mWidth, (mMarginInfoTop+15), infoBorderPaint1);
		canvas.drawText("Utgifter", mMarginLeft*2, (mMarginInfoTop+6), infoTextPaint1);
		
		canvas.drawRect(mMarginLeft, ((mMarginInfoTop-15)+mMarginInfoY), mWidth, ((mMarginInfoTop+15)+mMarginInfoY), infoBackgroundPaint2);
		canvas.drawRect(mMarginLeft, ((mMarginInfoTop-15)+mMarginInfoY), mWidth, ((mMarginInfoTop+15)+mMarginInfoY), infoBorderPaint2);
		canvas.drawText("Inkomster", mMarginLeft*2, (mMarginInfoY+mMarginInfoTop+6), infoTextPaint2);
	}

}
