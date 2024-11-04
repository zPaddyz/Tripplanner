package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.GuideDTO;
import dat.entities.Guide;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class GuideDAO implements IDAO<GuideDTO, Integer> {

    private static GuideDAO instance;
    private static EntityManagerFactory emf;

    public GuideDAO(EntityManagerFactory _emf){
        this.emf = _emf;
    }

    public static GuideDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GuideDAO();
        }
        return instance;
    }

    @Override
    public GuideDTO getByID(Integer integer) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, integer);
            em.getTransaction().commit();
            return guide != null ? new GuideDTO(guide) : null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<GuideDTO> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<GuideDTO> query = em.createQuery("SELECT new dat.dtos.GuideDTO(h) FROM Guide h", GuideDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public GuideDTO create(GuideDTO doctorDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = new Guide(doctorDTO);
            em.persist(guide);
            em.getTransaction().commit();
            return new GuideDTO(guide);
        }
    }

    @Override
    public GuideDTO update(Integer integer, GuideDTO guideDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the Guide entity by ID
            Guide e = em.find(Guide.class, integer);
            if (e == null) {
                em.getTransaction().rollback();
                return null; // Return null if the entity is not found
            }

            // Update fields
            e.setFirstName(guideDTO.getFirstName());
            e.setLastName(guideDTO.getLastName());
            e.setYearsOfExperience(guideDTO.getYearsOfExperience());
            e.setEmail(guideDTO.getEmail());
            e.setPhone(guideDTO.getPhone());

            // Merge and commit the transaction
            Guide mergedGuide = em.merge(e);
            em.getTransaction().commit();

            return mergedGuide != null ? new GuideDTO(mergedGuide) : null;
        }
    }


    @Override
    public void delete(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, integer);
            if (guide != null) {
                em.remove(guide);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Guide guide = em.find(Guide.class, integer);
            return guide != null;
        }
    }

    //TODO: skal nok slettes!
/*
    public List<GuideDTO> doctorByBirthdateRange(LocalDate from, LocalDate to) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Guide> query = em.createQuery(
                    "SELECT d FROM Guide d WHERE d.dateOfBirth BETWEEN :from AND :to", Guide.class);
            query.setParameter("from", from);
            query.setParameter("to", to);
            List<Guide> guides = query.getResultList();
            return guides.stream()
                    .map(GuideDTO::new)
                    .collect(Collectors.toList());
        }
    }

    public List<GuideDTO> doctorBySpeciality(Guide.Speciality speciality) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Guide> query = em.createQuery("SELECT d FROM Guide d WHERE d.speciality = :speciality", Guide.class);
            query.setParameter("speciality", speciality);
            List<Guide> guides = query.getResultList();
            return guides.stream()
                    .map(GuideDTO::new)
                    .collect(Collectors.toList());
        }
    }*/
}


