package com.workshop.ETrade.Tests.Acceptance.User.Admin;

import com.workshop.ETrade.Service.SystemService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RemoveMemberTest {
    SystemService systemService;
    String guestName;

    @Before
    public void setUp() throws Exception {

        systemService = new SystemService();
        guestName = systemService.enterSystem().getVal();
        systemService.signUp(guestName, "Andalus", "100","Andalus","Andalus");
        systemService.signUp(guestName, "Andalus1", "200","Andalus1","Andalus1");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void RemoveMemberSuccessTest() {
        Assert.assertTrue(systemService.login(guestName, "Andalus", "100").getVal());
        guestName = systemService.logOut("Andalus").getVal();
        systemService.login(guestName, "domain", "domain");
        systemService.removeMember("domain", "Andalus");
        guestName = systemService.logOut("domain").getVal();
        Assert.assertFalse((systemService.login(guestName, "Andalus", "100").getVal()));
    }

    @Test
    public void RemoveMemberFailTest() {
        Assert.assertTrue(systemService.login(guestName, "Andalus", "100").getVal());
        Assert.assertFalse(systemService.removeMember("domain", "Andalus").getVal());
        guestName = systemService.logOut("Andalus").getVal();
        Assert.assertTrue(systemService.login(guestName, "Andalus1", "200").getVal());
    }

    @Test
    public void RemoveManagerTest(){ //cant remove because manager
        systemService.login(guestName, "Andalus", "100");
        systemService.openStore("Andalus", "Mega", 123);
        guestName = systemService.logOut("Andalus").getVal();
        systemService.login(guestName, "domain", "domain");
        systemService.removeMember("domain", "Andalus");
        Assert.assertFalse(systemService.removeMember("domain", "Andalus").getVal());
    }
}
