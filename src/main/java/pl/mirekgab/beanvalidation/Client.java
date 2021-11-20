package pl.mirekgab.beanvalidation;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

/**
 *
 * @author mirek
 */
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotBlank(message = "city is mandatory")
    private String city;

    @Column(name = "postal_code")
    @Pattern(regexp = "\\d{2}-\\d{3}", message = "postal code must match format XX-XXX")
    private String postalCode;

    @Email
    @NotBlank
    private String email;
    
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @PastOrPresent(message = "birth date must be past or present")
    @Column(name="birth_date")
    private LocalDate birthDate;
    
    @Positive
    @Column(name="positive_number")
    private Integer positiveNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }    
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getPositiveNumber() {
        return positiveNumber;
    }

    public void setPositiveNumber(Integer positiveNumber) {
        this.positiveNumber = positiveNumber;
    }
    
    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name=" + name + ", city=" + city + ", postalCode=" + postalCode + ", email=" + email + ", birthDate=" + birthDate + '}';
    }


}
