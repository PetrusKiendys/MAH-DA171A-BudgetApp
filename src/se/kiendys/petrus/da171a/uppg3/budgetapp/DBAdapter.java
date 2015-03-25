package se.kiendys.petrus.da171a.uppg3.budgetapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import se.kiendys.petrus.da171a.uppg3.budgetapp.BudgetConstants;

public class DBAdapter {
	
    static final String KEY_ROWID = "_id";
    static final String KEY_TYPE = "type";
    static final String KEY_DATE = "date";
	static final String KEY_CATEGORY = "category";
	static final String KEY_AMOUNT = "amount";

    static final String DATABASE_NAME = "BudgetDB";
    static final String DATABASE_TABLE = "transactions";
    static final int	DATABASE_VERSION = 1;

    // REMARK: may not parse constants correctly, in which case revert to BA4AD template
    static final String DATABASE_CREATE =
        "create table " + DATABASE_TABLE + " (_id integer primary key autoincrement, "
        +	KEY_TYPE +		" text not null, "
        +  	KEY_DATE +		" text not null, "
        +	KEY_CATEGORY +	" text not null, "
        +	KEY_AMOUNT + 	" text not null" + ");";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    
    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
                Log.d(BudgetConstants.DEBUG_TAG, "DBAdapter - DatabaseHelper - onCreate(SQLiteDatabase);");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(BudgetConstants.DEBUG_TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            // REMARK: may not parse constants correctly, in which case revert to BA4AD template
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() 
    {
        DBHelper.close();
    }

    //---insert an entry into the database---
    /**
     * @return RowID of the table
     */
    public long insertEntry(String type, String date, String category, String amount) 
    {
        ContentValues initialValues = new ContentValues();
        
        initialValues.put(KEY_TYPE, type);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_CATEGORY, category);
        initialValues.put(KEY_AMOUNT, amount);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular entry---
    public boolean deleteEntry(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all entries---
    // NOTE: ordered by date in an ascending order
    public Cursor getAllEntries() throws SQLException
    {
    	Log.d(BudgetConstants.DEBUG_TAG, "DBAdapter - DatabaseHelper - getAllEntries();");
        return db.query(DATABASE_TABLE,
        		new String[] {
        		KEY_ROWID,
        		KEY_TYPE,
        		KEY_DATE,
        		KEY_CATEGORY,
        		KEY_AMOUNT},
        		null, null, null, null, KEY_DATE+" ASC");
    }

    //---retrieves entries based on rowId---
    public Cursor getEntry(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ROWID,
                		KEY_TYPE,
        				KEY_DATE,
        				KEY_CATEGORY,
        				KEY_AMOUNT},
        				KEY_ROWID + "=" + rowId,
        				null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    // TODO: this should be deprecated/modified and called from getEntry(String searchstr, int dbColumnID)
    //---retrieves entries based on category---
    public Cursor getEntry(String category) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ROWID,
                		KEY_TYPE,
        				KEY_DATE,
        				KEY_CATEGORY,
        				KEY_AMOUNT},
        				KEY_CATEGORY + "= '" + category + "'",
        				null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    //---general function for retrieving entries---
    public Cursor getEntry(String searchstr, int dbColumnIdentifier) throws SQLException {
    	Cursor res = null;
    	
    	switch(dbColumnIdentifier) {
    		case BudgetConstants.DB_TYPE:
    			Cursor mCursor =
                    db.query(true, DATABASE_TABLE, new String[] {
                    		KEY_ROWID,
                    		KEY_TYPE,
            				KEY_DATE,
            				KEY_CATEGORY,
            				KEY_AMOUNT},
            				KEY_TYPE + "= '" + searchstr + "'",
            				null, null, null, null, null);
    			if (mCursor != null) {
    				mCursor.moveToFirst();
    			}
    			res = mCursor;
    			break;
    	}
    	
    	return res;
    }
    
    //---updates an entry---
    public boolean updateEntry(long rowID, String type, String date, String category, String amount) 
    {
        ContentValues args = new ContentValues();
        
        args.put(KEY_TYPE, type);
		args.put(KEY_DATE, date);
		args.put(KEY_CATEGORY, category);
		args.put(KEY_AMOUNT, amount);
        
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowID, null) > 0;
    }

}
