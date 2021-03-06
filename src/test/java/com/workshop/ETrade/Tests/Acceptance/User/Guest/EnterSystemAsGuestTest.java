package com.workshop.ETrade.Tests.Acceptance.User.Guest;

import com.workshop.ETrade.Service.SystemService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EnterSystemAsGuestTest {
    private SystemService systemService;

    @Before
    public void setUp() throws Exception {
        systemService = new SystemService();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void EnterSystemAsGuestTest(){
        String name = systemService.enterSystem().getVal();
        Assert.assertTrue(name.contains("guest"));
    }
}
