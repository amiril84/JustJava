package com.example.amiril.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends ActionBarActivity {

    int quantity = 0;
    int price = 0;
    String coffeeOrderSummary = "";
    String buyerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        EditText et = (EditText) findViewById(R.id.text_nama);
        buyerName = et.getText().toString();
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.state_checkbox_whipped_cream);
        boolean addWhippedCream = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.state_checkbox_chocolate);
        boolean addChocolate = chocolateCheckBox.isChecked();
        price = calculatePrice(addWhippedCream, addChocolate);
        coffeeOrderSummary = createOrderSummary(price, addWhippedCream, addChocolate, buyerName);
        displayMessage(coffeeOrderSummary);

    }


    /**
     * Calculates the price of the order.
     */
    private int calculatePrice(boolean withWhippedCream, boolean withChocolate) {
        int priceWhippedCream = 1;
        int priceChocolate = 2;
        int priceBase = 5;


        if (withWhippedCream) {
            priceBase = priceBase + priceWhippedCream;
        }
        if (withChocolate) {
            priceBase = priceBase + priceChocolate;
        }

        return priceBase * quantity;


    }

    private String createOrderSummary(int price, boolean withWhippedCream, boolean withChocolate, String theName) {
        return "Name: " + theName + "\nAdd whipped cream? " + withWhippedCream + "\nAdd chocolate? " + withChocolate + "\nQuantity:" + quantity + "\nTotal: $" + price + "\n" + getString(R.string.thank_you);
    }


    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    public void increment(View view) {

        quantity = quantity + 1;
        display(quantity);
    }

    public void decrement(View view) {

        quantity = quantity - 1;
        display(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if (number > 100) {
            number = 100;
            quantity = 100;
            CharSequence text = "You cannot have more than 100 coffees!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }

        if (number < 0) {
            number = 0;
            quantity = 0;
            CharSequence text = "You cannot have less than 0 coffee!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    public void sendEmailReceipt(View view) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if (price == 0) {
            CharSequence text = "Please submit order first!";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {

            EditText et = (EditText) findViewById(R.id.text_email);
            String alamatEmail = et.getText().toString();
            String defSubject = buyerName + "'s Coffee Purchase Receipt";
            Uri emailUri = Uri.parse("mailto:");
            Intent myEmailIntent = new Intent(Intent.ACTION_SENDTO, emailUri);
            myEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{alamatEmail});
            myEmailIntent.putExtra(Intent.EXTRA_SUBJECT, defSubject);
            myEmailIntent.putExtra(Intent.EXTRA_TEXT, coffeeOrderSummary);

            if (myEmailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(myEmailIntent);
            }


        }
    }

}