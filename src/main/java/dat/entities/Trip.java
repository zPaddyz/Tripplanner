package dat.entities;

import dat.dtos.TripDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "starttime", nullable = false)
    private LocalTime startTime;

    @Column(name = "endtime", nullable = false)
    private LocalTime endTime;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guide_id")
    private Guide guide;

    // Constructor using TripDTO
    public Trip(TripDTO tripDTO) {
        this.id = tripDTO.getId();
        this.name = tripDTO.getName();
        this.date = tripDTO.getDate();
        this.startTime = tripDTO.getStartTime();
        this.endTime = tripDTO.getEndTime();
        this.longitude = tripDTO.getLongitude();
        this.latitude = tripDTO.getLatitude();
        this.price = tripDTO.getPrice();
        this.category = tripDTO.getCategory();
        this.guide = tripDTO.getGuide();
    }

    // Full constructor including all fields
    public Trip(String name, LocalDate date, LocalTime startTime, LocalTime endTime,
                Double longitude, Double latitude, Double price, Guide guide) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.price = price;
        this.guide = guide;
    }

    // Constructor without guide
    public Trip(String name, LocalDate date, LocalTime startTime, LocalTime endTime,
                Double longitude, Double latitude, Double price, Category category) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.price = price;
        this.category = category;
    }
    public enum Category {
        beach, city, forest, lake, sea, snow}
}


