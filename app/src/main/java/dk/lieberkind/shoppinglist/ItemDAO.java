package dk.lieberkind.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by tomaslieberkind on 21/03/15.
 */
public class ItemDAO {

    public static final String TABLE_NAME = "items";

    public static final String ID_COL = "_id";
    public static final String NAME_COL = "name";
    public static final String AMOUNT_COL = "amount";

    DBHelper helper;
    SQLiteDatabase db;

    /**
     * Create a new instance of this DAO
     *
     * @param context
     */
    public ItemDAO(Context context) {
        helper = new DBHelper(context);
    }

    /**
     * Open the connection to the database
     */
    public void openDatabaseConnection() {
        db = helper.getWritableDatabase();
    }

    /**
     * Close the connection to the database
     */
    public void closeDatabaseConnection() {
        helper.close();
    }

    /**
     * Get all the items from the database
     *
     * @return Cursor of all contents of the items table, ordered by name
     */
    public Cursor all() {
        String[] columns = new String[] {ID_COL, NAME_COL, AMOUNT_COL};

        return db.query(TABLE_NAME, columns, null, null, null, null, NAME_COL);
    }

    /**
     * Add an item to the items table
     *
     * @param name The name of the item
     * @param amount The amount of the item
     */
    public void add(String name, int amount) {
        ContentValues values = new ContentValues();

        values.put(NAME_COL, name);
        values.put(AMOUNT_COL, amount);

        db.insert(TABLE_NAME, null, values);
    }

    /**
     * Increment the amount of an item by one
     *
     * @param itemId The id of the item to increment
     */
    public void incrementAmount(int itemId) {
        String[] bindings = new String[] { String.valueOf(itemId) };

        db.execSQL("UPDATE items SET amount=amount+1 WHERE _id = ?", bindings);
    }

    /**
     * Decrement the amount of an item by one
     *
     * @param itemId The id of the item to decrement
     */
    public void decrementAmount(int itemId) {
        String[] bindings = new String[] { String.valueOf(itemId) };

        db.execSQL("UPDATE items SET amount=amount-1 WHERE _id = ?", bindings);
    }

    /**
     * Delete an item
     *
     * @param itemId The id of the item to delete
     */
    public void delete(int itemId) {
        db.delete(TABLE_NAME, ID_COL + "= ?", new String[] { String.valueOf(itemId) });
    }

    /**
     * Get the sum of all items added to the shopping list
     * @return int
     */
    public int sum() {
        Cursor cursor = db.rawQuery("SELECT sum(" + AMOUNT_COL + ") FROM " + TABLE_NAME + ";", null);

        return cursor.moveToFirst() ? cursor.getInt(0) : 0;
    }
}
