package com.insurancecorp.insurecar.model;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;
import java.util.Collection;

/**
 * Entity that represents a customer who can hold insurance policies.
 */
@Entity
@Getter @Setter
@View(name="Complete", members=
    "personalInfo [" +
        "firstName, lastName;" +
        "email, phoneNumber;" +
        "dateOfBirth;" +
        "address;" +
    "];" +
    "policies"
)
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Required
    @Column(length = 50)
    private String firstName;
    
    @Required
    @Column(length = 50)
    private String lastName;
    
    @Required
    @Column(length = 100)
    @DisplaySize(30)
    private String email;
    
    @Column(length = 20)
    @DisplaySize(15)
    private String phoneNumber;
    
    @Stereotype("DATE")
    private java.util.Date dateOfBirth;
    
    @Column(length = 200)
    @Stereotype("MEMO")
    private String address;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    @ListProperties("policyNumber, startDate, endDate, status, premium")
    private Collection<Policy> policies;
    
    /**
     * Returns the full name of the customer.
     */
    public String toString() {
        return firstName + " " + lastName;
    }
}
