package com.insurancecorp.insurecar.model;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import org.openxava.calculators.ICalculator;

/**
 * Entity that represents an insurance policy.
 */
@Entity
@Getter @Setter
@View(name="Complete", members=
    "policyInfo [" +
        "policyNumber;" +
        "customer;" +
        "startDate, endDate;" +
        "status;" +
        "premium;" +
        "coverageType;" +
        "deductible;" +
    "];" +
    "vehicles;" +
    "claims"
)
public class Policy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Required
    @Column(length = 20, unique = true)
    @DisplaySize(15)
    private String policyNumber;
    
    @ManyToOne(optional = false)
    @ReferenceView("Complete")
    private Customer customer;
    
    @Required
    @Stereotype("DATE")
    private Date startDate;
    
    @Required
    @Stereotype("DATE")
    private Date endDate;
    
    @Required
    @Column(length = 20)
    @DefaultValueCalculator(value = DefaultStatusCalculator.class)
    private String status; // ACTIVE, EXPIRED, CANCELLED
    
    @Required
    @Money
    private BigDecimal premium;
    
    @Required
    @Column(length = 50)
    private String coverageType; // BASIC, COMPREHENSIVE, THIRD_PARTY
    
    @Money
    private BigDecimal deductible;
    
    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL)
    @ListProperties("make, model, year, licensePlate, vin")
    private Collection<Vehicle> vehicles;
    
    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL)
    @ListProperties("claimNumber, dateOfIncident, status, amount")
    private Collection<Claim> claims;
    
    /**
     * Returns a string representation of the policy.
     */
    public String toString() {
        return policyNumber;
    }
    
    /**
     * Default calculator for policy status.
     */
    public static class DefaultStatusCalculator implements ICalculator {
        
        public Object calculate() throws Exception {
            return "ACTIVE";
        }
    }
}
