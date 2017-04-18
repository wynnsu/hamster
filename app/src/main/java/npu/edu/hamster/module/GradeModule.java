package npu.edu.hamster.module;

/**
 * Created by su153 on 2/14/2017.
 */

public class GradeModule extends BaseModule {
    protected String iconUrl;
    protected String title;
    protected String content;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getType() {
        return CardContent.GRADE;
    }
}
