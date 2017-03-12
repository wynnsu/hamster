package npu.edu.hamster.module;

import cz.msebera.android.httpclient.entity.ContentType;

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

    @Override
    public int getType() {
        return CardContent.LOGIN;
    }
}
