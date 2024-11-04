package dat.daos.impl;

import dat.daos.IDAO;
import dat.daos.ITripGuideDAO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TripDAO implements IDAO<TripDTO, Integer>, ITripGuideDAO {

    private static TripDAO instance;
    private static EntityManagerFactory emf;

    public TripDAO(EntityManagerFactory _emf) {
        emf = _emf;
    }

    public static TripDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripDAO();
        }
        return instance;
    }

    @Override
    public TripDTO getByID(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            em.getTransaction().commit();
            return trip != null ? new TripDTO(trip) : null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<TripDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TripDTO> query = em.createQuery(
                    "SELECT new dat.dtos.TripDTO(a) FROM Trip a", TripDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public TripDTO create(TripDTO appointmentDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = new Trip(appointmentDTO);
            em.persist(trip);
            em.getTransaction().commit();
            return new TripDTO(trip);
        }
    }

    @Override
    public TripDTO update(Integer id, TripDTO tripDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the Trip entity by ID
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                em.getTransaction().rollback();
                return null; // Return null if the entity is not found
            }

            // Update fields
            trip.setName(tripDTO.getName());
            trip.setDate(tripDTO.getDate());
            trip.setStartTime(tripDTO.getStartTime());
            trip.setEndTime(tripDTO.getEndTime());
            trip.setLongitude(tripDTO.getLongitude());
            trip.setLatitude(tripDTO.getLatitude());
            trip.setPrice(tripDTO.getPrice());

            // Merge and commit the transaction
            Trip mergedTrip = em.merge(trip);
            em.getTransaction().commit();

            return mergedTrip != null ? new TripDTO(mergedTrip) : null;
        }
    }


    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip != null) {
                em.remove(trip);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, id);
            return trip != null;
        }
    }


    //TODO: SKAL TJEKKES!
        @Override
        public void addGuideToTrip(int tripId, int guideId) {
            EntityManager em = emf.createEntityManager();
            Trip trip;
            try {
                em.getTransaction().begin();

                // Find the Trip by ID
                trip = em.find(Trip.class, tripId);
                if (trip == null) {
                    throw new IllegalArgumentException("Trip not found with ID: " + tripId);
                }

                // Find the Guide by ID
                Guide guide = em.find(Guide.class, guideId);
                if (guide == null) {
                    throw new IllegalArgumentException("Guide not found with ID: " + guideId);
                }

                // Associate the Guide with the Trip
                trip.setGuide(guide);

                // Persist changes
                em.merge(trip);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw e; // Re-throw the exception after rollback
            } finally {
                em.close();
            }
        }

        @Override
        public Set<TripDTO> getTripsByGuide(int guideId) {
            EntityManager em = emf.createEntityManager();
            Set<TripDTO> trips = new HashSet<>();
            try {
                // Query to find trips by guide
                TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t WHERE t.guide.id = :guideId", Trip.class);
                query.setParameter("guideId", guideId);
                List<Trip> tripList = query.getResultList();

                // Convert Trip entities to TripDTOs
                for (Trip trip : tripList) {
                    trips.add(new TripDTO(trip));
                }
            } finally {
                em.close();
            }
            return trips;
        }

    @Override
    public List<TripDTO> filterByCategory(String category) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TripDTO> query = em.createQuery(
                    "SELECT new dat.dtos.TripDTO(a) FROM Trip a WHERE a.category = :category", TripDTO.class);
            query.setParameter("category", Trip.Category.valueOf(category));
            return query.getResultList();
        }
    }
}
