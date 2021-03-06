package npu.edu.hamster.module;

/**
 * Created by su153 on 2/14/2017.
 */

public class EventModule extends BaseModule {
    protected String month;
    protected String day;
    protected String content;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getType() {
        return CardContent.EVENT;
    }
}
