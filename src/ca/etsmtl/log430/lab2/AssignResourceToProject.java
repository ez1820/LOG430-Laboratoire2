package ca.etsmtl.log430.lab2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;

import ca.etsmtl.log430.common.Menus;
import ca.etsmtl.log430.common.Project;
import ca.etsmtl.log430.common.Resource;
import ca.etsmtl.log430.common.ProjectList;

/**
 * Assigns resources to projects.
 *  
 * @author A.J. Lattanze, CMU
 * @version 1.5, 2013-Oct-06
 */

/*
 * Modification Log **********************************************************
 * v1.5, R. Champagne, 2013-Oct-06 - Various refactorings for new lab.
 * 
 * v1.4, R. Champagne, 2012-Jun-19 - Various refactorings for new lab.
 * 
 * v1.3, R. Champagne, 2012-Feb-14 - Various refactorings for new lab.
 * 
 * v1.2, R. Champagne, 2011-Feb-24 - Various refactorings, conversion of
 * comments to javadoc format.
 * 
 * v1.1, R. Champagne, 2002-Jun-19 - Adapted for use at ETS.
 * 
 * v1.0, A.J. Lattanze, 12/29/99 - Original version.
 * ***************************************************************************
 */
public class AssignResourceToProject extends Communication
{
	public AssignResourceToProject(Integer registrationNumber, String componentName) {
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
		Resource myResource = new Resource();
		Project myProject = new Project();

		if (registrationNumber.compareTo((Integer)notificationNumber) == 0) {
			addToReceiverList("ListResourcesComponent");
			addToReceiverList("ListProjectsComponent");

			// Display the resources and prompt the user to pick one

			signalReceivers("ListResourcesComponent");

			myResource = menu.pickResource(CommonData.theListOfResources.getListOfResources());

			if (myResource != null) {
				/*
				 * Display the projects that are available and ask the user to
				 * pick one
				 */
				signalReceivers("ListProjectsComponent");

				myProject = menu.pickProject(CommonData.theListOfProjects.getListOfProjects());

				if (myProject != null)	{	
					/*
					 * If the selected project and resource exist, then complete
					 * the assignment process.
					 */
					
					try {
						if(isOverloaded(myProject, CommonData.theListOfProjects.getListOfProjects(), myResource)){
							System.out.println("You cannot assign this project to this resource. He will be overloaded");						
						}else {
							myProject.assignResource(myResource);
							myResource.assignProject(myProject);
							System.out.println("Project added successfully to the resource");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} else {
					System.out.println("\n\n *** Project not found ***");
				} 
			} else {
				System.out.println("\n\n *** Resource not found ***");
			}
		}
	}
	
	/**
	 * Tests whether a resource would be overloaded if we assigned him the project passed in the parameters
	 * 
	 * @param newProject
	 * @param projectList
	 * @return
	 * @throws ParseException
	 */
	private boolean isOverloaded(Project newProject, ProjectList projectList, Resource ress) throws ParseException {
		boolean overloaded = false;
		boolean done = false;

		Project oldProject;
		int newPriority = getPriorityValue(newProject.getPriority());
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CANADA);
		Date newStartDate, newEndDate;

		newStartDate = format.parse(newProject.getStartDate());
		newEndDate = format.parse(newProject.getEndDate());
		
		//Check if he'd be overloaded with the projects that we previously assigned to him
		while(!done){
			oldProject = ress.getPreviouslyAssignedProjectList().getNextProject();
			
			if (oldProject == null || overloaded==true){
				done = true;
			}
			else{
				oldProject = projectList.findProjectByID(oldProject.getID());
				
				int oldPriority = getPriorityValue(oldProject.getPriority());
				
				if(newStartDate.compareTo(format.parse(oldProject.getStartDate()))>=0 && (newStartDate.compareTo(format.parse(oldProject.getEndDate())))<=0){
					if((newPriority + oldPriority) > 100){
						overloaded = true;
					}
				}
				else if(newEndDate.compareTo(format.parse(oldProject.getStartDate()))>=0 && (newEndDate.compareTo(format.parse(oldProject.getEndDate())))<=0){
					if((newPriority + oldPriority) > 100){
						overloaded = true;
					}					
				}
				else if(format.parse(oldProject.getStartDate()).compareTo(newStartDate)>=0 && (format.parse(oldProject.getEndDate()).compareTo(newEndDate))<=0){
					if((newPriority + oldPriority) > 100){
						overloaded = true;
					}					
				}
			}			
		}
			
		done = false;
		
		//Check if he'd be overloaded with the projects that we assigned to him
		while(!done){
			oldProject = ress.getProjectsAssigned().getNextProject();
			
			if (oldProject == null || overloaded==true){
				done = true;
			}
			else{
				oldProject = projectList.findProjectByID(oldProject.getID());
				
				int oldPriority = Integer.parseInt(oldProject.getPriority());
				
				if(newStartDate.compareTo(format.parse(oldProject.getStartDate()))>=0 && (newStartDate.compareTo(format.parse(oldProject.getEndDate())))<=0){
					if((newPriority + oldPriority) > 100){
						overloaded = true;
					}
				}
				else if(newEndDate.compareTo(format.parse(oldProject.getStartDate()))>=0 && (newEndDate.compareTo(format.parse(oldProject.getEndDate())))<=0){
					if((newPriority + oldPriority) > 100){
						overloaded = true;
					}					
				}
				else if(format.parse(oldProject.getStartDate()).compareTo(newStartDate)>=0 && (format.parse(oldProject.getEndDate()).compareTo(newEndDate))<=0){
					if((newPriority + oldPriority) > 100){
						overloaded = true;
					}					
				}
			}			
		}
		
		return overloaded;
	}
	
	private int getPriorityValue(String priority){
		int val = 0;
		switch (priority) {
		case "L":
			val = 25;
			break;
		case "M":
			val = 50;
			break;
		case "H":
			val = 100;
			break;
		}
		
		return val;
	}
}