package org.example.todoapp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TodoItem {
    private final String title;
    //bool型はJavaではboolean
    private final BooleanProperty done = new SimpleBooleanProperty();

    public TodoItem(String title, boolean done) {
        this.title = title;
        this.done.set(done);
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return done.get();
    }

    //チェックボックスとつなぐためのプロパティ
    //監視対象として今自分のdone状態がどうなっているのかを渡す
    public BooleanProperty doneProperty() {
        return done;
    }

    // 一覧に表示される文字
    //クラスは基本的にToString()を持っています
    @Override
    public String toString() {
        return title;
    }
}
