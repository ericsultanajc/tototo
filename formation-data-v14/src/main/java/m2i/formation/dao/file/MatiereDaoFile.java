package m2i.formation.dao.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import m2i.formation.dao.IMatiereDao;
import m2i.formation.exception.FormationDataException;
import m2i.formation.model.Difficulte;
import m2i.formation.model.Matiere;

public class MatiereDaoFile implements IMatiereDao {

	@Override
	public List<Matiere> findAll() {
		List<Matiere> matieres = read();

		return matieres;
	}

	@Override
	public Matiere find(Long id) {
		List<Matiere> matieres = read();

		for (Matiere matiere : matieres) {
			if (matiere.getId() == id) {
				return matiere;
			}
		}

		return null;
	}

	@Override
	public void create(Matiere obj) {
		List<Matiere> matieres = read();

		// Recherche de l'identifiant maximum
		long max = 0;
		for (Matiere matiere : matieres) {
			if (matiere.getId() > max) {
				max = matiere.getId();
			}
		}

		// Incrément du max pour attribuer à l'objet nouvellement créé
		max++;

		obj.setId(max);

		matieres.add(obj);

		write(matieres);
	}

	@Override
	public Matiere update(Matiere obj) {
		List<Matiere> matieres = read();

		// Recherche de la position dans la liste en fonction de l'identifiant
		int index;
		boolean find = false;

		for (index = 0; index < matieres.size(); index++) {
			if (obj.getId() == matieres.get(index).getId()) {
				find = true;
				break;
			}
		}

		// Si élément trouvé, on écrase la cellule avec le nouvel objet
		if (find) {
			matieres.set(index, obj);

			write(matieres);
		} else {
			throw new FormationDataException("Matiere n°" + obj.getId() + " non trouvée");
		}

		return obj;
	}

	@Override
	public void delete(Long id) {
		List<Matiere> matieres = read();

		// Recherche de la position dans la liste en fonction de l'identifiant
		int index = 0;
		boolean find = false;

		for (; index < matieres.size(); index++) {
			if (id == matieres.get(index).getId()) {
				find = true;
				break;
			}
		}

		// Si élément trouvé, on supprime la cellule
		if (find) {
			matieres.remove(index);

			write(matieres);
		} else {
			throw new FormationDataException("Matiere n°" + id + " non trouvée");
		}
	}

	@Override
	public List<Matiere> findAllByDifficulte(Difficulte difficulte) {
		List<Matiere> matieres = new ArrayList<Matiere>();

		for (Matiere matiere : read()) {
			if (matiere.getDifficulte().equals(difficulte)) {
				matieres.add(matiere);
			}
		}

		return matieres;
	}

	// lire le fichier et charger en mémoire la liste des matières
	private List<Matiere> read() {
		// initialisation une liste vide de matière
		List<Matiere> matieres = new ArrayList<Matiere>();

		// on positionne le chemin du fichier
		Path path = Paths.get("matieres.csv");

		File file = path.toFile();
		
		// vérification de l'existence du fichier
		if (file.exists()) {
			try {
				// lecture de toutes les lignes du fichier en une fois
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

				// parcours ligne à ligne du fichier
				for (String line : lines) {
					// séparation des données en précisant le séparateur
					String[] items = line.split(";");

					// récupération des données brutes
					String csvId = items[0];
					String csvNom = items[1];
					String csvDuree = items[2];
					String csvDifficulte = items[3];

					// conversion des données
					Long id = Long.valueOf(csvId);
					String nom = csvNom;
					int duree = Integer.valueOf(csvDuree);
					Difficulte difficulte = Difficulte.valueOf(csvDifficulte);

					// création de l'instance pour la ligne
					Matiere matiere = new Matiere(id, nom, duree, difficulte);

					// ajout de l'instance dans la liste
					matieres.add(matiere);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return matieres;
	}

	// parcourir la liste des matières et écrire dans le fichier (en mode overwrite)
	private void write(List<Matiere> matieres) {
		// initialisation une liste vide de chaine de caractères
		List<String> lines = new ArrayList<String>();

		// on parcout les matières passées en paramètre
		for (Matiere matiere : matieres) {
			// transformation objet vers string (id;nom;duree;difficule)
			StringBuilder sb = new StringBuilder();
			sb.append(matiere.getId()).append(";");
			sb.append(matiere.getNom()).append(";");
			sb.append(matiere.getDuree()).append(";");
			sb.append(matiere.getDifficulte());

			String line = sb.toString();

			// on rajoute la ligne créée à la liste globale
			lines.add(line);
		}

		// on positionne le chemin du fichier
		Path path = Paths.get("matieres.csv");

		try {
			// on écrit toutes les lignes en mémoire "lines" dans le fichier en réécrasant à
			// chaque fois
			Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new FormationDataException(e);
		}
	}

}
