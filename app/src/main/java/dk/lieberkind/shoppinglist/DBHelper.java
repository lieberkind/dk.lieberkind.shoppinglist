package dk.lieberkind.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tomaslieberkind on 21/03/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ShoppingList";
    public static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table items(_id integer primary key autoincrement, name text, amount integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table items");
        onCreate(db);
    }
}
