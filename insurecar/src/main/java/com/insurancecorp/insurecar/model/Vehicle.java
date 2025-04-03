package com.insurancecorp.insurecar.model;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entity that represents a vehicle that can be insured.
 */
@Entity
@Getter @Setter
@View(name="Complete", members=
    "vehicleInfo [" +
        "make, model;" +
        "year, color;" +
        "licensePlate;" +
        "vin;" +
    "];" +
    "technicalInfo [" +
        "engineType;" +
        "mileage;" +
        "value;" +
    "]"
)
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Required
    @Column(length = 50)
    private String make;
    
    @Required
    @Column(length = 50)
    private String model;
    
    @Required
    private Integer year;
    
    @Column(length = 30)
    private String color;
    
    @Required
    @Column(length = 20)
    @DisplaySize(15)
    private String licensePlate;
    
    @Required
    @Column(length = 17)
    @DisplaySize(20)
    private String vin; // Vehicle Identification Number
    
    @Column(length = 30)
    private String engineType;
    
    private Integer mileage;
    
    @Money
    private BigDecimal value;
    
    @ManyToOne
    @ReferenceView("Complete")
    private Policy policy;
    
    /**
     * Returns a string representation of the vehicle.
     */
    public String toString() {
        return year + " " + make + " " + model + " (" + licensePlate + ")";
    }
}
