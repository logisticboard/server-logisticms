package com.example.logisticms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "fleet_operator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FleetOperator extends BaseEntity{

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(updatable = false, nullable = false)
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private String gstNumber;

    @Column(nullable = false)
    private String contactPhone;

    @Column(nullable = false)
    private String contactEmail;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 500)
    private String description;

    @OneToMany(mappedBy = "fleetOperator", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Driver> drivers;

    @OneToMany(mappedBy = "fleetOperator", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Truck> trucks;

    @OneToMany(mappedBy = "fleetOperator", fetch = FetchType.LAZY)
    @JsonIgnore
    List<FleetOperatorMember> members;

}

