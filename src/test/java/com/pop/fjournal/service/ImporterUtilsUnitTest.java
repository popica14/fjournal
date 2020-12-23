package com.pop.fjournal.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.List;

import com.pop.fjournal.domain.Meal;
import com.pop.fjournal.domain.User;
import com.pop.fjournal.domain.enumeration.MealType;
import com.pop.fjournal.security.SecurityUtils;
import com.pop.fjournal.service.dto.MealDTO;
import com.pop.fjournal.service.dto.WeightDTO;
import com.pop.fjournal.service.dto.utilDtos.ImporterEntry;
import com.pop.fjournal.service.impl.ImporterUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the {@link SecurityUtils} utility class.
 */
public class ImporterUtilsUnitTest {

    String testSeparator = "###";
    String testEntry = "22.09.2020###"
        + "1 cafea + lapte veg., 06.30: Toast cu Avocado si ou###2 felii, 50 g###"
        + "13.00: Ciuperci cu orez mexican###150g orez + leg###"
        + "16.30: 1 banana###"
        + "###19.00: Supa crema linte###350 ml###06.15: 63,2 kg###";

    Meal breakfastTest;
    Meal snackTest;
    Meal dinnerTest;
    Meal lunchTest;
    int mealCountTest = 4;
    User testUser;
    WeightDTO weightDTO;
    Float weightTest = 63.2f;
    ZonedDateTime weightTestDateTime;

    @BeforeEach
    public void init() {
        ZonedDateTime testDateTime = ZonedDateTime.now();
        testDateTime = testDateTime.withHour(6);
        testDateTime = testDateTime.withMinute(30);
        testDateTime = testDateTime.withDayOfMonth(22);
        testDateTime = testDateTime.withMonth(9);
        testDateTime = testDateTime.withYear(2020);
        testDateTime = testDateTime.withSecond(0);
        testDateTime = testDateTime.withNano(0);

         weightTestDateTime= testDateTime.minusMinutes(15);
        weightDTO =  new WeightDTO();

        breakfastTest = new Meal();
        breakfastTest.setDescription("1 cafea + lapte veg., Toast cu Avocado si ou");
        breakfastTest.setType(MealType.BREAKFAST);
        breakfastTest.setDate(testDateTime);
        breakfastTest.setPortionSize("2 felii, 50 g");

        lunchTest = new Meal();
        lunchTest.setDescription("Ciuperci cu orez mexican");
        lunchTest.setType(MealType.LUNCH);
        lunchTest.setDate(testDateTime.withHour(13).withMinute(0));
        lunchTest.setPortionSize("150g orez + leg");

        dinnerTest = new Meal();
        dinnerTest.setDescription("Supa crema linte");
        dinnerTest.setType(MealType.DINNER);
        dinnerTest.setDate(testDateTime.withHour(19).withMinute(0));
        dinnerTest.setPortionSize("350 ml");


        testUser = new User();
        testUser.setLogin("test login");
        testUser.setEmail("test email @ test.co.uk");
    }

    @Test
    public void testImporterParserMethod() throws ParseException {
        ImporterEntry
            result  = ImporterUtils.getMealsAndWeightFromJournalTextEntry(testEntry, testSeparator, testUser);

        List<MealDTO> mealFromJournalTextEntryList = result.getMeals();
        List<WeightDTO> weightFromJournalTextEntryList = result.getWeights();

        assertThat(weightFromJournalTextEntryList.size()).isEqualTo(1);

        WeightDTO weightFromJournalTextEntry = weightFromJournalTextEntryList.get(0);

        assertThat(weightFromJournalTextEntry.getValue()).isEqualTo(weightTest);
        assertThat(weightFromJournalTextEntry.getDate()).isEqualTo(weightTestDateTime);
        assertThat(weightFromJournalTextEntry.getMyWeigthLogin()).isEqualTo(testUser.getLogin());

        assertThat(mealFromJournalTextEntryList.size()).isEqualTo(mealCountTest);

        MealDTO breakfastResult = mealFromJournalTextEntryList.get(0);
        MealDTO lunchResult = mealFromJournalTextEntryList.get(1);
        MealDTO dinnerResult = mealFromJournalTextEntryList.get(2);

        assertThat(breakfastResult.getDescription()).isEqualTo(breakfastTest.getDescription());
        assertThat(breakfastResult.getDate()).isEqualTo(breakfastTest.getDate());
        assertThat(breakfastResult.getType()).isEqualTo(breakfastTest.getType());
        assertThat(breakfastResult.getMyMealId()).isEqualTo(testUser.getId());
        assertThat(breakfastResult.getMyMealLogin()).isEqualTo(testUser.getLogin());
        assertThat(breakfastResult.getPortionSize()).isEqualTo(breakfastTest.getPortionSize());

        assertThat(lunchResult.getDescription()).isEqualTo(lunchTest.getDescription());
        assertThat(lunchResult.getDate()).isEqualTo(lunchTest.getDate());
        assertThat(lunchResult.getType()).isEqualTo(lunchTest.getType());
        assertThat(lunchResult.getMyMealId()).isEqualTo(testUser.getId());
        assertThat(lunchResult.getMyMealLogin()).isEqualTo(testUser.getLogin());
        assertThat(lunchResult.getPortionSize()).isEqualTo(lunchTest.getPortionSize());

        assertThat(dinnerResult.getDescription()).isEqualTo(dinnerTest.getDescription());
        assertThat(dinnerResult.getDate()).isEqualTo(dinnerTest.getDate());
        assertThat(dinnerResult.getType()).isEqualTo(dinnerTest.getType());
        assertThat(dinnerResult.getMyMealId()).isEqualTo(testUser.getId());
        assertThat(dinnerResult.getMyMealLogin()).isEqualTo(testUser.getLogin());
        assertThat(dinnerResult.getPortionSize()).isEqualTo(dinnerTest.getPortionSize());

    }
}
