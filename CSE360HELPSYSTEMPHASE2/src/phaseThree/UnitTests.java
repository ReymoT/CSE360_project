package phaseThree;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class UnitTests {

	DatabaseHelper db;
	String group1 = "Group 1";
	String role = "Student";
	
	@Test
	public void SN1() {
		try {
			db = new DatabaseHelper();
			db.connectToDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			assertEquals(true, db.login("a", "a", "Admin", "Group 1"));
			assertEquals(false, db.login("a", "b", "Admin", "Group 3"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void SN2() {
		try {
			db = new DatabaseHelper();
			db.connectToDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			db.register("testUser5", "testUser", "Student", "Group 1", "v, s");
			assertEquals(true, db.login("testUser5", "testUser", "Student", "Group 1"));
			assertEquals(false, db.login("testUser", "testtttt", "Student", "Group 1"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void SN3() {
		try {
			db = new DatabaseHelper();
			db.connectToDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			assertEquals("a, v, s, e, d, b, r, h", db.getUserRights("a"));
			assertEquals("", db.getUserRights("l"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void SN4() {
		try {
			db = new DatabaseHelper();
			db.connectToDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertEquals(true, db.doesUserExist("a"));
		assertEquals(false, db.doesUserExist("ssx"));
	}
	
	@Test
	public void SN5() {
		try {
			db = new DatabaseHelper();
			db.connectToDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			assertEquals(true, db.doesGroupHaveInstructors("Group 1"));
			assertEquals(false, db.doesGroupHaveInstructors("Group 3"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void SN6() {
		try {
			db = new DatabaseHelper();
			db.connectToDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			assertEquals(true, db.hasRight("a", "e"));
			assertEquals(false, db.hasRight("testUser3", "h"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
