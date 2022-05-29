package com.example.travelcompanyapplication.db_controller;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class TravelCompanyDBFiller {
    private TravelCompanyDBHelper travelCompanyDBHelper;
    private String[][] hotelsData = {
            {"Paris, France", "Avia Hôtel Saphir Montparnasse", "https://goo.gl/maps/kKMiP9qUYV28AyhF9", "197", "3", "https://media-cdn.tripadvisor.com/media/photo-s/07/0d/c4/c8/avia-montparnasse-hotel.jpg"},
            {"Paris, France", "Novotel Suites Paris Montreuil Vincennes", "https://goo.gl/maps/dcLZ5d9fH4B6pHGMA", "279", "4", "https://media-cdn.tripadvisor.com/media/photo-s/1c/d4/43/4c/exterior-view.jpg"},
            {"Paris, France", "L'Empire Paris", "https://goo.gl/maps/SQYHbX3JTzAAQAhe6", "325", "4", "https://media-cdn.tripadvisor.com/media/photo-s/04/94/89/33/l-empire-paris.jpg"},
            {"Paris, France", "Hotel Scarlett", "https://goo.gl/maps/D487yEi4keYnwkLPA", "237", "3", "https://media-cdn.tripadvisor.com/media/photo-s/0b/97/1f/f3/hotel-scarlett.jpg"},
            {"Rome, Italy", "Hotel Relais Dei Papi", "https://goo.gl/maps/UMkKyr5zy8T7T8Gm7", "387", "3", "https://dynamic-media-cdn.tripadvisor.com/media/photo-s/02/e8/48/dc/relais-dei-papi.jpg?w=600&h=-1&s=1"},
            {"Rome, Italy", "Hotel Borromeo", "https://goo.gl/maps/dvTXs4JhC6vLjbyV8", "465", "3", "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/02/47/bb/1d/borromeo-hotel.jpg?w=1000&h=-1&s=1"},
            {"Rome, Italy", "Rome Central Rooms", "https://g.page/RomeCentralRooms?share", "124", "4", "https://media-cdn.tripadvisor.com/media/photo-m/1280/15/60/a4/21/pantheon-room-camera.jpg"},
            {"Rome, Italy", "Hotel Pacific", "https://g.page/PacificHotelRoma?share", "149", "3", "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1c/f8/47/26/hotel-pacific-roma.jpg?w=1000&h=-1&s=1"},
            {"Moscow, Russia", "Отель St. Regis", "https://g.page/the-st--regis-moscow-nikolskaya?share", "733", "5", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/194428535.jpg?k=1ce1d4d1e8729f328f35f5975ab8144742e87fea8b11bbc6414a011133da599b&o=&hp=1"},
            {"Moscow, Russia", "Отель Mercure", "https://goo.gl/maps/6ACa6yZ7MRCMVqGi7", "61", "4", "https://media-cdn.tripadvisor.com/media/photo-s/1c/d9/90/a6/exterior-view.jpg"},
            {"Moscow, Russia", "Гостиница Метрополь", "https://goo.gl/maps/Wk3rFEAsNuoD1PfLA", "408", "5", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/335205641.jpg?k=2308fba2d7e1185d3e7e8d22c8ba206a81d196e90bbc901a197e6b0d2577c994&o=&hp=1"},
            {"Moscow, Russia", "Отель Ibis", "https://g.page/ibis-moscow-kievskaya?share", "51", "3", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/252335333.jpg?k=87b1533553c64acee2d5aacbf0e93dcd1726c170ccb9980f0a97de64090621f6&o=&hp=1"},
            {"Saint Petersburg, Russia", "Отель Гельвеция", "https://goo.gl/maps/Noy8X7w7uj9xsxg57", "262", "5", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/278534132.jpg?k=574c2e2c1430993260f6408ff8e65f7446db607df38da0bc0b6d3d266aa86d6b&o=&hp=1"},
            {"Saint Petersburg, Russia", "Отель Пушка ИНН", "https://goo.gl/maps/ivt13d1HTe9erCtZ6", "174", "4", "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/0e/a0/f7/58/pushka-inn-hotel.jpg?w=900&h=-1&s=1"},
            {"Saint Petersburg, Russia", "Домина Санкт-Петербург", "https://goo.gl/maps/qd9QHkQjC1QJk4q97", "185", "5", "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1d/0b/8c/13/caption.jpg?w=900&h=-1&s=1"},
            {"Saint Petersburg, Russia", "Мери Отель", "https://goo.gl/maps/EZJ1X3KHh65qtzao6", "60", "3", "https://www.maryhotelgroup.com/upload/iblock/99e/99ed06ded31dd886c78fc6e15251e7c2.jpg"},
            {"Saint Petersburg, Russia", "The Faces Petrogradskay", "https://goo.gl/maps/QBbR3sCaw1nY5aiq9", "85", "3", "https://media-cdn.tripadvisor.com/media/photo-s/10/bd/e2/59/the-faces.jpg"},
            {"Tomsk, Russia", "Гостиница Эдем", "https://goo.gl/maps/ZfPGvhXZ3XyeyjVB8", "21", "3", "https://media-cdn.tripadvisor.com/media/photo-s/05/18/8e/9c/caption.jpg"},
            {"Tomsk, Russia", "Кухтерин Отель", "https://g.page/kuhterinhotel?share", "108", "5", "https://media-cdn.tripadvisor.com/media/photo-m/1280/1c/97/a1/48/central-hall.jpg"},
            {"Tomsk, Russia", "Рубин конгресс-центр", "https://goo.gl/maps/dVVBWZscZKkxsgeR7", "25", "3", "https://rubin.tomsk.ru/wp-content/uploads/2018/03/2.jpg"},
            {"Tomsk, Russia", "Ксандер Отель", "https://goo.gl/maps/8G3HhzUf3SU5p5WX8", "85", "5", "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1b/32/a1/6e/xander-hotel-tomsk.jpg?w=900&h=-1&s=1"},
            {"Tomsk, Russia", "Тоян", "https://goo.gl/maps/Yz7P8RZ7ZdxepKFx5", "108", "4", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/54266014.jpg?k=be09703e978c9ba03cf5ce9c35238b269e846077e6da2f06ff95d524e2365c59&o=&hp=1"},
            {"Tomsk, Russia", "Сибирь", "https://goo.gl/maps/PLkdny1UYDgBAtMW7", "60", "3", "https://media-cdn.tripadvisor.com/media/photo-s/0e/f6/72/6f/caption.jpg"},
            {"Tomsk, Russia", "Гостиница Томск", "https://goo.gl/maps/Vz6kNsDY2jwQ4RB68", "49", "3", "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/07/ab/a0/cc/caption.jpg?w=1200&h=-1&s=1"},
            {"Tomsk, Russia", "Отель «Элегант»", "https://goo.gl/maps/jZRPhJT2sCHLmPj29", "55", "4", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/40507938.jpg?k=5c7fa5651d530f36729d54974c6efdaeabce216d27892c3183473c18ecd6368b&o=&hp=1"},
            {"Tomsk, Russia", "Спорт-Отель", "https://goo.gl/maps/tpd9L94JqQr5usGE6", "49", "3", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/73276179.jpg?k=aeeaf9bd785b4612dede5a22ed72ea985e33c267963f028a94a8f83e15daad2c&o=&hp=1"},
            {"Tomsk, Russia", "Магистрат", "https://goo.gl/maps/Pgu4RsQWmVVtzBhh6", "102", "4", "https://cf.bstatic.com/xdata/images/hotel/max1024x768/277859912.jpg?k=a0355af948f49d336deceea0ae5d6d881daef3884b2bcbdec47cf67218a17fdb&o=&hp=1"},
    };

    private String[][] flightsData = {
            {"Moscow, Russia", "Paris, France", "23.08.2022 02:08", "23.08.2022 05:08", "Transavia", "4 555"},
            {"Moscow, Russia", "Paris, France", "23.08.2022 10:08", "23.08.2022 13:28", "Air France", "3 559"},
            {"Moscow, Russia", "Paris, France", "15.08.2022 15:37", "15.08.2022 18:48", "S7", "2 558"},
            {"Moscow, Russia", "Paris, France", "15.04.2022 15:37", "15.04.2022 18:48", "S7", "4 198"},
            {"Paris, France", "Moscow, Russia", "23.09.2022 02:08", "23.09.2022 05:08", "Transavia", "4 555"},
            {"Paris, France", "Moscow, Russia", "23.09.2022 10:08", "23.09.2022 13:28", "Air France", "3 559"},
            {"Paris, France", "Moscow, Russia", "15.09.2022 15:37", "15.09.2022 18:48", "S7", "2 558"},
            {"Paris, France", "Moscow, Russia", "15.05.2022 15:37", "15.05.2022 18:48", "S7", "4 198"},
            {"Saint Petersburg, Russia", "Paris, France", "24.09.2022 02:08", "24.09.2022 05:08", "Transavia", "5 169"},
            {"Saint Petersburg, Russia", "Paris, France", "24.08.2022 10:08", "24.08.2022 13:28", "Air France", "3 559"},
            {"Saint Petersburg, Russia", "Paris, France", "17.08.2022 15:37", "17.08.2022 18:48", "S7", "5 100"},
            {"Paris, France", "Saint Petersburg, Russia", "24.10.2022 02:08", "24.10.2022 05:08", "Transavia", "5 169"},
            {"Paris, France", "Saint Petersburg, Russia", "24.09.2022 10:08", "24.09.2022 13:28", "Air France", "3 559"},
            {"Paris, France", "Saint Petersburg, Russia", "17.09.2022 15:37", "17.09.2022 18:48", "S7", "5 100"},
            {"Moscow, Russia", "Rome, Italy", "18.07.2022 12:54", "18.07.2022 16:04", "Air Europa", "4 555"},
            {"Moscow, Russia", "Rome, Italy", "18.07.2022 17:54", "18.07.2022 20:07", "KLM", "6 415"},
            {"Moscow, Russia", "Rome, Italy", "15.08.2022 15:37", "15.08.2022 18:48", "S7", "4 756"},
            {"Rome, Italy", "Moscow, Russia", "18.08.2022 12:54", "18.08.2022 16:04", "Air Europa", "4 555"},
            {"Rome, Italy", "Moscow, Russia", "18.08.2022 17:54", "18.08.2022 20:07", "KLM", "6 415"},
            {"Rome, Italy", "Moscow, Russia", "15.09.2022 15:37", "15.09.2022 18:48", "S7", "4 756"},
            {"Saint Petersburg, Russia", "Rome, Italy", "24.09.2022 02:08", "24.09.2022 05:08", "KLM", "3 596"},
            {"Saint Petersburg, Russia", "Rome, Italy", "24.08.2022 10:08", "24.08.2022 13:28", "British Airlines", "6 127"},
            {"Saint Petersburg, Russia", "Rome, Italy", "17.08.2022 15:37", "17.08.2022 18:48", "S7", "3 649"},
            {"Rome, Italy", "Saint Petersburg, Russia", "24.09.2022 02:08", "24.09.2022 05:08", "KLM", "3 596"},
            {"Rome, Italy", "Saint Petersburg, Russia", "24.08.2022 10:08", "24.08.2022 13:28", "British Airlines", "6 127"},
            {"Rome, Italy", "Saint Petersburg, Russia", "17.08.2022 15:37", "17.08.2022 18:48", "S7", "3 649"},
            {"Moscow, Russia", "Tomsk, Russia", "23.08.2022 21:15", "24.08.2022 10:55", "Pobeda", "12 751"},
            {"Moscow, Russia", "Tomsk, Russia", "23.08.2022 23:25", "24.08.2022 07:30", "S7", "13 478"},
            {"Moscow, Russia", "Tomsk, Russia", "15.08.2022 23:55", "14.08.2022 10:55", "S7", "14 350"},
            {"Tomsk, Russia", "Moscow, Russia", "30.08.2022 17:10", "31.08.2022 10:55", "Pobeda", "13 916"},
            {"Tomsk, Russia", "Moscow, Russia", "30.08.2022 23:25", "31.08.2022 07:28", "S7", "13 559"},
            {"Tomsk, Russia", "Moscow, Russia", "29.08.2022 23:37", "31.08.2022 10:48", "S7", "12 558"}
    };

    private String[][] guideData = {
            {"Moscow, Russia", "Moscow is the capital and largest city of Russia. The city stands on the Moskva River in Central Russia, with a population estimated at 12.4 million residents within the city limits, over 17 million residents in the urban area, and over 20 million residents in the metropolitan area. The city covers an area of 2,511 square kilometers (970 sq mi), while the urban area covers 5,891 square kilometers (2,275 sq mi), and the metropolitan area covers over 26,000 square kilometers (10,000 sq mi). Moscow is among the world's largest cities; being the most populous city entirely in Europe, the largest urban and metropolitan area in Europe, and the largest city by land area on the European continent.", "https://www.tripadvisor.ru/Attractions-g298484-Activities-Moscow_Central_Russia.html"},
            {"Paris, France", "Paris is the capital and most populous city of France, with an estimated population of 2,165,423 residents in 2019 in an area of more than 105 km² (41 sq mi), making it the 34th most densely populated city in the world in 2020. Since the 17th century, Paris has been one of the world's major centres of finance, diplomacy, commerce, fashion, gastronomy, science, and arts, and has sometimes been referred to as the capital of the world. The City of Paris is the centre and seat of government of the region and province of Île-de-France, or Paris Region, with an estimated population of 12,997,058 in 2020, or about 18% of the population of France, making it in 2020 the second largest metropolitan area in the OECD, and 14th largest in the world in 2015. The Paris Region had a GDP of €709 billion ($808 billion) in 2017. According to the Economist Intelligence Unit Worldwide Cost of Living Survey, in 2021 Paris was the city with the second-highest cost of living in the world, tied with Singapore, and after Tel Aviv.", "https://www.tripadvisor.ru/Attractions-g187147-Activities-Paris_Ile_de_France.html"},
            {"Saint Petersburg, Russia", "Saint Petersburg, formerly known as Petrograd (1914–1924) and later Leningrad (1924–1991), is the second-largest city in Russia. It is situated on the Neva River, at the head of the Gulf of Finland on the Baltic Sea, with a population of roughly 5.4 million residents. Saint Petersburg is the fourth-most populous city in Europe, the most populous city on the Baltic Sea, as well as the world's northernmost city with over 1 million residents. As Russia's Imperial capital, and a historically strategic port, it is governed as a federal city.\n" +
                    "The city was founded by Tsar Peter the Great on 27 May 1703 on the site of a captured Swedish fortress, and was named after apostle Saint Peter. In Russia, Saint Petersburg is historically and culturally associated with the birth of the Russian Empire and Russia's entry into modern history as a European great power. It served as a capital of the Tsardom of Russia, and the subsequent Russian Empire, from 1713 to 1918 (being replaced by Moscow for a short period of time between 1728 and 1730). After the October Revolution in 1917, the Bolsheviks moved their government to Moscow.\n", "https://www.tripadvisor.ru/Attractions-g298507-Activities-St_Petersburg_Northwestern_District.html"},
            {"Rome, Italy", "Rome is the capital city of Italy. It is also the capital of the Lazio region, the centre of the Metropolitan City of Rome, and a special comune named Comune di Roma Capitale. With 2,860,009 residents in 1,285 km2 (496.1 sq mi), Rome is the country's most populated comune and the third most populous city in the European Union by population within city limits. The Metropolitan City of Rome, with a population of 4,355,725 residents, is the most populous metropolitan city in Italy. Its metropolitan area is the third-most populous within Italy. Rome is located in the central-western portion of the Italian Peninsula, within Lazio (Latium), along the shores of the Tiber. Vatican City (the smallest country in the world) is an independent country inside the city boundaries of Rome, the only existing example of a country within a city. Rome is often referred to as the City of Seven Hills due to its geographic location, and also as the \"Eternal City\". Rome is generally considered to be the \"cradle of Western Christian culture and civilization\", and the center of the Catholic Church.", "https://www.tripadvisor.ru/Attractions-g187791-Activities-Rome_Lazio.html"},
            {"Tomsk, Russia", "Tomsk is a city and the administrative center of Tomsk Oblast in Russia, located on the Tom River. The city's population was 524,669 (2010 Census); 487,838 (2002 Census); 501,963 (1989 Census).\n" +
                    "Tomsk is considered one of the oldest towns in Siberia. It celebrated its 410th anniversary in 2014. The city is a notable educational and scientific center with six state universities, over 100,000 students, and the oldest university in Siberia.\n", "https://www.tripadvisor.ru/Attractions-g665310-Activities-Tomsk_Tomsk_Oblast_Siberian_District.html"}
    };

    public TravelCompanyDBFiller(TravelCompanyDBHelper travelCompanyDBHelper) {
        this.travelCompanyDBHelper = travelCompanyDBHelper;
    }

    private void addHotelRow(String[] data) {
        SQLiteDatabase database = travelCompanyDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TravelCompanyContract.HotelEntry.CITY, data[0]);
        values.put(TravelCompanyContract.HotelEntry.HOTEL_NAME, data[1]);
        values.put(TravelCompanyContract.HotelEntry.MAP_LINK, data[2]);
        values.put(TravelCompanyContract.HotelEntry.COST, data[3]);
        values.put(TravelCompanyContract.HotelEntry.RATING, data[4]);
        values.put(TravelCompanyContract.HotelEntry.IMAGE_URL, data[5]);

        long newRowId = database.insert(
                TravelCompanyContract.HotelEntry.TABLE_NAME,
                null,
                values);
    }

    private void addFlightRow(String[] data) {
        SQLiteDatabase database = travelCompanyDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TravelCompanyContract.FlightEntry.DEPARTURE_CITY, data[0]);
        values.put(TravelCompanyContract.FlightEntry.ARRIVAL_CITY, data[1]);
        values.put(TravelCompanyContract.FlightEntry.DEPARTURE_DATE, data[2]);
        values.put(TravelCompanyContract.FlightEntry.ARRIVAL_DATE, data[3]);
        values.put(TravelCompanyContract.FlightEntry.AIRLINE, data[4]);
        values.put(TravelCompanyContract.FlightEntry.COST, data[5]);

        long newRowId = database.insert(
                TravelCompanyContract.FlightEntry.TABLE_NAME,
                null,
                values);
    }

    private void addGuideRow(String[] data) {
        SQLiteDatabase database = travelCompanyDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TravelCompanyContract.GuideEntry.CITY_TITLE, data[0]);
        values.put(TravelCompanyContract.GuideEntry.DESCRIPTION, data[1]);
        values.put(TravelCompanyContract.GuideEntry.LINK, data[2]);

        long newRowId = database.insert(
                TravelCompanyContract.GuideEntry.TABLE_NAME,
                null,
                values);
    }

    public void fillDB() {
        for (int i = 0; i < hotelsData.length; i++) {
            addHotelRow(hotelsData[i]);
        }
        for (int i = 0; i < flightsData.length; i++) {
            addFlightRow(flightsData[i]);
        }
        for (int i = 0; i < guideData.length; i++) {
            addGuideRow(guideData[i]);
        }
    }
}
