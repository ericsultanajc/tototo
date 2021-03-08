package m2i.formation.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import m2i.formation.Application;
import m2i.formation.dao.IMatiereDao;
import m2i.formation.exception.FormationDataException;
import m2i.formation.model.Difficulte;
import m2i.formation.model.Matiere;

public class MatiereDaoSql implements IMatiereDao {

	@Override
	public List<Matiere> findAll() {
		List<Matiere> matieres = new ArrayList<Matiere>();

		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection.prepareStatement("SELECT ID, NAME, DURATION, DIFFICULTY FROM SUBJECT");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Long id = rs.getLong("ID");
				String nom = rs.getString("NAME");
				int duree = rs.getInt("DURATION");
				Difficulte difficulte = Difficulte.valueOf(rs.getString("DIFFICULTY"));

				Matiere matiere = new Matiere(id, nom, duree, difficulte);

				matieres.add(matiere);
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

		return matieres;
	}

	@Override
	public Matiere find(Long id) {
		Matiere matiere = null;

		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection
					.prepareStatement("SELECT NAME, DURATION, DIFFICULTY FROM SUBJECT WHERE ID = ?");

			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String nom = rs.getString("NAME");
				int duree = rs.getInt("DURATION");
				Difficulte difficulte = Difficulte.valueOf(rs.getString("DIFFICULTY"));

				matiere = new Matiere(id, nom, duree, difficulte);
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

		return matiere;
	}

	@Override
	public void create(Matiere obj) {
		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();
			
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO SUBJECT (NAME, DURATION, DIFFICULTY) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, obj.getNom());
			ps.setInt(2, obj.getDuree());
			ps.setString(3, obj.getDifficulte().name());

			int rows = ps.executeUpdate();

			if (rows != 1) {
				throw new FormationDataException("Problème à l'insertion de la matière " + obj.getNom());
			} else {
				ResultSet keys = ps.getGeneratedKeys();
				if(keys.next()) {
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
	public Matiere update(Matiere obj) {
		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection
					.prepareStatement("UPDATE SUBJECT SET NAME = ?, DURATION = ?, DIFFICULTY = ? WHERE ID = ?");

			ps.setString(1, obj.getNom());
			ps.setInt(2, obj.getDuree());
			ps.setString(3, obj.getDifficulte().name());
			ps.setLong(4, obj.getId());

			int rows = ps.executeUpdate();

			if (rows != 1) {
				throw new FormationDataException("Problème à la mise à jour de la matière " + obj.getNom());
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

			PreparedStatement ps = connection
					.prepareStatement("DELETE FROM SUBJECT WHERE ID = ?");

			ps.setLong(1, id);

			int rows = ps.executeUpdate();

			if (rows != 1) {
				throw new FormationDataException("Problème à la suppresion de la matière n°" + id);
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
	public List<Matiere> findAllByDifficulte(Difficulte difficulte) {
		List<Matiere> matieres = new ArrayList<Matiere>();

		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection.prepareStatement("SELECT ID, NAME, DURATION FROM SUBJECT WHERE DIFFICULTY = ?");

			ps.setString(1, difficulte.name());
			
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Long id = rs.getLong("ID");
				String nom = rs.getString("NAME");
				int duree = rs.getInt("DURATION");

				Matiere matiere = new Matiere(id, nom, duree, difficulte);

				matieres.add(matiere);
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

		return matieres;
	}

}
