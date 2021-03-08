package m2i.formation.test;

import m2i.formation.Application;
import m2i.formation.dao.IAdresseDao;
import m2i.formation.model.Adresse;

public class TestDaoJpa {
	public static void main(String[] args) {
		IAdresseDao adresseDao = Application.getInstance().getAdresseDao();

		Adresse ericAdresse = new Adresse("1 rue de la Paix", "1er étage", "75008", "Paris", "France"); // new

		adresseDao.create(ericAdresse);
		
		ericAdresse.setCodePostal("75001"); // managed - dirty checking
		
		adresseDao.update(ericAdresse);

		ericAdresse.setComplement("2ème étage"); // detached

		adresseDao.update(ericAdresse);
		
		adresseDao.delete(ericAdresse.getId());
	}
}
