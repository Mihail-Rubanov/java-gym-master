package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> timetable = new HashMap<>();
    private Map<Coach, Integer> numberOfTrainings = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> targetMap =
                timetable.getOrDefault(trainingSession.getDayOfWeek(),
                        new TreeMap<>());
        ArrayList<TrainingSession> targetList = targetMap.getOrDefault(trainingSession.getTimeOfDay(),
                new ArrayList<>());
        targetList.add(trainingSession);
        targetMap.put(trainingSession.getTimeOfDay(), targetList);
        timetable.put(trainingSession.getDayOfWeek(), targetMap);//сохраняем занятие в расписании
        if (numberOfTrainings.get(trainingSession.getCoach()) == null) {
            numberOfTrainings.put(trainingSession.getCoach(), 1);
        } else {
            int count = numberOfTrainings.get(trainingSession.getCoach());
            numberOfTrainings.put(trainingSession.getCoach(), count + 1);
        }
    }

    public TreeMap<TimeOfDay, ArrayList<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);//как реализовать, тоже непонятно, но сложность должна быть О(1)
    }

    public ArrayList<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).get(timeOfDay); //как реализовать, тоже непонятно, но сложность должна быть О(1)
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        List<CounterOfTrainings> counterList = new ArrayList<>();
        for (Map.Entry<Coach, Integer> coach: numberOfTrainings.entrySet()) {
            counterList.add(new CounterOfTrainings(coach.getKey(), coach.getValue()));
        }
        Collections.sort(counterList);
        return counterList.reversed();
    }
}
