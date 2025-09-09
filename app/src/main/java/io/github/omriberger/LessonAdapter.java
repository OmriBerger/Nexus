package io.github.omriberger;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private final List<ScheduleData.Lesson> lessons;
    private final Map<Integer, String> hourNames;

    public LessonAdapter(List<ScheduleData.Lesson> lessons, Map<Integer, String> hourNames) {
        this.lessons = lessons;
        this.hourNames = hourNames;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        holder.itemView.setVisibility(View.VISIBLE);
        holder.itemView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

        if (position < lessons.size()) {
            showLesson(holder, lessons.get(position));
        } else {
            holder.itemView.setVisibility(View.INVISIBLE);
            holder.itemView.getLayoutParams().height = 500;
        }
    }

    @SuppressLint("SetTextI18n")
    private void showLesson(@NonNull LessonViewHolder holder, ScheduleData.Lesson lesson) {
        holder.subjectName.setText(lesson.subject != null ? lesson.subject : "");
        String teacher = (lesson.teacherPrivateName != null ? lesson.teacherPrivateName : "") + " " +
                (lesson.teacherLastName != null ? lesson.teacherLastName : "");
        holder.teacherName.setText(teacher.trim());
        if (lesson.roomID != -1) {
            var room = RoomLookup.getRoom(lesson.roomID);
            if (room != null) {
                if (room.getNumber() != null && !room.getNumber().isEmpty()) {
                    holder.roomName.setText("חדר: " + room.getNumber() + " - " + room.getName());
                } else {
                    holder.roomName.setText("חדר: " + room.getName());
                }
            } else holder.roomName.setText("Unknown Room");
        } else {
            holder.roomName.setText("");
        }


        String hourName = hourNames.get(lesson.hour);
        if (hourName == null || hourName.isEmpty()) hourName = ScheduleData.getHourFromNumber(lesson.hour);
        holder.hourRange.setText(hourName.isEmpty() ? "Unknown Hour" : hourName);
        holder.hourNumber.setText(String.valueOf(lesson.hour));


        holder.itemView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return lessons.size() + 1;
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, teacherName, roomName, hourRange, hourNumber;

        LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            teacherName = itemView.findViewById(R.id.teacherName);
            roomName = itemView.findViewById(R.id.roomName);
            hourRange = itemView.findViewById(R.id.hourRange);
            hourNumber = itemView.findViewById(R.id.hourNumber); // <-- Add this
        }
    }

}
