package io.github.omriberger;

import java.util.ArrayList;
import java.util.List;

public class ScheduleData {

    public static class WeeklySchedule {
        public boolean status;
        public List<DaySchedule> data = new ArrayList<>();
    }

    public static class DaySchedule {
        public int dayIndex;
        public List<HourData> hoursData = new ArrayList<>();
    }

    public static class HourData {
        public int hour;
        public String hourName;
        public List<Lesson> scheduale = new ArrayList<>();
        public List<Change> changes = new ArrayList<>();
        public List<Event> events = new ArrayList<>();
        public List<Exam> exams = new ArrayList<>();
    }

    public static class Lesson {
        public int day;
        public int hour;
        public int roomID;
        public int studyGroupID;
        public String subject;
        public String subjectLevel;
        public String room;
        public String teacherPrivateName;
        public String teacherLastName;
        public Object classes;
        public Object capsule;
        public Boolean isPartani;
        public List<Change> changes = new ArrayList<>();
    }

    public static class Change {
        // define fields if needed
    }

    public static class Event {
        // define fields if needed
    }

    public static class Exam {
        public int id;
        public String title;
        public String date;
        public int from_hour;
        public int to_hour;
        public String textualType;
        public Object job;
        public Object extraJobData;
        public Object payment;
        public Object note;
        public String supervisors;
        public String groups;
        public Integer studyGroupID;
        public String rooms;
        public Object relatedStudents;
    }

    public static String getHourFromNumber(int hour) {
        switch (hour) {
            case 0:  return "7:45-8:25";
            case 1:  return "8:30-9:15";
            case 2:  return "9:15-10:00";
            case 3:  return "10:15-11:00";
            case 4:  return "11:00-11:45";
            case 5:  return "12:00-12:45";
            case 6:  return "12:45-13:30";
            case 7:  return "13:45-14:25";
            case 8:  return "14:25-15:05";
            case 9:  return "15:15-15:55";
            case 10:  return "15:55-16:35";
            case 11:  return "16:40-17:20";
            case 12:  return "17:20-18:00";
            default:
                throw new IllegalStateException("Unexpected value: " + hour);
        }
    }
}
