package cn.com.chaoba.rxjavademo.others.rxbus.event;

/**
 * @author 张全
 */

public class UserEvent {
    private long id;
    private String name;

    public UserEvent(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
