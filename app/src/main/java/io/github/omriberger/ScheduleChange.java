package io.github.omriberger;

public class ScheduleChange {
    public int dayIndex;          // 0 = Sunday, 1 = Monday, etc.
    public String hourName;       // e.g., "08:00-08:45"
    public String subject;
    public String room;
    public String teacher;
    public String type;           // e.g., "Added", "Removed", "Updated"

    public ScheduleChange(int dayIndex, String hourName, String subject, String room, String teacher, String type) {
        this.dayIndex = dayIndex;
        this.hourName = hourName;
        this.subject = subject;
        this.room = room;
        this.teacher = teacher;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Day " + dayIndex + ", " + hourName + ": " + subject + " (" + room + ") [" + teacher + "] " + type;
    }
}
