package com.loel;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages({"com.loel.serviceTests", "com.loel.controllerTests"})
public class PpmToolApplicationTests {

}