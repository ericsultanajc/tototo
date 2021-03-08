package m2i.formation.dao.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import m2i.formation.Application;
import m2i.formation.dao.IPersonneDao;
import m2i.formation.exception.FormationDataException;
import m2i.formation.model.Adresse;
import m2i.formation.model.Civilite;
import m2i.formation.model.Formateur;
import m2i.formation.model.Matiere;
import m2i.formation.model.Personne;
import m2i.formation.model.Stagiaire;

public class PersonneDaoFile implements IPersonneDao {

	@Override
	public List<Personne> findAll() {
		List<Personne> personnes = read();

		return personnes;
	}

	@Override
	public Personne find(Long id) {
		List<Personne> personnes = read();

		for (Personne personne : personnes) {
			if (personne.getId() == id) {
				return personne;
			}
		}

		return null;
	}

	@Override
	public void create(Personne obj) {
		List<Personne> personnes = read();

		long max = 0;
		for (Personne personne : personnes) {
			if (personne.getId() > max) {
				max = personne.getId();
			}
		}

		max++;

		obj.setId(max);

		personnes.add(obj);

		write(personnes);

	}

	@Override
	public Personne update(Personne obj) {
		List<Personne> personnes = read();

		int index;
		boolean find = false;

		for (index = 0; index < personnes.size(); index++) {
			if (obj.getId() == personnes.get(index).getId()) {
				find = true;
				break;
			}
		}

		if (find) {
			personnes.set(index, obj);

			write(personnes);
		} else {
			throw new FormationDataException("Personne n°" + obj.getId() + " non trouvée");
		}
		
		return obj;
	}

	@Override
	public void delete(Long id) {
		List<Personne> personnes = read();

		int index = 0;
		boolean find = false;

		for (; index < personnes.size(); index++) {
			if (id == personnes.get(index).getId()) {
				find = true;
				break;
			}
		}

		if (find) {
			personnes.remove(index);

			write(personnes);
		} else {
			throw new FormationDataException("Personne n°" + id + " non trouvée");
		}
	}

	private List<Personne> read() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		List<Personne> personnes = new ArrayList<Personne>();
		Map<Stagiaire, Long> liaisons = new HashMap<Stagiaire, Long>();
		Map<Long, List<Matiere>> competences = new HashMap<Long, List<Matiere>>();

		Path pathCompetence = Paths.get("competences.csv");

		if (pathCompetence.toFile().exists()) {
			try {
				List<String> lines = Files.readAllLines(pathCompetence, StandardCharsets.UTF_8);

				for (String line : lines) {
					String[] items = line.split(";", -1);

					Long idFormateur = Long.valueOf(items[0]);
					Long idMatiere = Long.valueOf(items[1]);

					if (!competences.containsKey(idFormateur)) {
						competences.put(idFormateur, new ArrayList<Matiere>());
					}
					
					Matiere matiere = Application.getInstance().getMatiereDao().find(idMatiere);
					
					competences.get(idFormateur).add(matiere);
				}
			} catch (IOException e) {
				throw new FormationDataException(e);
			}

		}

		Path path = Paths.get("personnes.csv");

		File file = path.toFile();

		if (file.exists()) {
			try {
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

				for (String line : lines) {
					String[] items = line.split(";", -1);

					String type = items[0];
					Long id = Long.valueOf(items[1]);
					Civilite civilite = !items[2].isEmpty() ? Civilite.valueOf(items[2]) : null;
					String nom = items[3];
					String prenom = items[4];
					String email = items[5];
					Date dtEmbauche = null;
					try {
						dtEmbauche = sdf.parse(items[6]);
					} catch (ParseException e) {
					}
					int experience = 0;
					try {
						experience = Integer.valueOf(items[7]);
					} catch (NumberFormatException e) {
					}
					boolean interne = Boolean.valueOf(items[8]);
					Date dtNaissance = null;
					try {
						dtNaissance = sdf.parse(items[9]);
					} catch (ParseException e) {
					}

					Long idAdresse = !items[10].isEmpty() ? Long.valueOf(items[10]) : null;

					Long idFormateur = !items[11].isEmpty() ? Long.valueOf(items[11]) : null;

					Personne personne = null;

					if (type.contentEquals("F")) {
						personne = new Formateur();

						Formateur formateur = (Formateur) personne;

						formateur.setDtEmbauche(dtEmbauche);
						formateur.setExperience(experience);
						formateur.setInterne(interne);
						
						if(competences.containsKey(formateur.getId())) {
							formateur.setCompetences(competences.get(formateur.getId()));
						}
					} else {
						personne = new Stagiaire();

						((Stagiaire) personne).setDtNaissance(dtNaissance);

						if (idFormateur != null) {
							liaisons.put((Stagiaire) personne, idFormateur);
						}
					}

					personne.setId(id);
					personne.setCivilite(civilite);
					personne.setNom(nom);
					personne.setPrenom(prenom);
					personne.setEmail(email);

					if (idAdresse != null) {
						Adresse adresse = Application.getInstance().getAdresseDao().find(idAdresse);

						personne.setAdresse(adresse);
					}

					personnes.add(personne);
				}

				for (Stagiaire stagiaire : liaisons.keySet()) {
					Long idFormateur = liaisons.get(stagiaire);

					for (Personne personne : personnes) {
						if (personne.getId() == idFormateur) {
							stagiaire.setFormateur((Formateur) personne);
							break;
						}
					}
				}

			} catch (IOException e) {
				throw new FormationDataException(e);
			}
		}

		return personnes;
	}

	private void write(List<Personne> personnes) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		List<String> competences = new ArrayList<String>();
		List<String> lines = new ArrayList<String>();

		for (Personne personne : personnes) {
			StringBuilder sb = new StringBuilder();

			if (personne instanceof Formateur) {
				sb.append("F");
			} else {
				sb.append("S");
			}

			sb.append(";");
			sb.append(personne.getId()).append(";");
			sb.append(personne.getCivilite()).append(";");
			sb.append(personne.getNom()).append(";");
			sb.append(personne.getPrenom()).append(";");
			sb.append(personne.getEmail()).append(";");

			if (personne instanceof Formateur) {
				Formateur formateur = (Formateur) personne;
				sb.append(sdf.format(formateur.getDtEmbauche())).append(";");
				sb.append(formateur.getExperience()).append(";");
				sb.append(formateur.isInterne()).append(";").append(";");
			} else {
				Stagiaire stagiaire = (Stagiaire) personne;
				sb.append(";");
				sb.append(";");
				sb.append(";");
				sb.append(sdf.format(stagiaire.getDtNaissance())).append(";");
			}

			if (personne.getAdresse() != null && personne.getAdresse().getId() != null) {
				sb.append(personne.getAdresse().getId());
			}

			sb.append(";");

			if (personne instanceof Stagiaire && ((Stagiaire) personne).getFormateur() != null
					&& ((Stagiaire) personne).getFormateur().getId() != null) {
				sb.append(((Stagiaire) personne).getFormateur().getId());
			}

			String line = sb.toString();

			lines.add(line);

			if (personne instanceof Formateur) {
				for (Matiere matiere : ((Formateur) personne).getCompetences()) {
					if (matiere.getId() == null) {
						throw new FormationDataException("La matière non persistente " + matiere.getNom()
								+ " du formateur n°" + personne.getId());
					}
					String str = personne.getId() + ";" + matiere.getId();
					competences.add(str);

				}
			}

		}

		Path path = Paths.get("personnes.csv");

		try {
			Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new FormationDataException(e);
		}

		Path pathCompetence = Paths.get("competences.csv");

		try {
			Files.write(pathCompetence, competences, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new FormationDataException(e);
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
