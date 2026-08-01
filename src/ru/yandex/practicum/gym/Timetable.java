package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek targetDay = trainingSession.getDayOfWeek();
        TimeOfDay targetTime = trainingSession.getTimeOfDay(); // Переменные метода

        TreeMap<TimeOfDay, ArrayList<TrainingSession>> targetMap = timetable.getOrDefault(targetDay, new TreeMap<>());
        ArrayList<TrainingSession> targetList = targetMap.getOrDefault(targetTime, new ArrayList<>());
        targetList.add(trainingSession);
        targetMap.put(targetTime, targetList);
        timetable.put(targetDay, targetMap); //сохраняем занятие в расписании
    }

    public TreeMap<TimeOfDay, ArrayList<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (timetable.get(dayOfWeek) != null) {
            return timetable.get(dayOfWeek);//как реализовать, тоже непонятно, но сложность должна быть О(1)
        } else {
            return new TreeMap<>();
        }
    }

    public ArrayList<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> targetMap = timetable.get(dayOfWeek);

        if (targetMap != null) {
            if (targetMap.get(timeOfDay) != null) {
                return targetMap.get(timeOfDay); //как реализовать, тоже непонятно, но сложность должна быть О(1)
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> numberOfTrainings = new HashMap<>();
        List<CounterOfTrainings> counterList = new ArrayList<>();

        for (Map.Entry<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> day: timetable.entrySet()) {
            Map<TimeOfDay, ArrayList<TrainingSession>> daySessions = day.getValue();
            for (Map.Entry<TimeOfDay, ArrayList<TrainingSession>> time: daySessions.entrySet()) {
                List<TrainingSession> timeSessions = time.getValue();
                for (TrainingSession session: timeSessions) {
                    Coach coach = session.getCoach();
                    if (numberOfTrainings.containsKey(coach)) {
                        int count = numberOfTrainings.get(coach);
                        numberOfTrainings.put(coach, count + 1);
                    } else {
                        numberOfTrainings.put(coach, 1);
                    }
                }
            }
        }
        for (Map.Entry<Coach, Integer> coach: numberOfTrainings.entrySet()) {
            counterList.add(new CounterOfTrainings(coach.getKey(), coach.getValue()));
        }
        Collections.sort(counterList);
        return counterList.reversed();
    }
}
