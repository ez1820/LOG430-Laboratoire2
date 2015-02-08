package ca.etsmtl.log430.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class defines the Resource object for the system. Besides the basic
 * attributes, there are two lists maintained. alreadyAssignedProjectList is a
 * ProjectList object that maintains a list of projects that the resource was
 * already assigned to prior to this execution of the system.
 * projectsAssignedList is also a ProjectList object that maintains a list of
 * projects assigned to the resource durint the current execution or session.
 * 
 * @author A.J. Lattanze, CMU
 * @version 1.6, 2013-Sep-13
 */

/* Modification Log
 ****************************************************************************
 * v1.6, R. Champagne, 2013-Sep-13 - Various refactorings for new lab.
 * 
 * v1.5, R. Champagne, 2012-Jun-19 - Various refactorings for new lab.
 * 
 * v1.4, R. Champagne, 2012-May-31 - Various refactorings for new lab.
 * 
 * v1.3, R. Champagne, 2012-Feb-02 - Various refactorings for new lab.
 * 
 * v1.2, 2011-Feb-02, R. Champagne - Various refactorings, javadoc comments.
 *  
 * v1.1, 2002-May-21, R. Champagne - Adapted for use at ETS. 
 * 
 * v1.0, 12/29/99, A.J. Lattanze - Original version.

 ****************************************************************************/

public class Resource {

	/**
	 * Resource's last name
	 */
	private String lastName;

	/**
	 * Resource's first name
	 */
	private String firstName;

	/**
	 * Resource's identification number
	 */
	private String id;

	/**
	 * Resource role 
	 */
	private String role;

	/**
	 *  List of projects the resource is already allocated to
	 */
	private ProjectList alreadyAssignedProjectList = new ProjectList();

	/**
	 *  List of projects assigned to the resource in this session
	 */
	private ProjectList projectsAssignedList = new ProjectList();

	/**
	 * Assigns a project to a resource.
	 * 
	 * @param project
	 */
	public void assignProject(Project project) {

		getProjectsAssigned().addProject(project);

	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public void setPreviouslyAssignedProjectList(ProjectList projectList) {
		this.alreadyAssignedProjectList = projectList;
	}

	public ProjectList getPreviouslyAssignedProjectList() {
		return alreadyAssignedProjectList;
	}

	public void setProjectsAssigned(ProjectList projectList) {
		this.projectsAssignedList = projectList;
	}

	public ProjectList getProjectsAssigned() {
		return projectsAssignedList;
	}
	
	/**
	 * Tests wether a resource would be overloaded if we assigned him the project passed in the parameters
	 * 
	 * @param newProject
	 * @param projectList
	 * @return
	 * @throws ParseException
	 */
	public boolean isOverloaded(Project newProject, ProjectList projectList) throws ParseException {
		boolean overloaded = false;
		boolean done = false;

		Project oldProject;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CANADA);
		Date newStartDate, newEndDate;

		newStartDate = format.parse(newProject.getStartDate());
		newEndDate = format.parse(newProject.getEndDate());
		this.alreadyAssignedProjectList.goToFrontOfList();
		this.projectsAssignedList.goToFrontOfList();
		
		//Check if he'd be overloaded with the projects that we previously assigned to him
		while(!done){
			oldProject = this.alreadyAssignedProjectList.getNextProject();

			if (oldProject == null || overloaded==true){
				done = true;
			}
			else{
				oldProject = projectList.findProjectByID(oldProject.getID());

				if(newStartDate.compareTo(format.parse(oldProject.getStartDate()))>=0 && (newStartDate.compareTo(format.parse(oldProject.getEndDate())))<=0){
					if((newProject.getLoadByPriority(newProject.getPriority())+oldProject.getLoadByPriority(oldProject.getPriority()))>=100){
						System.out.println("You cannot assign this project to this resource. He will be overloaded");
						overloaded = true;
					}
				}
				else if(newEndDate.compareTo(format.parse(oldProject.getStartDate()))>=0 && (newEndDate.compareTo(format.parse(oldProject.getEndDate())))<=0){
					if((newProject.getLoadByPriority(newProject.getPriority())+oldProject.getLoadByPriority(oldProject.getPriority()))>=100){
						System.out.println("You cannot assign this project to this resource. He will be overloaded");
						overloaded = true;
					}					
				}
				else if(format.parse(oldProject.getStartDate()).compareTo(newStartDate)>=0 && (format.parse(oldProject.getEndDate()).compareTo(newEndDate))<=0){
					if((newProject.getLoadByPriority(newProject.getPriority())+oldProject.getLoadByPriority(oldProject.getPriority()))>=100){
						System.out.println("You cannot assign this project to this resource. He will be overloaded");
						overloaded = true;
					}					
				}
			}			
		}

		done = false;
		
		//Check if he'd be overloaded with the projects assigned to him during the current session (while the program is running)
		while(!done){
			oldProject = this.projectsAssignedList.getNextProject();

			if (oldProject == null || overloaded==true){
				done = true;
			}
			else{
				oldProject = projectList.findProjectByID(oldProject.getID());

				if(newStartDate.after(format.parse(oldProject.getStartDate())) && (newStartDate.before(format.parse(oldProject.getEndDate())))){
					if((newProject.getLoadByPriority(newProject.getPriority())+oldProject.getLoadByPriority(oldProject.getPriority()))>=100){
						System.out.println("You cannot assign this project to this resource. He will be overloaded");
						overloaded = true;
					}					
				}
				else if(newEndDate.after(format.parse(oldProject.getStartDate())) && (newEndDate.before(format.parse(oldProject.getEndDate())))){
					if((newProject.getLoadByPriority(newProject.getPriority())+oldProject.getLoadByPriority(oldProject.getPriority()))>=100){
						System.out.println("You cannot assign this project to this resource. He will be overloaded");
						overloaded = true;
					}					
				}
				else if(format.parse(oldProject.getStartDate()).after(newStartDate) && (format.parse(oldProject.getEndDate()).before(newEndDate))){
					if((newProject.getLoadByPriority(newProject.getPriority())+oldProject.getLoadByPriority(oldProject.getPriority()))>=100){
						System.out.println("You cannot assign this project to this resource. He will be overloaded");
						overloaded = true;
					}					
				}
			}

		}
		
		return overloaded;
	}

} // Resource class