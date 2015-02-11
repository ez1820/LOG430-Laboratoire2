package ca.etsmtl.log430.lab2;

import java.util.Observable;

import ca.etsmtl.log430.common.Displays;
import ca.etsmtl.log430.common.Menus;
import ca.etsmtl.log430.common.Resource;

/**
 * Upon notification, first lists the resources and asks the user to pick one by
 * specifying its ID. If the resource's ID is valid, then the projects previously assigned
 * to that resource are listed.
 * 
 * @author Christian Bamatembera
 * @version 1.0, 2015-Feb-11
 */

/*
 * Modification Log **********************************************************
 * 
 * v1.0, Christian Bamatembera, 02/11/2015 - Original version.
 * ***************************************************************************
 */

public class ListProjectsPreviouslyAssignedToResource extends Communication {

	public ListProjectsPreviouslyAssignedToResource(Integer registrationNumber,
			String componentName) {
		super(registrationNumber, componentName);
	}

	/**
	 * The update() method is an abstract method that is called whenever the
	 * notifyObservers() method is called by the Observable class. First we
	 * check to see if the NotificationNumber is equal to this thread's
	 * RegistrationNumber. If it is, then we execute.
	 * 
	 * @see ca.etsmtl.log430.lab2.Communication#update(java.util.Observable,
	 *      java.lang.Object)
	 */
	public void update(Observable thing, Object notificationNumber) {
		Menus menu = new Menus();
		Displays display = new Displays();
		Resource myResource = new Resource();

		if (registrationNumber.compareTo((Integer) notificationNumber) == 0) {
			/*
			 * First we use a Displays object to list all of the resources. Then
			 * we ask the user to pick a resource using a Menus object.
			 */
			addToReceiverList("ListResourcesComponent");
			signalReceivers("ListResourcesComponent");
			myResource = menu.pickResource(CommonData.theListOfResources
					.getListOfResources());

			/*
			 * If the user selected an invalid resource, then a message is
			 * printed to the terminal.
			 */
			if (myResource != null) {
				display.displayProjectsPreviouslyAssignedToResource(myResource, CommonData.theListOfProjects.getListOfProjects());
			} else {
				System.out.println("\n\n *** Resource not found ***");
			}
		}
		removeFromReceiverList("ListResourcesComponent");
	}
}