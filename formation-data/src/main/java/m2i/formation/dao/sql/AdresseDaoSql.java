package m2i.formation.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import m2i.formation.Application;
import m2i.formation.dao.IAdresseDao;
import m2i.formation.exception.FormationDataException;
import m2i.formation.model.Adresse;

public class AdresseDaoSql implements IAdresseDao {

	@Override
	public List<Adresse> findAll() {
		List<Adresse> adresses = new ArrayList<Adresse>();

		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection
					.prepareStatement("SELECT ID, STREET, COMPLEMENT, ZIPCODE, CITY, COUNTRY FROM ADRESS");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Long id = rs.getLong("ID");
				String rue = rs.getString("STREET");
				String complement = rs.getString("COMPLEMENT");
				String codePostal = rs.getString("ZIPCODE");
				String ville = rs.getString("CITY");
				String pays = rs.getString("COUNTRY");
				

				Adresse adresse = new Adresse(id, rue, complement, codePostal, ville, pays);

				adresses.add(adresse);
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

		return adresses;
	}

	@Override
	public Adresse find(Long id) {
		Adresse adresse = null;

		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection
					.prepareStatement("SELECT STREET, COMPLEMENT, ZIPCODE, CITY, COUNTRY FROM ADRESS WHERE ID = ?");

			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String rue = rs.getString("STREET");
				String complement = rs.getString("COMPLEMENT");
				String codePostal = rs.getString("ZIPCODE");
				String ville = rs.getString("CITY");
				String pays = rs.getString("COUNTRY");

				adresse = new Adresse(id, rue, complement, codePostal, ville, pays);
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

		return adresse;
	}

	@Override
	public void create(Adresse obj) {
		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO ADRESS (STREET, COMPLEMENT, ZIPCODE, CITY, COUNTRY) VALUES (?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			ps.setString(1, obj.getRue());
			ps.setString(2, obj.getComplement());
			ps.setString(3, obj.getCodePostal());
			ps.setString(4, obj.getVille());
			ps.setString(5, obj.getPays());

			int rows = ps.executeUpdate();

			if (rows != 1) {
				throw new FormationDataException("Problème à l'insertion de l'adresse " + obj.getRue());
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
	public Adresse update(Adresse obj) {
		Connection connection = null;

		try {
			connection = Application.getInstance().getConnection();

			PreparedStatement ps = connection.prepareStatement(
					"UPDATE ADRESS SET STREET = ?, COMPLEMENT = ?, ZIPCODE = ?, CITY = ?, COUNTRY = ? WHERE ID = ?");

			ps.setString(1, obj.getRue());
			ps.setString(2, obj.getComplement());
			ps.setString(3, obj.getCodePostal());
			ps.setString(4, obj.getVille());
			ps.setString(5, obj.getPays());
			ps.setLong(6, obj.getId());

			int rows = ps.executeUpdate();

			if (rows != 1) {
				throw new FormationDataException("Problème à la mise à jour de l'adresse n°" + obj.getId());
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

			PreparedStatement ps = connection.prepareStatement("DELETE FROM ADRESS WHERE ID = ?");

			ps.setLong(1, id);

			int rows = ps.executeUpdate();

			if (rows != 1) {
				throw new FormationDataException("Problème à la suppresion de l'adresse n°" + id);
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
	public List<Adresse> findAllByVille(String ville) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Adresse> findAllByCodePostal(String codePostal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Adresse> findAllByVilleOrCodePostal(String ville, String codePostal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Adresse findByPersonne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
