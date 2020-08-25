package com.wkowalczyk.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name ="zdarzenie")
public class Zdarzenie {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_elektrowni", nullable = false)
    private Elektrownia elektrownia;

    private String typZdarzenia;
    private Number ubytekMocy;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataRozpoczecia;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataZakonczenia;

    public Zdarzenie(long id,TypZdarzenia typZdarzenia, Number ubytekMocy, Elektrownia elektrownia){
        this.id=id;
        this.typZdarzenia = typZdarzenia.name();
        this.ubytekMocy=ubytekMocy;
        this.elektrownia=elektrownia;
    }
}
