package com.example.travelcompanyapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.travelcompanyapplication.db_controller.TravelCompanyContract;
import com.example.travelcompanyapplication.db_controller.TravelCompanyDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AccountViewBuilder implements View.OnClickListener {
    private TravelCompanyDBHelper travelCompanyDBHelper;
    private LayoutInflater inflater;
    private ViewGroup container;
    private static View view;
    private LinearLayout flightsLayout;
    private LinearLayout hotelsLayout;
    private EditText nameEditText;
    private EditText emailEditText;
    private Button saveButton;
    private Boolean isUpdate = false;
    private SharedPreferences sharedPreferences;

    public AccountViewBuilder(TravelCompanyDBHelper travelCompanyDBHelper, LayoutInflater inflater, ViewGroup container) {
        this.travelCompanyDBHelper = travelCompanyDBHelper;
        this.inflater = inflater;
        this.container = container;
    }

    private void setupTopView() {
        nameEditText = (EditText) view.findViewById(R.id.account_name_editText);
        emailEditText = (EditText) view.findViewById(R.id.account_email_editText);
        saveButton = (Button) view.findViewById(R.id.account_save_button);
        saveButton.setOnClickListener(this);

        String name = sharedPreferences.getString("accountName", "accountName");
        String email = sharedPreferences.getString("accountEmail", "accountEmail");
        nameEditText.setText(name);
        emailEditText.setText(email);
    }

    public View createView(String selection, String[] selectionArgs) {
        if (!isUpdate) {
            view = inflater.inflate(R.layout.fragment_account, container, false);
            flightsLayout = (LinearLayout) view.findViewById(R.id.chosen_flights_container);
            hotelsLayout = (LinearLayout) view.findViewById(R.id.chosen_hotels_container);
            sharedPreferences = view.getContext().getSharedPreferences(view.getContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            isUpdate = true;
        } else {
            flightsLayout.removeAllViewsInLayout();
            hotelsLayout.removeAllViewsInLayout();
        }

        setupTopView();

        ArrayList<ArrayList<String>> flightTicketsData = travelCompanyDBHelper.getAccountFlightRecords(selection, selectionArgs);

        for (ArrayList<String> row : flightTicketsData) {

            TableLayout tableLayout = createFlightTable(row);

            View tableDivider = new View(view.getContext());
            tableDivider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
            tableDivider.setBackgroundColor(view.getResources().getColor(R.color.dark_blue));

            flightsLayout.addView(tableLayout);
            flightsLayout.addView(tableDivider);
        }

        ArrayList<ArrayList<String>> bookedHotelsData = travelCompanyDBHelper.getAccountHotelRecords(selection, selectionArgs);

        for (ArrayList<String> row : bookedHotelsData) {
            TableLayout tableLayout = createHotelTable(row);

            View tableDivider = new View(view.getContext());
            tableDivider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
            tableDivider.setBackgroundColor(view.getResources().getColor(R.color.dark_blue));

            hotelsLayout.addView(tableLayout);
            hotelsLayout.addView(tableDivider);
        }
        return view;
    }

    private TableLayout createFlightTable(ArrayList<String> flightTicket) {
        ArrayList<String> flightData = travelCompanyDBHelper.getFlightRecords(TravelCompanyContract.FlightEntry._ID + " LIKE ?", new String[]{flightTicket.get(1)}).get(0);
        TableLayout tableLayout = FlightsViewBuilder.createTableInfo(view, flightData);

        TableLayout.LayoutParams numberOfTicketsLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        numberOfTicketsLayoutParams.setMargins(0, 70, 0, 0);
        TextView numberOfTicketsView = HotelsViewBuilder.createTableColumn(view, numberOfTicketsLayoutParams, "Number of tickets: " + flightTicket.get(2), 18, Integer.parseInt(flightData.get(0)) * 10000 + 17);
        tableLayout.addView(numberOfTicketsView);

        Integer totalCost = Integer.valueOf(flightData.get(6).replace(" ", "")) * Integer.valueOf(flightTicket.get(2)) / 60;

        TableLayout.LayoutParams totalCostLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        totalCostLayoutParams.setMargins(0, 50, 0, 20);
        TextView totalCostView = HotelsViewBuilder.createTableColumn(view, totalCostLayoutParams, "Total cost: " + totalCost + " $", 24, Integer.parseInt(flightData.get(0)) * 10000 + 18);
//        totalCostView.setTextColor(view.getResources().getColor(R.color.dark_blue));
        totalCostView.setTypeface(null, Typeface.BOLD);
        tableLayout.addView(totalCostView);

        Button deleteFlightButton = new Button(view.getContext());
        TableLayout.LayoutParams deleteFlightButtonLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        deleteFlightButtonLayoutParams.setMargins(0, 20, 0, 30);
        deleteFlightButton.setLayoutParams(deleteFlightButtonLayoutParams);
        deleteFlightButton.setText("Return ticket");
        deleteFlightButton.setId(Integer.parseInt(flightData.get(0)) * 10000 + 19);
        deleteFlightButton.setOnClickListener(deleteFlight(flightTicket.get(0)));
        deleteFlightButton.setBackgroundColor(view.getResources().getColor(R.color.blue));
        deleteFlightButton.setTextColor(view.getResources().getColor(R.color.white));
        deleteFlightButton.setPadding(0, 10, 0, 10);
        tableLayout.addView(deleteFlightButton);

        return tableLayout;
    }

    private TableLayout createHotelTable(ArrayList<String> bookingInfo) {
        ArrayList<String> hotelData = travelCompanyDBHelper.getHotelRecords(TravelCompanyContract.HotelEntry._ID + " LIKE ?", new String[]{bookingInfo.get(3)}).get(0);
        TableLayout tableLayout = HotelsViewBuilder.createTableInfo(view, hotelData);
        TableLayout.LayoutParams adultsNumberLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        adultsNumberLayoutParams.setMargins(0, 50, 0, 0);
        TextView adultsNumberView = HotelsViewBuilder.createTableColumn(view, adultsNumberLayoutParams, "Number of adults: " + bookingInfo.get(4), 18, Integer.parseInt(hotelData.get(0)) * 10000 + 10);
        tableLayout.addView(adultsNumberView);
        TableLayout.LayoutParams kidsNumberLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        kidsNumberLayoutParams.setMargins(0, 20, 0, 0);
        TextView kidsNumberView = HotelsViewBuilder.createTableColumn(view, kidsNumberLayoutParams, "Number of kids: " + bookingInfo.get(5), 18, Integer.parseInt(hotelData.get(0)) * 10000 + 11);
        tableLayout.addView(kidsNumberView);

        TableLayout.LayoutParams arrivalDateLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        arrivalDateLayoutParams.setMargins(0, 50, 0, 0);
        TextView arrivalDateNumberView = HotelsViewBuilder.createTableColumn(view, arrivalDateLayoutParams, "Arrival date: " + bookingInfo.get(1), 18, Integer.parseInt(hotelData.get(0)) * 10000 + 12);
        tableLayout.addView(arrivalDateNumberView);
        TableLayout.LayoutParams departureDateLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        departureDateLayoutParams.setMargins(0, 20, 0, 0);
        TextView departureDateNumberView = HotelsViewBuilder.createTableColumn(view, departureDateLayoutParams, "Departure date: " + bookingInfo.get(2), 18, Integer.parseInt(hotelData.get(0)) * 10000 + 13);
        tableLayout.addView(departureDateNumberView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar arrivalCalendar = Calendar.getInstance();
        Calendar departureCalendar = Calendar.getInstance();
        Calendar difference = Calendar.getInstance();
        Integer numberOfNights = 0;
        try {
            arrivalCalendar.setTime(dateFormat.parse(bookingInfo.get(1)));
            departureCalendar.setTime(dateFormat.parse(bookingInfo.get(2)));
            difference.setTimeInMillis(arrivalCalendar.getTimeInMillis() - departureCalendar.getTimeInMillis());
            numberOfNights = difference.get(Calendar.DAY_OF_YEAR) - 1;
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        Double totalCost = (Double.valueOf(bookingInfo.get(4)) + Double.valueOf(bookingInfo.get(5)) / 2) * numberOfNights * Integer.valueOf(hotelData.get(4));

        TableLayout.LayoutParams numberOfNightsLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        numberOfNightsLayoutParams.setMargins(0, 20, 0, 0);
        TextView numberOfNightsView = HotelsViewBuilder.createTableColumn(view, departureDateLayoutParams, "Number of nights: " + numberOfNights, 18, Integer.parseInt(hotelData.get(0)) * 10000 + 14);
        tableLayout.addView(numberOfNightsView);

        TableLayout.LayoutParams totalCostLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        totalCostLayoutParams.setMargins(0, 50, 0, 20);
        TextView totalCostView = HotelsViewBuilder.createTableColumn(view, totalCostLayoutParams, "Total cost for room: " + totalCost + " $", 24, Integer.parseInt(hotelData.get(0)) * 10000 + 15);
        totalCostView.setTextColor(view.getResources().getColor(R.color.dark_blue));
        tableLayout.addView(totalCostView);

        Button deleteHotelButton = new Button(view.getContext());
        TableLayout.LayoutParams deleteHotelButtonLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        deleteHotelButtonLayoutParams.setMargins(0, 20, 0, 30);
        deleteHotelButton.setLayoutParams(deleteHotelButtonLayoutParams);
        deleteHotelButton.setText("Cancel booking");
        deleteHotelButton.setId(Integer.parseInt(hotelData.get(0)) * 10000 + 16);
        deleteHotelButton.setBackgroundColor(view.getResources().getColor(R.color.blue));
        deleteHotelButton.setTextColor(view.getResources().getColor(R.color.white));
        deleteHotelButton.setPadding(0, 10, 0, 10);
        deleteHotelButton.setOnClickListener(deleteRoom(bookingInfo.get(0)));
        tableLayout.addView(deleteHotelButton);

        return tableLayout;
    }

    private View.OnClickListener deleteRoom(String rowId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = TravelCompanyContract.AccountHotelEntry._ID + " LIKE ?";
                String[] selectionArgs = {rowId};
                SQLiteDatabase database = travelCompanyDBHelper.getWritableDatabase();
                database.delete(TravelCompanyContract.AccountHotelEntry.TABLE_NAME, selection, selectionArgs);
                createView(null, null);
            }
        };
    }

    private View.OnClickListener deleteFlight(String rowId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = TravelCompanyContract.AccountFlightEntry._ID + " LIKE ?";
                String[] selectionArgs = {rowId};
                SQLiteDatabase database = travelCompanyDBHelper.getWritableDatabase();
                database.delete(TravelCompanyContract.AccountFlightEntry.TABLE_NAME, selection, selectionArgs);
                createView(null, null);
            }
        };
    }

    @Override
    public void onClick(View view) {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accountName", name);
        editor.putString("accountEmail", email);
        editor.commit();
        createAlert("Account info", "Your data was successfully saved");
    }

    private void createAlert(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

        alert.setTitle(title);
        alert.setMessage(message);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }
}
