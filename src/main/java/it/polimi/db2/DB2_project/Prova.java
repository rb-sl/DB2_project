package it.polimi.db2.DB2_project;

import javax.persistence.*;

@Entity
@Table(name = "TABLE_NAME", schema = "GMA")
public class Prova {
    @Id
    @GeneratedValue
    @Column(name = "column_1")
    private int id;
    public Prova(){}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
