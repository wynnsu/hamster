package npu.edu.hamster.module;

/**
 * Created by su153 on 2/14/2017.
 */

public interface CardModule {
    public static enum ContentType{
        EVENT,
        NEWS,
        GRADE,
        HOMEWORK,
        COURSE
    };
    public ContentType getContentType();
    public void setContentType(ContentType type);
}
