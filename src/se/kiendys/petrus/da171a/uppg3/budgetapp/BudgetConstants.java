package se.kiendys.petrus.da171a.uppg3.budgetapp;

public class BudgetConstants {
	
	public static final String DEBUG_TAG = "DEV_MSG";
	public static final int CATEGORY_FOOD = 0;
	public static final int CATEGORY_LEISURE = 2;
	public static final int CATEGORY_PHONE = 3;
	public static final int CATEGORY_CAR = 5;
	public static final int DB_ROWID = 0;
	public static final int DB_TYPE = 1;
	public static final int DB_DATE = 2;
	public static final int DB_CATEGORY = 3;
	public static final int DB_AMOUNT = 4;
	public static final String ERROR_INVALID_AMOUNT_INCOME = "Du glömde fylla i \"Inkomst\" rutan!";	// QUESTION: How to fetch this from strings.xml - R.strings.error_invalid_amount_income ?
	public static final String ERROR_INVALID_AMOUNT_EXPENSES = "Du glömde fylla i \"Utgift\" rutan!";	// QUESTION: refer to above question
	public static final int DISPLAY_MONTH = 0;
	public static final int DISPLAY_ALL = 1;
	public static final int DB_LIMIT_MONTH = 0;
	public static final int DB_LIMIT_ALL = 1;
	public static final int FILTER_LIMIT_MONTH = 0;
	public static final int FILTER_LIMIT_ALL = 1;
}
