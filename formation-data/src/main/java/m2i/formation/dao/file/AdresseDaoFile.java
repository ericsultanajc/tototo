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

import m2i.formation.dao.IAdresseDao;
import m2i.formation.exception.FormationDataException;
import m2i.formation.model.Adresse;

public class AdresseDaoFile implements IAdresseDao {

	@Override
	public List<Adresse> findAll() {
		return read();
	}

	@Override
	public Adresse find(Long id) {
		List<Adresse> adresses = read();

		for (Adresse adresse : adresses) {
			if (adresse.getId() == id) {
				return adresse;
			}
		}

		return null;
	}

	@Override
	public void create(Adresse obj) {
		List<Adresse> adresses = read();

		long max = 0;
		for (Adresse adresse : adresses) {
			if (adresse.getId() > max) {
				max = adresse.getId();
			}
		}

		max++;

		obj.setId(max);

		adresses.add(obj);

		write(adresses);
	}

	@Override
	public Adresse update(Adresse obj) {
		List<Adresse> adresses = read();

		int index;
		boolean find = false;

		for (index = 0; index < adresses.size(); index++) {
			if (obj.getId() == adresses.get(index).getId()) {
				find = true;
				break;
			}
		}

		if (find) {
			adresses.set(index, obj);

			write(adresses);
		} else {
			throw new FormationDataException("Adresse n°" + obj.getId() + " non trouvée");
		}
		
		return obj;
	}

	@Override
	public void delete(Long id) {
		List<Adresse> adresses = read();

		int index = 0;
		boolean find = false;

		for (; index < adresses.size(); index++) {
			if (id == adresses.get(index).getId()) {
				find = true;
				break;
			}
		}

		if (find) {
			adresses.remove(index);

			write(adresses);
		} else {
			throw new FormationDataException("Adresse n°" + id + " non trouvée");
		}
	}

	private List<Adresse> read() {
		List<Adresse> adresses = new ArrayList<Adresse>();

		Path path = Paths.get("adresses.csv");

		File file = path.toFile();

		if (file.exists()) {
			try {
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

				for (String line : lines) {
					String[] items = line.split(";");

					String csvId = items[0];
					String csvRue = items[1];
					String csvComplement = items[2];
					String csvCodePostal = items[3];
					String csvVille = items[4];
					String csvPays = items[5];

					Long id = Long.valueOf(csvId);
					String rue = csvRue;
					String complement = csvComplement;
					String codePostal = csvCodePostal;
					String ville = csvVille;
					String pays = csvPays;

					Adresse adresse = new Adresse(id, rue, complement, codePostal, ville, pays);

					adresses.add(adresse);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return adresses;
	}

	private void write(List<Adresse> adresses) {
		List<String> lines = new ArrayList<String>();

		for (Adresse adresse : adresses) {
			StringBuilder sb = new StringBuilder();
			sb.append(adresse.getId()).append(";");
			sb.append(adresse.getRue()).append(";");
			sb.append(adresse.getComplement()).append(";");
			sb.append(adresse.getCodePostal()).append(";");
			sb.append(adresse.getVille()).append(";");
			sb.append(adresse.getPays());

			String line = sb.toString();

			lines.add(line);
		}

		Path path = Paths.get("adresses.csv");

		try {
			Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new FormationDataException(e);
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
