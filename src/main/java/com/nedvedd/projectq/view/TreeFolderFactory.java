package com.nedvedd.projectq.view;

import com.nedvedd.projectq.model.TreeFolder;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;

/**
 * Custom factory pro operace s bunkami ve strome se slozkami (TreeView)
 *
 * @author Dominik Nedved
 * @version 21.05.2024
 */
public class TreeFolderFactory extends TreeCell<TreeFolder> {

    /** TextField jako editor nazvu slozky */
    private TextField folderNameInputTF;

    /**
     * Akce zahajeni editace pri double-clicku na bunku
     */
    @Override
    public void startEdit() {
        super.startEdit();

        // vytvori novy editor bunky
        if (folderNameInputTF == null) {
            createEditor();
        }

        setText(null);
        folderNameInputTF.setText(getFolderName());
        setGraphic(folderNameInputTF);
    }

    /**
     * Metoda pro prepnuti bunky do zobrazovaciho modu (zruseni editace nazvu slozky).
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getFinalFolderName());
        setGraphic(null);
    }

    /**
     * Aktualizuje vzhled bunky
     *
     * @param item novy item pro bunku
     * @param empty true pokud je bunka prazdna (reprezentuje pouze prazdnou radku), false pokud prazdna neni
     */
    @Override
    protected void updateItem(TreeFolder item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (folderNameInputTF != null) {
                    folderNameInputTF.setText(getFolderName());
                    setText(null);
                    setGraphic(folderNameInputTF);
                }
            } else {
                setText(getFinalFolderName());
                setGraphic(null);
            }
        }
    }

    /**
     * @return symbol a jmeno slozky
     */
    private String getFinalFolderName() {
        String folderSymbol = "\uD83D\uDDC0";
        return folderSymbol + " " + getItem().getFolderName();
    }

    /**
     * @return jmeno bez symbolu (ikonu uzivatel logicky "natvrdo" menit nemuze)
     */
    private String getFolderName() {
        return getItem().getFolderName();
    }

    /**
     * Metoda pro vytvoreni editoru.
     */
    private void createEditor() {
        double tfSize = 150;
        folderNameInputTF = new TextField();
        folderNameInputTF.setMinWidth(tfSize);
        folderNameInputTF.setPrefWidth(tfSize);
        folderNameInputTF.setMaxWidth(tfSize);

        // nastaveni reakci na klavesy
        folderNameInputTF.setOnKeyReleased(event -> {
            // pro potvrzeni:
            if (event.getCode() == KeyCode.ENTER) {
                // overeni vstupu:
                if (folderNameInputTF.getText().isEmpty()) {
                    // pokud neni zadny vstup, editace se zrusi
                    cancelEdit();
                } else {
                    TreeFolder item = getItem();
                    item.setFolderName(folderNameInputTF.getText());
                    commitEdit(item);
                }
                // pokud je editace zrusena
            } else if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });
    }
}
