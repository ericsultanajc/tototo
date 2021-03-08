package m2i.formation.test;

import java.text.ParseException;

import m2i.formation.Application;
import m2i.formation.dao.IMatiereDao;
import m2i.formation.model.Difficulte;
import m2i.formation.model.Matiere;

public class FormationDao {

	public static void main(String[] args) throws ParseException {
		IMatiereDao matiereDao = Application.getInstance().getMatiereDao();

		Matiere html = new Matiere("HTML", 2, Difficulte.FACILE);
		Matiere javascript = new Matiere("Javascript", 3, Difficulte.MOYEN);

		Matiere java = new Matiere("JAVA", 5, Difficulte.FACILE);
		Matiere springCore = new Matiere("Spring Core", 3, Difficulte.MOYEN);
		Matiere angular = new Matiere("Angular", 5, Difficulte.DIFFICILE);

		matiereDao.create(html);
		matiereDao.create(javascript);
		matiereDao.create(java);
		matiereDao.create(springCore);
		matiereDao.create(angular);

		for (Matiere matiere : matiereDao.findAll()) {
			System.out.println(matiere.getId() + "-" + matiere.getNom());
		}

		Matiere htmlFind = matiereDao.find(html.getId());
		
		htmlFind.setDuree(4);
		
		matiereDao.update(htmlFind);
		
		matiereDao.delete(angular.getId());
		
		System.out.println("################");
		
		for (Matiere matiere : matiereDao.findAllByDifficulte(Difficulte.DIFFICILE)) {
			System.out.println(matiere.getId() + "-" + matiere.getNom());
		}

	}

}
