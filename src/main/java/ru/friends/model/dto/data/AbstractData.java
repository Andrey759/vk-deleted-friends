package ru.friends.model.dto.data;

import lombok.Data;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Data
public abstract class AbstractData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    long remoteId;

    @Column
    String firstName;

    @Column
    String lastName;

}
