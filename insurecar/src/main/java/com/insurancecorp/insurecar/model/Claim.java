package com.insurancecorp.insurecar.model;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;
import org.openxava.calculators.ICalculator;

/**
 * Entity that represents an insurance claim.
 */
@Entity
@Getter @Setter
@View(name="Complete", members=
    "claimInfo [" +
        "claimNumber;" +
        "policy;" +
        "dateOfIncident, dateOfClaim;" +
        "status;" +
        "amount;" +
    "];" +
    "details [" +
        "description;" +
        "location;" +
    "]"
)
public class Claim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Required
    @Column(length = 20, unique = true)
    @DisplaySize(15)
    private String claimNumber;
    
    @ManyToOne(optional = false)
    @ReferenceView("Complete")
    private Policy policy;
    
    @Required
    @Stereotype("DATE")
    private Date dateOfIncident;
    
    @Required
    @Stereotype("DATE")
    @DefaultValueCalculator(CurrentDateCalculator.class)
    private Date dateOfClaim;
    
    @Required
    @Column(length = 20)
    @DefaultValueCalculator(DefaultStatusCalculator.class)
    private String status; // SUBMITTED, UNDER_REVIEW, APPROVED, DENIED, PAID
    
    @Required
    @Money
    private BigDecimal amount;
    
    @Column(length = 1000)
    @Stereotype("MEMO")
    private String description;
    
    @Column(length = 200)
    private String location;
    
    /**
     * Returns a string representation of the claim.
     */
    public String toString() {
        return claimNumber;
    }
    
    /**
     * Default calculator for claim status.
     */
    public static class DefaultStatusCalculator implements ICalculator {
        
        public Object calculate() throws Exception {
            return "SUBMITTED";
        }
    }
    
    /**
     * Calculator for setting the current date.
     */
    public static class CurrentDateCalculator implements ICalculator {
        
        public Object calculate() throws Exception {
            return new Date();
        }
    }
}
