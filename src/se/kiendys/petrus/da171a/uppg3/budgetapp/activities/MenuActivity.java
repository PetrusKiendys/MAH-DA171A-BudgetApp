package se.kiendys.petrus.da171a.uppg3.budgetapp.activities;

import se.kiendys.petrus.da171a.uppg3.budgetapp.AboutDialog;
import se.kiendys.petrus.da171a.uppg3.budgetapp.R;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

public class MenuActivity extends Activity {
	
	private static final int DIALOG_ABOUT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {	// QUESTION: Why are onCreate methods 'protected' ?
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_menu);
    }
    
/*********************
 * OnClick events
 *********************/

    
    public void onClickIncome(View v)	// QUESTION: Why are onClick-methods 'public' ?
    {
    	Intent intent = new Intent(this, IncomeActivity.class);
    	startActivity(intent);
    }
    
    public void onClickExpenses(View v)
    {
    	Intent intent = new Intent(this, ExpensesActivity.class);
    	startActivity(intent);
    }
    
    public void onClickConsumption(View v)
    {
    	Intent intent = new Intent(this, ConsumptionActivity.class);
    	startActivity(intent);
    }
    
    public void onClickDistribution(View v)
    {
    	Intent intent = new Intent(this, DistributionActivity.class);
    	startActivity(intent);
    }
    
    public void onClickList(View v)
    {
    	Intent intent = new Intent(this, ListActivity.class);
    	startActivity(intent);
    }
    
    public void onClickAuthor(View v)
    {
    	showDialog(DIALOG_ABOUT);
    }
    
    protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ABOUT:
			return new AboutDialog(this);
		}
		return null;
	}
    
}
