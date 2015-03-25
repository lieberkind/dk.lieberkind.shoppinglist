package dk.lieberkind.shoppinglist;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemFragment extends Fragment {

    private ShoppingListActivity shoppingList;

    private EditText itemNameField;
    private EditText itemAmountField;
    private Button addItemButton;

    public AddItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_item, container, false);

        itemNameField = (EditText) view.findViewById(R.id.item_name);
        itemAmountField = (EditText) view.findViewById(R.id.item_amount);
        addItemButton = (Button) view.findViewById(R.id.add_item_button);

        createButtonListeners();

        return view;
    }

    private void createButtonListeners() {
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = itemNameField.getText().toString();
                String amount = itemAmountField.getText().toString();
                String message;

                if(name.isEmpty() || amount.isEmpty()) {
                    message = "Fill in both item and amount fields";
                } else if (Integer.parseInt(amount) == 0) {
                    message = "Amount must be greater than 0";
                } else {
                    shoppingList.addItem(name, Integer.parseInt(amount));

                    // Reset the fields
                    itemNameField.setText("");
                    itemAmountField.setText("");

                    // Request focus for the name field
                    itemNameField.requestFocus();

                    // Set a success message
                    message = "Added " + amount + " x " + name;
                }

                // Show the message to the user
                Toast.makeText(shoppingList, message, Toast.LENGTH_SHORT).show();
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
