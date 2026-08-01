package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {
    Timetable timetable;
    TrainingSession singleTrainingSession;
    TrainingSession thursdayChildTrainingSession;
    TrainingSession thursdayAdultTrainingSession;
    Group groupChild;
    Group groupAdult;
    Coach coach;
    Coach coach2;
    Coach coach3;
    Coach coach4;
    TreeMap<TimeOfDay, ArrayList<TrainingSession>> testMap;

    @BeforeEach
    void BeforeEach() {
        timetable = new Timetable();

        groupChild = new Group("Акробатика для детей", Age.CHILD, 60);

        coach = new Coach("Васильев", "Николай", "Сергеевич");
        coach2 = new Coach("Антипенко", "Владимир", "Владимирович");
        coach3 = new Coach("Кириллов", "Никита", "Михайлович");
        coach4 = new Coach("Якубенко", "Сева", "Анотольевич");

        singleTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);

        groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);
        testMap = new TreeMap<>();
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        assertEquals(timetable.getTrainingSessionsForDay(singleTrainingSession.getDayOfWeek()).size(), 1);
        //Проверить, что за понедельник вернулось одно занятие
        assertEquals(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY), testMap);
        //Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        ArrayList<TrainingSession> hours13 = new ArrayList<>();
        hours13.add(thursdayChildTrainingSession);
        ArrayList<TrainingSession> hours20 = new ArrayList<>();
        hours20.add(thursdayAdultTrainingSession);

        TreeMap<TimeOfDay, ArrayList<TrainingSession>> testThursday = new TreeMap<>();
        testThursday.put(new TimeOfDay(20, 0), hours20);
        testThursday.put(new TimeOfDay(13, 0), hours13);

        assertEquals(timetable.getTrainingSessionsForDay(singleTrainingSession.getDayOfWeek()).size(), 1);
        // Проверить, что за понедельник вернулось одно занятие
        assertEquals(timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY), testThursday);
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        assertEquals(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY), testMap);
        // Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        assertEquals(timetable.getTrainingSessionsForDayAndTime(singleTrainingSession.getDayOfWeek(),
                singleTrainingSession.getTimeOfDay()).size(), 1);
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        ArrayList<TrainingSession> testList = new ArrayList<>();
        assertEquals(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)), testList);
    }

    @Test
    void testGetCountByCoaches() {
        TrainingSession gym1 = new TrainingSession(groupChild, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0));
        TrainingSession gym2 = new TrainingSession(groupChild, coach2,
                DayOfWeek.FRIDAY, new TimeOfDay(18, 0));
        TrainingSession gym3 = new TrainingSession(groupChild, coach2,
                DayOfWeek.SUNDAY, new TimeOfDay(13, 0)); // 3 тренировки для тренера 2
        TrainingSession gym4 = new TrainingSession(groupAdult, coach3,
                DayOfWeek.TUESDAY, new TimeOfDay(19, 0));
        TrainingSession gym5 = new TrainingSession(groupAdult, coach3,
                DayOfWeek.THURSDAY, new TimeOfDay(19, 0)); // 2 тренировки для тренера 3
        TrainingSession gym6 = new TrainingSession(groupAdult, coach4,
                DayOfWeek.SATURDAY, new TimeOfDay(15, 0)); // 1 тренировка для тренера 4

        timetable.addNewTrainingSession(gym6);
        timetable.addNewTrainingSession(gym5);
        timetable.addNewTrainingSession(gym4);
        timetable.addNewTrainingSession(gym3);
        timetable.addNewTrainingSession(gym2);
        timetable.addNewTrainingSession(gym1);

        assertEquals(timetable.getCountByCoaches().size(), 4);

        CounterOfTrainings c1 = new CounterOfTrainings(coach, 4);
        CounterOfTrainings c2 = new CounterOfTrainings(coach2, 3);
        CounterOfTrainings c3 = new CounterOfTrainings(coach3, 2);
        CounterOfTrainings c4 = new CounterOfTrainings(coach4, 1);
        List<CounterOfTrainings> testList = new ArrayList<>();

        testList.add(c3);
        testList.add(c4);
        testList.add(c1);
        testList.add(c2);

        assertNotEquals(timetable.getCountByCoaches(), testList.reversed());

        Collections.sort(testList);
        assertEquals(timetable.getCountByCoaches(), testList.reversed());
    }

}
