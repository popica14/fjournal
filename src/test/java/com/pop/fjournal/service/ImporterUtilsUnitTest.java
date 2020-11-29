package com.pop.fjournal.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.time.Instant;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import com.pop.fjournal.domain.Meal;
import com.pop.fjournal.domain.User;
import com.pop.fjournal.domain.enumeration.MealType;
import com.pop.fjournal.repository.UserRepository;
import com.pop.fjournal.security.SecurityUtils;
import com.pop.fjournal.service.impl.ImporterUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    int mealCountTest = 3;
    User testUser;

    @BeforeEach
    public void init() {
        Calendar testCalendar = new GregorianCalendar(2020, 9, 22, 6, 30,0);
        testCalendar.add(2,-1); // TODO Not correct!!!!!
        breakfastTest = new Meal();
        breakfastTest.setDescription("1 cafea + lapte veg., Toast cu Avocado si ou");
        breakfastTest.setDate(testCalendar.toInstant());
        breakfastTest.setType(MealType.BREAKFAST);

        lunchTest = new Meal();
        lunchTest.setDescription("Ciuperci cu orez mexican");
        lunchTest.setDate(testCalendar.toInstant());
        lunchTest.setType(MealType.LUNCH);


        testUser = new User();
        testUser.setLogin("test login");
        testUser.setEmail("test email @ test.co.uk");
    }

    @Test
    public void testImporterParserMethod() throws ParseException {

        List<Meal> mealFromJournalTextEntry = ImporterUtils.getMealsFromJournalTextEntry(testEntry, testSeparator, testUser);

        assertThat(mealFromJournalTextEntry.size()).isEqualTo(mealCountTest);

        Meal breakfastResult = mealFromJournalTextEntry.get(0);

        assertThat(breakfastResult.getDescription()).isEqualTo(breakfastTest.getDescription());
        assertThat(breakfastResult.getDate()).isEqualTo(breakfastTest.getDate());
        assertThat(breakfastResult.getType()).isEqualTo(breakfastTest.getType());
        assertThat(breakfastResult.getMyMeal().getEmail()).isEqualTo(testUser.getEmail());
    }
}
