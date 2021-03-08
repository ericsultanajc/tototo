package m2i.formation.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import m2i.formation.Application;
import m2i.formation.model.Adresse;

public class TestJpa {
	public static void main(String[] args) {

		EntityManager em = null;
		EntityTransaction tx = null;

		Adresse ericAdresse = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();

			tx.begin();

			ericAdresse = new Adresse("1 rue de la Paix", "1er étage", "75008", "Paris", "France"); // new

			em.persist(ericAdresse); // managed

			ericAdresse.setCodePostal("75001"); // managed - dirty checking

			tx.commit(); // em.flush(); => analyse de tous les changements d'état précédents
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

		ericAdresse.setComplement("2ème étage"); // detached

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();

			Adresse ericAdresseCopy = em.merge(ericAdresse); // managed : ericAdresseCopy - detached : ericAdresse

			em.remove(ericAdresseCopy); // removed (à supprimer)

			em.persist(ericAdresseCopy); // managed

//		Formateur eric = em.find(Formateur.class, 1L);

			tx.commit(); // em.flush(); => analyse de tous les changements d'état précédents
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			if (em != null) {
				em.close();
			}
		}

	}
}
