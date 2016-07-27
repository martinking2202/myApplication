package uk.co.barrybiscuit.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyApplication.db";
    public static final String order_Table_Name = "orders";
    public static final String order_Column_ID = "order_id";
    public static final String order_Column_Quantity = "order_quantity";
    public static final String order_Column_Price = "order_price";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table orders " +"(order_id integer primary key, order_quantity integer,order_price text)");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("DROP TABLE IF EXISTS orders");
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertOrder (int quantity, double price){
        SQLiteDatabase dbWrite = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_quantity", quantity);
        contentValues.put("order_price", price);
        dbWrite.insert("orders", null, contentValues);
        return true;
    }

    public int numberOfRows(){
        SQLiteDatabase dbRead = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(dbRead, order_Table_Name);
    }

    public Integer deleteContact (Integer id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("orders",
                "order_id = ? ",
                new String[] { Integer.toString(id) });
    }

    public List<Map<String, String>> getAllOrders(){
        SQLiteDatabase dbRead = this.getReadableDatabase();
        List<Map<String, String>> list = new ArrayList<>();
        Cursor results = dbRead.rawQuery("select * from orders", null);
        results.moveToFirst();

        while(!results.isAfterLast()){
            Map<String, String> map = new HashMap<String, String>();
            map.put("order_id", results.getString(results.getColumnIndex(order_Column_ID)));
            map.put("order_quantity", results.getString(results.getColumnIndex(order_Column_Quantity)));
            map.put("order_price", results.getString(results.getColumnIndex(order_Column_Price)));
            list.add(map);
            results.moveToNext();
        }
        results.close();
        return list;
    }
}