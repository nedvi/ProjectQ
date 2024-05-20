package com.nedvedd.projectq.view;

import com.nedvedd.projectq.data.TreeFolder;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;

public class TreeFolderFactory extends TreeCell<TreeFolder> {

    /** TextField jako editor */
    private TextField folderNameInputTF;

    @Override
    public void startEdit() {
        super.startEdit();

        // vytvori novy editor bunky
        if (folderNameInputTF == null) {
            createEditor();
        }

        // "vypnuti" rendereru
        setText(null);
        // nastaveni editoru - vytvareni obsahu
        folderNameInputTF.setText(createEditorContent());
        // zobrazeni editoru v bunce
        setGraphic(folderNameInputTF);
    }

    /**
     * Metoda pro prepnuti bunky do zobrazovaciho modu.
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();

        // nastaveni obsahu rendereru (label)
        setText(createContent());
        // odstraneni editoru z bunky
        setGraphic(null);
    }

    @Override
    protected void updateItem(TreeFolder item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            // pokud je bunka v modu editace:
            if (isEditing()) {
                if (folderNameInputTF != null) {
                    // nastaveni obsahu editoru
                    folderNameInputTF.setText(createEditorContent());
                    // "vypnuti" rendereru
                    setText(null);
                    // pridani editoru do grafu sceny
                    setGraphic(folderNameInputTF);
                }
                // pokud je bunka v modu zobrazeni:
            } else {
                // nastaveni obsahu rendereru
                setText(createContent());
                // "vypnuti" editoru
                setGraphic(null);
            }
        }
    }

    /**
     * Metoda pro vytvoreni obsahu rendereru
     *
     * @return symbol a jmeno itemu
     */
    private String createContent() {
        /*
         * Vyber symbolu slozky podle toho, zda TreeItem typu FOLDER je zrovna otevreny nebo naopak uzavreny
         * closed folder: "\uD83D\uDDC0", open folder: "\uD83D\uDCC2"
         */
        String folderSymbol = (getTreeItem().isExpanded() ? "\uD83D\uDCC2" : "\uD83D\uDDC0");
        return folderSymbol + " " + getItem().getFolderName();
    }

    /**
     * Metoda pro vytvoreni obsahu editoru.
     *
     * @return jmeno bez symbolu (ikonu uzivatel logicky "natvrdo" menit nemuze)
     */
    private String createEditorContent() {
        return getItem().getFolderName();
    }

    /**
     * Metoda pro vytvoreni editoru.
     */
    private void createEditor() {
        // editor je zalozen na TextFieldu
        folderNameInputTF = new TextField();
        double tfSize = 150;
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
                    // getting acces to the element from the model, representing
                    // edited value
                    // ziskani pristupu k elementu z modelu, reprezentujici upravenou hodnotu
                    TreeFolder item = getItem();
                    // nastaveni noveho nazvu do datoveho modelu
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
