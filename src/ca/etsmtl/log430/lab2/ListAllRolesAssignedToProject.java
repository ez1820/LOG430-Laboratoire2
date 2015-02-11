package ca.etsmtl.log430.lab2;

import java.util.Observable;

import ca.etsmtl.log430.common.Displays;
import ca.etsmtl.log430.common.Menus;
import ca.etsmtl.log430.common.Project;

/**
 * Upon notification, first lists the project and asks the user to pick one by
 * specifying its ID. If the project's ID is valid, then all the roles assigned
 * to that project are displayed.
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

public class ListAllRolesAssignedToProject extends Communication {

	public ListAllRolesAssignedToProject(Integer registrationNumber,
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
		Project myProject = new Project();

		if (registrationNumber.compareTo((Integer) notificationNumber) == 0) {
			/*
			 * First we use a Displays object to list all of the projects. Then
			 * we ask the user to pick a project using a Menus object.
			 */
			addToReceiverList("ListProjectsComponent");
			signalReceivers("ListProjectsComponent");
			myProject = menu.pickProject(CommonData.theListOfProjects
					.getListOfProjects());

			/*
			 * If the user selected an invalid project, then a message is
			 * printed to the terminal.
			 */
			if (myProject != null) {
				display.displayAllRolesAssignedToProject(myProject, CommonData.theListOfResources
						.getListOfResources());
			} else {
				System.out.println("\n\n *** Project not found ***");
			}
		}
		removeFromReceiverList("ListProjectsComponent");
	}
}