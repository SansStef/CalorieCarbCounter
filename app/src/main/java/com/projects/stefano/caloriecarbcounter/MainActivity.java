package com.projects.stefano.caloriecarbcounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFoodDialog();
            }
        });
    }

    protected void showFoodDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View dialog = inflater.inflate(R.layout.add_food, null);

        builder.setView(dialog);

        TextView title = new TextView(this);
        // You Can Customise your Title here
        title.setText(R.string.add_food_title);
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        builder.setCustomTitle(title);

        //Add food views
        final EditText editText_Food = (EditText) dialog.findViewById(R.id.editText_food);
        final EditText calorieCount = (EditText) dialog.findViewById(R.id.editText_calorieCount);
        final EditText carbCount = (EditText) dialog.findViewById(R.id.editText_carbCount);

        // Set up the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Food dialog variables
                String food = editText_Food.getText().toString();
                String calories = calorieCount.getText().toString();
                String carbs = carbCount.getText().toString();

                // Get the TableLayout
                TableLayout foodTable = (TableLayout) findViewById(R.id.food_table);

                // Create a TableRow
                TableRow row = new TableRow(MainActivity.this);
                row.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                // Create a TextView for food description
                TextView labelFood = new TextView(MainActivity.this);
                labelFood.setText(food);
                labelFood.setTextColor(Color.BLACK);
                labelFood.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                row.addView(labelFood);

                // Create a TextView for calories
                TextView labelCalories = new TextView(MainActivity.this);
                labelCalories.setText(calories);
                labelCalories.setTextColor(Color.BLACK);
                labelCalories.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                row.addView(labelCalories);

                // Create a TextView for carbs
                TextView labelCarbs = new TextView(MainActivity.this);
                labelCarbs.setText(carbs);
                labelCarbs.setTextColor(Color.BLACK);
                labelCarbs.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                row.addView(labelCarbs);

                // Add the TableRow to the TableLayout
                foodTable.addView(row, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));

                MainActivity.this.addToTotals(Integer.parseInt(calories), Integer.parseInt(carbs));
                MainActivity.this.subtractFromRemaining(Integer.parseInt(calories), Integer.parseInt(carbs));

            }
        });
        builder.setNegativeButton(R.string.cancel, null);

        builder.show();
    }

    private void addToTotals(int calories, int carbs){
        TextView calorieTotal = (TextView) findViewById(R.id.caloriesTotal);
        int totCal = Integer.parseInt(calorieTotal.getText().toString());
        calorieTotal.setText(Integer.toString(totCal + calories));

        TextView carbTotal = (TextView) findViewById(R.id.carbsTotal);
        int totCarb = Integer.parseInt(carbTotal.getText().toString());
        carbTotal.setText(Integer.toString(totCarb + carbs));
    }

    private void subtractFromRemaining(int calories, int carbs){
        TextView calorieLeft = (TextView) findViewById(R.id.caloriesLeft);
        int remCal = Integer.parseInt(calorieLeft.getText().toString());
        calorieLeft.setText(Integer.toString(remCal - calories));
        if (remCal - calories < 0){
            calorieLeft.setTextColor(Color.RED);
        }

        TextView carbLeft = (TextView) findViewById(R.id.carbsLeft);
        int remCarb = Integer.parseInt(carbLeft.getText().toString());
        carbLeft.setText(Integer.toString(remCarb - carbs));
        if (remCarb - carbs < 0){
            carbLeft.setTextColor(Color.RED);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
