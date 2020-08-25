package com.wkowalczyk.restapi.model;

import lombok.*;

import javax.persistence.*;

import java.io.Serializable;
import java.util.*;
@ToString
@EqualsAndHashCode
@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "elektrownia")
public class Elektrownia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nazwaElektrowni;
    private Number mocElektrowni;

    @OneToMany(mappedBy = "elektrownia", fetch=FetchType.EAGER)
    private List<Zdarzenie> zdarzenia = new ArrayList<>();

    public Elektrownia(long id, String nazwaElektrowni, Number mocElektrowni) {
        this.id = id;
        this.nazwaElektrowni = nazwaElektrowni;
        this.mocElektrowni = mocElektrowni;
    }

    public int getMocPoAwarii(){
        int totalPowerLoss=0;
        for (Zdarzenie zdarzenie : zdarzenia)
        {
            totalPowerLoss+=zdarzenie.getUbytekMocy().intValue();
        }
        return mocElektrowni.intValue()-totalPowerLoss;
    }
}
