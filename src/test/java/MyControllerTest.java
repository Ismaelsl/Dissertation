import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userID")).thenReturn(3);	
		
		controller = new MyController();
		view = new ModelAndView();
		connect = new DBConnection();
		newConnection = connect.connect();
		sqlController = new SQLController();
		
		user = new User(3, "student", "student","asd@asd.com", 2, 2017);
		userFail = new User(3, "student", "wrongpassword","asd@asd.com", 2, 2017);
		
		list1 = new ArrayList<CheckList>();
		list1.add(new CheckList(1, "22/2/12", "test1", "4x5", "Test", "15:00", "16:00"));
		list1.add(new CheckList(2, "22/2/12", "test2", "4x5", "Test", "15:00", "16:00"));
		list1.add(new CheckList(3, "22/2/12", "test3", "4x5", "Test", "15:00", "16:00"));
		list1.add(new CheckList(4, "22/2/12", "test4", "4x5", "Test", "15:00", "16:00"));
		
		list2 = new ArrayList<CheckList>();
		list2.add(new CheckList(1, "22/2/12", "test1", "4x5", "Test", "15:00", "16:00"));
		list2.add(new CheckList(2, "22/2/12", "test2", "4x5", "Test", "15:00", "16:00"));
		list2.add(new CheckList(7, "22/2/12", "test7", "4x5", "Test", "15:00", "16:00"));
		list2.add(new CheckList(8, "22/2/12", "test8", "4x5", "Test", "15:00", "16:00"));
		
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
	public void testRemoveprojectPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveEditProject() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearch() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchStudent() {
		fail("Not yet implemented");
	}


	@Test
	public void testNewchecklistPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveChecklist() {
		fail("Not yet implemented");
	}


	@Test
	public void testChecklistListPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testBubblesrt() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditChecklistPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveChecklistPage() {
		fail("Not yet implemented");
	}


	@Test
	public void testMakeVisibleChecklistPage() {
		fail("Not yet implemented");
	}


	@Test
	public void testSaveEditChecklist() {
		fail("Not yet implemented");
	}


	@Test
	public void testRegisterInterestPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testRegisterinterest() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeVisibleInterestPage() {
		fail("Not yet implemented");
	}


	@Test
	public void testRemoveInterest() {
		fail("Not yet implemented");
	}


	@Test
	public void testRemoveInterestFinal() {
		fail("Not yet implemented");
	}


	@Test
	public void testRemoveInterestStudent() {
		fail("Not yet implemented");
	}


	@Test
	public void testGetListProjectInterested() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetListOfProjectsLecturer() {
		fail("Not yet implemented");
	}

	@Test
	public void testLecturerProjectListWithInterest() {
		fail("Not yet implemented");
	}

	@Test
	public void testLecturerProjectListWithApprovedInterest() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetListOfProjectsNoVisibleLecturer() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeAProjectVisible() {
		fail("Not yet implemented");
	}


	@Test
	public void testApproveInterestPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testAllStudentsPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testAllStudentsByYearPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllProjectStudentList() {
		fail("Not yet implemented");
	}

	@Test
	public void testPreviousYear() {
		fail("Not yet implemented");
	}

	@Test
	public void testSeeProviousYearProject() {
		fail("Not yet implemented");
	}

	@Test
	public void testSeeAllProjectsActualYear() {
		fail("Not yet implemented");
	}

	@Test
	public void testSeeAllNextYearProjects() {
		fail("Not yet implemented");
	}

	@Test
	public void testUploadOneNewStudent() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveStudent() {
		fail("Not yet implemented");
	}

	@Test
	public void testUploadFileForNewStudents() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewStudentToDBFromCSV() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditStudent() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveEditStudent() {
		fail("Not yet implemented");
	}

	@Test
	public void testLogout() {
		fail("Not yet implemented");
	}
}