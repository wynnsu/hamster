package npu.edu.hamster.module;

/**
 * Created by su153 on 2/26/2017.
 */

public class LoginModule extends BaseModule {
    protected String content;
    protected String imgUrl;
    private ContentType type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }
}
