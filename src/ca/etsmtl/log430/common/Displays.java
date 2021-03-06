package ca.etsmtl.log430.common;

import ca.etsmtl.log430.common.Project;
import ca.etsmtl.log430.common.ResourceList;
import ca.etsmtl.log430.common.Resource;


/**
 * This class displays various types of information on projects and resources
 * (individually and as lists) to the screen.
 * 
 * @author A.J. Lattanze, CMU
 * @version 1.6, 2013-Sep-13
 */

/*
 * Modification Log
 * ************************************************************************
 * v1.6, R. Champagne, 2013-Sep-13 - Various refactorings for new lab.
 * 
 * v1.5, R. Champagne, 2012-Jun-19 - Various refactorings for new lab.
 * 
 * v1.3, R. Champagne, 2012-Feb-02 - Various refactorings for new lab.
 * 
 * v1.2, 2011-Feb-02, R. Champagne - Various refactorings, javadoc comments.
 * 
 * v1.1, 2002-May-21, R. Champagne - Adapted for use at ETS.
 * 
 * v1.0, 12/29/99, A.J. Lattanze - Original version.
 * ************************************************************************
 */

public class Displays {

	private int lineCount = 0;
	private int maxLinesDisplayed = 18;

	/**
	 * Counts the number of lines that has been printed. Once a set number of
	 * lines has been printed, the user is asked to press the enter key to
	 * continue. This prevents lines of text from scrolling off of the page.
	 * 
	 * @param linesToAdd
	 */
	private void lineCheck(int linesToAdd) {

		Termio terminal = new Termio();

		if (lineCount >= maxLinesDisplayed) {

			lineCount = 0;
			System.out.print("\n*** Press Enter To Continue ***");
			terminal.keyboardReadChar();

		} else {

			lineCount += linesToAdd;

		} // if

	} // LineCheck

	/**
	 * Displays a resource object's elements as follows: Resource's first name,
	 * last name, ID number, role.
	 * 
	 * Note that the projects previously assigned to the resource and the projects
	 * assigned to the resource in this execution of the system are not displayed.
	 * 
	 * @param resource
	 */
	public void displayResource(Resource resource) {

		System.out.println(resource.getID() + " "
				+ resource.getFirstName() + " "
				+ resource.getLastName() + " "
				+ resource.getRole());
	}

	/**
	 * Displays a project object's elements as follows: ID, name, start date,
	 * end date, and priority. Note that the resources assigned to the project
	 * are not listed by this method.
	 * 
	 * @param project
	 */
	public void displayProject(Project project) {
		System.out.println(project.getID() + " "
				+ project.getProjectName() + " "
				+ project.getStartDate() + " "
				+ project.getEndDate() + " "
				+ project.getPriority());
	}

	/**
	 * Lists the resources that have been assigned to the project.
	 * 
	 * @param project
	 */
	public void displayResourcesAssignedToProject(Project project) {

		boolean done;
		Resource resource;

		System.out.println("\nResources assigned to: " + " "
				+ project.getID() + " " + project.getProjectName() + " :");
		lineCheck(1);

		System.out
		.println("===========================================================");
		lineCheck(1);

		project.getResourcesAssigned().goToFrontOfList();
		done = false;

		while (!done) {

			resource = project.getResourcesAssigned().getNextResource();

			if (resource == null) {

				done = true;

			} else {

				displayResource(resource);

			} // if

		} // while

	}

	/**
	 * Lists the projects currently assigned to a resource during this session.
	 * 
	 * @param resource
	 */
	public void displayProjectsAssignedToResource(Resource resource) {

		boolean done;
		Project project;

		System.out.println("\nProjects assigned (in this session) to : "
				+ resource.getFirstName() + " " + resource.getLastName() + " "
				+ resource.getID());
		lineCheck(2);
		System.out
		.println("========================================================= ");
		lineCheck(1);

		resource.getProjectsAssigned().goToFrontOfList();
		done = false;

		while (!done) {

			project = resource.getProjectsAssigned().getNextProject();

			if (project == null) {

				done = true;

			} else {

				displayProject(project);
				lineCheck(2);

			} // if

		} // while

	}

	/**
	 * Lists the projects currently assigned to a resource during this session.
	 * 
	 * @param resource
	 */
	public void displayProjectsPreviouslyAssignedToResource(Resource resource, ProjectList list) {

		boolean done;
		Project project;

		System.out.println("\nProjects previously assigned to : "
				+ resource.getFirstName() + " " + resource.getLastName() + " "
				+ resource.getID());
		lineCheck(2);
		System.out.println("========================================================= ");
		lineCheck(1);

		resource.getPreviouslyAssignedProjectList().goToFrontOfList();
		done = false;

		while (!done) {

			project = resource.getPreviouslyAssignedProjectList().getNextProject();

			if (project == null || project.getProjectName() == null) {
				done = true;

			} else {
				displayProject(list.findProjectByID(project.getID()));
				lineCheck(2);

			} // if

		} // while

	}
	
	/**
	 * 
	 * @param project
	 * @param list
	 */
	public void displayAllRolesAssignedToProject(Project project, ResourceList list){

		boolean done;
		Resource resource;
		int countANA = 0, countDES = 0, countPRG = 0, countTST = 0;
		int countPreviousANA = 0, countPreviousDES = 0, countPreviousPRG = 0, countPreviousTST = 0;

		list.goToFrontOfList();
		done = false;

		while (!done) {
			
			resource = list.getNextResource();

			if (resource == null) {
				done = true;
			} else {

				if(resource.getRole().equals("ANA")){
					if(resource.getPreviouslyAssignedProjectList().findProjectByID(project.getID()) != null)
						countPreviousANA++;
					if(resource.getProjectsAssigned().findProjectByID(project.getID()) != null)
						countANA++;
				}
				else if(resource.getRole().equals("DES")){
					if(resource.getPreviouslyAssignedProjectList().findProjectByID(project.getID()) != null)
						countPreviousDES++;
					if(resource.getProjectsAssigned().findProjectByID(project.getID()) != null)
						countDES++;
				}
				else if(resource.getRole().equals("PRG")){
					if(resource.getPreviouslyAssignedProjectList().findProjectByID(project.getID()) != null)
						countPreviousPRG++;
					if(resource.getProjectsAssigned().findProjectByID(project.getID()) != null)
						countPRG++;
				}
				else if(resource.getRole().equals("TST")){
					if(resource.getPreviouslyAssignedProjectList().findProjectByID(project.getID()) != null)
						countPreviousTST++;
					if(resource.getProjectsAssigned().findProjectByID(project.getID()) != null)
						countTST++;
				}
			} // if

		} // while

		countANA+=countPreviousANA;
		countDES+=countPreviousDES;
		countPRG+=countPreviousPRG;
		countTST+=countPreviousTST;

		System.out.println("Avant\nANA "+countPreviousANA+"\nDES "+countPreviousDES+"\nPRG "+countPreviousPRG+"\nTST "+countPreviousTST+"\n");
		System.out.println("Apres\nANA "+countANA+"\nDES "+countDES+"\nPRG "+countPRG+"\nTST "+countTST+"\n\n");

		countANA=0;
		countPreviousANA=0;
		countDES=0;
		countPreviousDES=0;
		countPRG=0;
		countPreviousPRG=0;
		countTST=0;
		countPreviousTST=0;

		
	}

	/**
	 * Displays the resources in a resource list. Displays the same information
	 * that is listed in the displayResource() method listed above.
	 * 
	 * @param list
	 */
	public void displayResourceList(ResourceList list) {

		boolean done;
		Resource resource;

		System.out.print("\n");
		lineCheck(1);

		list.goToFrontOfList();

		done = false;

		while (!done) {

			resource = list.getNextResource();

			if (resource == null) {

				done = true;

			} else {

				displayResource(resource);
				lineCheck(1);

			} // if

		} // while

	}

	/**
	 * Displays the projects in a project list. Displays the same
	 * information that is listed in the displayProject() method listed above.
	 * 
	 * @param list
	 */
	public void displayProjectList(ProjectList list) {

		boolean done;
		Project project;

		System.out.print("\n");
		lineCheck(1);

		list.goToFrontOfList();
		done = false;

		while (!done) {

			project = list.getNextProject();

			if (project == null) {

				done = true;

			} else {

				displayProject(project);
				lineCheck(1);

			} // if

		} // while

	}

} // Display