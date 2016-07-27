package uk.co.barrybiscuit.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView quantity;
    private String value;
    private int quantityValue;
    private double price;
    DBHelper orderObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        orderObject = new DBHelper(this);

        this.quantity = (TextView)findViewById(R.id.quantity_text_view);
        this.value = quantity.getText().toString();
        this.quantityValue = Integer.parseInt(value);
        this.price = 3.50;
        showRows();
        showAllOrders();
    }

    public void submitOrder(View view) {
        orderObject.insertOrder(this.quantityValue, (this.price * this.quantityValue));
    }

    public void incrementValue(View view) {
        int newQuantity = this.quantityValue + 1;
        this.quantityValue = newQuantity;
        display(newQuantity);
        displayPrice(newQuantity*this.price);
    }

    public void decrementValue(View view) {
        int newQuantity = this.quantityValue - 1;
        if(newQuantity < 0){
            newQuantity = 0;
        }
        this.quantityValue = newQuantity;
        display(newQuantity);
        displayPrice(newQuantity*this.price);
    }

    private void displayPrice(double number) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(String.valueOf(NumberFormat.getCurrencyInstance().format(number)));
    }

    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(number));
    }

    private void showRows() {
        int allOrdersRowNumber = orderObject.numberOfRows();

        TextView rowBox = (TextView) findViewById(R.id.rows_text_view);
        rowBox.setText(String.valueOf(allOrdersRowNumber));
    }

    private void showAllOrders(){
        float scale = this.getResources().getDisplayMetrics().density;
        int dp64 = (int) (64 * scale + 0.5f);
        int dp16 = (int) (16 * scale + 0.5f);
        int dp8 = (int) (8 * scale + 0.5f);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_Container);
        LinearLayout FeedContainer = new LinearLayout(this);
            FeedContainer.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            FeedContainer.setLayoutParams(LLParams);

        LinearLayout orderContainerLabels = new LinearLayout(this);
            orderContainerLabels.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams main_params_Labels = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            orderContainerLabels.setLayoutParams(main_params_Labels);
            orderContainerLabels.setWeightSum(4f);

            TextView orderNoLabel = new TextView(this);
                orderNoLabel.setText("Order ID");
                LinearLayout.LayoutParams ID_params_Label = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                orderNoLabel.setGravity(Gravity.CENTER_VERTICAL);
                ID_params_Label.weight = 1f;
                orderNoLabel.setPadding(dp8, 0, dp8, 0);
                orderNoLabel.setBackgroundColor(0xDDDDDD);
                orderNoLabel.setLayoutParams(ID_params_Label);
            orderContainerLabels.addView(orderNoLabel);


            TextView orderQuantityLabel = new TextView(this);
                orderQuantityLabel.setText("Quantity");
                LinearLayout.LayoutParams Quantity_params_Label = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                orderQuantityLabel.setGravity(Gravity.CENTER);
                Quantity_params_Label.weight = 1f;
                orderQuantityLabel.setPadding(dp8, 0, dp8, 0);
                orderQuantityLabel.setLayoutParams(Quantity_params_Label);
            orderContainerLabels.addView(orderQuantityLabel);


            TextView orderPriceLabel = new TextView(this);
                orderPriceLabel.setText("Price");
                LinearLayout.LayoutParams Price_params_Label = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                orderPriceLabel.setGravity(Gravity.CENTER);
                Price_params_Label.weight = 1f;
                orderPriceLabel.setPadding(dp8, 0, dp8, 0);
                orderPriceLabel.setLayoutParams(Price_params_Label);
            orderContainerLabels.addView(orderPriceLabel);


            TextView removeOrderLabel = new TextView(this);
                LinearLayout.LayoutParams removeOrderParams_Label = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                removeOrderLabel.setGravity(Gravity.CENTER);
                removeOrderLabel.setText("");
                removeOrderLabel.setWidth(dp64);
                removeOrderLabel.setHeight(dp64);
                removeOrderLabel.setPadding(dp8, 0, dp8, 0);
                removeOrderLabel.setLayoutParams(removeOrderParams_Label);
            orderContainerLabels.addView(removeOrderLabel);
        FeedContainer.addView(orderContainerLabels);

        List<Map<String, String>> allOrders = orderObject.getAllOrders();
        for(Map order : allOrders){
            Object orderIDValue = order.get("order_id");
            Object orderQuantityValue = order.get("order_quantity");
            Object orderPriceValue = order.get("order_price");

            LinearLayout orderContainer = new LinearLayout(this);
                orderContainer.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams main_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                orderContainer.setLayoutParams(main_params);
                orderContainer.setWeightSum(3f);
                orderContainer.setPadding(0, dp8, 0, dp8);
                orderContainer.setTag(Integer.parseInt((String) orderIDValue));

                TextView orderNo = new TextView(this);
                    orderNo.setText((String)"Order #"+orderIDValue);
                    LinearLayout.LayoutParams ID_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    orderNo.setGravity(Gravity.CENTER_VERTICAL);
                    ID_params.weight = 1f;
                    orderNo.setPadding(dp8, 0, dp8, 0);
                    orderNo.setBackgroundColor(0xDDDDDD);
                    orderNo.setLayoutParams(ID_params);
                orderContainer.addView(orderNo);


                TextView orderQuantity = new TextView(this);
                    orderQuantity.setText((String)orderQuantityValue);
                    LinearLayout.LayoutParams Quantity_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    orderQuantity.setGravity(Gravity.CENTER);
                    Quantity_params.weight = 1f;
                    orderQuantity.setPadding(dp8, 0, dp8, 0);
                    orderQuantity.setLayoutParams(Quantity_params);
                orderContainer.addView(orderQuantity);


                TextView orderPrice = new TextView(this);
                    orderPrice.setText((String)"£"+orderPriceValue);
                    LinearLayout.LayoutParams Price_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    orderPrice.setGravity(Gravity.CENTER);
                    Price_params.weight = 1f;
                    orderPrice.setPadding(dp8, 0, dp8, 0);
                    orderPrice.setLayoutParams(Price_params);
                orderContainer.addView(orderPrice);


                Button removeOrder = new Button(this);
                    removeOrder.setText("X");
                    LinearLayout.LayoutParams removeOrderParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    removeOrder.setGravity(Gravity.CENTER);
                    removeOrder.setWidth(dp16);
                    removeOrder.setHeight(dp16);
                    removeOrder.setBackgroundColor(Color.parseColor("#DDDDDD"));
                    removeOrder.setPadding(dp8, dp8, dp8, dp8);
                    removeOrder.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            ViewGroup parentView = (ViewGroup) view.getParent();
                            int parentID = (Integer) parentView.getTag();
                            ((ViewGroup) parentView.getParent()).removeView(parentView);
                            orderObject.deleteContact(parentID);
                        }
                    });
                    removeOrder.setLayoutParams(removeOrderParams);
                orderContainer.addView(removeOrder);

            FeedContainer.addView(orderContainer);
        }
        mainLayout.addView(FeedContainer);
    }
}