package cn.com.chaoba.rxjavademo.others.rxbus.event;

/**
 * @author 张全
 */

public class UserChildEvent extends UserEvent {
    public UserChildEvent(long id, String name) {
        super(id, name);
    }

    @Override
    public String toString() {
        return "UserChildEvent{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
