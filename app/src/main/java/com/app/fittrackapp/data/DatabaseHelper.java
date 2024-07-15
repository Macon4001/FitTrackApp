package com.app.fittrackapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    private Context context;
    private static final String DATABASE_NAME = "gymapp.db";
    private static final int DATABASE_VERSION = 43;
    private static final String TABLE_WE = "exercises";
    private static final String COL_WE_PLAN_NAME = "plan_name";
    private static final String COL_WE_ROUTINE_NAME = "routine";
    private static final String COL_WE_EXERCISE_NAME = "exercise_name";
    private static final String COL_WE_SETS = "sets";
    private static final String COL_WE_REPETITIONS = "repetitions";
    private static final String COL_WE_WEIGHT = "weight";
    private static final String TABLE_PM = "preset_meals";
    private static final String COL_PM_INDEX = "meal_index";
    private static final String COL_PM_NAME = "name";
    private static final String COL_PM_CATEGORY = "category";
    private static final String COL_PM_CALORIES = "calories";
    private static final String COL_PM_FAT = "fat";
    private static final String COL_PM_CARBS = "carbs";
    private static final String COL_PM_SUGAR = "sugar";
    private static final String COL_PM_PROTEIN = "protein";
    private static final String COL_PM_SALT = "salt";
    private static final String COL_PM_FIBER = "fiber";

    private static final String TABLE_PMC = "meal_categories";
    private static final String COL_PMC_NAME = "name";

    private static final String TABLE_CM = "consumed_meals";
    private static final String COL_CM_DATE = "date";
    private static final String COL_CM_INDEX = "meal_index";
    private static final String COL_CM_AMOUNT = "amount";

    private static final String TABLE_BD = "bodydata";
    private static final String COLUMN_BD_DATE = "date";
    private static final String COLUMN_BD_WEIGHT = "weight";
    private static final String COLUMN_BD_CHEST = "chest";
    private static final String COLUMN_BD_BELLY = "belly";
    private static final String COLUMN_BD_BUTT = "butt";
    private static final String COLUMN_BD_WAIST = "waist";
    private static final String COLUMN_BD_ARM_R = "arm_right";
    private static final String COLUMN_BD_ARM_L = "arm_left";
    private static final String COLUMN_BD_LEG_R = "leg_right";
    private static final String COLUMN_BD_LEG_L = "leg_left";
    // profile
    private static final String TABLE_S_GOAL = "settings_goals";
    private static final String COL_S_INDEX = "settings_index";
    private static final String COL_S_GOAL_CALORIES = "goal_calories";
    private static final String COL_S_GOAL_FAT = "goal_fat";
    private static final String COL_S_GOAL_CARBS = "goal_carbs";
    private static final String COL_S_GOAL_PROTEIN = "goal_protein";
    private static final String TABLE_S_LANG = "settings_lang";
    private static final String COL_S_LANG = "language";

    //exercises
    private static final String TABLE_WP = "workout_plans";
    private static final String COL_WP_NAME = "plan_name";

    private static final String TABLE_WR = "workout_routines";
    private static final String COL_WR_PLAN_NAME = "plan_name";
    private static final String COL_WR_ROUTINE_NAME = "routine_name";




    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_BD + " ("
                + COLUMN_BD_DATE + " TEXT PRIMARY KEY, "
                + COLUMN_BD_WEIGHT + " REAL, "
                + COLUMN_BD_CHEST + " REAL, "
                + COLUMN_BD_BELLY + " REAL, "
                + COLUMN_BD_BUTT + " REAL, "
                + COLUMN_BD_WAIST + " REAL, "
                + COLUMN_BD_ARM_R + " REAL, "
                + COLUMN_BD_ARM_L + " REAL, "
                + COLUMN_BD_LEG_R + " REAL, "
                + COLUMN_BD_LEG_L + " REAL);"
        );

        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_PM + " ("
                 + COL_PM_INDEX + " TEXT PRIMARY KEY, "
                 + COL_PM_NAME + " TEXT, "
                 + COL_PM_CATEGORY + " TEXT, "
                 + COL_PM_CALORIES + " REAL, "
                 + COL_PM_FAT + " REAL, "
                 + COL_PM_CARBS + " REAL, "
                 + COL_PM_SUGAR + " REAL, "
                 + COL_PM_PROTEIN + " REAL, "
                 + COL_PM_SALT + " REAL, "
                 + COL_PM_FIBER + " REAL);"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PMC + " (" + COL_PMC_NAME + " TEXT PRIMARY KEY);");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CM + " ("
                + COL_CM_DATE + " TEXT, "
                + COL_CM_INDEX + " TEXT, "
                + COL_CM_AMOUNT + " REAL, " +
                "PRIMARY KEY (" + COL_CM_INDEX + ", " + COL_CM_AMOUNT + "));"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_WE + " ("
                + COL_WE_PLAN_NAME + " TEXT, "
                + COL_WE_ROUTINE_NAME + " TEXT, "
                + COL_WE_EXERCISE_NAME + " TEXT, "
                + COL_WE_SETS + " INTEGER, "
                + COL_WE_REPETITIONS + " INTEGER, "
                + COL_WE_WEIGHT + " REAL"
                + ");");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_WP + " ("+ COL_WP_NAME + " TEXT PRIMARY KEY);");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_WR + " ("+ COL_WR_PLAN_NAME + " TEXT, " + COL_WR_ROUTINE_NAME + " TEXT, PRIMARY KEY (" + COL_WR_PLAN_NAME + ", " + COL_WR_ROUTINE_NAME + "));");

        // Create table settings
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_S_GOAL + " ("
                + COL_S_INDEX + " INTEGER PRIMARY KEY, "
                + COL_S_GOAL_CALORIES + " REAL, "
                + COL_S_GOAL_FAT + " REAL, "
                + COL_S_GOAL_CARBS + " REAL, "
                + COL_S_GOAL_PROTEIN + " REAL);");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_S_LANG + " ("
                + COL_S_INDEX + " INTEGER PRIMARY KEY, "
                + COL_S_LANG + " TEXT);");


        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PM + " VALUES('000000000', 'Apple (100 g)', 'Fruits and Vegetables', 52, 0.17, 0, 13.81, 10.39, 0.26,7)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PM + " VALUES('000000001', 'Banana (100 g)', 'Fruits and Vegetables', 95, 0.33, 0.0, 22.84, 12.23, 1.0,5)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PM + " VALUES('000000002', 'Orange (100 g)', 'Fruits and Vegetables', 95, 0.33, 0.0, 22.84, 12.23, 1.0,5)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PM + " VALUES('000000003', 'Fish (100 g)', 'Fruits and Vegetables', 95, 0.33, 0.0, 22.84, 12.23, 1.0,5)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PM + " VALUES('000000004', 'Potatos (100 g)', 'Fruits and Vegetables', 95, 0.33, 0.0, 22.84, 12.23, 1.0,5)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PM + " VALUES('000000005', 'Beef (100 g)', 'Grains and Cereals', 95, 0.33, 0.0, 22.84, 12.23, 1.0,5)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PM + " VALUES('000000006', 'Milk (1 l)', 'Drinks', 95, 0.33, 0.0, 22.84, 12.23, 1.0,5)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PM + " VALUES('000000007', 'Bread (100 g)', 'Grains and Cereals', 95, 0.33, 0.0, 22.84, 12.23, 1.0,5)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PM + " VALUES('000000008', 'Rice (100 g)', 'Supplements', 95, 0.33, 0.0, 22.84, 12.23, 1.0,5)");

        // Add meal categories
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Fruits and Vegetables');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Drinks');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Grains and Cereals');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Spices, Sauces, Oils');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Veggie Products');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Sweets and Spread');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Animal Products');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Convenience Foods');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Supplements');");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_PMC + " VALUES('Custom');");

        // Workout plan names
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WP + " VALUES('Workout Plan')");

        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WR + " VALUES('Workout Plan', 'Push Day')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WR + " VALUES('Workout Plan', 'Pull Day')");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WR + " VALUES('Workout Plan', 'Leg Day')");

        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WE + " VALUES('Workout Plan', 'Push Day', 'Chest Press', 3, 6, 20)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WE + " VALUES('Workout Plan', 'Push Day', 'Shoulder Press', 3, 6, 20)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WE + " VALUES('Workout Plan', 'Pull Day', 'Lat Pull Down', 3, 8, 20)");
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_WE + " VALUES('Workout Plan', 'Leg Day', 'Leg Press', 3, 6, 20)");

        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_S_GOAL + " VALUES(0, 2500, 200, 100, 160)");

        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_S_LANG + " VALUES(0, 'system')");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PM);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PMC);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CM);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BD);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_S_GOAL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_S_LANG);

        onCreate(sqLiteDatabase);
    }



    public Cursor getPresetMealsSimpleAllCategories() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " + COL_PM_INDEX + ", " + COL_PM_NAME + ", " + COL_PM_CALORIES + " FROM " + TABLE_PM + " ORDER BY " + COL_PM_NAME +  " ASC LIMIT 100;",
                    null
            );
        }

        return cursor;
    }

    public Cursor getPresetMealsSimpleFromCategory(String category) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " + COL_PM_INDEX + ", " + COL_PM_NAME + ", " + COL_PM_CALORIES + " FROM " + TABLE_PM + " WHERE " + COL_PM_CATEGORY + "='" + category + "'" + " ORDER BY " + COL_PM_NAME +  " ASC;",
                    null
            );
        }

        return cursor;
    }

    public Cursor getPresetMealDetails(String foodUUID) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_PM + " WHERE " + COL_PM_INDEX + "='" + foodUUID + "';", null);
        }

        return cursor;
    }

    public Cursor getPresetMealCategories() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT " + COL_PMC_NAME + " FROM " + TABLE_PMC + " ORDER BY " + COL_PMC_NAME + " ASC;", null);
        }

        return cursor;
    }

    public Cursor getConsumedMeals(String date) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " + TABLE_PM + "." + COL_PM_INDEX + ", " + TABLE_PM + "." + COL_PM_NAME + ", " + TABLE_PM + "." + COL_PM_CALORIES + ", " + TABLE_CM + "." + COL_CM_AMOUNT + " " +
                            "FROM " + TABLE_CM + " " +
                            "LEFT JOIN " + TABLE_PM + " ON " + TABLE_CM + "." + COL_CM_INDEX + "=" + TABLE_PM + "." + COL_PM_INDEX + " " +
                            "WHERE " + TABLE_CM + "." + COL_CM_DATE + "='" + date + "';",
                    null);
        }

        return cursor;
    }

    public Cursor getConsumedMealsSums(String date) {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {

            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " +
                            "SUM (" + TABLE_PM + "." + COL_PM_CALORIES + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_FAT + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_CARBS + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_SUGAR + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_PROTEIN + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_SALT + " * " + TABLE_CM + "." + COL_CM_AMOUNT + "), " +
                            "SUM (" + TABLE_PM + "." + COL_PM_FIBER + " * " + TABLE_CM + "." + COL_CM_AMOUNT + ") " +

                            "FROM " + TABLE_CM + " " +
                            "LEFT JOIN " + TABLE_PM + " ON " + TABLE_CM + "." + COL_CM_INDEX + "=" + TABLE_PM + "." + COL_PM_INDEX + " " +
                            "WHERE " + TABLE_CM + "." + COL_CM_DATE + "='" + date + "';",
                    null);

        }

        return cursor;
    }

    public void addOrReplacePresetMeal(String uuid, String name, String category, double[] data) {

        // Get database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COL_PM_INDEX, uuid);
        cv.put(COL_PM_NAME, name);
        cv.put(COL_PM_CATEGORY, category);
        cv.put(COL_PM_CALORIES, data[0]);
        cv.put(COL_PM_FAT, data[1]);
        cv.put(COL_PM_CARBS, data[2]);
        cv.put(COL_PM_SUGAR, data[3]);
        cv.put(COL_PM_PROTEIN, data[4]);
        cv.put(COL_PM_SALT, data[5]);
        cv.put(COL_PM_FIBER, data[6]);
   ;


        // Insert data into database
        long result = sqLiteDatabase.replaceOrThrow(TABLE_PM, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        }

    }

    public void addOrReplaceConsumedMeal(String date, String mealUUID, double amount) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_CM_INDEX, mealUUID);
        cv.put(COL_CM_DATE, date);
        cv.put(COL_CM_AMOUNT, amount);

        sqLiteDatabase.replaceOrThrow(TABLE_CM, null, cv);
    }
    public Cursor getWorkoutRoutines(String planName) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            // cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT " + COL_WE_ROUTINE_NAME + " FROM " + TABLE_WE + " WHERE " + COL_WE_PLAN_NAME + "='" + plan +"';", null);
            cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT " + COL_WR_ROUTINE_NAME + " FROM " + TABLE_WR + " WHERE " + COL_WR_PLAN_NAME + "='" + planName + "';", null);
        }

        return cursor;
    }

    public void removeConsumedMeal(String date, String mealUUID) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.delete(TABLE_CM, COL_CM_INDEX + "= ? AND " + COL_CM_DATE + "= ?", new String[] {mealUUID, date});
        }
    }


    public Cursor getWorkoutPlans() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery("SELECT DISTINCT * FROM " + TABLE_WP + ";", null);
        }

        return cursor;
    }


    public Cursor getWorkoutExercises(String plan, String routine) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " + COL_WE_EXERCISE_NAME + ", " + COL_WE_SETS + ", " + COL_WE_REPETITIONS + ", " + COL_WE_WEIGHT +
                            " FROM " + TABLE_WE +
                            " WHERE " + COL_WE_PLAN_NAME + "='" + plan + "' AND " + COL_WE_ROUTINE_NAME + "='" + routine +"';",
                    null);
        }

        return cursor;
    }

    public void addWorkoutPlan(String planName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.execSQL("INSERT OR REPLACE INTO " + TABLE_WP + " VALUES('" + planName + "')");
        }
    }

    public void addWorkoutRoutine(String planName, String routineName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase != null) {
            ContentValues cv = new ContentValues();
            cv.put(COL_WR_PLAN_NAME, planName);
            cv.put(COL_WR_ROUTINE_NAME, routineName);

            sqLiteDatabase.replaceOrThrow(TABLE_WR, null, cv);
        }
    }

    public void addWorkoutExercise(String planName, String routineName, String exerciseName, int sets, int repetitions, double weight) {
        // Get database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Create content values to put into the database
        ContentValues cv = new ContentValues();
        cv.put(COL_WE_PLAN_NAME, planName);
        cv.put(COL_WE_ROUTINE_NAME, routineName);
        cv.put(COL_WE_EXERCISE_NAME, exerciseName);
        cv.put(COL_WE_SETS, sets);
        cv.put(COL_WE_REPETITIONS, repetitions);
        cv.put(COL_WE_WEIGHT, weight);

        long result = sqLiteDatabase.replaceOrThrow(TABLE_WE, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateWorkoutPlanName(String oldPlanName, String newPlanName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        if (sqLiteDatabase != null) {
            // Remove old plan name from table "plans"
            sqLiteDatabase.delete(TABLE_WP, COL_WP_NAME + "= ?", new String[] {oldPlanName});

            // Add new plan name to table "plans"
            ContentValues cv = new ContentValues();
            cv.put(COL_WP_NAME, newPlanName);
            sqLiteDatabase.insertOrThrow(TABLE_WP, null, cv);

            // Update plan names in table "exercises"
            cv = new ContentValues();
            cv.put(COL_WE_PLAN_NAME, newPlanName);
            sqLiteDatabase.update(TABLE_WE, cv, COL_WE_PLAN_NAME + "= ?", new String[]{oldPlanName});

            // Update plan names in table "routines"
            cv = new ContentValues();
            cv.put(COL_WR_PLAN_NAME, newPlanName);
            sqLiteDatabase.update(TABLE_WR, cv, COL_WR_PLAN_NAME + "= ?", new String[]{oldPlanName});
        }
    }

    public void updateWorkoutRoutineName(String planName, String oldRoutineName, String newRoutineName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        if (sqLiteDatabase != null) {
            ContentValues cv = new ContentValues();
            cv.put(COL_WR_ROUTINE_NAME, newRoutineName);
            sqLiteDatabase.update(TABLE_WR, cv, COL_WR_PLAN_NAME + "= ? AND " + COL_WR_ROUTINE_NAME + "= ?", new String[] {planName, oldRoutineName});

            cv = new ContentValues();
            cv.put(COL_WE_ROUTINE_NAME, newRoutineName);
            sqLiteDatabase.update(TABLE_WE, cv, COL_WE_ROUTINE_NAME + "= ?", new String[]{oldRoutineName});
        }
    }

    public void deleteWorkoutPlan(String planName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_WP, COL_WP_NAME + "= ?", new String[] {planName});

        sqLiteDatabase.delete(TABLE_WE, COL_WE_PLAN_NAME + "= ?", new String[] {planName});
    }

    public void deleteWorkoutRoutine(String planName, String routineName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_WR, COL_WR_PLAN_NAME + "= ? AND " + COL_WR_ROUTINE_NAME + "= ?", new String[] {planName, routineName});

        sqLiteDatabase.delete(TABLE_WE, COL_WE_PLAN_NAME + "= ? AND " + COL_WE_ROUTINE_NAME + "= ?", new String[] {planName, routineName});
    }

    public void deleteWorkoutExercise(String planName, String routineName, String exerciseName) {
        // Get database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if (sqLiteDatabase != null) {
            sqLiteDatabase.delete(TABLE_WE, COL_WE_PLAN_NAME + "= ? AND " + COL_WE_ROUTINE_NAME + "= ? AND " + COL_WE_EXERCISE_NAME + "= ?", new String[] {planName, routineName, exerciseName});
        }

    }
    public void setSettingsLanguage(String language) {
        // Get database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COL_S_INDEX, 0);
        cv.put(COL_S_LANG, language);

        long result = sqLiteDatabase.replaceOrThrow(TABLE_S_LANG, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save settings", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Saved settings. Close App to apply changes.", Toast.LENGTH_SHORT).show();
        }
    }


    public Cursor getSettingsGoals() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(
                    "SELECT " + COL_S_GOAL_CALORIES + ", " + COL_S_GOAL_FAT + ", " + COL_S_GOAL_CARBS + ", " + COL_S_GOAL_PROTEIN +
                    " FROM " + TABLE_S_GOAL +
                    " WHERE " + COL_S_INDEX + "=0;",
                    null);
        }

        return cursor;
    }

    public void addDataBody(String date, double weight, double chest, double belly, double butt, double waist, double arm_r, double arm_l, double leg_r, double leg_l){


        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BD_DATE, date);
        cv.put(COLUMN_BD_WEIGHT, weight);
        cv.put(COLUMN_BD_CHEST, chest);
        cv.put(COLUMN_BD_BELLY, belly);
        cv.put(COLUMN_BD_BUTT, butt);
        cv.put(COLUMN_BD_WAIST, waist);
        cv.put(COLUMN_BD_ARM_R, arm_r);
        cv.put(COLUMN_BD_ARM_L, arm_l);
        cv.put(COLUMN_BD_LEG_R, leg_r);
        cv.put(COLUMN_BD_LEG_L, leg_l);

        long result = sqLiteDatabase.replaceOrThrow(TABLE_BD, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        }
    }


    public void setSettingsGoals(double goalCalories, double goalFat, double goalCarbs, double goalProtein) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // Create content values to put into the database
        ContentValues cv = new ContentValues();

        cv.put(COL_S_INDEX, 0);
        cv.put(COL_S_GOAL_CALORIES, goalCalories);
        cv.put(COL_S_GOAL_FAT, goalFat);
        cv.put(COL_S_GOAL_CARBS, goalCarbs);
        cv.put(COL_S_GOAL_PROTEIN, goalProtein);

        long result = sqLiteDatabase.replaceOrThrow(TABLE_S_GOAL, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save settings", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Saved settings", Toast.LENGTH_SHORT).show();
        }
    }




}
