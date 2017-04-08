package ru.friends.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.friends.model.dto.data.AbstractData;

import javax.persistence.*;
import java.time.Instant;

@Entity
@EqualsAndHashCode
@ToString
@Data
public class DataChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "data_id")
    AbstractData data;

    @Column
    Instant detectTimeMin;

    @Column
    Instant detectTimeMax;

    @Column
    String fieldName;

    @Column
    String oldValue;

    @Column
    String newValue;

}
