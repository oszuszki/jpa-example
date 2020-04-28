package person;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import person.Address;
import person.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Main {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        short count = 10;

        EntityManager em = emf.createEntityManager();

        for (int i = 0; i < count; ++i) {
            Person person = randomPerson();
            logger.info("Created Person instance: " + person);

            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();

            logger.info("ID of freshly crated Person: " + person.getId());

        }

        em.close();
        emf.close();
    }

    /**
     * Creates a random {@code Person} object and fills it with random data using Java-Faker.
     *
     * @return a {@code Person} object
     */
    public static Person randomPerson() {
        Faker faker = new Faker();

        Person person = Person.builder()
                .name(faker.name().fullName())
                .dob(dateToLocalDate(faker.date().birthday()))
                .gender(faker.options().option(Person.Gender.MALE, Person.Gender.FEMALE))
                .email(faker.internet().emailAddress())
                .profession(faker.company().profession())
                .build();

        Address address = Address.builder()
                .country(faker.address().country())
                .state(faker.address().state())
                .zip(faker.address().zipCode())
                .city(faker.address().city())
                .streetAddress(faker.address().streetAddress())
                .build();
        person.setAddress(address);

        return person;
    }

    /**
     * Convert a {@code Date} object to {@code LocalDate}.
     *
     * @param date the {@code Date} object that has to be converted to {@code LocalDate}
     * @return a {@code LocalDate} instance
     */
    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}