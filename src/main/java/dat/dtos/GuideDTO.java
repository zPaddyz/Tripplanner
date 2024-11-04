package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dat.entities.Guide;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuideDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private int yearsOfExperience;
    private String email;
    private int phone;

    @JsonIgnore
    private Set<TripDTO> trips = new HashSet<>();

    // Constructor to convert from Guide entity to GuideDTO
    public GuideDTO(Guide guide) {
        this.id = guide.getId();
        this.firstName = guide.getFirstName();
        this.lastName = guide.getLastName();
        this.yearsOfExperience = guide.getYearsOfExperience();
        this.email = guide.getEmail();
        this.phone = guide.getPhone();

        // Initialize trips if Guide has trips
        if (guide.getTrips() != null) {
            guide.getTrips().forEach(trip -> trips.add(new TripDTO(trip)));
        }
    }

    // Constructor without trips (e.g., for simpler cases where trips info isn't needed)
    public GuideDTO(String firstName, String lastName, int yearsOfExperience, String email, int phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearsOfExperience = yearsOfExperience;
        this.email = email;
        this.phone = phone;
    }

    // Static utility method to convert a list of Guide entities to a list of GuideDTOs
    public static List<GuideDTO> toGuideDTOList(List<Guide> guides) {
        return guides.stream().map(GuideDTO::new).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuideDTO guideDto)) return false;
        return getId().equals(guideDto.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    // Getters and Setters can be added here as needed
}

