package npu.edu.hamster.module;

/**
 * Created by su153 on 2/14/2017.
 */

public class CourseModule extends BaseModule {
    protected String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getType() {
        return CardContent.COURSE;
    }
}
