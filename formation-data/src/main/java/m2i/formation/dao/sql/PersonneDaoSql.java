package m2i.formation.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import m2i.formation.Application;
import m2i.formation.dao.IPersonneDao;
import m2i.formation.exception.FormationDataException;
import m2i.formation.model.Adresse;
import m2i.formation.model.Civilite;
import m2i.formation.model.Formateur;
import m2i.formation.model.Personne;
import m2i.formation.model.Stagiaire;

public class PersonneDaoSql implements IPersonneDao {

	@Override
	public List<Personne> findAll() {
		List<Personne> personnes = new ArrayList<Personne>();

		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection.prepareStatement(
					"SELECT ID, TYPE, CIVILITY, LASTNAME, FIRSTNAME, EMAIL, BIRTHDATE, HIREDATE, EXPERIENCE, INTERNE, ADRESS_ID FROM PERSON");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Long id = rs.getLong("ID");
				String type = rs.getString("TYPE");
				Civilite civilite = !rs.getString("CIVILITY").isEmpty() ? Civilite.valueOf(rs.getString("CIVILITY"))
						: null;
				String nom = rs.getString("LASTNAME");
				String prenom = rs.getString("FIRSTNAME");
				String email = rs.getString("EMAIL");
				Date dtNaissance = rs.getDate("BIRTHDATE");
				Date dtEmbauche = rs.getDate("HIREDATE");
				int experience = rs.getInt("EXPERIENCE");
				boolean interne = rs.getBoolean("INTERNE");
				Long idAdresse = rs.getLong("ADRESS_ID");

				Personne personne = null;

				if (type.contentEquals("F")) { // == à tester
					personne = new Formateur(id, civilite, nom, prenom, email, dtEmbauche, experience, interne);
				} else {
					personne = new Stagiaire(id, civilite, nom, prenom, email, dtNaissance);
				}

				if (idAdresse != null) {
					Adresse adresse = Application.getInstance().getAdresseDao().find(idAdresse);

					personne.setAdresse(adresse);
				}

				personnes.add(personne);
			}

		} catch (SQLException e) {
			throw new FormationDataException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new FormationDataException(e);
			}
		}

		return personnes;
	}

	@Override
	public Personne find(Long id) {
		Personne personne = null;

		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection.prepareStatement(
					"SELECT TYPE, CIVILITY, LASTNAME, FIRSTNAME, EMAIL, BIRTHDATE, HIREDATE, EXPERIENCE, INTERNE, ADRESS_ID FROM PERSON WHERE ID = ?");

			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String type = rs.getString("TYPE");
				Civilite civilite = !rs.getString("CIVILITY").isEmpty() ? Civilite.valueOf(rs.getString("CIVILITY"))
						: null;
				String nom = rs.getString("LASTNAME");
				String prenom = rs.getString("FIRSTNAME");
				String email = rs.getString("EMAIL");
				Date dtNaissance = rs.getDate("BIRTHDATE");
				Date dtEmbauche = rs.getDate("HIREDATE");
				int experience = rs.getInt("EXPERIENCE");
				boolean interne = rs.getBoolean("INTERNE");
				Long idAdresse = rs.getLong("ADRESS_ID");

				if (type.contentEquals("F")) {
					personne = new Formateur(id, civilite, nom, prenom, email, dtEmbauche, experience, interne);
				} else {
					personne = new Stagiaire(id, civilite, nom, prenom, email, dtNaissance);
				}

				if (idAdresse != null) {
					Adresse adresse = Application.getInstance().getAdresseDao().find(idAdresse);

					personne.setAdresse(adresse);
				}
			}
		} catch (SQLException e) {
			throw new FormationDataException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new FormationDataException(e);
			}
		}

		return personne;
	}

	@Override
	public void create(Personne obj) {
		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO PERSON (TYPE, CIVILITY, LASTNAME, FIRSTNAME, EMAIL, BIRTHDATE, HIREDATE, EXPERIENCE, INTERNE, ADRESS_ID) VALUES (?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			ps.setString(2, obj.getCivilite().name());
			ps.setString(3, obj.getNom());
			ps.setString(4, obj.getPrenom());
			ps.setString(5, obj.getEmail());

			if (obj instanceof Formateur) {
				Formateur formateur = (Formateur) obj;
				ps.setString(1, "F");
				ps.setNull(6, Types.DATE);
				ps.setDate(7, new java.sql.Date(formateur.getDtEmbauche().getTime()));
				ps.setInt(8, formateur.getExperience());
				ps.setBoolean(9, formateur.isInterne());
			} else {
				Stagiaire stagiaire = (Stagiaire) obj;
				ps.setString(1, "S");
				ps.setDate(6, new java.sql.Date(stagiaire.getDtNaissance().getTime()));
				ps.setNull(7, Types.DATE);
				ps.setNull(8, Types.INTEGER);
				ps.setNull(9, Types.BOOLEAN);
			}

			if (obj.getAdresse() != null && obj.getAdresse().getId() != null) {
				ps.setLong(10, obj.getAdresse().getId());
			} else {
				ps.setNull(10, Types.INTEGER);
			}

			int rows = ps.executeUpdate();

			if (rows != 1) {
				throw new FormationDataException("Problème à l'insertion de la personne " + obj.getNom());
			} else {
				ResultSet keys = ps.getGeneratedKeys();
				if (keys.next()) {
					Long id = keys.getLong(1);
					obj.setId(id);
				}
			}

		} catch (SQLException e) {
			throw new FormationDataException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new FormationDataException(e);
			}
		}

	}

	@Override
	public Personne update(Personne obj) {
		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection.prepareStatement(
					"UPDATE PERSON SET CIVILITY = ?, LASTNAME = ?, FIRSTNAME = ?, EMAIL = ?, BIRTHDATE = ?, HIREDATE = ?, EXPERIENCE = ?, INTERNE = ?, ADRESS_ID = ? WHERE ID = ?");

			ps.setString(1, obj.getCivilite().name());
			ps.setString(2, obj.getNom());
			ps.setString(3, obj.getPrenom());
			ps.setString(4, obj.getEmail());

			if (obj instanceof Formateur) {
				Formateur formateur = (Formateur) obj;
				ps.setNull(5, Types.DATE);
				ps.setDate(6, new java.sql.Date(formateur.getDtEmbauche().getTime()));
				ps.setInt(7, formateur.getExperience());
				ps.setBoolean(8, formateur.isInterne());
			} else {
				Stagiaire stagiaire = (Stagiaire) obj;
				ps.setDate(5, new java.sql.Date(stagiaire.getDtNaissance().getTime()));
				ps.setNull(6, Types.DATE);
				ps.setNull(7, Types.INTEGER);
				ps.setNull(8, Types.BOOLEAN);
			}

			if (obj.getAdresse() != null && obj.getAdresse().getId() != null) {
				ps.setLong(9, obj.getAdresse().getId());
			} else {
				ps.setNull(9, Types.INTEGER);
			}

			ps.setLong(10, obj.getId());

			int rows = ps.executeUpdate();

			if (rows != 1) {
				throw new FormationDataException("Problème à la mise à jour de la personne n°" + obj.getId());
			}

		} catch (SQLException e) {
			throw new FormationDataException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new FormationDataException(e);
			}
		}

		return obj;
	}

	@Override
	public void delete(Long id) {
		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection.prepareStatement("DELETE FROM PERSON WHERE ID = ?");

			ps.setLong(1, id);

			int rows = ps.executeUpdate();

			if (rows != 1) {
				throw new FormationDataException("Problème à la suppresion de la personne n°" + id);
			}

		} catch (SQLException e) {
			throw new FormationDataException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new FormationDataException(e);
			}
		}
	}

	@Override
	public List<Personne> findAllByVille(String ville) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Formateur> findAllFormateurByMatiere(String nom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Formateur> findAllFormateur() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Stagiaire> findAllStagiaire() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Formateur findByStagiaire(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Stagiaire> findAllStagiaireByFormateur(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Formateur> findAllFormateurExperienceGreaterThan(int experience) {
		// TODO Auto-generated method stub
		return null;
	}

}
