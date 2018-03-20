package nacp.nacp.nacp.LocalDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ravi on 11/7/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="userDetails";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_USER_DETAILS="tuser";
    private static final String KEY_FID="fid";
    private static final String KEY_FMID="fuser";

    public DatabaseHandler(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_userDetails_TABLE="CREATE TABLE "+ TABLE_USER_DETAILS +" ( "+ KEY_FID + " INTEGER PRIMARY KEY," + KEY_FMID + " TEXT"+")" ;
        db.execSQL(CREATE_userDetails_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_USER_DETAILS);
        onCreate(db);
    }

    public boolean insertUserDetail (String fmid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FMID, fmid);
        db.insert(TABLE_USER_DETAILS, null, contentValues);
        db.close();
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_USER_DETAILS+" where "+KEY_FID+"="+id+"", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_USER_DETAILS);
        return numRows;
    }
    public boolean updateContact (Integer id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FMID, name);
        db.update(TABLE_USER_DETAILS, contentValues, "fid = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
}
