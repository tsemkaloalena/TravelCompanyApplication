package com.example.travelcompanyapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.travelcompanyapplication.db_controller.TravelCompanyContract;
import com.example.travelcompanyapplication.db_controller.TravelCompanyDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public class FlightsViewBuilder implements View.OnClickListener {
    private TravelCompanyDBHelper travelCompanyDBHelper;
    private LayoutInflater inflater;
    private ViewGroup container;
    private EditText departureDateEditText;
    private Spinner departureCityDropdown;
    private Spinner arrivalCityDropdown;
    private TextView numberOfTicketsTextView;
    private SeekBar numberOfTicketsBar;
    private Button findFlightsButton;
    private Button clearFiltersButton;
    private static View view;
    private LinearLayout layout;
    ArrayList<String> departureCities = new ArrayList<String>();
    ArrayList<String> arrivalCities = new ArrayList<String>();

    public FlightsViewBuilder(TravelCompanyDBHelper travelCompanyDBHelper, LayoutInflater inflater, ViewGroup container) {
        this.travelCompanyDBHelper = travelCompanyDBHelper;
        this.inflater = inflater;
        this.container = container;
        if (view == null) {
            createView(null, null, false);
        }
    }

    public View getView() {
        return view;
    }

    private View changeView() {
        String departureCity = departureCityDropdown.getSelectedItem().toString();
        String arrivalCity = arrivalCityDropdown.getSelectedItem().toString();
        String departureDate = departureDateEditText.getText().toString();
        Integer numberOfTickets = numberOfTicketsBar.getProgress();

        ArrayList<String> selection = new ArrayList<>();
        ArrayList<String> selectionArgs = new ArrayList<>();
        if (!("Departure city".equals(departureCity))) {
            selection.add(TravelCompanyContract.FlightEntry.DEPARTURE_CITY + "=?");
            selectionArgs.add(departureCity);
        }
        if (!("Arrival city".equals(arrivalCity))) {
            selection.add(TravelCompanyContract.FlightEntry.ARRIVAL_CITY + "=?");
            selectionArgs.add(arrivalCity);
        }
        if (!("".equals(departureDate))) {
            selection.add(TravelCompanyContract.FlightEntry.DEPARTURE_DATE + " LIKE ?");
            selectionArgs.add("%" + departureDate + "%");
        }

        view = createView(String.join(" and ", selection), selectionArgs.toArray(new String[0]), true);
        return view;
    }

    private View createView(String selection, String[] selectionArgs, Boolean update) {
        if (!update) {
            view = inflater.inflate(R.layout.fragment_flights, container, false);
            layout = (LinearLayout) view.findViewById(R.id.flights_container);
        } else {
            layout.removeAllViewsInLayout();
        }

        ArrayList<ArrayList<String>> data = travelCompanyDBHelper.getFlightRecords(selection, selectionArgs);

        for (ArrayList<String> row : data) {
            TableLayout tableLayout = createTableInfo(view, row);

            Button ticketBuyButton = new Button(view.getContext());
            TableLayout.LayoutParams ticketBuyButtonLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            ticketBuyButtonLayoutParams.setMargins(0, 20, 0, 30);
            ticketBuyButton.setLayoutParams(ticketBuyButtonLayoutParams);
            ticketBuyButton.setText("Buy ticket");
            ticketBuyButton.setPadding(0, 15, 0, 15);
            ticketBuyButton.setBackgroundColor(view.getResources().getColor(R.color.dark_blue));

            ticketBuyButton.setTextColor(view.getResources().getColor(R.color.white));
            ticketBuyButton.setId(Integer.parseInt(row.get(0)) * 10000 + 9);
            ticketBuyButton.setOnClickListener(buyTicket(row.get(0)));
            tableLayout.addView(ticketBuyButton);

            View tableDivider = new View(view.getContext());
            tableDivider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
            tableDivider.setBackgroundColor(view.getResources().getColor(R.color.dark_blue));

            if (!update) {
                departureCities.add(row.get(1));
                arrivalCities.add(row.get(2));
            }

            layout.addView(tableLayout);
            layout.addView(tableDivider);
        }

        departureCities = new ArrayList<>(new HashSet<String>(departureCities));
        arrivalCities = new ArrayList<>(new HashSet<String>(arrivalCities));
        departureCities.add(0, "Departure city");
        arrivalCities.add(0, "Arrival city");
        if (!update) {
            setupMenu();
        }
        return view;
    }

    public static TableLayout createTableInfo(View view, ArrayList<String> row) {
        TableLayout tableLayout = new TableLayout(view.getContext());
        LinearLayout.LayoutParams tableLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tableLayoutParams.setMargins(0, 20, 0, 20);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setLayoutParams(tableLayoutParams);

        TableRow dateRow = new TableRow(view.getContext());
        dateRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        TableRow.LayoutParams departureDateLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
        TextView departureDateView = createTableColumn(view, departureDateLayoutParams, row.get(3).split(" ")[0], 18, Integer.parseInt(row.get(0)) * 10000 + 1);
        TableRow.LayoutParams arrivalDateLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
        TextView arrivalDateView = createTableColumn(view, arrivalDateLayoutParams, row.get(4).split(" ")[0], 18, Integer.parseInt(row.get(0)) * 10000 + 2);
        dateRow.addView(departureDateView);
        dateRow.addView(arrivalDateView);
        tableLayout.addView(dateRow);

        TableRow timeRow = new TableRow(view.getContext());
        timeRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        TableRow.LayoutParams departureTimeLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
        TextView departureTimeView = createTableColumn(view, departureTimeLayoutParams, row.get(3).split(" ")[1], 34, Integer.parseInt(row.get(0)) * 10000 + 3);
        TableRow.LayoutParams arrivalTimeLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
        TextView arrivalTimeView = createTableColumn(view, arrivalTimeLayoutParams, row.get(4).split(" ")[1], 34, Integer.parseInt(row.get(0)) * 10000 + 4);
        timeRow.addView(departureTimeView);
        timeRow.addView(arrivalTimeView);
        tableLayout.addView(timeRow);

        TableRow cityRow = new TableRow(view.getContext());
        TableLayout.LayoutParams cityRowLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        cityRowLayoutParams.setMargins(0, 20, 0, 0);
        cityRow.setLayoutParams(cityRowLayoutParams);
        TableRow.LayoutParams departureCityLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
        TextView departureCityView = createTableColumn(view, departureCityLayoutParams, row.get(1), 18, Integer.parseInt(row.get(0)) * 10000 + 5);
        TableRow.LayoutParams arrivalCityLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
        TextView arrivalCityView = createTableColumn(view, arrivalCityLayoutParams, row.get(2), 18, Integer.parseInt(row.get(0)) * 10000 + 6);
        cityRow.addView(departureCityView);
        cityRow.addView(arrivalCityView);
        tableLayout.addView(cityRow);

        TableRow costRow = new TableRow(view.getContext());
        TableLayout.LayoutParams costRowLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        costRowLayoutParams.setMargins(0, 40, 0, 0);
        costRow.setLayoutParams(costRowLayoutParams);
        TableRow.LayoutParams costLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TextView costView = createTableColumn(view, costLayoutParams, Integer.valueOf(row.get(6).replace(" ", "")) / 60 + " $", 28, Integer.parseInt(row.get(0)) * 10000 + 7);
        costRow.addView(costView);
        tableLayout.addView(costRow);

        TableRow airlineRow = new TableRow(view.getContext());
        TableLayout.LayoutParams airlineRowLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        airlineRowLayoutParams.setMargins(0, 20, 0, 0);
        airlineRow.setLayoutParams(airlineRowLayoutParams);
        TableRow.LayoutParams airlineLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TextView airlineView = createTableColumn(view, airlineLayoutParams, row.get(5), 18, Integer.parseInt(row.get(0)) * 10000 + 8);
        airlineRow.addView(airlineView);
        tableLayout.addView(airlineRow);

        return tableLayout;
    }

    public static TextView createTableColumn(View view, TableRow.LayoutParams layoutParams, String text, Integer textSize, Integer id) {
        TextView textView = new TextView(view.getContext());
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setId(id);
        return textView;
    }

    private void setupMenu() {
        departureCityDropdown = view.findViewById(R.id.airport_departure_city_spinner);
        ArrayAdapter<String> departureCityAdapter = createSpinnerAdapter(departureCities.toArray(new String[0]));
        departureCityDropdown.setAdapter(departureCityAdapter);
        arrivalCityDropdown = view.findViewById(R.id.airport_arrival_city_spinner);
        ArrayAdapter<String> arrivalCityAdapter = createSpinnerAdapter(arrivalCities.toArray(new String[0]));
        arrivalCityDropdown.setAdapter(arrivalCityAdapter);

        departureDateEditText = (EditText) view.findViewById(R.id.airport_departure_date_text_edit);
        setupDateDialog(departureDateEditText);

        numberOfTicketsTextView = (TextView) view.findViewById(R.id.airport_number_of_tickets_text_view);
        numberOfTicketsBar = (SeekBar) view.findViewById(R.id.airport_airport_number_of_tickets_seek_bar);
        numberOfTicketsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (numberOfTicketsBar.getProgress() == 0) {
                    numberOfTicketsBar.setProgress(1);
                    progress = 1;
                }
                numberOfTicketsTextView.setText("Number of tickets: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        findFlightsButton = (Button) view.findViewById(R.id.find_flights_button);
        findFlightsButton.setOnClickListener(this);
        clearFiltersButton = (Button) view.findViewById(R.id.airport_clear_filters_button);
        clearFiltersButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_flights_button:
                this.view = changeView();
                break;
            case R.id.airport_clear_filters_button:
                createView(null, null, true);
                setupMenu();
                departureDateEditText.setText("");
                numberOfTicketsBar.setProgress(1);
                numberOfTicketsTextView.setText("Number of tickets: 1");
                break;
        }
    }

    private View.OnClickListener buyTicket(String ticketId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer numberOfTickets = numberOfTicketsBar.getProgress();

                SQLiteDatabase database = travelCompanyDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TravelCompanyContract.AccountFlightEntry.FLIGHT_ID, ticketId);
                values.put(TravelCompanyContract.AccountFlightEntry.TICKETS_NUMBER, numberOfTickets);

                long newRowId = database.insert(
                        TravelCompanyContract.AccountFlightEntry.TABLE_NAME,
                        null,
                        values);
                createAlert("Ticket", "The ticket was successfully bought");
            }
        };
    }

    private ArrayAdapter<String> createSpinnerAdapter(String[] listItems) {
        return new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, listItems) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
    }

    private void setupDateDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                editText.setText(dateFormat.format(calendar.getTime()));
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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
