package m2i.formation.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import m2i.formation.Application;
import m2i.formation.dao.IPersonneDao;
import m2i.formation.model.Civilite;
import m2i.formation.model.Formateur;
import m2i.formation.model.Personne;
import m2i.formation.model.Stagiaire;

public class PersonneDaoJpa implements IPersonneDao {

	@Override
	public List<Personne> findAll() {
		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

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

		return null;
	}

	@Override
	public Personne find(Long id) {
		Personne personne = null;

		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			personne = em.find(Personne.class, id);

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

		return personne;
	}

	@Override
	public void create(Personne obj) {
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
	public Personne update(Personne obj) {
		Personne personne = null;

		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			personne = em.merge(obj);

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

		return personne;
	}

	@Override
	public void delete(Long id) {
		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			Personne personne = em.find(Personne.class, id);
			em.remove(personne);

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
	public List<Personne> findAllByVille(String ville) {
		List<Personne> personnes = new ArrayList<Personne>();
		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			TypedQuery<Personne> query = em.createQuery("select p from Personne p where p.adresse.ville = :ville",
					Personne.class);

			query.setParameter("ville", ville);

			personnes = query.getResultList();

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
		return personnes;
	}

	@Override
	public List<Formateur> findAllFormateurByMatiere(String nom) {
		List<Formateur> formateurs = new ArrayList<Formateur>();
		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			TypedQuery<Formateur> query = em.createQuery(
					"select f from Formateur f left join f.competences c where c.nom = :nom", Formateur.class);

			query.setParameter("nom", nom);

			formateurs = query.getResultList();

//			Même résultat mais en démarrant par la classe Matiere			
//			TypedQuery<Formateur> queryBis = em.createQuery("select f from Matiere m join m.formateurs f where m.nom = :nom", Formateur.class);

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
		return formateurs;
	}

	public List<Personne> search(Civilite civilite, String nom, String prenom, String email) {
		List<Personne> personnes = new ArrayList<Personne>();
		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<Personne> criteriaQuery = criteriaBuilder.createQuery(Personne.class);

			Root<Personne> root = criteriaQuery.from(Personne.class); // from Personne

			ParameterExpression<Civilite> civ = criteriaBuilder.parameter(Civilite.class);

			criteriaQuery.select(root); // select p from Personne p
			criteriaQuery.where(criteriaBuilder.equal(criteriaBuilder.literal(1), criteriaBuilder.literal(1)));

			if (civilite != null) {
				criteriaQuery.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("civilite"), civ)));
			}

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

		return personnes;
	}

	@Override
	public List<Formateur> findAllFormateur() {
		List<Formateur> formateurs = new ArrayList<Formateur>();
		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			TypedQuery<Formateur> query = em.createQuery("select f from Formateur f", Formateur.class);

			formateurs = query.getResultList();

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
		return formateurs;
	}

	@Override
	public List<Stagiaire> findAllStagiaire() {
		List<Stagiaire> stagiaires = new ArrayList<Stagiaire>();
		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

			TypedQuery<Stagiaire> query = em.createQuery("select s from Stagiaire s", Stagiaire.class);

			stagiaires = query.getResultList();

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

		return stagiaires;
	}

	@Override
	public Formateur findByStagiaire(Long id) {
		Formateur formateur = null;

		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

//			TypedQuery<Formateur> query = em.createQuery("select f from Formateur f join f.stagiaires s where s.id = :id", Formateur.class);
//			TypedQuery<Formateur> query = em.createQuery("select f from Stagiaire s join s.formateur f where s.id = :id", Formateur.class);
			TypedQuery<Formateur> query = em.createQuery("select s.formateur from Stagiaire s where s.id = :id", Formateur.class);

			formateur = query.getSingleResult();

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

		return formateur;
	}

	@Override
	public List<Stagiaire> findAllStagiaireByFormateur(Long id) {
		List<Stagiaire> stagiaires = new ArrayList<Stagiaire>();
		EntityManager em = null;
		EntityTransaction tx = null;

		try {
			em = Application.getInstance().getEmf().createEntityManager();
			tx = em.getTransaction();
			tx.begin();

//			TypedQuery<Stagiaire> query = em.createQuery("select s from Formateur f join f.stagiaires s where f.id = :id", Stagiaire.class);
//			TypedQuery<Stagiaire> query = em.createQuery("select s from Stagiaire s where s.formateur.id = :id", Stagiaire.class);
			TypedQuery<Stagiaire> query = em.createQuery("select s from Stagiaire s join s.formateur f where f.id = :id", Stagiaire.class);

			query.setParameter("id", id);
			
			stagiaires = query.getResultList();

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

		return stagiaires;
	}

	@Override
	public List<Formateur> findAllFormateurExperienceGreaterThan(int experience) {
		// TODO Auto-generated method stub
		return null;
	}

}
