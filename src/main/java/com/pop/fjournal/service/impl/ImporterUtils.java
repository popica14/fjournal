package com.pop.fjournal.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

import com.pop.fjournal.domain.Importer;
import com.pop.fjournal.domain.Meal;
import com.pop.fjournal.domain.User;
import com.pop.fjournal.domain.enumeration.MealType;

/**
 * Utility class for Importer Service.
 */
public final class ImporterUtils {

    private ImporterUtils() {
    }

    /**
     * Create a new journal entry Meal from a text Journal entry
     *
     * @return the Meal.
     */
    public static List getMealsFromJournalTextEntry(String journalEntry, String separator, User owner) throws ParseException {
        try {
            List<Meal> retVal = new ArrayList<Meal>();

            String[] entryProperties = journalEntry.split(separator);

            String dateText = entryProperties[0];
            ZonedDateTime date = getDateFromText(dateText);

            String breakfast = entryProperties[1];
            String breakfastQuantity = entryProperties[2];
            retVal.add(getBreakfastFromText(date, owner, breakfast, breakfastQuantity));

            String lunch = entryProperties[3];
            String lunchQuantity = entryProperties[4];
            retVal.add(getLunchFromText(date, owner, lunch, lunchQuantity));

            String dinner = entryProperties[5];
            String dinnerQuantity = entryProperties[6];
            retVal.add(getDinnerFromText(date, owner, dinner, dinnerQuantity));

            String snack = entryProperties[7];
            String snackQuantity = entryProperties[8];
            retVal.add(getSnackFromText(date, owner, snack, snackQuantity));

            if (entryProperties.length > 8) {
                String nightShift = entryProperties[9];
                //                retVal.add(getNightShiftFromText(date, owner, nightShift));
            }

            return retVal;
        } catch (Exception e) {
            throw e;
        }
    }

    private static ZonedDateTime getDateFromText(String dateText) throws ParseException {

        Calendar calendar = new GregorianCalendar();
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = formatter.parse(dateText);
        calendar.setTime(date);

        ZonedDateTime dateTime = ZonedDateTime.now();
        dateTime = date.toInstant().atZone(dateTime.getZone());
        return dateTime;
    }

    private static Meal getNightShiftFromText(Calendar date, User owner, String nightShift) {
        return new Meal();
    }

    private static Meal getSnackFromText(ZonedDateTime date, User owner, String breakfast,
                                         String breakfastQuantity) {
        return new Meal();
    }

    private static Meal getDinnerFromText(ZonedDateTime date, User owner, String breakfast,
                                          String breakfastQuantity) {
        return new Meal();
    }

    private static Meal getLunchFromText(ZonedDateTime date, User owner, String breakfast,
                                         String breakfastQuantity) {
        return new Meal();
    }

    private static Meal getBreakfastFromText(ZonedDateTime date, User owner, String breakfast,
                                             String breakfastQuantity) {

        //1 cafea + lapte veg., 06.30: Toast cu Avocado si ou
        Meal breakfastMeal = new Meal();

        breakfastMeal.setDate(getMealDateAndTime(date, breakfast));

        breakfastMeal.setType(MealType.BREAKFAST);
        breakfastMeal.setMyMeal(owner);

        String description = getDescriptionFromText(breakfast);
        breakfastMeal.setDescription(description);

        return breakfastMeal;
    }

    private static ZonedDateTime getMealDateAndTime(ZonedDateTime date, String mealText) {
        int hour = getHourFromMealText(mealText);
        int minute = getMinuteFromMealText(mealText);

        date = date.withHour(hour);
        date = date.withMinute(minute);

        return date;
    }

    private static String getDescriptionFromText(String breakfast) {
        int index = breakfast.indexOf(":");
        return breakfast.substring(0, index - 5).concat(breakfast.substring(index + 2));
    }

    private static int getHourFromMealText(String breakfast) {
        int index = breakfast.indexOf(":");
        return Integer.parseInt(breakfast.substring(index - 5, index - 3));
    }

    private static int getMinuteFromMealText(String breakfast) {
        int index = breakfast.indexOf(":");
        return Integer.parseInt(breakfast.substring(index - 2, index));
    }

    public static void importJournal(Importer importer) throws IOException {

        InputStream inputStream = new ByteArrayInputStream(importer.getFile());
        File importFile = new File("/tmp/output.txt");

        Files.copy(inputStream, importFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        Scanner scanner = new Scanner(new BufferedReader(new FileReader("xanadu.txt")));
        try {
            while (scanner.hasNextLine()) {
                String journalEntry = scanner.nextLine();
                getMealsFromJournalTextEntry(journalEntry, importer.getSeparator(), importer.getOwner());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
