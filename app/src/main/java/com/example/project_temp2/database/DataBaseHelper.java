package com.example.project_temp2.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.project_temp2.shared.AllCars;
import com.example.project_temp2.car.CarModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class DataBaseHelper extends SQLiteOpenHelper {


    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Users Table (Role 0 --> Customer, 1 --> Dealer, 2 --> Employee)
        sqLiteDatabase.execSQL("CREATE TABLE Users ("
                + "email TEXT PRIMARY KEY,"
                + "first_name TEXT,"
                + "last_name TEXT,"
                + "gender TEXT,"
                + "hashed_password TEXT,"
                + "country TEXT,"
                + "city TEXT,"
                + "phone_number TEXT,"
                + "profileURI TEXT,"
                + "supervisor TEXT,"
                + "role INTEGER)");

        //  Cars table
        sqLiteDatabase.execSQL("CREATE TABLE Cars ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "manufacturer TEXT,"
                + "type TEXT,"
                + "price REAL,"
                + "model TEXT,"
                + "miles REAL,"
                + "image INTEGER,"
                + "promoPercentage REAL,"
                + "status INTEGER,"
                + "reserved INTEGER,"
                + "rating REAL,"
                + "carDealer TEXT)");

        //  Favorites table
        sqLiteDatabase.execSQL("CREATE TABLE Favorites ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "carID INTEGER,"
                + "userEmail TEXT,"
                + "FOREIGN KEY(carID) REFERENCES Cars(id),"
                + "FOREIGN KEY(userEmail) REFERENCES Users(email))");

        //  Reservations table
        sqLiteDatabase.execSQL("CREATE TABLE Reservations ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "reservationDT TEXT,"
                + "carID INTEGER,"
                + "userEmail TEXT,"
                + "price REAL,"
                + "offer REAL,"
                + "returnDT TEXT,"
                + "rating REAL,"
                + "FOREIGN KEY(carID) REFERENCES Cars(id),"
                + "FOREIGN KEY(userEmail) REFERENCES Users(email))");

        //  Cities table
        sqLiteDatabase.execSQL("CREATE TABLE Cities ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "country_id INTEGER,"
                + "name TEXT,"
                + "FOREIGN KEY(country_id) REFERENCES Countries(id))");

        // Countries table
        sqLiteDatabase.execSQL("CREATE TABLE Countries ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "phone_code TEXT)");

        String hashed1 = BCrypt.withDefaults().hashToString(12, "yousef".toCharArray());
        String hashed2 = BCrypt.withDefaults().hashToString(12, "khaled".toCharArray());
        String hashed3 = BCrypt.withDefaults().hashToString(12, "mohammed".toCharArray());
        String hashed4 = BCrypt.withDefaults().hashToString(12, "customer".toCharArray());

        // Seed database with admin
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", "yousef@dealer.com");
        contentValues.put("first_name", "Yousef");
        contentValues.put("last_name", "Shamasneh");
        contentValues.put("gender", "male");
        contentValues.put("hashed_password", hashed1);
        contentValues.put("country", "Palestine");
        contentValues.put("city", "Ramallah");
        contentValues.put("phone_number", "059");
        contentValues.put("role", 1);
        sqLiteDatabase.insert("Users", null, contentValues);

        contentValues.put("email", "khaled@dealer.com");
        contentValues.put("first_name", "Khaled");
        contentValues.put("last_name", "Roub");
        contentValues.put("gender", "male");
        contentValues.put("hashed_password", hashed2);
        contentValues.put("country", "Palestine");
        contentValues.put("city", "Jerusalem");
        contentValues.put("phone_number", "056");
        contentValues.put("role", 1);
        sqLiteDatabase.insert("Users", null, contentValues);

        contentValues.put("email", "mohammed@dealer.com");
        contentValues.put("first_name", "Mohammed");
        contentValues.put("last_name", "Ali");
        contentValues.put("gender", "male");
        contentValues.put("hashed_password", hashed3);
        contentValues.put("country", "Palestine");
        contentValues.put("city", "Jerusalem");
        contentValues.put("phone_number", "056");
        contentValues.put("role", 1);
        sqLiteDatabase.insert("Users", null, contentValues);

        contentValues.put("email", "customer@customer.com");
        contentValues.put("first_name", "Customer");
        contentValues.put("last_name", "Customer");
        contentValues.put("gender", "male");
        contentValues.put("hashed_password", hashed4);
        contentValues.put("country", "Palestine");
        contentValues.put("city", "Jerusalem");
        contentValues.put("phone_number", "056");
        contentValues.put("role", 0);
        sqLiteDatabase.insert("Users", null, contentValues);

        // Seed database with countries
        sqLiteDatabase.execSQL("INSERT INTO Countries (name, phone_code) VALUES ('Palestine', '+970')");
        sqLiteDatabase.execSQL("INSERT INTO Countries (name, phone_code) VALUES ('Lebanon', '+961')");
        sqLiteDatabase.execSQL("INSERT INTO Countries (name, phone_code) VALUES ('Germany', '+49')");
        sqLiteDatabase.execSQL("INSERT INTO Countries (name, phone_code) VALUES ('Switzerland', '+41')");
        sqLiteDatabase.execSQL("INSERT INTO Countries (name, phone_code) VALUES ('United States', '+1')");
        sqLiteDatabase.execSQL("INSERT INTO Countries (name, phone_code) VALUES ('United Kingdom', '+44')");
        sqLiteDatabase.execSQL("INSERT INTO Countries (name, phone_code) VALUES ('Canada', '+44')");
        sqLiteDatabase.execSQL("INSERT INTO Countries (name, phone_code) VALUES ('Australia', '+61')");

        // Seed database with cities
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (1, 'Ramallah')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (1, 'Jerusalem')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (1, 'Hebron')");

        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (2, 'Beirut')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (2, 'Baalbek')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (2, 'Tripoli')");

        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (3, 'Berlin')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (3, 'Hamburg')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (3, 'Munich')");

        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (4, 'Geneva')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (4, 'Lugano')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (4, 'Zurich')");

        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (5, 'New York City')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (5, 'Chicago')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (5, 'Houston')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (5, 'Miami')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (5, 'Los Angeles')");

        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (6, 'London')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (6, 'Manchester')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (6, 'Birmingham')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (6, 'Liverpool')");

        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (7, 'Toronto')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (7, 'Ottawa')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (7, 'Montreal')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (7, 'Vancouver')");

        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (8, 'Sydney')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (8, 'Melbourne')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (8, 'Brisbane')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (8, 'Perth')");
        sqLiteDatabase.execSQL("INSERT INTO Cities (country_id, name) VALUES (8, 'Adelaide')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public List<String> getAllCountries() {
        List<String> countriesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM Countries";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String countryName = cursor.getString(cursor.getColumnIndex("name"));
                countriesList.add(countryName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return countriesList;
    }

    @SuppressLint("Range")
    public List<String> getCitiesByCountryName(String countryName) {
        List<String> citiesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectCountryIdQuery = "SELECT id FROM Countries" +
                " WHERE name = ?";

        Cursor countryCursor = db.rawQuery(selectCountryIdQuery, new String[]{countryName});

        int countryId = -1;

        if (countryCursor.moveToFirst()) {
            countryId = countryCursor.getInt(countryCursor.getColumnIndex("id"));
        }

        countryCursor.close();

        if (countryId != -1) {
            String selectCitiesQuery = "SELECT * FROM Cities" +
                    " WHERE country_id = ?";

            Cursor cursor = db.rawQuery(selectCitiesQuery, new String[]{String.valueOf(countryId)});

            if (cursor.moveToFirst()) {
                do {
                    String cityName = cursor.getString(cursor.getColumnIndex("name"));
                    citiesList.add(cityName);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        db.close();

        return citiesList;
    }

    @SuppressLint("Range")
    public String getPhoneCodeByCountryName(String countryName) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectPhoneCodeQuery = "SELECT phone_code FROM Countries" +
                " WHERE name = ?";

        Cursor cursor = db.rawQuery(selectPhoneCodeQuery, new String[]{countryName});

        String phoneCode = null;

        if (cursor.moveToFirst()) {
            phoneCode = cursor.getString(cursor.getColumnIndex("phone_code"));
        }

        cursor.close();
        db.close();

        return phoneCode;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] columns = {"email", "first_name", "last_name", "gender", "hashed_password", "country", "city", "phone_number", "profileURI", "role", "supervisor"};
        String selection = "email = ?";
        String[] selectionArgs = {email};

        return sqLiteDatabase.query("Users", columns, selection, selectionArgs, null, null, null);
    }

    @SuppressLint("Range")
    public User getUserPhoneAndLocation(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String[] columns = {
                "email", "first_name", "last_name", "country", "city", "phone_number"
        };

        String selection = "email=?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query("Users", columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
            user.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
            user.setCountry(cursor.getString(cursor.getColumnIndex("country")));
            user.setCity(cursor.getString(cursor.getColumnIndex("city")));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phone_number")));
        }

        cursor.close();
        db.close();

        return user;
    }

    @SuppressLint("Range")
    public List<CarModel> getAllCars() {
        List<CarModel> carList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Cars";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                String model = cursor.getString(cursor.getColumnIndex("model"));
                double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
                int image = cursor.getInt(cursor.getColumnIndex("image"));
                double promoPercentage = cursor.getDouble(cursor.getColumnIndex("promoPercentage"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                int reserved = cursor.getInt(cursor.getColumnIndex("reserved"));
                double rating = cursor.getDouble(cursor.getColumnIndex("rating"));
                String carDealer = cursor.getString(cursor.getColumnIndex("carDealer"));

                CarModel car = new CarModel(id, manufacturer, type, (float) price, model, (float) miles, image, (float) promoPercentage, status, reserved, rating, carDealer);
                carList.add(car);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();

        return carList;
    }

    public void insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("email", user.getEmail());
        contentValues.put("first_name", user.getFirstName());
        contentValues.put("last_name", user.getLastName());
        contentValues.put("gender", user.getGender());
        contentValues.put("hashed_password", user.getHashedPassword());
        contentValues.put("country", user.getCountry());
        contentValues.put("city", user.getCity());
        contentValues.put("phone_number", user.getPhoneNumber());
        contentValues.put("role", user.getRole());
        contentValues.put("supervisor", user.getSupervisor());
        sqLiteDatabase.insert("Users", null, contentValues);

        sqLiteDatabase.close();
    }

    public void insertCar() {
        AllCars listCars = AllCars.getInstance();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (CarModel car : listCars.cars) {
            contentValues.put("id", car.getId());
            contentValues.put("manufacturer", car.getManufacturer());
            contentValues.put("type", car.getType());
            contentValues.put("price", car.getPrice());
            contentValues.put("model", car.getModel());
            contentValues.put("miles", car.getMiles());
            contentValues.put("image", car.getImage());
            contentValues.put("promoPercentage", car.getPromoPercentage());
            contentValues.put("status", car.getStatus());
            contentValues.put("reserved", car.getReserved());
            contentValues.put("rating", car.getRating());
            contentValues.put("carDealer", car.getCarDealer());

            long rowId = sqLiteDatabase.insert("Cars", null, contentValues);
            System.out.println();

        }

        sqLiteDatabase.close();
    }

    public void insertFavorite(Favorites favorite) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("carID", favorite.getCarID());
        contentValues.put("userEmail", favorite.getUserEmail());
        long rowId = sqLiteDatabase.insert("Favorites", null, contentValues);

        sqLiteDatabase.close();
    }

    @SuppressLint("Range")
    public int getFavoriteId(int carId, String userEmail) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String[] projection = {"id"};
        String selection = "carID=? AND userEmail=?";
        String[] selectionArgs = {String.valueOf(carId), userEmail};

        Cursor cursor = sqLiteDatabase.query("Favorites", projection, selection, selectionArgs, null, null, null);

        int favoriteId = -1;

        if (cursor.moveToFirst()) {
            favoriteId = cursor.getInt(cursor.getColumnIndex("id"));
        }

        cursor.close();
        sqLiteDatabase.close();

        return favoriteId;
    }


    public void removeFavorite(int favoriteId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(favoriteId)};

        int deletedRows = sqLiteDatabase.delete("Favorites", selection, selectionArgs);

        sqLiteDatabase.close();
    }

    public List<Favorites> getFavoritesByUserEmail(String userEmail) {
        List<Favorites> favoritesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Favorites WHERE userEmail = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int favoriteId = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") int carId = cursor.getInt(cursor.getColumnIndex("carID"));

                Favorites favorite = new Favorites(favoriteId, carId, userEmail);
                favoritesList.add(favorite);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return favoritesList;
    }

    @SuppressLint("Range")
    public List<CarModel> getFavoriteCars(String userEmail) {
        List<CarModel> favoriteCarsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            String query = "SELECT Cars.*, Favorites.* FROM Cars " +
                    "JOIN Favorites ON Cars.id = Favorites.carID " +
                    "WHERE Favorites.userEmail = ?";
            Cursor cursor = db.rawQuery(query, new String[]{userEmail});

            if (cursor.moveToFirst()) {
                do {
                    CarModel car = new CarModel(cursor.getInt(cursor.getColumnIndex("carID")),
                            cursor.getString(cursor.getColumnIndex("manufacturer")),
                            cursor.getString(cursor.getColumnIndex("type")),
                            (float) cursor.getDouble(cursor.getColumnIndex("price")),
                            cursor.getString(cursor.getColumnIndex("model")),
                            (float) cursor.getDouble(cursor.getColumnIndex("miles")),
                            cursor.getInt(cursor.getColumnIndex("image")),
                            (float) cursor.getDouble(cursor.getColumnIndex("promoPercentage")),
                            cursor.getInt(cursor.getColumnIndex("status")),
                            cursor.getInt(cursor.getColumnIndex("reserved")),
                            cursor.getDouble(cursor.getColumnIndex("rating")),
                            cursor.getString(cursor.getColumnIndex("carDealer")));

                    favoriteCarsList.add(car);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        }

        return favoriteCarsList;
    }

    public void insertReservation(String userEmail, int carId, double price, double offer) {
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String reservationDT = dateFormat.format(new Date());

        ContentValues values = new ContentValues();
        values.put("reservationDT", reservationDT);
        values.put("carID", carId);
        values.put("userEmail", userEmail);
        values.put("price", price);
        values.put("offer", offer);

        long reservationId = db.insert("Reservations", null, values);

        db.close();
    }

    @SuppressLint("Range")
    public List<CarModel> getCarsWithPromoPercentage() {
        List<CarModel> carsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Cars WHERE promoPercentage > 0";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                String model = cursor.getString(cursor.getColumnIndex("model"));
                double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
                int image = cursor.getInt(cursor.getColumnIndex("image"));
                double promoPercentage = cursor.getDouble(cursor.getColumnIndex("promoPercentage"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                int reserved = cursor.getInt(cursor.getColumnIndex("reserved"));
                double rating = cursor.getDouble(cursor.getColumnIndex("rating"));
                String carDealer = cursor.getString(cursor.getColumnIndex("carDealer"));

                CarModel car = new CarModel(id, manufacturer, type, (float) price, model, (float) miles, image, (float) promoPercentage, status, reserved, rating, carDealer);
                carsList.add(car);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return carsList;
    }

    @SuppressLint("Range")
    public List<ReservedCarDetails> getAllReservedCarDetails() {
        List<ReservedCarDetails> reservedCarDetailsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Cars.*, Reservations.reservationDT, Reservations.returnDT, Reservations.price, Reservations.offer, Users.first_name, Users.last_name, Users.email " +
                "FROM Cars " +
                "INNER JOIN Reservations ON Cars.id = Reservations.carID " +
                "INNER JOIN Users ON Reservations.userEmail = Users.email";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int carId = cursor.getInt(cursor.getColumnIndex("id"));
                String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                String model = cursor.getString(cursor.getColumnIndex("model"));
                double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
                int image = cursor.getInt(cursor.getColumnIndex("image"));
                double promoPercentage = cursor.getDouble(cursor.getColumnIndex("promoPercentage"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                int reserved = cursor.getInt(cursor.getColumnIndex("reserved"));

                String reservationDT = cursor.getString(cursor.getColumnIndex("reservationDT"));
                String returnDT = cursor.getString(cursor.getColumnIndex("returnDT"));
                double reservationPrice = cursor.getDouble(cursor.getColumnIndex("price"));
                double offer = cursor.getDouble(cursor.getColumnIndex("offer"));

                String reserverFirstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String reserverLastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String reserverEmail = cursor.getString(cursor.getColumnIndex("email"));
                String reserverName = reserverFirstName + " " + reserverLastName;

                ReservedCarDetails reservedCarDetails = new ReservedCarDetails(carId, manufacturer, type, price, model, miles, image, promoPercentage, status, reserved, reservationDT, returnDT, reservationPrice, offer, reserverName, reserverEmail);
                reservedCarDetailsList.add(reservedCarDetails);

            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();

        return reservedCarDetailsList;
    }

    @SuppressLint("Range")
    public List<ReservedCarDetails> getReservedCarDetailsByUserEmail(String userEmail) {
        List<ReservedCarDetails> reservedCarDetailsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Cars.*, Reservations.reservationDT, Reservations.returnDT, Reservations.price, Reservations.offer, Users.first_name, Users.last_name, Users.email " +
                "FROM Cars " +
                "INNER JOIN Reservations ON Cars.id = Reservations.carID " +
                "INNER JOIN Users ON Reservations.userEmail = Users.email " +
                "WHERE Users.email = ?";

        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int carId = cursor.getInt(cursor.getColumnIndex("id"));
                String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                String model = cursor.getString(cursor.getColumnIndex("model"));
                double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
                int image = cursor.getInt(cursor.getColumnIndex("image"));
                double promoPercentage = cursor.getDouble(cursor.getColumnIndex("promoPercentage"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                int reserved = cursor.getInt(cursor.getColumnIndex("reserved"));

                String reservationDT = cursor.getString(cursor.getColumnIndex("reservationDT"));
                String returnDT = cursor.getString(cursor.getColumnIndex("returnDT"));
                double reservationPrice = cursor.getDouble(cursor.getColumnIndex("price"));
                double offer = cursor.getDouble(cursor.getColumnIndex("offer"));

                String reserverFirstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String reserverLastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String reserverEmail = cursor.getString(cursor.getColumnIndex("email"));
                String reserverName = reserverFirstName + " " + reserverLastName;

                ReservedCarDetails reservedCarDetails = new ReservedCarDetails(carId, manufacturer, type, price, model, miles, image, promoPercentage, status, reserved, reservationDT, returnDT, reservationPrice, offer, reserverName, reserverEmail);
                reservedCarDetailsList.add(reservedCarDetails);

            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();

        return reservedCarDetailsList;
    }


    public void updateReservedStatus(int carId, int reservedStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("reserved", reservedStatus);

        db.update("Cars", values, "id = ?", new String[]{String.valueOf(carId)});

        db.close();
    }

    public void updateReturnDate(String userEmail, int carId, String newReturnDT) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("returnDT", newReturnDT);

        int rowsAffected = db.update("Reservations", values, "userEmail = ? AND carID = ?",
                new String[]{userEmail, String.valueOf(carId)});

        db.close();
    }

    public void updateReservationRating(String userEmail, int carId, String reservationDate, float newRating) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("rating", newRating);

        String whereClause = "userEmail = ? AND carID = ? AND reservationDT = ?";
        String[] whereArgs = {userEmail, String.valueOf(carId), reservationDate};

        int rowsAffected = database.update("Reservations", values, whereClause, whereArgs);

        database.close();
    }

    @SuppressLint("Range")
    public double getAverageRatingForCar(int carId) {
        SQLiteDatabase database = getReadableDatabase();

        String query = "SELECT AVG(rating) AS avgRating FROM Reservations WHERE carID = ? AND rating IS NOT NULL";
        String[] selectionArgs = {String.valueOf(carId)};

        Cursor cursor = database.rawQuery(query, selectionArgs);

        double averageRating = 0;

        if (cursor.moveToFirst()) {
            averageRating = cursor.getDouble(cursor.getColumnIndex("avgRating"));
        }

        cursor.close();
        database.close();

        return averageRating;
    }

    @SuppressLint("Range")
    public double getRatingForCarByDate(int carId, String reservationDate) {
        SQLiteDatabase database = getReadableDatabase();

        String query = "SELECT rating FROM Reservations WHERE carID = ? AND reservationDT = ?";
        String[] selectionArgs = {String.valueOf(carId), reservationDate};

        Cursor cursor = database.rawQuery(query, selectionArgs);

        double rating = 0;

        if (cursor.moveToFirst()) {
            rating = cursor.getDouble(cursor.getColumnIndex("rating"));
        }

        cursor.close();
        database.close();

        return rating;
    }

    public boolean hasCarsWithPromo() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM Cars WHERE promoPercentage > 0";
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count > 0;
    }

    @SuppressLint("Range")
    public List<User> getAllCustomer() {
        List<User> usersList = new ArrayList<>();

        String selectQuery = "SELECT * FROM Users WHERE role = 0";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
                user.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
                user.setProfileURI(cursor.getString(cursor.getColumnIndex("profileURI")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                usersList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return usersList;
    }

    public void deleteUserByEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Users", "email=?", new String[]{email});
        db.close();
    }

    public void updateProfileImagePath(String email, String imagePath) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("profileURI", imagePath);

        try {
            String whereClause = "email=?";
            String[] whereArgs = {email};
            sqLiteDatabase.update("Users", contentValues, whereClause, whereArgs);
            Log.d("DataBaseHelper", "Profile image path updated for email: " + email);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataBaseHelper", "Error updating profile image path for email: " + email);
        } finally {
            sqLiteDatabase.close();
        }
    }

    public void updateUserProfile(String email, ContentValues updatedValues) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try {
            String whereClause = "email=?";
            String[] whereArgs = {email};
            sqLiteDatabase.update("Users", updatedValues, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();
        }
    }

    public Cursor getUserData(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] columns = {"email", "first_name", "last_name", "hashed_password", "phone_number", "profileURI"};

        Cursor cursor = sqLiteDatabase.query("Users", columns, "email=?", new String[]{email}, null, null, null);

        return cursor;
    }

    @SuppressLint("Range")
    public HashMap<String, String> getDealers() {
        HashMap<String, String> emailAndFullNamesMap = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"email", "first_name", "last_name"};

        String selection = "role = ?";
        String[] selectionArgs = {"1"};

        Cursor cursor = db.query("Users", columns, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
            String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
            String fullName = firstName + " " + lastName;
            emailAndFullNamesMap.put(email, fullName);
        }

        cursor.close();
        db.close();

        return emailAndFullNamesMap;
    }

    @SuppressLint("Range")
    public CarModel getLastReservedCarDetails(String carDealer, String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        CarModel lastReservedCarDetails = null;

        String query = "SELECT Cars.manufacturer, Cars.image, Cars.type, Cars.miles, Cars.model " +
                "FROM Cars " +
                "JOIN Reservations ON Cars.id = Reservations.carID " +
                "WHERE Cars.carDealer = ? AND Reservations.userEmail = ? " +
                "ORDER BY Reservations.reservationDT DESC " +
                "LIMIT 1";

        Cursor cursor = db.rawQuery(query, new String[]{carDealer, userEmail});

        if (cursor.moveToFirst()) {
            String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
            String model = cursor.getString(cursor.getColumnIndex("model"));
            int image = cursor.getInt(cursor.getColumnIndex("image"));

            lastReservedCarDetails = new CarModel();
            lastReservedCarDetails.setManufacturer(manufacturer);
            lastReservedCarDetails.setType(type);
            lastReservedCarDetails.setModel(model);
            lastReservedCarDetails.setMiles((float) miles);
            lastReservedCarDetails.setImage(image);

        }

        cursor.close();
        db.close();

        return lastReservedCarDetails;
    }

    @SuppressLint("Range")
    public CarModel getLatestFavoriteCarByDealer(String userEmail, String carDealerEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        CarModel latestFavoriteCar = null;

        String query = "SELECT Cars.manufacturer, Cars.image, Cars.type, Cars.model, Cars.price, Cars.miles, Cars.rating" +
                " FROM Cars" +
                " INNER JOIN Favorites ON Cars.id = Favorites.carID" +
                " WHERE Favorites.userEmail = ? AND Cars.carDealer = ?" +
                " ORDER BY Favorites.id DESC" +
                " LIMIT 1";

        Cursor cursor = db.rawQuery(query, new String[]{userEmail, carDealerEmail});

        if (cursor != null && cursor.moveToFirst()) {
            String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String model = cursor.getString(cursor.getColumnIndex("model"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
            double rating = cursor.getDouble(cursor.getColumnIndex("rating"));
            int image = cursor.getInt(cursor.getColumnIndex("image"));

            latestFavoriteCar = new CarModel();
            latestFavoriteCar.setManufacturer(manufacturer);
            latestFavoriteCar.setType(type);
            latestFavoriteCar.setModel(model);
            latestFavoriteCar.setMiles((float) miles);
            latestFavoriteCar.setPrice((float) price);
            latestFavoriteCar.setRating((float) rating);
            latestFavoriteCar.setImage(image);

            cursor.close();
        }

        db.close();

        return latestFavoriteCar;
    }

    @SuppressLint("Range")
    public CarModel getLatestFavoriteCar(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        CarModel latestFavoriteCar = null;

        String query = "SELECT Cars.manufacturer, Cars.type, Cars.image, Cars.model, Cars.price, Cars.miles, Cars.rating" +
                " FROM Cars" +
                " INNER JOIN Favorites ON Cars.id = Favorites.carID" +
                " WHERE Favorites.userEmail = ?" +
                " ORDER BY Favorites.id DESC" +
                " LIMIT 1";

        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        if (cursor != null && cursor.moveToFirst()) {
            String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String model = cursor.getString(cursor.getColumnIndex("model"));
            double price = cursor.getDouble(cursor.getColumnIndex("price"));
            double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
            double rating = cursor.getDouble(cursor.getColumnIndex("rating"));
            int image = cursor.getInt(cursor.getColumnIndex("image"));

            latestFavoriteCar = new CarModel();
            latestFavoriteCar.setManufacturer(manufacturer);
            latestFavoriteCar.setType(type);
            latestFavoriteCar.setModel(model);
            latestFavoriteCar.setMiles((float) miles);
            latestFavoriteCar.setPrice((float) price);
            latestFavoriteCar.setRating((float) rating);
            latestFavoriteCar.setImage(image);

            cursor.close();
        }

        db.close();

        return latestFavoriteCar;
    }

    @SuppressLint("Range")
    public CarModel getLastReservedCar(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        CarModel lastReservedCarDetails = null;

        String query = "SELECT Cars.manufacturer, Cars.image, Cars.type, Cars.miles, Cars.model " +
                "FROM Cars " +
                "JOIN Reservations ON Cars.id = Reservations.carID " +
                "WHERE Reservations.userEmail = ? " +
                "ORDER BY Reservations.reservationDT DESC " +
                "LIMIT 1";

        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        if (cursor.moveToFirst()) {
            String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
            String model = cursor.getString(cursor.getColumnIndex("model"));
            int image = cursor.getInt(cursor.getColumnIndex("image"));

            lastReservedCarDetails = new CarModel();
            lastReservedCarDetails.setManufacturer(manufacturer);
            lastReservedCarDetails.setType(type);
            lastReservedCarDetails.setModel(model);
            lastReservedCarDetails.setMiles((float) miles);
            lastReservedCarDetails.setImage(image);

        }

        cursor.close();
        db.close();

        return lastReservedCarDetails;
    }

    @SuppressLint("Range")
    public List<CarModel> getCarsByDealer(String dealerName) {
        List<CarModel> carList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Cars WHERE carDealer = ?";

        Cursor cursor = db.rawQuery(query, new String[]{dealerName});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                String model = cursor.getString(cursor.getColumnIndex("model"));
                double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
                int image = cursor.getInt(cursor.getColumnIndex("image"));
                double promoPercentage = cursor.getDouble(cursor.getColumnIndex("promoPercentage"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                int reserved = cursor.getInt(cursor.getColumnIndex("reserved"));
                double rating = cursor.getDouble(cursor.getColumnIndex("rating"));
                String carDealer = cursor.getString(cursor.getColumnIndex("carDealer"));

                CarModel car = new CarModel(id, manufacturer, type, (float) price, model, (float) miles, image, (float) promoPercentage, status, reserved, rating, carDealer);
                carList.add(car);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();

        return carList;
    }

    @SuppressLint("Range")
    public List<CarModel> getFavoriteCarsByDealer(String userEmail, String carDealer) {
        List<CarModel> favoriteCarsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            String query = "SELECT Cars.*, Favorites.* FROM Cars " +
                    "JOIN Favorites ON Cars.id = Favorites.carID " +
                    "WHERE Favorites.userEmail = ? AND Cars.carDealer = ?";
            Cursor cursor = db.rawQuery(query, new String[]{userEmail, carDealer});

            if (cursor.moveToFirst()) {
                do {
                    CarModel car = new CarModel(cursor.getInt(cursor.getColumnIndex("carID")),
                            cursor.getString(cursor.getColumnIndex("manufacturer")),
                            cursor.getString(cursor.getColumnIndex("type")),
                            (float) cursor.getDouble(cursor.getColumnIndex("price")),
                            cursor.getString(cursor.getColumnIndex("model")),
                            (float) cursor.getDouble(cursor.getColumnIndex("miles")),
                            cursor.getInt(cursor.getColumnIndex("image")),
                            (float) cursor.getDouble(cursor.getColumnIndex("promoPercentage")),
                            cursor.getInt(cursor.getColumnIndex("status")),
                            cursor.getInt(cursor.getColumnIndex("reserved")),
                            cursor.getDouble(cursor.getColumnIndex("rating")),
                            cursor.getString(cursor.getColumnIndex("carDealer")));

                    favoriteCarsList.add(car);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        }

        return favoriteCarsList;
    }

    @SuppressLint("Range")
    public List<ReservedCarDetails> getReservedCarDetailsByUserAndDealer(String userEmail, String carDealer) {
        List<ReservedCarDetails> reservedCarDetailsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Cars.*, Reservations.reservationDT, Reservations.returnDT, Reservations.price, Reservations.offer, Users.first_name, Users.last_name, Users.email " +
                "FROM Cars " +
                "INNER JOIN Reservations ON Cars.id = Reservations.carID " +
                "INNER JOIN Users ON Reservations.userEmail = Users.email " +
                "WHERE Users.email = ? AND Cars.carDealer = ?";

        Cursor cursor = db.rawQuery(query, new String[]{userEmail, carDealer});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int carId = cursor.getInt(cursor.getColumnIndex("id"));
                String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                String model = cursor.getString(cursor.getColumnIndex("model"));
                double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
                int image = cursor.getInt(cursor.getColumnIndex("image"));
                double promoPercentage = cursor.getDouble(cursor.getColumnIndex("promoPercentage"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                int reserved = cursor.getInt(cursor.getColumnIndex("reserved"));

                String reservationDT = cursor.getString(cursor.getColumnIndex("reservationDT"));
                String returnDT = cursor.getString(cursor.getColumnIndex("returnDT"));
                double reservationPrice = cursor.getDouble(cursor.getColumnIndex("price"));
                double offer = cursor.getDouble(cursor.getColumnIndex("offer"));

                String reserverFirstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String reserverLastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String reserverEmail = cursor.getString(cursor.getColumnIndex("email"));
                String reserverName = reserverFirstName + " " + reserverLastName;

                ReservedCarDetails reservedCarDetails = new ReservedCarDetails(carId, manufacturer, type, price, model, miles, image, promoPercentage, status, reserved, reservationDT, returnDT, reservationPrice, offer, reserverName, reserverEmail);
                reservedCarDetailsList.add(reservedCarDetails);

            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();

        return reservedCarDetailsList;
    }

    @SuppressLint("Range")
    public List<CarModel> getCarsWithPromoPercentageByDealer(String carDealer) {
        List<CarModel> carsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Cars WHERE promoPercentage > 0 AND carDealer = ?";
        Cursor cursor = db.rawQuery(query, new String[]{carDealer});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                String model = cursor.getString(cursor.getColumnIndex("model"));
                double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
                int image = cursor.getInt(cursor.getColumnIndex("image"));
                double promoPercentage = cursor.getDouble(cursor.getColumnIndex("promoPercentage"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                int reserved = cursor.getInt(cursor.getColumnIndex("reserved"));
                double rating = cursor.getDouble(cursor.getColumnIndex("rating"));
                String dealer = cursor.getString(cursor.getColumnIndex("carDealer"));

                CarModel car = new CarModel(id, manufacturer, type, (float) price, model, (float) miles, image, (float) promoPercentage, status, reserved, rating, dealer);
                carsList.add(car);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return carsList;
    }

    @SuppressLint("Range")
    public List<ReservedCarDetails> getReservedCarDetailsByDealer(String carDealer) {
        List<ReservedCarDetails> reservedCarDetailsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Cars.*, Reservations.reservationDT, Reservations.returnDT, Reservations.price, Reservations.offer, Users.first_name, Users.last_name, Users.email " +
                "FROM Cars " +
                "INNER JOIN Reservations ON Cars.id = Reservations.carID " +
                "INNER JOIN Users ON Reservations.userEmail = Users.email " +
                "WHERE Cars.carDealer = ?";

        Cursor cursor = db.rawQuery(query, new String[]{carDealer});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int carId = cursor.getInt(cursor.getColumnIndex("id"));
                String manufacturer = cursor.getString(cursor.getColumnIndex("manufacturer"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                String model = cursor.getString(cursor.getColumnIndex("model"));
                double miles = cursor.getDouble(cursor.getColumnIndex("miles"));
                int image = cursor.getInt(cursor.getColumnIndex("image"));
                double promoPercentage = cursor.getDouble(cursor.getColumnIndex("promoPercentage"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                int reserved = cursor.getInt(cursor.getColumnIndex("reserved"));

                String reservationDT = cursor.getString(cursor.getColumnIndex("reservationDT"));
                String returnDT = cursor.getString(cursor.getColumnIndex("returnDT"));
                double reservationPrice = cursor.getDouble(cursor.getColumnIndex("price"));
                double offer = cursor.getDouble(cursor.getColumnIndex("offer"));

                String reserverFirstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String reserverLastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String reserverEmail = cursor.getString(cursor.getColumnIndex("email"));
                String reserverName = reserverFirstName + " " + reserverLastName;

                ReservedCarDetails reservedCarDetails = new ReservedCarDetails(carId, manufacturer, type, price, model, miles, image, promoPercentage, status, reserved, reservationDT, returnDT, reservationPrice, offer, reserverName, reserverEmail);
                reservedCarDetailsList.add(reservedCarDetails);

            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();

        return reservedCarDetailsList;
    }

    public void updateCarRating(int carId, double newRating) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("rating", newRating);

        long done = db.update("Cars", values, "id = ?", new String[]{String.valueOf(carId)});

        db.close();
    }
}
