package npu.edu.hamster.module;

/**
 * Created by su153 on 2/14/2017.
 */

public class NewsModule extends BaseModule {
    protected String title;
    protected String imgUrl;
    protected String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getType() {
        return CardContent.NEWS;
    }
}
