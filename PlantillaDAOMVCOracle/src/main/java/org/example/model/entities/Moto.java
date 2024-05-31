package org.example.model.entities;

import java.util.Collection;
import java.util.TreeSet;

public class Moto {

    private Long id;
    private String modelMoto;
    private double pes;
    private boolean esEnMarches;

    private Collection<Quantitat> quantitat;


    public Moto(){}

    public Moto(String modelMoto, double pes, boolean esEnMarches, Collection<Quantitat> quantitat) {
        this.modelMoto = modelMoto;
        this.pes = pes;
        this.esEnMarches=esEnMarches;
        this.quantitat =quantitat;
    }

    public Moto(Long id, String modelMoto) {
        this.id = id;
        this.modelMoto = modelMoto;
    }

    public Moto(long id, String modelMoto, double pes) {
        this.id = id;
        this.modelMoto = modelMoto;
        this.pes = pes;

    }

    public Moto(long id, String modelMoto, double pes,boolean esEnMarches, TreeSet<Quantitat> quantitat) {
        this.id = id;
        this.modelMoto = modelMoto;
        this.pes = pes;
        this.esEnMarches = esEnMarches;
        this.quantitat = quantitat;
    }

    public Collection<Quantitat> getQuantitat() {
        return quantitat;
    }

    private void setQuantitat(Collection<Quantitat> quantitat) {
        this.quantitat = quantitat;
    }

    public String getModelMoto() {
        return modelMoto;
    }

    public void setModelMoto(String modelMoto) {
        this.modelMoto = modelMoto;
    }

    public double getPes() {
        return pes;
    }

    public void setPes(double pes) {
        this.pes = pes;
    }

    public boolean isEsEnMarches() {
        return esEnMarches;
    }

    public void setEsEnMarches(boolean esEnMarches) {
        this.esEnMarches = esEnMarches;
    }

    public String getModel() {
        return modelMoto;
    }

    public boolean isEnMarches() {
        return esEnMarches;
    }

    public long getId() {
        return id;
    }

    public static class Quantitat implements Comparable<Quantitat>{

        @Override
        public int compareTo(Quantitat o) {
            return this.provincia.compareTo(o.getProvincia());
        }

        public static enum Provincia {
            //Llista de provincies de Espanya de la A a la Z
            ALAVA, ALBACETE, ALICANTE, ALMERIA, ASTURIAS, AVILA, BADAJOZ, BALEARES, BARCELONA, BURGOS, CACERES, CADIZ, CANTABRIA, CASTELLON, CEUTA, CIUDAD_REAL, CORDOBA, CUENCA, GERONA, GRANADA, GUADALAJARA, GUIPUZCOA, HUELVA, HUESCA, JAEN, LA_CORUNA, LA_RIOJA, LAS_PALMAS, LEON, LERIDA, LUGO, MADRID, MALAGA, MELILLA, MURCIA, NAVARRA, ORENSE, PALENCIA, PONTEVEDRA, SALAMANCA, SANTA_CRUZ_DE_TENERIFE, SEGOVIA, SEVILLA, SORIA, TARRAGONA, TERUEL, TOLEDO, VALENCIA, VALLADOLID, VIZCAYA, ZAMORA, ZARAGOZA


        }

        private Provincia provincia;
        private int quantitat;

        public Quantitat(Provincia provincia, int quantitat) {
            this.provincia = provincia;
            this.quantitat = quantitat;
        }

        public Provincia getProvincia() {
            return provincia;
        }

        public void setProvincia(Provincia provincia) {
            this.provincia = provincia;
        }

        public int getQuantitat() {
            return quantitat;
        }

        public void setQuantitat(int quantitat) {
            this.quantitat = quantitat;
        }
    }


}
