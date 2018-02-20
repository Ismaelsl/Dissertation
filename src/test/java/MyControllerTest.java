import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dissertationWeb.classes.DBConnection;
import org.dissertationWeb.config.ApplicationContextConfig;
import org.dissertationWeb.config.SpringWebAppInitializer;
import org.dissertationWeb.config.WebMvcConfig;
import org.dissertationWeb.controller.SQLController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ ApplicationContextConfig.class, 
		SpringWebAppInitializer.class, WebMvcConfig.class })
public class MyControllerTest {

	private Connection newConnection;

	private SQLController sqlController;
	 
	private MockMvc mockMvc; 
 
	@Before
	public void setUp(HttpServletRequest request) throws Exception {
		/* mockMvc = MockMvcBuilders
				             .annotationConfigSetup(ApplicationContextConfig.class, 
				            			SpringWebAppInitializer.class, WebMvcConfig.class)
				             .build();*/

		DBConnection connect = new DBConnection();
		newConnection = connect.connect();
		sqlController = new SQLController();//object to control all the SQL activities
		HttpSession session = request.getSession() ;
		int numberOfProject = 5;
		int numberOfEvents = 7;
		//I could have a class that keep the value of the count, old and new and save the object of that class in the session
		//But I think that this call are not that complex to the DB and will not take too many resources
		int oldNumberOfProject = 5;
		int oldNumberOfEvent = 7;
		//System.out.println("number of projects are " + numberOfProject + " number of events are " + numberOfEvents);
		//System.out.println("Old number of projects are " + oldNumberOfProject + " Old number of events are " + oldNumberOfEvent);
		session.setAttribute("userID", 5);
		session.setAttribute("userType", 3);
		session.setAttribute("yearUser", "2017");
		session.setAttribute("projectNum", numberOfProject);
		session.setAttribute("eventNum", numberOfEvents);
		session.setAttribute("oldProjectNum", oldNumberOfProject);
		session.setAttribute("oldEventNum", oldNumberOfEvent);
		session.setMaxInactiveInterval(600);//right now is 600 seconds since I need to do some testing and need the session to last longer
	}

	@After
	public void tearDown() throws Exception {
		newConnection = null;
		sqlController = null;
	}

	@Test
	public void testCheckDBConnection() throws SQLException {
		assertTrue("Test DB succeess", newConnection.isClosed());
	}

	@Test
	public void testRemoveDuplicateFromEventList() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveDuplicateFromProjectList() {
		fail("Not yet implemented");
	}

	@Test
	public void testKeepConnection() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckSchedule() {
		fail("Not yet implemented");
	}

	@Test
	public void testHomePage() {
		fail("Not yet implemented");
	}

	@Test
	public void testLogin() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckLogin() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckLoginGET() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateSession() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSession() {
		fail("Not yet implemented");
	}

	@Test
	public void testContactusPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testProjectListPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testProjectListToApprovePage() {
		fail("Not yet implemented");
	}

	@Test
	public void testApproveprojectPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testApproveProjectGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewprojectPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewprojectNextYearPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewprojectProposalPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveProposal() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveProposalGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditprojectPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveprojectPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveEditProject() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveEditGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearch() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchStudent() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchStudentGet() {
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
	public void testSaveChecklistGet() {
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
	public void testEditChecklistGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveChecklistPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveChecklistGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeVisibleChecklistPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeVisibleChecklistGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveEditChecklist() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveEditChecklistGet() {
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
	public void testMakeInterestVisibleGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveInterest() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveInterestGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveInterestFinal() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveInterestFinalGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveInterestStudent() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveInterestStudentGet() {
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
	public void testGetListOfProjectsNoVisibleLecturer() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeAProjectVisible() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeItVisibleGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testApproveInterestPage() {
		fail("Not yet implemented");
	}

	@Test
	public void testApproveInterestGet() {
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
	public void testGetStudentProjectsGet() {
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
	public void testSeeProjectsByYearGet() {
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
	public void testUploadFileForNewStudents() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewStudentToDBFromCSV() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewStudentToDBFromCSVGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditStudent() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditStudentGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveEditStudent() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveEditStudentGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testLogout() {
		fail("Not yet implemented");
	}

}
