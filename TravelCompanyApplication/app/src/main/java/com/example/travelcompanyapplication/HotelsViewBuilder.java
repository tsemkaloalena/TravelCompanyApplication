package com.example.travelcompanyapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.travelcompanyapplication.db_controller.TravelCompanyContract;
import com.example.travelcompanyapplication.db_controller.TravelCompanyDBHelper;
import com.example.travelcompanyapplication.ui.DownloadImageFromInternet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HotelsViewBuilder implements View.OnClickListener {
    private TravelCompanyDBHelper travelCompanyDBHelper;
    private LayoutInflater inflater;
    private ViewGroup container;
    private static View view;
    private LinearLayout layout;
    private ArrayList<String> cities = new ArrayList<String>();
    private Spinner cityDropdown;
    private EditText departureDateEditText;
    private EditText arrivalDateEditText;
    private CheckBox hotelStarsCheckbox0;
    private CheckBox hotelStarsCheckbox1;
    private CheckBox hotelStarsCheckbox2;
    private CheckBox hotelStarsCheckbox3;
    private CheckBox hotelStarsCheckbox4;
    private CheckBox hotelStarsCheckbox5;
    private EditText adultsNumberEditText;
    private EditText kidsNumberEditText;
    private Button findHotelsButton;
    private Button clearFiltersButton;

    public HotelsViewBuilder(TravelCompanyDBHelper travelCompanyDBHelper, LayoutInflater inflater, ViewGroup container) {
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
        String city = cityDropdown.getSelectedItem().toString();

        ArrayList<String> selection = new ArrayList<>();
        ArrayList<String> selectionStars = new ArrayList<>();
        ArrayList<String> selectionArgs = new ArrayList<>();
        if (hotelStarsCheckbox0.isChecked()) {
            selectionArgs.add("0");
        }
        if (hotelStarsCheckbox1.isChecked()) {
            selectionArgs.add("1");
        }
        if (hotelStarsCheckbox2.isChecked()) {
            selectionArgs.add("2");
        }
        if (hotelStarsCheckbox3.isChecked()) {
            selectionArgs.add("3");
        }
        if (hotelStarsCheckbox4.isChecked()) {
            selectionArgs.add("4");
        }
        if (hotelStarsCheckbox5.isChecked()) {
            selectionArgs.add("5");
        }
        for (int i = 0; i < selectionArgs.size(); i++) {
            selectionStars.add(TravelCompanyContract.HotelEntry.RATING + "=?");
        }
        if (selectionStars.size() > 0) {
            selection.add("(" + String.join(" or ", selectionStars) + ")");
        }

        if (!("City".equals(city))) {
            selection.add(TravelCompanyContract.HotelEntry.CITY + "=?");
            selectionArgs.add(city);
        }

        view = createView(String.join(" and ", selection), selectionArgs.toArray(new String[0]), true);
        return view;
    }

    private View createView(String selection, String[] selectionArgs, Boolean update) {
        if (!update) {
            view = inflater.inflate(R.layout.fragment_hotels, container, false);
            layout = (LinearLayout) view.findViewById(R.id.hotels_container);
        } else {
            layout.removeAllViewsInLayout();
        }

        ArrayList<ArrayList<String>> data = travelCompanyDBHelper.getHotelRecords(selection, selectionArgs);
        for (ArrayList<String> row : data) {
            TableLayout tableLayout = createTableInfo(view, row);

            Button bookHotelButton = new Button(view.getContext());
            TableLayout.LayoutParams bookHotelButtonLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            bookHotelButtonLayoutParams.setMargins(0, 20, 0, 30);
            bookHotelButton.setLayoutParams(bookHotelButtonLayoutParams);
            bookHotelButton.setText("Book room");
            bookHotelButton.setId(Integer.parseInt(row.get(0)) * 10000 + 7);
            bookHotelButton.setOnClickListener(bookRoom(row.get(0)));
            bookHotelButton.setPadding(0, 10, 0, 10);
            bookHotelButton.setBackgroundColor(view.getResources().getColor(R.color.yellow));
            tableLayout.addView(bookHotelButton);

            View tableDivider = new View(view.getContext());
            tableDivider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
            tableDivider.setBackgroundColor(view.getResources().getColor(R.color.dark_blue));

            layout.addView(tableLayout);
            layout.addView(tableDivider);

            if (!update && !(cities.contains(row.get(1)))) {
                cities.add(row.get(1));
            }
        }
        if (!update) {
            cities.add(0, "City");
            setupMenu();
        }
        return view;
    }

    public static TableLayout createTableInfo(View view, ArrayList<String> row) {
        TableLayout tableLayout = new TableLayout(view.getContext());
        LinearLayout.LayoutParams tableLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tableLayoutParams.setMargins(0, 20, 0, 20);
        tableLayout.setLayoutParams(tableLayoutParams);

        TableRow hotelNameRow = new TableRow(view.getContext());
        TableLayout.LayoutParams hotelNameRowLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        hotelNameRowLayoutParams.setMargins(0, 50, 0, 20);
        hotelNameRow.setLayoutParams(hotelNameRowLayoutParams);
        TableRow.LayoutParams hotelNameLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
        TextView hotelNameView = createTableColumn(view, hotelNameLayoutParams, row.get(2), 24, Integer.parseInt(row.get(0)) * 10000 + 1);
        hotelNameRow.addView(hotelNameView);
        tableLayout.addView(hotelNameRow);

        TableRow hotelStarBarRow = new TableRow(view.getContext());
        TableLayout.LayoutParams hotelStarBarRowLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        hotelStarBarRow.setLayoutParams(hotelStarBarRowLayoutParams);
        TableRow.LayoutParams hotelStarBarLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0);
        RatingBar hotelStarBar = new RatingBar(view.getContext(), null, android.R.attr.ratingBarStyleSmall);
        hotelStarBar.setLayoutParams(hotelStarBarLayoutParams);
        hotelStarBar.setId(Integer.parseInt(row.get(0)) * 10000 + 2);
        hotelStarBar.setNumStars(5);
        hotelStarBar.setRating(Integer.parseInt(row.get(5)));
        hotelStarBar.setIsIndicator(true);
        hotelStarBarRow.addView(hotelStarBar);
        tableLayout.addView(hotelStarBarRow);

        TableRow cityRow = new TableRow(view.getContext());
        TableLayout.LayoutParams cityRowLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1);
        cityRow.setLayoutParams(cityRowLayoutParams);
        TableRow.LayoutParams cityLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1);
        TextView cityView = createTableColumn(view, cityLayoutParams, row.get(1), 18, Integer.parseInt(row.get(0)) * 10000 + 3);
        TableRow.LayoutParams mapLinkLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1);
        Button linkMapButton = new Button(view.getContext());
        linkMapButton.setLayoutParams(mapLinkLayoutParams);
        linkMapButton.setText("Open map");
        linkMapButton.setId(Integer.parseInt(row.get(0)) * 10000 + 4);
        linkMapButton.setPadding(0, 10, 0, 10);
        linkMapButton.setBackgroundColor(view.getResources().getColor(R.color.dark_blue));
        linkMapButton.setTextColor(view.getResources().getColor(R.color.white));
        linkMapButton.setOnClickListener(openMap(view, row.get(3)));
        cityRow.addView(cityView);
        cityRow.addView(linkMapButton);
        tableLayout.addView(cityRow);

        TableLayout.LayoutParams hotelImageLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1);
        hotelImageLayoutParams.setMargins(0, 40, 0, 40);
        ImageView hotelImageView = new ImageView(view.getContext());
        hotelImageView.setLayoutParams(hotelImageLayoutParams);
        hotelImageView.setAdjustViewBounds(true);
        new DownloadImageFromInternet(hotelImageView, view.getContext()).execute(row.get(6));
        tableLayout.addView(hotelImageView);

        TableRow.LayoutParams costLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TextView costView = createTableColumn(view, costLayoutParams, row.get(4) + " $ per night", 24, Integer.parseInt(row.get(0)) * 10000 + 6);
        tableLayout.addView(costView);

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

    public static TextView createTableColumn(View view, TableLayout.LayoutParams layoutParams, String text, Integer textSize, Integer id) {
        TextView textView = new TextView(view.getContext());
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setId(id);
        return textView;
    }

    private void setupMenu() {
        cityDropdown = view.findViewById(R.id.hotel_city_spinner);
        ArrayAdapter<String> cityAdapter = createSpinnerAdapter(cities.toArray(new String[0]));
        cityDropdown.setAdapter(cityAdapter);

        departureDateEditText = (EditText) view.findViewById(R.id.hotel_departure_date_text_edit);
        arrivalDateEditText = (EditText) view.findViewById(R.id.hotel_arrival_date_text_edit);
        setupDateDialog(departureDateEditText);
        setupDateDialog(arrivalDateEditText);

        hotelStarsCheckbox0 = (CheckBox) view.findViewById(R.id.star_check_box_0);
        hotelStarsCheckbox1 = (CheckBox) view.findViewById(R.id.star_check_box_1);
        hotelStarsCheckbox2 = (CheckBox) view.findViewById(R.id.star_check_box_2);
        hotelStarsCheckbox3 = (CheckBox) view.findViewById(R.id.star_check_box_3);
        hotelStarsCheckbox4 = (CheckBox) view.findViewById(R.id.star_check_box_4);
        hotelStarsCheckbox5 = (CheckBox) view.findViewById(R.id.star_check_box_5);

        adultsNumberEditText = (EditText) view.findViewById(R.id.adults_number_edit_text);
        kidsNumberEditText = (EditText) view.findViewById(R.id.kids_number_edit_text);

        findHotelsButton = (Button) view.findViewById(R.id.find_hotels_button);
        findHotelsButton.setOnClickListener(this);
        clearFiltersButton = (Button) view.findViewById(R.id.hotel_clear_filters_button);
        clearFiltersButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_hotels_button:
                this.view = changeView();
                break;
            case R.id.hotel_clear_filters_button:
                createView(null, null, true);
                setupMenu();
                departureDateEditText.setText("");
                arrivalDateEditText.setText("");
                hotelStarsCheckbox0.setChecked(false);
                hotelStarsCheckbox1.setChecked(false);
                hotelStarsCheckbox2.setChecked(false);
                hotelStarsCheckbox3.setChecked(false);
                hotelStarsCheckbox4.setChecked(false);
                hotelStarsCheckbox5.setChecked(false);
                adultsNumberEditText.setText("");
                kidsNumberEditText.setText("");
                break;
        }
    }

    private View.OnClickListener bookRoom(String hotelId) {
        Boolean writingAllowed = false;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String departureDate = departureDateEditText.getText().toString();
                String arrivalDate = arrivalDateEditText.getText().toString();

                if ("".equals(departureDate)) {
                    createAlert("Empty field", "Departure date is not set");
                    return;
                }
                if ("".equals(arrivalDate)) {
                    createAlert("Empty field", "Arrival date is not set");
                    return;
                }
                if ("0".equals(adultsNumberEditText.getText().toString())) {
                    createAlert("Wrong data", "Number of adults can not be 0");
                    return;
                }

                Integer numberOfAdults;
                if ("".equals(adultsNumberEditText.getText().toString())) {
                    numberOfAdults = 2;
                } else {
                    numberOfAdults = Integer.parseInt(adultsNumberEditText.getText().toString());
                }
                Integer numberOfKids;
                if ("".equals(kidsNumberEditText.getText().toString())) {
                    numberOfKids = 0;
                } else {
                    numberOfKids = Integer.parseInt(kidsNumberEditText.getText().toString());
                }

                SQLiteDatabase database = travelCompanyDBHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TravelCompanyContract.AccountHotelEntry.DEPARTURE_DATE, departureDate);
                values.put(TravelCompanyContract.AccountHotelEntry.ARRIVAL_DATE, arrivalDate);
                values.put(TravelCompanyContract.AccountHotelEntry.HOTEL_ID, hotelId);
                values.put(TravelCompanyContract.AccountHotelEntry.ADULTS_NUMBER, numberOfAdults);
                values.put(TravelCompanyContract.AccountHotelEntry.KIDS_NUMBER, numberOfKids);

                long newRowId = database.insert(
                        TravelCompanyContract.AccountHotelEntry.TABLE_NAME,
                        null,
                        values);
                createAlert("Booking", "The hotel was successfully booked");
            }
        };
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

    public static View.OnClickListener openMap(View view, String mapLink) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri location = Uri.parse(mapLink);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                view.getContext().startActivity(mapIntent);
            }
        };
    }
}
