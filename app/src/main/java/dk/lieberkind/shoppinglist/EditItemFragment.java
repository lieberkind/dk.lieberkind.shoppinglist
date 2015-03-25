package dk.lieberkind.shoppinglist;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class EditItemFragment extends Fragment {

    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_ID = "item_id";
    public static final String ITEM_AMOUNT = "item_amount";

    private int itemId;
    private String itemName;
    private int itemAmount;

    private ShoppingListActivity shoppingList;

    private TextView itemNameTextView;
    private Button incrementButton;
    private Button decrementButton;
    private Button doneButton;

    /**
     * Use this factory method to add a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditItemFragment.
     */
    public static EditItemFragment newInstance(int itemId, String itemName, int itemAmount) {
        EditItemFragment fragment = new EditItemFragment();
        Bundle args = new Bundle();
        args.putInt(ITEM_ID, itemId);
        args.putString(ITEM_NAME, itemName);
        args.putInt(ITEM_AMOUNT, itemAmount);
        fragment.setArguments(args);
        return fragment;
    }

    public EditItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemId = getArguments().getInt(ITEM_ID);
            itemName = getArguments().getString(ITEM_NAME);
            itemAmount = getArguments().getInt(ITEM_AMOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("onCreateView...");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_item, container, false);

        itemNameTextView = (TextView) view.findViewById(R.id.edit_item_name);
        incrementButton = (Button) view.findViewById(R.id.increment_amount_button);
        decrementButton = (Button) view.findViewById(R.id.decrement_amount_button);
        doneButton = (Button) view.findViewById(R.id.edit_done_button);

        if(getArguments() != null) {
            itemNameTextView.setText(itemName);
        }

        createButtonListeners(view);

        return view;
    }

    private void createButtonListeners(View view) {

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingList.incrementAmount(itemId);
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(--itemAmount <= 0) {
                    shoppingList.delete(itemId);
                    shoppingList.closeEditItem();
                } else {
                    shoppingList.decrementAmount(itemId);
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingList.closeEditItem();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            shoppingList = (ShoppingListActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Only a ShoppingListActivity can host this fragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        shoppingList = null;
    }
}
