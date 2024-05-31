package org.example.view;

import org.example.model.entities.Moto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ModelComponentsVisuals {

    private DefaultTableModel modelTaulaAlumne;
    private DefaultTableModel modelTaulaMat;
    private ComboBoxModel<Moto.Quantitat.Provincia> comboBoxModel;



    //Getters


    public ComboBoxModel<Moto.Quantitat.Provincia> getComboBoxModel() {
        return comboBoxModel;
    }

    public DefaultTableModel getModelTaulaMoto() {
        return modelTaulaAlumne;
    }

    public DefaultTableModel getModelTaulaQuantitat() {
        return modelTaulaMat;
    }

    public ModelComponentsVisuals() {


        //Anem a definir l'estructura de la taula dels alumnes
        modelTaulaAlumne =new DefaultTableModel(new Object[]{"Model","Pes","Es en marches?","Object"},0){
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }



            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Double.class;
                    case 2:
                        return Boolean.class;
                    default:
                        return Object.class;
                }
            }
        };




        //Anem a definir l'estructura de la taula de les matrícules
        modelTaulaMat =new DefaultTableModel(new Object[]{"Provincia","Quantitat"},0){
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }

            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Moto.Quantitat.Provincia.class;
                    case 1:
                        return Integer.class;
                    default:
                        return Object.class;
                }
            }
        };



        //Estructura del comboBox
        comboBoxModel=new DefaultComboBoxModel<>(Moto.Quantitat.Provincia.values());



    }
}
