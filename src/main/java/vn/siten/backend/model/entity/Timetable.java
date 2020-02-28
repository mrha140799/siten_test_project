package vn.siten.backend.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.siten.backend.model.TimetableId;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity
@Table(name = "timetables")
@Data
@NoArgsConstructor
public class Timetable {

    @Id
    private TimetableId timetableId;
    @Min(2)
    @Max(8)
    private int day;
    private String className;
    private Date startDate;
    private Date endDate;

    public Timetable(TimetableId timetableId, int day, String className, Date startDate, Date endDate) {
        this.timetableId = timetableId;
        this.day = day;
        this.className = className;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
