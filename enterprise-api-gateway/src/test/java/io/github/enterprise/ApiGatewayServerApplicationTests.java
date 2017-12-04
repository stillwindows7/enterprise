package io.github.enterprise;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ApiGatewayServerApplicationTests extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public ApiGatewayServerApplicationTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ApiGatewayServerApplicationTests.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}
}
