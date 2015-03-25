package se.kiendys.petrus.da171a.uppg3.budgetapp.activities;

import java.util.Calendar;

import se.kiendys.petrus.da171a.uppg3.budgetapp.BudgetConstants;
import se.kiendys.petrus.da171a.uppg3.budgetapp.DBAdapter;
import se.kiendys.petrus.da171a.uppg3.budgetapp.PieChartView;
import se.kiendys.petrus.da171a.uppg3.budgetapp.R;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class DistributionActivity extends Activity {
	
	private DBAdapter db;
	private PieChartView pieChart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_distribution);
		init();
	}

	public void init() {
		db = new DBAdapter(this);
		pieChart = (PieChartView)findViewById(R.id.viewPieChart);
		
		pieChart.setDistributionPercentage(calcDistributionPercentage());
	}
	
	private double calcDistributionPercentage() {
		double income = calcIncome();
		double expenses = calcExpenses();
		Log.d(BudgetConstants.DEBUG_TAG, "income: "+income+", expenses: "+expenses);
		
		return (income/(income+expenses));
	}
	
	private double calcExpenses() {
		Calendar cal = Calendar.getInstance();
		Cursor c = getAllExpenses();
		return getSumForMonth(c, (cal.get(Calendar.MONTH)+1), cal.get(Calendar.YEAR));
	}
	
	private double calcIncome() {
		Calendar cal = Calendar.getInstance();
		Cursor c = getAllIncome();
		return getSumForMonth(c, (cal.get(Calendar.MONTH)+1), cal.get(Calendar.YEAR));
	}

	private Cursor getAllIncome() {
		db.open();
		Cursor c = db.getEntry(getResources().getString(R.string.db_type_income), BudgetConstants.DB_TYPE);
		db.close();
		return c;
	}

	private Cursor getAllExpenses() {
		db.open();
		Cursor c = db.getEntry(getResources().getString(R.string.db_type_expense), BudgetConstants.DB_TYPE);
		db.close();
		return c;
	}
	
	private double getSumForMonth(Cursor c, int currMonth,int currYear) {
		double res = 0.0;
		if (c.moveToFirst()) {
			do {
				String entryDate = c.getString(BudgetConstants.DB_DATE);
				int entryMonth = getMonth(entryDate);
				int entryYear = getYear(entryDate);
				if ((entryMonth==currMonth) && (entryYear==currYear))
					res += Double.parseDouble(c.getString(BudgetConstants.DB_AMOUNT));
			} while (c.moveToNext());
		} else
			return res;
		return res;
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