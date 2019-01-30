package entity;
/**
 *
 * 在线用户数
 * Created by hky on 2018/12/10.
 *
 * */
public class UserCount {
    private String Record_time;//当前时间
    private String UserCount;//当前时间所对应的用户数
    private String id;

    public UserCount() {
    }

    public String getRecord_time() {
        return Record_time;
    }

    public void setRecord_time(String record_time) {
        Record_time = record_time;
    }

    public String getUserCount() {
        return UserCount;
    }

    public void setUserCount(String userCount) {
        UserCount = userCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserCount(String record_time, String userCount, String id) {
        Record_time = record_time;
        UserCount = userCount;
        this.id = id;
    }
}
