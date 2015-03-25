package se.kiendys.petrus.da171a.uppg3.budgetapp.activities;

import java.util.Calendar;

import se.kiendys.petrus.da171a.uppg3.budgetapp.BudgetConstants;
import se.kiendys.petrus.da171a.uppg3.budgetapp.DBAdapter;
import se.kiendys.petrus.da171a.uppg3.budgetapp.R;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

public class ConsumptionActivity extends Activity {
	
	DBAdapter db;
	
	ProgressBar pbFood, pbLeisure, pbPhone, pbCar;
	int procFood, procLeisure, procPhone, procCar;
	String[] categoryStrArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_consumption);
		
		db = new DBAdapter(this);
		
		pbFood = (ProgressBar)findViewById(R.id.pbConsumptionActivityFood);
		pbLeisure = (ProgressBar)findViewById(R.id.pbConsumptionActivityLeisure);
		pbPhone = (ProgressBar)findViewById(R.id.pbConsumptionActivityPhone);
		pbCar = (ProgressBar)findViewById(R.id.pbConsumptionActivityCar);
		
		categoryStrArray = getResources().getStringArray(R.array.gui_activity_expenses_category_array);
		
		initializeViews();
		acquireData();
		updateViews();
		
		Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - onCreate(Bundle);\n procFood: "+procFood);
		Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - onCreate(Bundle);\n pbFood.getProgress(): "+pbFood.getProgress());
		
		Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - onCreate(Bundle);\n procLeisure: "+procLeisure);
		Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - onCreate(Bundle);\n pbLeisure.getProgress(): "+pbLeisure.getProgress());
		
		Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - onCreate(Bundle);\n procPhone: "+procPhone);
		Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - onCreate(Bundle);\n pbPhone.getProgress(): "+pbPhone.getProgress());
		
		Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - onCreate(Bundle);\n procCar: "+procCar);
		Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - onCreate(Bundle);\n pbCar.getProgress(): "+pbCar.getProgress());
	}
	
	private void initializeViews() {		// TODO: further generalize this method (to work with an arbitrary amount of ProgressBars)
		initializeProgressBar(pbFood);
		initializeProgressBar(pbLeisure);
		initializeProgressBar(pbPhone);
		initializeProgressBar(pbCar);
	}
	
	private void initializeProgressBar(ProgressBar pb) {
		pb.setMax(100);
		pb.setProgress(0);
	}

	private void acquireData() {		// TODO: further generalize this method (to work with an arbitrary amount of ProgressBars)
		procFood = calcConsumptionProc(categoryStrArray[BudgetConstants.CATEGORY_FOOD]);
		procLeisure = calcConsumptionProc(categoryStrArray[BudgetConstants.CATEGORY_LEISURE]);
		procPhone = calcConsumptionProc(categoryStrArray[BudgetConstants.CATEGORY_PHONE]);
		procCar = calcConsumptionProc(categoryStrArray[BudgetConstants.CATEGORY_CAR]);
	}
	
	private void updateViews() {		// TODO: further generalize this method (to work with an arbitrary amount of ProgressBars)
		pbFood.setProgress(procFood);
		pbLeisure.setProgress(procLeisure);
		pbPhone.setProgress(procPhone);
		pbCar.setProgress(procCar);
	}
	
	
	
	
	
	/**
	 * calculates the percentage of a given category that is consumed (for the current month)
	 * @param category - the category whose percentage is to be calculated
	 * @return a percentage of the consumption as an integer (0-100)
	 */
	private int calcConsumptionProc(String category) {
		Calendar cal = Calendar.getInstance();
		int sum = calcCategorySum(category, (cal.get(Calendar.MONTH)+1), cal.get(Calendar.YEAR));
		return calcCategoryProc(category, sum);
	}

	private int getCategoryIndex(String category) {
		for (int i=0; i<categoryStrArray.length; i++) {
			if (categoryStrArray[i].equals(category))
				return i;
		}
		return -1;
	}

	private int calcCategoryProc(String category, int sum) {
		int monthlyLimit = getMonthlyLimit(category);
		if (monthlyLimit < 0)
			Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - calcCategoryProc(String, int);\n getMonthlyLimit(String) returned a negative number!");
		Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - calcCategoryProc(String, int);\n sum: "+sum);
		Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - calcCategoryProc(String, int);\n monthlyLimit: "+monthlyLimit);
		
		return (int)(((double)sum/monthlyLimit)*100);
	}

	private int getMonthlyLimit(String category) {
		int categoryIndex = getCategoryIndex(category);
		if (categoryIndex < 0)
			Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - getMonthlyLimit(String);\n getCategoryIndex(String) returned a negative number!");
		switch(categoryIndex) {
			case BudgetConstants.CATEGORY_FOOD:
				return Integer.parseInt(getResources().getString(R.string.settings_monthly_limit_food));
			case BudgetConstants.CATEGORY_LEISURE:
				return Integer.parseInt(getResources().getString(R.string.settings_monthly_limit_leisure));
			case BudgetConstants.CATEGORY_PHONE:
				return Integer.parseInt(getResources().getString(R.string.settings_monthly_limit_phone));
			case BudgetConstants.CATEGORY_CAR:
				return Integer.parseInt(getResources().getString(R.string.settings_monthly_limit_car));
		}
		return -1;
	}

	private int calcCategorySum(String category, int currMonth, int currYear) {		// the month, 1-based
		int sum = 0;
		
		db.open();
		Cursor c = db.getEntry(category);
		if (c.moveToFirst()) {
			do {
				String entryDate = c.getString(BudgetConstants.DB_DATE);
				Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - calcCategorySum(String, int, int);\nCursor pointing to entry with date: "+entryDate);
				Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - calcCategorySum(String, int, int);\nCurrent category: "+category);
				
				if (entryIsWithinMonthRange(c, currMonth, currYear)) {
					Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - calcCategorySum(String, int, int);\nentryIsWithinMonthRange condition satisfied!");
					sum += Integer.parseInt(c.getString(BudgetConstants.DB_AMOUNT));
				}
			} while (c.moveToNext());
		}
		db.close();
		
		return sum;
	}

	private boolean entryIsWithinMonthRange(Cursor c, int currMonth, int currYear) {
		String entryDate = c.getString(BudgetConstants.DB_DATE);
		int entryYear = getYear(entryDate);
		int entryMonth = getMonth(entryDate);
		
		Log.d(BudgetConstants.DEBUG_TAG, "ConsumptionActivity - entryIsWithinMonthRange(Cursor, int, int);\n" +
				"Comparison performed on the following variables:\n[currMonth currYear entryMonth entryYear]: [" + 
				currMonth+" "+currYear+" "+entryMonth+" "+entryYear+"]");
		
		if ((entryYear == currYear) && (entryMonth == currMonth))
			return true;
		return false;
	}

	private int getMonth(String entryDate) {
		String subStr = entryDate.substring(5);
		String res = "";
		int i = 0;
		
		while (subStr.charAt(i) != '-') {
			res += subStr.charAt(i);
			i++;
		}
		
		return Integer.parseInt(res);
	}

	private int getYear(String entryDate) {
		return Integer.parseInt(entryDate.substring(0,4));
	}
	
}
