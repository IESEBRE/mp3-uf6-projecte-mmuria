package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MotoView extends JFrame{
    private JTabbedPane pestanyes;
    private JTable taula;
    private JScrollPane scrollPane1;
    private JButton insertarButton;
    private JButton modificarButton;
    private JButton borrarButton;
    private JTextField campModelMoto;
    private JTextField campPes;
    private JCheckBox caixaEsEnMarches;
    private JPanel panel;
    private JTable taulaQuantitat;
    private JComboBox comboProvincia;
    private JTextField campQuantitat;
    //private JTabbedPane PanelPestanya;

    //Getters


    public JTabbedPane getPestanyes() {
        return pestanyes;
    }

    public JTable getTaula() {
        return taula;
    }

    public JScrollPane getScrollPane1() {
        return scrollPane1;
    }

    public JButton getInsertarButton() {
        return insertarButton;
    }

    public JButton getModificarButton() {
        return modificarButton;
    }

    public JButton getBorrarButton() {
        return borrarButton;
    }

    public JTextField getCampModelMoto() {
        return campModelMoto;
    }

    public JTextField getCampPes() {
        return campPes;
    }

    public JCheckBox getCaixaEsEnMarches() {
        return caixaEsEnMarches;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JTable getTaulaMat() {
        return taulaQuantitat;
    }

    public JComboBox getComboProvincia() {
        return comboProvincia;
    }

    public JTextField getCampQuantitat() {
        return campQuantitat;
    }

    //Constructor de la classe
    public MotoView() {


        //Per poder vore la finestra
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(false);
    }

        private void createUIComponents() {
        // TODO: place custom component creation code here
        scrollPane1 = new JScrollPane();
        taula = new JTable();
        pestanyes = new JTabbedPane();
        taula.setModel(new DefaultTableModel());
        taula.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane1.setViewportView(taula);

    }
}
