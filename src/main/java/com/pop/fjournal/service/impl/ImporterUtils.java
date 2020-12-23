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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

import com.pop.fjournal.domain.Importer;
import com.pop.fjournal.domain.User;
import com.pop.fjournal.domain.enumeration.MealType;
import com.pop.fjournal.repository.WeightRepository;
import com.pop.fjournal.service.dto.MealDTO;
import com.pop.fjournal.service.dto.WeightDTO;
import com.pop.fjournal.service.dto.utilDtos.ImporterEntry;

/**
 * Utility class for Importer Service.
 */
public final class ImporterUtils {

    private final WeightRepository weightService;

    private ImporterUtils(WeightRepository weightService) {
        this.weightService = weightService;
    }

    /**
     * Create a new journal entry Meal from a text Journal entry
     *
     * @return the Meal.
     */
    public static ImporterEntry getMealsAndWeightFromJournalTextEntry(String journalEntry, String separator,
                                                                      User owner
    ) throws ParseException {
        try {
            //22.09.2020###1 cafea + lapte veg., 06.30: Toast cu Avocado si ou###2 felii, 50 g###
            // 13.00: Ciuperci cu orez mexican###150g orez + leg###
            // 16.30: 1 banana######
            // 19.00: Supa crema linte###350 ml###
            // 06.15: 63,2 kg###
            List<MealDTO> retValMealList = new ArrayList<>();
            List<WeightDTO> retValWeightsList = new ArrayList<>();

            String[] entryProperties = journalEntry.split(separator);

            String dateText = entryProperties[0];
            ZonedDateTime date = getDateFromText(dateText);

            String breakfast = entryProperties[1];
            String breakfastQuantity = entryProperties[2];
            if (getDateAndTimeFromText(date, breakfast).getHour() < 10) {
                retValMealList.add(getBreakfastFromText(date, owner, breakfast, breakfastQuantity));
            } else {
                retValMealList.add(getSnackFromText(date, owner, breakfast, breakfastQuantity));
            }

            String lunch = entryProperties[3];
            String lunchQuantity = entryProperties[4];
            int lunchHoursInDay = getDateAndTimeFromText(date, lunch).getHour();
            if (lunchHoursInDay > 10 && lunchHoursInDay < 12) {
                retValMealList.add(getSnackFromText(date, owner, breakfast, breakfastQuantity));
            } else {
                retValMealList.add(getLunchFromText(date, owner, lunch, lunchQuantity));
            }

            String dinner = entryProperties[5];
            String dinnerQuantity = entryProperties[6];
            getDateAndTimeFromText(date, dinner).getHour();
            if (lunchHoursInDay > 10 && lunchHoursInDay < 12) {
                retValMealList.add(getSnackFromText(date, owner, breakfast, breakfastQuantity));
            } else {
                retValMealList.add(getLunchFromText(date, owner, lunch, lunchQuantity));
            }

            retValMealList.add(getDinnerFromText(date, owner, dinner, dinnerQuantity));

            String snack = entryProperties[7];
            String snackQuantity = entryProperties[8];
            retValMealList.add(getSnackFromText(date, owner, snack, snackQuantity));

            if (entryProperties.length > 9) {
                if (!entryProperties[9].isEmpty()) {
                    if (entryProperties[9].contains("kg")) {
                        retValWeightsList.add(getWeightDTOFromString(date, owner, entryProperties[9]));
                    } else {
                        String nightShift = entryProperties[9];
                        retValMealList.add(getNightShiftFromText(date, owner, nightShift));
                    }
                }
            }

            ImporterEntry retVal = new ImporterEntry(retValMealList, retValWeightsList);
            return retVal;
        } catch (Exception e) {
            throw e;
        }
    }

    private static WeightDTO getWeightDTOFromString(ZonedDateTime date, User owner, String entryProperty) {
        WeightDTO weightDTO = new WeightDTO();
        weightDTO.setDate(getDateAndTimeFromText(date, entryProperty));

        weightDTO.setMyWeigthId(owner.getId());
        weightDTO.setMyWeigthLogin(owner.getLogin());
        Float weight = parseTextToWeight(entryProperty);
        if (weight > 0f) {
            weightDTO.setObservation("from importer");
        } else {
            weight = 60f;
            weightDTO.setObservation("from importer/ERROR: parsing weight");
        }
        weightDTO.setValue(weight);
        return weightDTO;
    }

    private static Float parseTextToWeight(String entryProperty) {

        String separator = " ";
        float weight;
        String[] split = entryProperty.split(separator);
        try {
            weight = Float.parseFloat(split[1].replace(',', '.'));
        } catch (NumberFormatException e) {
            weight = 0f;
        }
        return weight;
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

    private static MealDTO getNightShiftFromText(ZonedDateTime date, User owner, String nightShift) {
        return new MealDTO();
    }

    private static MealDTO getSnackFromText(ZonedDateTime date, User owner, String snack,
                                            String breakfastQuantity) {
        MealDTO snackMeal = new MealDTO();
        snackMeal.setType(MealType.SNACK);

        setMealProperties(date, owner, snack, breakfastQuantity, snackMeal);

        return snackMeal;
    }

    private static MealDTO getDinnerFromText(ZonedDateTime date, User owner, String dinner,
                                             String breakfastQuantity) {
        MealDTO dinnerMeal = new MealDTO();
        dinnerMeal.setType(MealType.LUNCH);

        setMealProperties(date, owner, dinner, breakfastQuantity, dinnerMeal);

        return dinnerMeal;
    }

    private static MealDTO getLunchFromText(ZonedDateTime date, User owner, String lunch,
                                            String breakfastQuantity) {
        MealDTO lunchMeal = new MealDTO();
        lunchMeal.setType(MealType.LUNCH);

        setMealProperties(date, owner, lunch, breakfastQuantity, lunchMeal);

        return lunchMeal;
    }

    private static void setMealProperties(ZonedDateTime date, User owner, String lunch, String breakfastQuantity,
                                          MealDTO lunchMeal) {
        lunchMeal.setDate(getDateAndTimeFromText(date, lunch));
        lunchMeal.setPortionSize(breakfastQuantity);
        lunchMeal.setMyMealId(owner.getId());
        lunchMeal.setMyMealLogin(owner.getLogin());

        String description = getDescriptionFromText(lunch);
        lunchMeal.setDescription(description);
    }

    private static MealDTO getBreakfastFromText(ZonedDateTime date, User owner, String breakfast,
                                                String breakfastQuantity) {

        MealDTO breakfastMeal = new MealDTO();
        breakfastMeal.setType(MealType.BREAKFAST);
        setMealProperties(date, owner, breakfast, breakfastQuantity, breakfastMeal);

        return breakfastMeal;
    }

    private static ZonedDateTime getDateAndTimeFromText(ZonedDateTime date, String mealText) {
        int hour = getHourFromText(mealText);
        int minute = getMinuteFromText(mealText);

        date = date.withHour(hour);
        date = date.withMinute(minute);

        return date;
    }

    private static String getDescriptionFromText(String breakfast) {
        int index = breakfast.indexOf(":");
        return breakfast.substring(0, index - 5).concat(breakfast.substring(index + 2));
    }

    private static int getHourFromText(String breakfast) {
        int index = breakfast.indexOf(":");
        return Integer.parseInt(breakfast.substring(index - 5, index - 3));
    }

    private static int getMinuteFromText(String breakfast) {
        int index = breakfast.indexOf(":");
        return Integer.parseInt(breakfast.substring(index - 2, index));
    }

    public static ImporterEntry importJournal(Importer importer) throws IOException {

        InputStream inputStream = new ByteArrayInputStream(importer.getFile());
        File importFile = new File("/tmp/output.txt");

        Files.copy(inputStream, importFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        Scanner scanner = new Scanner(new BufferedReader(new FileReader("xanadu.txt")));

        List<MealDTO> retValMealList = new ArrayList<>();
        List<WeightDTO> retValWeightList = new ArrayList<>();

        try {
            while (scanner.hasNextLine()) {
                String journalEntry = scanner.nextLine();
                ImporterEntry mealsAndWeightFromJournalTextEntry =
                    getMealsAndWeightFromJournalTextEntry(journalEntry, importer.getSeparator(), importer.getOwner());

                retValMealList.addAll(mealsAndWeightFromJournalTextEntry.getMeals());
                retValWeightList.addAll(mealsAndWeightFromJournalTextEntry.getWeights());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }

        ImporterEntry retVal = new ImporterEntry(retValMealList, retValWeightList);
        return retVal;
    }
}
