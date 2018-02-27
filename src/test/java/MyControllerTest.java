import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dissertationWeb.classes.CheckList;
import org.dissertationWeb.classes.DBConnection;
import org.dissertationWeb.classes.Project;
import org.dissertationWeb.classes.User;
import org.dissertationWeb.controller.MyController;
import org.dissertationWeb.controller.SQLController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration                      // <-- enable WebApp Test Support
@ContextConfiguration("/Spring-Mail.xml")
public class MyControllerTest {

	private Connection newConnection;

	@InjectMocks 
	private SQLController sqlController;

	private MyController controller;

	private ModelAndView view;

	private DBConnection connect;

	private HttpServletRequest request;

	private HttpSession session;

	private Model model;

	private User user;

	private User userFail;

	//Constant to control user type
	public static final int LECTURER = 1;

	public static final int STUDENT = 2;

	public static final int COORDINATOR = 3;

	List<CheckList> list1;

	List<CheckList> list2;

	List<Project> projectList1;

	List<Project> projectList2;

	@Before
	public void setUp() throws Exception {
		//Mockito for request and session
		model = mock(Model.class);
		request = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
		sqlController = mock(SQLController.class);
		//controller = mock(MyController.class);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userID")).thenReturn(3);		

		controller = new MyController();
		view = new ModelAndView();
		connect = new DBConnection();
		newConnection = connect.connect();
		//sqlController = new SQLController();

		user = new User(3, "student", "student","asd@asd.com", 2, 2017);
		userFail = new User(3, "student", "wrongpassword","asd@asd.com", 2, 2017);

		list1 = new ArrayList<CheckList>();
		list1.add(new CheckList(1, "22/2/12", "test1", "4x5", "Test", "15:00", "16:00"));
		list1.add(new CheckList(2, "22/2/12", "test2", "4x5", "Test", "15:00", "16:00"));
		list1.add(new CheckList(3, "22/2/12", "test3", "4x5", "Test", "15:00", "16:00"));
		list1.add(new CheckList(4, "22/2/12", "test4", "4x5", "Test", "15:00", "16:00"));

		list2 = new ArrayList<CheckList>();
		list2.add(new CheckList(1, "2017/3/12", "test1", "4x5", "Test", "15:00", "16:00"));
		list2.add(new CheckList(2, "2017/2/12", "test2", "4x5", "Test", "15:00", "16:00"));
		list2.add(new CheckList(7, "2018/2/12", "test7", "4x5", "Test", "15:00", "16:00"));
		list2.add(new CheckList(8, "2018/1/12", "test8", "4x5", "Test", "15:00", "16:00"));

		projectList1 = new ArrayList<Project>();		
		projectList1.add(new Project(1,2018, "test1", "HTML","none","test"));
		projectList1.add(new Project(2,2017, "test2", "HTML","none","test"));
		projectList1.add(new Project(3,2017, "test3", "HTML","none","test"));
		projectList1.add(new Project(4,2018, "test4", "HTML","none","test"));

		projectList2 = new ArrayList<Project>();
		projectList2.add(new Project(1,2018, "test1", "HTML","none","test"));
		projectList2.add(new Project(2,2017, "test2", "HTML","none","test"));
		projectList2.add(new Project(5,2017, "test5", "HTML","none","test"));
		projectList2.add(new Project(6,2018, "test6", "HTML","none","test"));
	}

	@After
	public void tearDown() throws Exception {
		controller = null;
		view = null;
		connect = null;
		newConnection = null;
		sqlController = null;
		list1 = null;
		list2 = null;
		projectList1 = null;
		projectList2 = null;
	}

	@Test
	public void testCheckDBConnection() throws SQLException {
		newConnection.close();
		assertTrue("Connection is close", newConnection.isClosed());
	}

	@Test
	public void testRemoveDuplicateFromEventList() {
		int sizeList1 = list1.size();
		controller.removeDuplicateFromEventList(list1, list2);
		int sizeAfterList1 = list1.size();
		assertTrue("Size list is not smaller",sizeList1 > sizeAfterList1);
		//This test found that I was reducing i to -1 in some cases, already fixed
	}

	@Test
	public void testRemoveDuplicateFromProjectList() {
		int sizeList1 = projectList1.size();
		controller.removeDuplicateFromProjectList(projectList1, projectList2);
		int sizeAfterList1 = projectList1.size();
		assertTrue("Size list is not smaller",sizeList1 > sizeAfterList1);
		//This test found that I was reducing i to -1 in some cases, already fixed
	}

	@Test
	public void testHomePage() throws SQLException {
		view = controller.homePage(request);
		assertNotNull(view);
	}

	@Test
	public void testLogin() throws SQLException {
		view = controller.login(request);
		assertNotNull(view);
	}

	@Test
	public void testCheckLogin() throws SQLException {
		view = controller.checkLogin(user, model, request);
		//Uncomment this line to see how, using a wrong userID and password will lead to errorPage instead of homePage
		//view = controller.checkLogin(userFail, model, request);
		assertNotEquals("errorPage", view.getViewName());
	}

	@Test
	public void testContactusPage() throws SQLException {
		view = controller.contactusPage(model, request);
		assertEquals("contactusPage", view.getViewName());
	}

	@Test
	public void testProjectListPage() throws SQLException {
		when(session.getAttribute("projectNum")).thenReturn(4);
		when(session.getAttribute("oldProjectNum")).thenReturn(4);
		view = controller.projectListPage(model, request);
		assertEquals("projectListPage", view.getViewName());
	}

	@Test
	public void testProjectListToApprovePage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.projectListToApprovePage(model, request);
		assertEquals("projectListtoapprovePage", view.getViewName());
	}

	@Test
	public void testApproveprojectPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.approveprojectPage(88, model, request);
		assertEquals("projectPage", view.getViewName());
	}

	@Test
	public void testNewprojectPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(LECTURER);
		view = controller.newprojectPage(model, request);
		assertEquals("newprojectPage", view.getViewName());
	}

	@Test
	public void testNewprojectNextYearPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(LECTURER);
		view = controller.newprojectNextYearPage(model, request);
		assertEquals("newprojectPage", view.getViewName());
	}

	@Test
	public void testNewprojectProposalPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(STUDENT);
		view = controller.newprojectProposalPage(model, request);
		assertEquals("projectproposalpage", view.getViewName());
	}

	@Test
	public void testSaveProposal() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(STUDENT);
		Project project = new Project();
		view = controller.saveProposal(project, model, request);
		assertEquals("homePage", view.getViewName());
	}

	@Test
	public void testEditprojectPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(STUDENT);
		view = controller.editprojectPage(88, model, request);
		assertEquals("editprojectPage", view.getViewName());
	}

	@Test
	public void testRemoveprojectPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.removeprojectPage(89, model, request);
		assertEquals("projectRemovedPage", view.getViewName());
	}

	@Test
	public void testSaveEditProject() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		when(sqlController.getActualYear()).thenReturn(2017); 
		SQLController sql = new SQLController();
		Project project = sql.getProject(88);
		project.setTopics(project.getTopics() + ", HTML5");//need to update the project if not test will fail (same project equal error)
		view = controller.saveEditProject(project, model, request);
		assertEquals("projectPage", view.getViewName());
		view = controller.saveEditProject(project, model, request);//project without modification
		assertEquals("errorPage", view.getViewName());
	}

	@Test
	public void testSave() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		Project project = new Project(102,2017, "test2", "HTML","none","test");
		project.setTopics(project.getTopics() + ", HTML90");//need to update the project if not test will fail (same project equal error)
		view = controller.save(project, model, request);
		assertEquals("projectPage", view.getViewName());
	}

	@Test
	/**
	 * Error in projectList found (if user tried to access projectlist directly without login, 500 error was show, fixed
	 * Beside this this test found that the query that I was running was wrong and was showing in the search result projects
	 * that are already taken, fixed
	 * @throws SQLException
	 */
	public void testSearch() throws SQLException {
		//This one works, since we have a project with the title AppSisted Parking
		view = controller.search("AppSisted Parking", null, null, "title", model, request);
		assertEquals("projectListPage", view.getViewName());
		//This one should fail since I am looking for a lecturer named APPSisted Parking
		view = controller.search("AppSisted Parking", "lecturer", null, null, model, request);
		assertEquals("errorPage", view.getViewName());
	}

	@Test
	public void testSearchStudent() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		//This one works, since we have a student with the name john
		view = controller.searchStudent("john", "name", null, model, request);
		assertEquals("studentListPage", view.getViewName());
		//This one should fail since that email does not exist in the database
		view = controller.searchStudent("notrealname@email.com", null, "email", model, request);
		assertEquals("errorPage", view.getViewName());
	}

	@Test
	public void testNewchecklistPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.newchecklistPage(model, request);
		assertEquals("checklistPage", view.getViewName());
	}

	@Test
	public void testSaveChecklist() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.saveChecklist(new CheckList(50, "2018-03-03", "test8", "4x5", "Test", "15:00", "16:00"), model, request);
		assertEquals("checklistViewPage", view.getViewName());
	}

	@Test
	public void testChecklistListPage() throws SQLException {
		when(session.getAttribute("eventNum")).thenReturn(3);	
		when(session.getAttribute("oldEventNum")).thenReturn(4);
		view = controller.checklistListPage(model, request);
		assertEquals("checklistListPage", view.getViewName());
	}

	@Test
	public void testBubblesrt() {
		CheckList checklist = list2.get(0);
		controller.bubblesrt(list2);
		CheckList checklist2 = list2.get(0);
		assertFalse("Bubble sort successful", checklist.getEventName().equals(checklist2.getEventName()));
		//Uncomment to see how the testing fail, this example shows that the list was sorted by date
		//assertTrue("Bubble sort was not successful", checklist.getEventName().equals(checklist2.getEventName()));
	}

	@Test
	public void testEditChecklistPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.editChecklistPage(31, model, request);
		assertEquals("editchecklistPage", view.getViewName());
	}

	@Test
	public void testRemoveChecklistPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.removeChecklistPage(31, model, request);
		assertEquals("projectRemovedPage", view.getViewName());
	}

	@Test
	public void testMakeVisibleChecklistPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.makeVisibleChecklistPage(31, model, request);
		assertEquals("checklistListPage", view.getViewName());
	}

	@Test
	public void testSaveEditChecklist() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		CheckList checklist = new CheckList(31, "2018-03-03", "test90", "4x5", "Test", "15:00", "17:00");
		view = controller.saveEditChecklist(checklist, model, request);
		assertEquals("checklistViewPage", view.getViewName());
	}

	@Test
	public void testRegisterInterestPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(STUDENT);
		when(session.getAttribute("userID")).thenReturn(7);//I need a student that does not have a final project already approved
		view = controller.registerInterestPage(100, model, request);
		
		assertEquals("interestProjectListPage", view.getViewName());
		when(session.getAttribute("userID")).thenReturn(3);//Since student with studentID 3 have a final project, this will show an error
		view = controller.registerInterestPage(100, model, request);
		assertEquals("errorPage", view.getViewName());
	}

	@Test
	public void testMakeVisibleInterestPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(STUDENT);
		//Uncomment this to show that lecturer are not allowed to enter to this area
		//when(session.getAttribute("userType")).thenReturn(LECTURER);
		when(session.getAttribute("userID")).thenReturn(5);//This student is the one that have this project not visible
		view = controller.makeVisibleInterestPage(87, model, request);
		assertEquals("interestProjectListPage", view.getViewName());
	}

	@Test
	public void testRemoveInterest() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(STUDENT);
		user = new User(3, "john", "111111","ismael.sanchez.leon@gmail.com", 2, 2017);
		view = controller.removeInterest(85, model, user, request);
		assertEquals("projectRemovedPage", view.getViewName());
	}

	@Test
	public void testRemoveInterestFinal() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		user = new User(5, "john", "111111","ismael.sanchez.leon@gmail.com", 2, 2017);
		view = controller.removeInterestFinal(87, model, user, request);
		assertEquals("projectRemovedPage", view.getViewName());
	}


	@Test
	public void testRemoveInterestStudent() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(STUDENT);
		user = new User(3, "john", "111111","ismael.sanchez.leon@gmail.com", 2, 2017);
		view = controller.removeInterestStudent(85, model, request, user);
		assertEquals("projectRemovedPage", view.getViewName());
	}


	@Test
	public void testGetListProjectInterested() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(STUDENT);
		when(session.getAttribute("userID")).thenReturn(5);
		view = controller.getListProjectInterested(model, request);
		assertEquals("interestProjectListPage", view.getViewName());
		
		//This test will show how a student with final project is loading a different view
		when(session.getAttribute("userID")).thenReturn(3);
		view = controller.getListProjectInterested(model, request);
		assertEquals("finalProjectPage", view.getViewName());
	}

	@Test
	public void testGetListOfProjectsLecturer() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(LECTURER);
		view = controller.getListOfProjectsLecturer(model, request);
		assertEquals("yourPersonalListPage", view.getViewName());
	}

	@Test
	public void testLecturerProjectListWithInterest() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(LECTURER);
		view = controller.lecturerProjectListWithInterest(model, request);
		assertEquals("yourPersonalListPageWithInterest", view.getViewName());
	}

	@Test
	public void testLecturerProjectListWithApprovedInterest() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(LECTURER);
		view = controller.lecturerProjectListWithApprovedInterest(model, request);
		assertEquals("projectlistapprovedinterest", view.getViewName());
	}

	@Test
	public void testGetListOfProjectsNoVisibleLecturer() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(LECTURER);
		view = controller.getListOfProjectsNoVisibleLecturer(model, request);
		assertEquals("notVisibleProjectPage", view.getViewName());
	}

	@Test
	public void testMakeAProjectVisible() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(LECTURER);
		when(session.getAttribute("userID")).thenReturn(6);
		view = controller.makeAProjectVisible(89, model, request);
		assertEquals("notVisibleProjectPage", view.getViewName());
	}

	@Test
	public void testApproveInterestPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(LECTURER);
		user = new User(2, "ismael", "123456","ismael.sanchez.leon@gmail.com", 2, 2017);
		view = controller.approveInterestPage(79, user, model, request);
		assertEquals("yourPersonalListPageWithInterest", view.getViewName());
	}

	@Test
	public void testAllStudentsPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.allStudentsPage(model, request);
		assertEquals("listtStudentsByYearPage", view.getViewName());
	}

	@Test
	public void testAllStudentsByYearPage() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.allStudentsByYearPage(model, request, 2017);
		assertEquals("studentListPage", view.getViewName());
		//This one will fails since I do not have students for year 2020
		view = controller.allStudentsByYearPage(model, request, 2020);
		assertEquals("errorPage", view.getViewName());
	}

	@Test
	public void testGetAllProjectStudentList() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.getAllProjectStudentList(5, model, request);
		assertEquals("allStudentProjectPage", view.getViewName());
	}

	@Test
	public void testPreviousYear() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.previousYear(model, request);
		assertEquals("yearListProjectPage", view.getViewName());
	}

	@Test
	public void testSeeProviousYearProject() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.seePreviousYearProject(model, request, 2015);
		assertEquals("previousyearprojectlist", view.getViewName());
		
		//This one will fail since I do not have projects for the year 2000
		view = controller.seePreviousYearProject(model, request, 2000);
		assertEquals("errorPage", view.getViewName());
	}

	@Test
	public void testSeeAllProjectsActualYear() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.seeAllProjectsActualYear(model, request);
		assertEquals("projectListByYearPage", view.getViewName());
	}

	@Test
	public void testSeeAllNextYearProjects() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.seeAllNextYearProjects(model, request);
		assertEquals("projectListByYearPage", view.getViewName());
	}

	@Test
	public void testUploadOneNewStudent() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.uploadOneNewStudent(model, request);
		assertEquals("addNewstudentsPage", view.getViewName());
	}

	@Test
	public void testSaveStudent() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		User student = new User(2, "newUser", "123456","ismael.sanchez.leon@gmail.com", 2, 2017);
		view = controller.saveStudent(model, request, student);
		assertEquals("listtStudentsByYearPage", view.getViewName());
	}

	@Test
	public void testUploadFileForNewStudents() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		view = controller.uploadFileForNewStudents(model, request);
		assertEquals("newstudentsuploadPage", view.getViewName());
	}

	@Test
	public void testAddNewStudentToDBFromCSV() throws IOException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		byte[] content = null;
		String fileName = null;
		MockMultipartFile mockMultipartFile = new MockMultipartFile(
			    "fileData",
			    fileName,
			    "text/plain",
			    content);
		view = controller.addNewStudentToDBFromCSV(mockMultipartFile, model, request);
		assertEquals("newstudentsuploadPage", view.getViewName());
	}

	@Test
	/**
	 * This test show that my get version of the post method related with user management were not working when
	 * someone that is not the coordinator tried to access them, Fixed
	 * I found that this method did not have the call to the method checkDBConnection(); this could lead
	 * to an access to the DB without having the DB connection running, fixed
	 * @throws SQLException
	 */
	public void testEditStudent() throws SQLException {
		view = controller.editStudent(5, model, request);
		assertEquals("editstudentPage", view.getViewName());
	}

	@Test
	public void testSaveEditStudent() throws SQLException {
		when(session.getAttribute("userType")).thenReturn(COORDINATOR);
		User student = new User(2, "newUser", "123456","meh@gmail.com", 2, 2017);
		view = controller.saveEditStudent(student, model, request);
		assertEquals("listtStudentsByYearPage", view.getViewName());
	}

	@Test
	public void testLogout() throws SQLException {
		when(session.getAttribute("userID")).thenReturn(3);	
		when(session.getAttribute("oldProjectNum")).thenReturn(3);	
		when(session.getAttribute("oldEventNum")).thenReturn(3);	
		view = controller.logout(model, request);
		assertNotNull(view);
	}
}