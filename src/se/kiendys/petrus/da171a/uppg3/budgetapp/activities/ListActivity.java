package se.kiendys.petrus.da171a.uppg3.budgetapp.activities;

import java.util.ArrayList;
import java.util.Calendar;

import se.kiendys.petrus.da171a.uppg3.budgetapp.BudgetConstants;
import se.kiendys.petrus.da171a.uppg3.budgetapp.DBAdapter;
import se.kiendys.petrus.da171a.uppg3.budgetapp.Entry;
import se.kiendys.petrus.da171a.uppg3.budgetapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.database.Cursor;

public class ListActivity extends Activity {
	TextView tvDate;
	TextView tvType;
	TextView tvAmount;
	Button btnThisMonth;
	Button btnShowAllData;
	DBAdapter db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_list_v2);
		
		init();
	}
	private void init() {
		db = new DBAdapter(this);
		tvDate = (TextView) findViewById(R.id.tvListActivityDbDate);
		tvType = (TextView) findViewById(R.id.tvListActivityDbType);
		tvAmount = (TextView) findViewById(R.id.tvListActivityDbAmount);
		btnThisMonth = (Button)findViewById(R.id.btnListActivityShowCurrMonth);
		btnShowAllData = (Button)findViewById(R.id.btnListActivityShowAll);
		
		updateGUI(BudgetConstants.DISPLAY_ALL);
	}
	
	public void onClickShowCurrMonth(View v) {
    	updateGUI(BudgetConstants.DISPLAY_MONTH);
    }
	
	public void onClickShowAll(View v) {
    	updateGUI(BudgetConstants.DISPLAY_ALL);
    }
	
	private void updateGUI(int displayScope) {
		Cursor c;
		ArrayList<Entry> entries;			// NOTE: making an Entry ArrayList due to the fact that rows in Cursors cannot be deleted
		switch(displayScope) {
		case BudgetConstants.DISPLAY_ALL:
			c = getAllTransactions();
			entries = extractEntries(c, BudgetConstants.FILTER_LIMIT_ALL);
			displayEntries(entries);
			break;
		case BudgetConstants.DISPLAY_MONTH:
			c = getAllTransactions();
			entries = extractEntries(c, BudgetConstants.FILTER_LIMIT_MONTH);
			displayEntries(entries);
			break;
		}
	}
	
	private void displayEntries(ArrayList<Entry> entries) {
		
		String date = "";
		String type = "";
		String amount = "";
		
		for (int i=entries.size()-1; i>=0; i--) {
			date += entries.get(i).getDate() + "\n";
			type += entries.get(i).getType() + "\n";
			amount += entries.get(i).getAmount() + "\n";
		}
		
		tvDate.setText(date);
		tvType.setText(type);
		tvAmount.setText(amount);
		
		db.close();					// NOTE: closing db at this point, otherwise error occurs: Cursor - Invalid statement in fillWindow()
	}
	
	private ArrayList<Entry> extractEntries(Cursor c, int filter) {
		ArrayList<Entry> res = new ArrayList<Entry>();
		Calendar cal = Calendar.getInstance();
		int currMonth = cal.get(Calendar.MONTH)+1;
		int currYear = cal.get(Calendar.YEAR);
		
		if (c.moveToFirst()) {
			do {
				if (filter==BudgetConstants.FILTER_LIMIT_MONTH) {
					String entryDate = c.getString(BudgetConstants.DB_DATE);
					int entryMonth = getMonth(entryDate);
					int entryYear = getYear(entryDate);
					Log.d(BudgetConstants.DEBUG_TAG, "entrydate: "+entryDate+", entrymonth: "+entryMonth+", entryyear: "+entryYear);
					if (!((entryMonth==currMonth) && (entryYear==currYear)))
						continue;
				}
				res.add(new Entry(c.getString(BudgetConstants.DB_DATE), c.getString(BudgetConstants.DB_TYPE), c.getString(BudgetConstants.DB_AMOUNT)));
			} while (c.moveToNext());
		}
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
	
	private Cursor getAllTransactions() {
		db.open();
		Cursor c = db.getAllEntries();
		return c;
	}

}
