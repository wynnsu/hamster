package npu.edu.hamster.module;

/**
 * Created by su153 on 2/26/2017.
 */

public class BaseModule implements CardContent {
    private ContentType type;
    @Override
    public ContentType getContentType() {
        return type;
    }

    @Override
    public void setContentType(ContentType type) {
        this.type=type;
    }
}
