package m2i.formation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import m2i.formation.dao.IAdresseDao;
import m2i.formation.dao.IMatiereDao;
import m2i.formation.dao.IPersonneDao;
import m2i.formation.dao.jpa.AdresseDaoJpa;
import m2i.formation.dao.jpa.MatiereDaoJpa;
import m2i.formation.dao.jpa.PersonneDaoJpa;

public class Application {
	private static Application instance = null;

	private final IAdresseDao adresseDao = new AdresseDaoJpa();
	private final IMatiereDao matiereDao = new MatiereDaoJpa();
	private final IPersonneDao personneDao = new PersonneDaoJpa();
	private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("formation-data");

	// empêcher d'instancier le singleton depuis l'extérieur
	private Application() {
	}

	// méthode static de création du singleton
	public static Application getInstance() {
		// on ne crée l'instance qu'au premier appel de la méthode
		if (instance == null) {
			instance = new Application();
		}

		return instance;
	}

	public IAdresseDao getAdresseDao() {
		return adresseDao;
	}

	public IMatiereDao getMatiereDao() {
		return matiereDao;
	}

	public IPersonneDao getPersonneDao() {
		return personneDao;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/formation-data", "root", "admin");
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

}
