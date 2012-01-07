package at.happylab.fablabtool.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import at.happylab.fablabtool.SelectOption;
import at.happylab.fablabtool.model.Consumable;
import at.happylab.fablabtool.model.Device;
import at.happylab.fablabtool.model.Package;

public class ConsumableManagement implements Serializable {

	private static final long serialVersionUID = -3130705490590748129L;

	@Inject
	private EntityManager em;

	public ConsumableManagement(EntityManager em) {
		this.em = em;
	}

	public ConsumableManagement() {

	}

	public List<Consumable> getAllConsumables() {
		return em.createQuery("from Consumable", Consumable.class).getResultList();

	}
	
	public List<SelectOption> getAllPackagesForDropDown() {
		List<Package> results = em.createQuery("from Consumable", Package.class).getResultList();

		List<SelectOption> selectOptions = new ArrayList<SelectOption>();

		for (Package p : results) {
			selectOptions.add(new SelectOption<Package>(p, p.getName()));
		}

		return selectOptions;
	}


}

