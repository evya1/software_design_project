package il.cshaifasweng.OCSFMediatorExample.client.Utility;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;

import java.util.function.Consumer;

public class CheckComboBoxTableCell<S, T> extends TableCell<S, ObservableList<T>> {
    private final CheckComboBox<T> checkComboBox;
    private final Consumer<T> onItemChecked;
    private final Consumer<T> onItemUnchecked;

    public CheckComboBoxTableCell(ObservableList<T> items, Consumer<T> onItemChecked, Consumer<T> onItemUnchecked) {
        this.checkComboBox = new CheckComboBox<>(items);
        this.onItemChecked = onItemChecked;
        this.onItemUnchecked = onItemUnchecked;

        this.checkComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<T>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (T addedItem : change.getAddedSubList()) {
                        if (onItemChecked != null) {
                            onItemChecked.accept(addedItem);
                        }
                    }
                }
                if (change.wasRemoved()) {
                    for (T removedItem : change.getRemoved()) {
                        if (onItemUnchecked != null) {
                            onItemUnchecked.accept(removedItem);
                        }
                    }
                }
            }
            commitEdit(FXCollections.observableArrayList(checkComboBox.getCheckModel().getCheckedItems()));
        });
    }

    @Override
    protected void updateItem(ObservableList<T> item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(checkComboBox);
            checkComboBox.getCheckModel().clearChecks();
            if (item != null) {
                item.forEach(checkComboBox.getCheckModel()::check);
            }
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (!isEmpty()) {
            checkComboBox.show();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        checkComboBox.hide();
    }

    @Override
    public void commitEdit(ObservableList<T> newValue) {
        super.commitEdit(newValue);
        checkComboBox.hide();
    }

    public static <S, T> Callback<TableColumn<S, ObservableList<T>>, TableCell<S, ObservableList<T>>> forTableColumn(
            ObservableList<T> items, Consumer<T> onItemChecked, Consumer<T> onItemUnchecked) {
        return col -> new CheckComboBoxTableCell<>(items, onItemChecked, onItemUnchecked);
    }

    public ObservableList<T> getCheckedItems() {
        return checkComboBox.getCheckModel().getCheckedItems();
    }
}
