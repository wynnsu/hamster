package npu.edu.hamster.module;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by su153 on 2/14/2017.
 */

public class AttendanceModule extends BaseModule {
    Map<String,String> attendanceMap=new HashMap<>();

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }

    int currentWeek;

    public Map<String, String> getAttendanceMap() {
        return attendanceMap;
    }

    public void setAttendanceMap(Map<String, String> attendanceMap) {
        this.attendanceMap = attendanceMap;
    }

    @Override
    public int getType() {
        return CardContent.ATTENDANCE;
    }
}
