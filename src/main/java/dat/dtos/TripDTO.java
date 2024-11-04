package dat.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dat.entities.Trip;
import dat.entities.Guide;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO {

    private Long id;
    private String name;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double longitude;
    private Double latitude;
    private Double price;
    private Trip.Category category;

    @JsonIgnore
    private Guide guide;

    // Constructor to convert from Trip entity to TripDTO
    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.date = trip.getDate();
        this.startTime = trip.getStartTime();
        this.endTime = trip.getEndTime();
        this.longitude = trip.getLongitude();
        this.latitude = trip.getLatitude();
        this.price = trip.getPrice();
        this.guide = trip.getGuide();
        this.category = trip.getCategory();
    }

    // Constructor without guide (e.g., for cases where guide info isn't needed)
    public TripDTO(String name, LocalDate date, LocalTime startTime, LocalTime endTime,
                   Double longitude, Double latitude, Double price, Trip.Category category) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.price = price;
        this.category = category;
    }

}


