package m2i.formation.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import m2i.formation.Application;
import m2i.formation.dao.IAdresseDao;
import m2i.formation.model.Adresse;

public class AdresseDaoJpa implements IAdresseDao {

	@Override
	public List<Adresse> findAll() {
		List<Adresse> adresses = new ArrayList<Adresse>();

		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			TypedQuery<Adresse> query = em.createQuery("select adr from Adresse adr", Adresse.class);

			adresses = query.getResultList();

			tx.commit();
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

		return adresses;
	}

	@Override
	public Adresse find(Long id) {
		Adresse adresse = null;

		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			adresse = em.find(Adresse.class, id);

			tx.commit();
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

		return adresse;
	}

	@Override
	public void create(Adresse obj) {
		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			em.persist(obj);

			tx.commit();
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

	@Override
	public Adresse update(Adresse obj) {
		Adresse adresse = null;

		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			adresse = em.merge(obj);

			tx.commit();
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

		return adresse;
	}

	@Override
	public void delete(Long id) {
		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			Adresse adresse = em.find(Adresse.class, id);
			em.remove(adresse);

			tx.commit();
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

	@Override
	public List<Adresse> findAllByVille(String ville) {
		List<Adresse> adresses = new ArrayList<Adresse>();

		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			TypedQuery<Adresse> query = em.createQuery("select adr from Adresse adr where adr.ville = :maVille", Adresse.class);

			query.setParameter("maVille", ville);
			
			adresses = query.getResultList();
			
			tx.commit();
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

		return adresses;
	}

	@Override
	public List<Adresse> findAllByCodePostal(String codePostal) {
		List<Adresse> adresses = new ArrayList<Adresse>();

		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			TypedQuery<Adresse> query = em.createQuery("select adr from Adresse adr where adr.codePostal = :cp", Adresse.class);

			query.setParameter("cp", codePostal);
			
			adresses = query.getResultList();
			
			tx.commit();
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

		return adresses;
	}

	@Override
	public List<Adresse> findAllByVilleOrCodePostal(String ville, String codePostal) {
		List<Adresse> adresses = new ArrayList<Adresse>();

		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			TypedQuery<Adresse> query = em.createQuery("select adr from Adresse adr where adr.ville = :ville or adr.codePostal = :cp", Adresse.class);

			query.setParameter("ville", ville);
			query.setParameter("cp", codePostal);
			
			adresses = query.getResultList();
			
			tx.commit();
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

		return adresses;
	}

	@Override
	public Adresse findByPersonne(Long id) {
		Adresse adresse = null;

		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			TypedQuery<Adresse> query = em.createQuery("select p.adresse from Personne p where p.id = :id", Adresse.class);

			query.setParameter("id", id);
			
			adresse = query.getSingleResult();
			
			tx.commit();
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

		return adresse;
	}

}
