package org.example.todoapp;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HelloController {

    @FXML private TextField inputField;
    @FXML private ListView<TodoItem> todoList;
    @FXML private Label countLabel;

    //一覧のデータ。チェックのON/OFF でも「変更有り」と分かるようにしておく
    private final ObservableList<TodoItem> items =
            FXCollections.observableArrayList(item -> new Observable[] {item.doneProperty() });

    // 保存先：c:\Users\(ユーザー名)\
    private static final Path SAVE_FILE = Path.of(System.getProperty("user.home"), "todo.txt");

    // 画面ができた直後に一回だけ呼ばれる(C#のForm_Load)
    @FXML
    public void initialize()
    {
        todoList.setItems(items);
        // 各行をチェックボックス付きで表示し、TodoItemのdoneとつなぐ
        todoList.setCellFactory(CheckBoxListCell.forListView(TodoItem::doneProperty));

        load();
        updateCount();

        //一覧がかわるたびに、件数の表示と保存をする
        items.addListener((ListChangeListener<TodoItem>) change -> {
            updateCount();
            save();
        });
    }
    //追加ボタン　入力欄でEnter
    @FXML
    public void onAdd()
    {
        String text = inputField.getText().trim();
        if (text.isEmpty()){
            return;
        }
        items.add(new TodoItem(text, false));
        inputField.clear();
        inputField.requestFocus();
    }

    //選択を削除
    @FXML
    public void onDelete()
    {
        TodoItem selected =  todoList.getSelectionModel().getSelectedItem();
        if (selected != null){
            items.remove(selected);
        }
    }

    //完了を片付ける
    @FXML
    public void onClearDone()
    {
        items.removeIf(TodoItem::isDone);
    }

    //残り0件/全0件 ...Stream API (C#のLINQ)
    private void updateCount(){
        long remaining = items.stream()
                .filter(item -> !item.isDone())
                .count();
        countLabel.setText("残り" + remaining + "件 / 全" + items.size() + "件");
    }

    // ====段階4：ファイルに保存・読み込み ====

    //1行 = 「完了なら１，未完了なら０」＋タブ＋タイトル
    private void save() {
        List<String> lines = items.stream()
                .map(item -> (item.isDone() ? "1" : "0") + "\t" + item.getTitle())
                .toList();
        try {
            Files.write(SAVE_FILE, lines, StandardCharsets.UTF_8);
        } catch (IOException e){
            showError("保存に失敗しました:"+ e.getMessage());
        }
    }

    private void load() {
    if (!Files.exists(SAVE_FILE)){
        return;
    }
    try {
        List<TodoItem> loaded = Files.readAllLines(SAVE_FILE, StandardCharsets.UTF_8).stream()
                .filter(line -> line.contains("\t"))
                .map(line -> {
                    String[] parts = line.split("\t", 2);
                    return new TodoItem(parts[1], parts[0].equals("1"));
                })
                .toList();
        items.setAll(loaded);
    } catch (IOException e){
        showError("保存に失敗しました:"+ e.getMessage());
    }
}

private void showError(String message){
    new Alert(Alert.AlertType.ERROR, message).show();}
}
