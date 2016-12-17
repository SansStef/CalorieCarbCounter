package com.projects.stefano.caloriecarbcounter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.projects.stefano.caloriecarbcounter.model.FoodEntry;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        TextView calorieLeft = (TextView) findViewById(R.id.caloriesLeft);
        calorieLeft.setText(prefs.getString("goalCalories","0"));
        TextView carbLeft = (TextView) findViewById(R.id.carbsLeft);
        carbLeft.setText(prefs.getString("goalCarbs","0"));

        // Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(MainActivity.this).build();
        realm = Realm.getInstance(realmConfig);

        RealmResults<FoodEntry> foods = realm.where(FoodEntry.class).findAll();
        for (FoodEntry food : foods){
            if(new LocalDate(food.getEntryDate()).equals(new LocalDate())){
                addFoodRow(food);
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFoodDialog();
            }
        });
    }

    protected void addFoodRow(FoodEntry food){
        // Get the TableLayout
        TableLayout foodTable = (TableLayout) findViewById(R.id.food_table);

        // Create a TableRow
        TableRow row = new TableRow(MainActivity.this);
        row.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        // Create a TextView for food description
        TextView labelFood = new TextView(MainActivity.this);
        labelFood.setText(food.getName());
        labelFood.setTextColor(Color.BLACK);
        labelFood.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        row.addView(labelFood);

        // Create a TextView for calories
        TextView labelCalories = new TextView(MainActivity.this);
        labelCalories.setText(""+food.getCalories());
        labelCalories.setTextColor(Color.BLACK);
        labelCalories.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        row.addView(labelCalories);

        // Create a TextView for carbs
        TextView labelCarbs = new TextView(MainActivity.this);
        labelCarbs.setText("" + food.getCarbs());
        labelCarbs.setTextColor(Color.BLACK);
        labelCarbs.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        row.addView(labelCarbs);

//        Button removeButton = new Button(MainActivity.this);
//        removeButton.setText("-");
//        removeButton.setWidth(50);
//        removeButton.setHeight(50);
//        TableRow.LayoutParams lp = new TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT,android.widget.TableRow.LayoutParams.MATCH_PARENT);
//        removeButton.setLayoutParams(lp);
//        row.addView(removeButton);

        // Add the TableRow to the TableLayout
        foodTable.addView(row, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        MainActivity.this.addToTotals(food.getCalories(), food.getCarbs());
        MainActivity.this.updateRemaining();
//        MainActivity.this.subtractFromRemaining(food.getCalories(), food.getCarbs());
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
                //Create FoodEntry
                FoodEntry food = new FoodEntry();

                //Food dialog variables
                food.setName(editText_Food.getText().toString());
                food.setCalories("".equals(calorieCount.getText().toString()) ? 0 : Integer.parseInt(calorieCount.getText().toString()));
                food.setCarbs("".equals(carbCount.getText().toString()) ? 0 : Integer.parseInt(carbCount.getText().toString()));
                food.setEntryDate(new Date());

                // Persist food data
                realm.beginTransaction();
                realm.copyToRealm(food);
                realm.commitTransaction();

                addFoodRow(food);
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (totCal > Integer.parseInt(prefs.getString("goalCalories", "0"))){
            calorieTotal.setTextColor(Color.RED);
        }
        if (totCarb + carbs > Integer.parseInt(prefs.getString("goalCarbs", "0"))){
            carbTotal.setTextColor(Color.RED);
        }
    }

    private void updateRemaining(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int goalCal = Integer.parseInt(prefs.getString("goalCalories", "0"));
        int goalCarb = Integer.parseInt(prefs.getString("goalCarbs", "0"));

        TextView calorieTotal = (TextView) findViewById(R.id.caloriesTotal);
        TextView carbTotal = (TextView) findViewById(R.id.carbsTotal);
        int totCal = Integer.parseInt(calorieTotal.getText().toString());
        int totCarb = Integer.parseInt(carbTotal.getText().toString());

        TextView calorieLeft = (TextView) findViewById(R.id.caloriesLeft);
        calorieLeft.setText(Integer.toString(goalCal - totCal));
        if (goalCal - totCal < 0){
            calorieLeft.setTextColor(Color.RED);
        }

        TextView carbLeft = (TextView) findViewById(R.id.carbsLeft);
        carbLeft.setText(Integer.toString(goalCarb - totCarb));
        if (goalCarb - totCarb < 0){
            carbLeft.setTextColor(Color.RED);
        }
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if("goalCalories".equals(key) || "goalCarbs".equals(key)){
            updateRemaining();
        }
    }
}
