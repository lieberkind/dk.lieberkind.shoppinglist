package dk.lieberkind.shoppinglist;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class ShoppingListActivity extends ListActivity {

    /**
     * The DAO through which we interact with the items table
     */
    private ItemDAO items;

    /**
     * The adapter to use for the item list
     */
    private SimpleCursorAdapter cursorAdapter;

    private TextView itemSumTextView;

    /**
     * The onCreate activity lifecycle method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        itemSumTextView = (TextView) findViewById(R.id.total_items_value);

        // Create the item data access object to interact with the database.
        // As we will be doing a lot of querying to the database, we open it in the onCreate method
        // and close it in the onStop method
        items = new ItemDAO(this);
        items.openDatabaseConnection();

        String[] fromColumns = {"name", "amount"};
        int[] toViews = { android.R.id.text1, android.R.id.text2 };

        Cursor cursor = items.all();
        cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, fromColumns, toViews, 0);

        setListAdapter(cursorAdapter);
        refreshItemSum();

        // Add the AddItemFragment to the top frame. We only want to do this the first time onCreate
        // is called, as the fragment manager will re-add the last active fragment to the frame on
        // configuration changes. Failing to make this check results in several instances of the
        // fragment being placed on top of each other within the frame
        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.add(R.id.action_buttons_frame, new AddItemFragment());
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        items.closeDatabaseConnection();
    }

    public void addItem(String name, int amount) {
        items.add(name, amount);
        refreshList();
        refreshItemSum();
    }

    public void incrementAmount(int itemId) {
        items.incrementAmount(itemId);
        refreshList();
        refreshItemSum();
    }

    public void decrementAmount(int itemId) {
        items.decrementAmount(itemId);
        refreshList();
        refreshItemSum();
    }

    public void delete(int itemId) {
        items.delete(itemId);
        refreshList();
        refreshItemSum();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Cursor cursor = (Cursor) cursorAdapter.getItem(position);
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        int amount = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("amount")));

        openEditItem((int) id, name, amount);
    }

    public void openEditItem(int itemId, String itemName, int itemAmount) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.action_buttons_frame, EditItemFragment.newInstance(itemId, itemName, itemAmount));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void closeEditItem() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.action_buttons_frame, new AddItemFragment());
        transaction.commit();
    }

    public void refreshList() {
        Cursor cursor = items.all();
        cursorAdapter.changeCursor(cursor);
    }

    private void refreshItemSum() {
        itemSumTextView.setText(String.valueOf(items.sum()));
    }

}
