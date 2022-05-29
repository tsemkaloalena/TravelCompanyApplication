package com.example.travelcompanyapplication.db_controller;

import android.provider.BaseColumns;

public class TravelCompanyContract {
    public static abstract class AccountFlightEntry implements BaseColumns {
        public static final String TABLE_NAME = "account_flight";
        public static final String FLIGHT_ID = "flight_id";
        public static final String TICKETS_NUMBER = "tickets_number";
    }

    public static abstract class AccountHotelEntry implements BaseColumns {
        public static final String TABLE_NAME = "account_hotels";
        public static final String DEPARTURE_DATE = "departure_date";
        public static final String ARRIVAL_DATE = "arrival_date";
        public static final String HOTEL_ID = "hotel_id";
        public static final String ADULTS_NUMBER = "adults_number";
        public static final String KIDS_NUMBER = "kids_number";
    }

    public static abstract class FlightEntry implements BaseColumns {
        public static final String TABLE_NAME = "flights";
        public static final String DEPARTURE_CITY = "departure_city";
        public static final String ARRIVAL_CITY = "arrival_city";
        public static final String DEPARTURE_DATE = "departure_date";
        public static final String ARRIVAL_DATE = "arrival_date";
        public static final String AIRLINE = "airline";
        public static final String COST = "cost";
    }

    public static abstract class GuideEntry implements BaseColumns {
        public static final String TABLE_NAME = "city_description";
        public static final String CITY_TITLE = "city_title";
        public static final String DESCRIPTION = "description";
        public static final String LINK = "link";
    }

    public static abstract class HotelEntry implements BaseColumns {
        public static final String TABLE_NAME = "hotels";
        public static final String CITY = "city";
        public static final String HOTEL_NAME = "hotel_name";
        public static final String MAP_LINK = "map_link";
        public static final String COST = "cost";
        public static final String RATING = "rating";
        public static final String IMAGE_URL = "image_url";
    }
}
