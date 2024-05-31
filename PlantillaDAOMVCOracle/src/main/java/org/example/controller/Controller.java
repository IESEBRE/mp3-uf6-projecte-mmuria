package org.example.controller;

import org.example.model.entities.Moto;
import org.example.model.exceptions.DAOException;
import org.example.model.entities.Moto.Quantitat;
import org.example.view.ModelComponentsVisuals;
import org.example.model.impls.MotoDAOJDBCOracleImpl;
import org.example.view.MotoView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Controller implements PropertyChangeListener { //1. Implementació de interfície PropertyChangeListener


    private ModelComponentsVisuals modelComponentsVisuals =new ModelComponentsVisuals();
    private MotoDAOJDBCOracleImpl dadesMoto;
    private MotoView view;

    public Controller(MotoDAOJDBCOracleImpl dadesMoto, MotoView view) {
        this.dadesMoto = dadesMoto;
        this.view = view;

        try {
            this.dadesMoto.creaTaulaMotos();
        } catch (DAOException e) {
            // Handle the exception
            this.setExcepcio(e);
        }

        //5. Necessari per a que Controller reaccione davant de canvis a les propietats lligades
        canvis.addPropertyChangeListener(this);

        lligaVistaModel();

        afegirListeners();

        //Si no hem tingut cap poroblema amb la BD, mostrem la finestra
        view.setVisible(true);

    }

    private void lligaVistaModel() {

        //Carreguem la taula motos en les dades de la BD
        try {
            setModelTaulaMoto(modelComponentsVisuals.getModelTaulaMoto(), dadesMoto.getAll());
        } catch (DAOException e) {
            this.setExcepcio(e);
        }

            //Fixem el model de la taula dels motos
        JTable taula = view.getTaula();
        taula.setModel(this.modelComponentsVisuals.getModelTaulaMoto());
        //Amago la columna que conté l'objecte moto
        taula.getColumnModel().getColumn(3).setMinWidth(0);
        taula.getColumnModel().getColumn(3).setMaxWidth(0);
        taula.getColumnModel().getColumn(3).setPreferredWidth(0);

        //Fixem el model de la taula de matrícules
        JTable taulaMat = view.getTaulaMat();
        taulaMat.setModel(this.modelComponentsVisuals.getModelTaulaQuantitat());

        //Posem valor a el combo d'MPs
        view.getComboProvincia().setModel(modelComponentsVisuals.getComboBoxModel());

        //Desactivem la pestanya de la matrícula
        view.getPestanyes().setEnabledAt(1, false);
        view.getPestanyes().setTitleAt(1, "Quantitat de ...");


    }

    private void setModelTaulaMoto(DefaultTableModel modelTaulaMoto, List<Moto> all) {

        // Fill the table model with data from the collection
        for (Moto estudiant : all) {
            modelTaulaMoto.addRow(new Object[]{estudiant.getModelMoto(), estudiant.getPes(), estudiant.isEsEnMarches(), estudiant});
        }
    }

    private void afegirListeners() {

        ModelComponentsVisuals modelo = this.modelComponentsVisuals;
        DefaultTableModel model = modelo.getModelTaulaMoto();
        DefaultTableModel modelQuantitat = modelo.getModelTaulaQuantitat();
        JTable taula = view.getTaula();
        JTable taulaMat = view.getTaulaMat();
        JButton insertarButton = view.getInsertarButton();
        JButton modificarButton = view.getModificarButton();
        JButton borrarButton = view.getBorrarButton();
        JTextField campModelMoto = view.getCampModelMoto();
        JTextField campPes = view.getCampPes();
        JCheckBox caixaEsEnMarches = view.getCaixaEsEnMarches();
        JTabbedPane pestanyes = view.getPestanyes();

        //Botó insertar
        view.getInsertarButton().addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField campModelMoto = view.getCampModelMoto();
                        JTextField campPes = view.getCampPes();
                        JCheckBox caixaEsEnMarches = view.getCaixaEsEnMarches();
                        DefaultTableModel model = modelo.getModelTaulaMoto();

                        if (pestanyes.getSelectedIndex() == 0) { // Si estem a la pestanya de moto
                            // Comprovem que totes les caselles continguen informació
                            if (campModelMoto.getText().isBlank() || campPes.getText().isBlank()) {
                                setExcepcio(new DAOException(2));
                            } else {
                                try {
                                    NumberFormat num = NumberFormat.getNumberInstance(Locale.getDefault()); // Creem un número que entèn la cultura que utilitza l'aplicació
                                    double pes;
                                    try {
                                        pes = Double.parseDouble(campPes.getText().replaceAll(",", "."));
                                    } catch (NumberFormatException nfe) {
                                        setExcepcio(new DAOException(5));
                                        return;
                                    }
                                    if (pes < 1 || pes > 800){
                                        setExcepcio(new DAOException(3));
                                    } else {
                                        Moto al = new Moto(campModelMoto.getText(), pes, caixaEsEnMarches.isSelected(), new TreeSet<Quantitat>());
                                        model.addRow(new Object[]{campModelMoto.getText(), pes, caixaEsEnMarches.isSelected(), al});

                                        // Inserir dades a la base de dades
                                        dadesMoto.save(al);
                                        actualizarTaulaMotos();

                                        campModelMoto.setText("");
                                        campModelMoto.setSelectionStart(0);
                                        campModelMoto.setSelectionEnd(campModelMoto.getText().length());
                                        campPes.setText("");
                                        campModelMoto.requestFocus(); // intentem que el foco vaigue al camp del nom
                                    }
                                } catch (DAOException sqlEx) {
                                    setExcepcio(new DAOException(6));
                                }
                            }
                        } else {
                            // Obtinc moto de la columna que conté l'objecte
                            Moto mo = (Moto) model.getValueAt(taula.getSelectedRow(), 3);
                            Quantitat m = new Quantitat((Quantitat.Provincia) view.getComboProvincia().getSelectedItem(), Integer.parseInt(view.getCampQuantitat().getText()));
                            mo.getQuantitat().add(m);
                            ompliQuantitat(mo, modelQuantitat);
                        }
                    }
                }
        );




        //Botó modificar
        view.getModificarButton().addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (pestanyes.getSelectedIndex() == 0) { // Si estem a la pestanya de moto
                            // Comprovem que hi hagi una fila seleccionada
                            if (taula.getSelectedRow() == -1) {
                                setExcepcio(new DAOException(4));
                            } else {
                                // Comprovem que totes les caselles continguen informació
                                if (campModelMoto.getText().isBlank() || campPes.getText().isBlank()) {
                                    setExcepcio(new DAOException(2));
                                } else {
                                    try {
                                        NumberFormat num = NumberFormat.getNumberInstance(Locale.getDefault()); // Creem un número que entèn la cultura que utilitza l'aplicació
                                        double pes;
                                        try {
                                            pes = Double.parseDouble(campPes.getText().replaceAll(",", "."));
                                        } catch (NumberFormatException nfe) {
                                            setExcepcio(new DAOException(5)); // Assuming 5 is the code for invalid weight format
                                            return;
                                        }
                                        if (pes < 1 || pes > 800){
                                            setExcepcio(new DAOException(3));
                                        } else {
                                            int selectedRow = taula.getSelectedRow();
                                            String modelMotoAntic = (String) model.getValueAt(selectedRow, 0); // Obtenim el model anterior per a identificar la fila a actualitzar

                                            model.setValueAt(campModelMoto.getText(), selectedRow, 0);
                                            model.setValueAt(pes, selectedRow, 1);
                                            model.setValueAt(caixaEsEnMarches.isSelected(), selectedRow, 2);
                                            Moto al = (Moto) model.getValueAt(selectedRow, 3);
                                            al.setModelMoto(campModelMoto.getText());
                                            al.setPes(pes);
                                            al.setEsEnMarches(caixaEsEnMarches.isSelected());

                                            // Actualitzar dades a la base de dades
                                            dadesMoto.update(al);
                                            actualizarTaulaMotos();

                                            campModelMoto.setText("");
                                            campPes.setText("");
                                            campModelMoto.requestFocus(); // intentem que el foco vaigue al camp del nom
                                        }
                                    } catch (DAOException sqlEx) {
                                        setExcepcio(new DAOException(6));
                                    }
                                }
                            }
                        } else {
                            // Obtinc moto de la columna que conté l'objecte
                            Moto mo = (Moto) model.getValueAt(taula.getSelectedRow(), 3); // Obtinc l'objecte de la fila seleccionada
                            Quantitat m = new Quantitat((Quantitat.Provincia) view.getComboProvincia().getSelectedItem(), Integer.parseInt(view.getCampQuantitat().getText()));
                            mo.getQuantitat().add(m);
                            ompliQuantitat(mo, modelQuantitat);
                        }
                    }
                }
        );

        taula.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //Obtenim el número de la fila seleccionada
                int filaSel = taula.getSelectedRow();

                if (filaSel != -1) {        //Tenim una fila seleccionada
                    //Posem els valors de la fila seleccionada als camps respectius
                    campModelMoto.setText(model.getValueAt(filaSel, 0).toString());
                    campPes.setText(model.getValueAt(filaSel, 1).toString().replaceAll("\\.", ","));
                    caixaEsEnMarches.setSelected((Boolean) model.getValueAt(filaSel, 2));

                    //Activem la pestanya de la matrícula de moto seleccionat
                    view.getPestanyes().setEnabledAt(1, false);
                    //view.getPestanyes().setTitleAt(1, "Quantitat de ..." + campModelMoto.getText());
                    view.getPestanyes().setTitleAt(1, "Quantitat de ...");

                    //Posem valor a el combo d'MPs
                    //view.getComboMP().setModel(modelo.getComboBoxModel());
                    ompliQuantitat((Moto) model.getValueAt(filaSel, 3), modelQuantitat);
                } else {                  //Hem deseleccionat una fila
                    //Posem els camps de text en blanc
                    campModelMoto.setText("");
                    campPes.setText("");

                    //Desactivem pestanyes
                    view.getPestanyes().setEnabledAt(1, false);
                    view.getPestanyes().setTitleAt(1, "Quantitat de ...");
                }
            }
        });

        view.getBorrarButton().addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (pestanyes.getSelectedIndex() == 0) { // Si estem a la pestanya de moto
                            // Comprovem que hi hagi una fila seleccionada
                            if (taula.getSelectedRow() == -1) {
                                setExcepcio(new DAOException(7));
                            } else {
                                try {
                                    int selectedRow = taula.getSelectedRow();
                                    Moto al = (Moto) model.getValueAt(selectedRow, 3);
                                    model.removeRow(selectedRow);

                                    // Esborrar dades de la base de dades
                                    dadesMoto.delete(al);
                                    actualizarTaulaMotos();

                                    campModelMoto.setText("");
                                    campPes.setText("");
                                    campModelMoto.requestFocus(); // intentem que el foco vaigue al camp del nom
                                } catch (DAOException sqlEx) {
                                    setExcepcio(new DAOException(8));
                                }
                            }
                        } else {
                            // Obtinc moto de la columna que conté l'objecte
                            Moto mo = (Moto) model.getValueAt(taula.getSelectedRow(), 3); // Obtinc l'objecte de la fila seleccionada
                            Quantitat m = new Quantitat((Quantitat.Provincia) view.getComboProvincia().getSelectedItem(), Integer.parseInt(view.getCampQuantitat().getText()));
                            mo.getQuantitat().add(m);
                            ompliQuantitat(mo, modelQuantitat);
                        }
                    }
                }
        );


    }
    private void actualizarTaulaMotos () {
        DefaultTableModel modelMotos = modelComponentsVisuals.getModelTaulaMoto();
        try {
            modelMotos.setRowCount(0);
            setModelTaulaMoto(modelMotos, dadesMoto.getAll());
        } catch (DAOException e) {
            setExcepcio(e);
        }
    }





    private static void ompliQuantitat(Moto al, DefaultTableModel modelQuantitat) {
        //Omplim el model de la taula de matrícula de motos seleccionat
        modelQuantitat.setRowCount(0);
        // Fill the table model with data from the collection
        for (Quantitat quantitat : al.getQuantitat()) {
            modelQuantitat.addRow(new Object[]{quantitat.getProvincia(), quantitat.getQuantitat()});
        }
    }


    //TRACTAMENT D'EXCEPCIONS

    //2. Propietat lligada per controlar quan genero una excepció
    public static final String PROP_EXCEPCIO="excepcio";
    private DAOException excepcio;

    public DAOException getExcepcio() {
        return excepcio;
    }

    public void setExcepcio(DAOException excepcio) {
        DAOException valorVell=this.excepcio;
        this.excepcio = excepcio;
        canvis.firePropertyChange(PROP_EXCEPCIO, valorVell,excepcio);
    }


    //3. Propietat PropertyChangesupport necessària per poder controlar les propietats lligades
    PropertyChangeSupport canvis=new PropertyChangeSupport(this);


    //4. Mètode on posarem el codi de tractament de les excepcions --> generat per la interfície PropertyChangeListener
    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        DAOException rebuda=(DAOException)evt.getNewValue();

        try {
            throw rebuda;
        } catch (DAOException e) {
            //Aquí farem ele tractament de les excepcions de l'aplicació
            switch(evt.getPropertyName()){
                case PROP_EXCEPCIO:

                    switch(rebuda.getTipo()){
                        case 0:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            System.exit(1);
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            //this.view.getCampNom().setText(rebuda.getMissatge());
                            this.view.getCampModelMoto().setSelectionStart(0);
                            this.view.getCampModelMoto().setSelectionEnd(this.view.getCampModelMoto().getText().length());
                            this.view.getCampModelMoto().requestFocus();
                            break;
                    }
                default:
                    JOptionPane.showMessageDialog(null, rebuda.getMessage());


            }
        }
    }

    // Mètode per a inserir dades a la base de dades


}
