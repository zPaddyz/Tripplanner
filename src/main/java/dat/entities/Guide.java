package dat.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dat.dtos.GuideDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "guide")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guide_id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Setter
    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Setter
    @Column(name = "years_of_experience", nullable = false)
    private int yearsOfExperience;

    @Setter
    @Column(name = "email", nullable = false)
    private String email;

    @Setter
    @Column(name = "phone", nullable = false)
    private int phone;

    @JsonIgnore
    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Trip> trips;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Full constructor
    public Guide(String firstName, String lastName, int yearsOfExperience, String email, int phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearsOfExperience = yearsOfExperience;
        this.email = email;
        this.phone = phone;
    }

    // Constructor using GuideDTO
    public Guide(GuideDTO guideDTO) {
        this.id = guideDTO.getId();
        this.firstName = guideDTO.getFirstName();
        this.lastName = guideDTO.getLastName();
        this.yearsOfExperience = guideDTO.getYearsOfExperience();
        this.email = guideDTO.getEmail();
        this.phone = guideDTO.getPhone();

        // Initialize trips if GuideDTO has appointments
        if (guideDTO.getTrips() != null) {
            guideDTO.getTrips().forEach(tripDTO -> trips.add(new Trip(tripDTO)));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guide guide = (Guide) o;
        return Objects.equals(firstName, guide.firstName) &&
                Objects.equals(lastName, guide.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
