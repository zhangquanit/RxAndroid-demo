package cn.com.chaoba.rxjavademo.others.rxbus.event;

/**
 * @author 张全
 */

public class PushEvent {
    private long id;
    private String name;

    public PushEvent(long id, String name) {
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
        return "PushEvent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
