package io.github.omriberger;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ScheduleData {

    public static class WeeklySchedule {
        private boolean status;
        private List<DaySchedule> data = new ArrayList<>();
        private String message;
        private String errorId;
        private String errorDescription;
        private String errorHTML;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public List<DaySchedule> getData() {
            return data;
        }

        public void setData(List<DaySchedule> data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getErrorId() {
            return errorId;
        }

        public void setErrorId(String errorId) {
            this.errorId = errorId;
        }

        public String getErrorDescription() {
            return errorDescription;
        }

        public void setErrorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
        }

        public String getErrorHTML() {
            return errorHTML;
        }

        public void setErrorHTML(String errorHTML) {
            this.errorHTML = errorHTML;
        }

        @Override
        public String toString() {
            return "WeeklySchedule{" +
                    "status=" + status +
                    ", data=" + data +
                    ", message='" + message + '\'' +
                    ", errorId='" + errorId + '\'' +
                    ", errorDescription='" + errorDescription + '\'' +
                    ", errorHTML='" + errorHTML + '\'' +
                    '}';
        }
    }

    public static class DaySchedule {
        private int dayIndex;
        private List<HourData> hoursData = new ArrayList<>();

        public int getDayIndex() {
            return dayIndex;
        }

        public void setDayIndex(int dayIndex) {
            this.dayIndex = dayIndex;
        }

        public List<HourData> getHoursData() {
            return hoursData;
        }

        public void setHoursData(List<HourData> hoursData) {
            this.hoursData = hoursData;
        }

        @Override
        public String toString() {
            return "DaySchedule{" +
                    "dayIndex=" + dayIndex +
                    ", hoursData=" + hoursData +
                    '}';
        }
    }

    public static class HourData {
        private int hour;
        private String hourName;
        private List<Lesson> schedule = new ArrayList<>();
        private List<Object> changes = new ArrayList<>();
        private List<Event> events = new ArrayList<>();
        private List<Object> exams = new ArrayList<>();

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public String getHourName() {
            return hourName;
        }

        public void setHourName(String hourName) {
            this.hourName = hourName;
        }

        public List<Lesson> getSchedule() {
            return schedule;
        }

        public void setSchedule(List<Lesson> schedule) {
            this.schedule = schedule;
        }

        public List<Object> getChanges() {
            return changes;
        }

        public void setChanges(List<Object> changes) {
            this.changes = changes;
        }

        public List<Event> getEvents() {
            return events;
        }

        public void setEvents(List<Event> events) {
            this.events = events;
        }

        public List<Object> getExams() {
            return exams;
        }

        public void setExams(List<Object> exams) {
            this.exams = exams;
        }

        @Override
        public String toString() {
            return "HourData{" +
                    "hour=" + hour +
                    ", hourName='" + hourName + '\'' +
                    ", schedule=" + schedule +
                    ", changes=" + changes +
                    ", events=" + events +
                    ", exams=" + exams +
                    '}';
        }
    }

    public static class Lesson {
        private int day;
        private int hour;
        private int roomID;
        private long studyGroupID;
        private String subject;
        private String subjectLevel;
        private String room;
        private String teacherPrivateName;
        private String teacherLastName;
        private Object classes;
        private Object capsule;
        private Object isPartani;
        private List<Object> changes = new ArrayList<>();

        public int getDay() { return day; }
        public void setDay(int day) { this.day = day; }

        public int getHour() { return hour; }
        public void setHour(int hour) { this.hour = hour; }

        public int getRoomID() { return roomID; }
        public void setRoomID(int roomID) { this.roomID = roomID; }

        public long getStudyGroupID() { return studyGroupID; }
        public void setStudyGroupID(long studyGroupID) { this.studyGroupID = studyGroupID; }

        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }

        public String getSubjectLevel() { return subjectLevel; }
        public void setSubjectLevel(String subjectLevel) { this.subjectLevel = subjectLevel; }

        public String getRoom() { return room; }
        public void setRoom(String room) { this.room = room; }

        public String getTeacherPrivateName() { return teacherPrivateName; }
        public void setTeacherPrivateName(String teacherPrivateName) { this.teacherPrivateName = teacherPrivateName; }

        public String getTeacherLastName() { return teacherLastName; }
        public void setTeacherLastName(String teacherLastName) { this.teacherLastName = teacherLastName; }

        public Object getClasses() { return classes; }
        public void setClasses(Object classes) { this.classes = classes; }

        public Object getCapsule() { return capsule; }
        public void setCapsule(Object capsule) { this.capsule = capsule; }

        public Object getIsPartani() { return isPartani; }
        public void setIsPartani(Object isPartani) { this.isPartani = isPartani; }

        public List<Object> getChanges() { return changes; }
        public void setChanges(List<Object> changes) { this.changes = changes; }

        @Override
        public String toString() {
            return "Lesson{" +
                    "day=" + day +
                    ", hour=" + hour +
                    ", roomID=" + roomID +
                    ", studyGroupID=" + studyGroupID +
                    ", subject='" + subject + '\'' +
                    ", subjectLevel='" + subjectLevel + '\'' +
                    ", room='" + room + '\'' +
                    ", teacherPrivateName='" + teacherPrivateName + '\'' +
                    ", teacherLastName='" + teacherLastName + '\'' +
                    ", changes=" + changes +
                    '}';
        }
    }

    public static class Event {
        private long id;
        private String title;
        private boolean cancelClassesLastDayUntilEnd;
        private int type;
        private String textualType;
        private String fromDate;
        private String toDate;
        private int fromHour;
        private int toHour;
        private Object job;
        private Object payment;
        private Object note;
        private String classes;
        private String accompaniers;
        private Object groups;
        private Object rooms;
        private Object relatedStudents;

        public long getId() { return id; }
        public void setId(long id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public boolean isCancelClassesLastDayUntilEnd() { return cancelClassesLastDayUntilEnd; }
        public void setCancelClassesLastDayUntilEnd(boolean cancelClassesLastDayUntilEnd) { this.cancelClassesLastDayUntilEnd = cancelClassesLastDayUntilEnd; }

        public int getType() { return type; }
        public void setType(int type) { this.type = type; }

        public String getTextualType() { return textualType; }
        public void setTextualType(String textualType) { this.textualType = textualType; }

        public String getFromDate() { return fromDate; }
        public void setFromDate(String fromDate) { this.fromDate = fromDate; }

        public String getToDate() { return toDate; }
        public void setToDate(String toDate) { this.toDate = toDate; }

        public int getFromHour() { return fromHour; }
        public void setFromHour(int fromHour) { this.fromHour = fromHour; }

        public int getToHour() { return toHour; }
        public void setToHour(int toHour) { this.toHour = toHour; }

        public Object getJob() { return job; }
        public void setJob(Object job) { this.job = job; }

        public Object getPayment() { return payment; }
        public void setPayment(Object payment) { this.payment = payment; }

        public Object getNote() { return note; }
        public void setNote(Object note) { this.note = note; }

        public String getClasses() { return classes; }
        public void setClasses(String classes) { this.classes = classes; }

        public String getAccompaniers() { return accompaniers; }
        public void setAccompaniers(String accompaniers) { this.accompaniers = accompaniers; }

        public Object getGroups() { return groups; }
        public void setGroups(Object groups) { this.groups = groups; }

        public Object getRooms() { return rooms; }
        public void setRooms(Object rooms) { this.rooms = rooms; }

        public Object getRelatedStudents() { return relatedStudents; }
        public void setRelatedStudents(Object relatedStudents) { this.relatedStudents = relatedStudents; }

        @NonNull
        @Override
        public String toString() {
            return "Event{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", cancelClassesLastDayUntilEnd=" + cancelClassesLastDayUntilEnd +
                    ", type=" + type +
                    ", textualType='" + textualType + '\'' +
                    ", fromDate='" + fromDate + '\'' +
                    ", toDate='" + toDate + '\'' +
                    ", fromHour=" + fromHour +
                    ", toHour=" + toHour +
                    ", classes='" + classes + '\'' +
                    ", accompaniers='" + accompaniers + '\'' +
                    '}';
        }
    }
}
