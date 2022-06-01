package com.workshop.ETrade.Tests.Acceptance.User.Member;

import com.workshop.ETrade.Service.SystemService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppointNewOwnerTest {
    @Autowired
    private SystemService systemService;

    @Before
    public void setUp() throws Exception {
        //systemService = new SystemService();
        String guestName = systemService.enterSystem().getVal();
        systemService.signUp(guestName, "Mira", "200","Mira","Mira");
        systemService.signUp(guestName, "Andalus", "100","Andalus","Andalus");
        systemService.signUp(guestName, "Andalus2", "102","Andalus2","Andalus2");
        systemService.login(guestName, "Andalus", "100");
        systemService.openStore("Andalus", "Mega", 123);
        //systemService.appointStoreManager("Andalus", "Mega", "Mira");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void AppointNewOwnerSuccessTest(){
        Assert.assertTrue(systemService.appointStoreOwner("Andalus", "Mega", "Mira").isSuccess());
        String guestName = systemService.logOut("Andalus").getVal();
        systemService.login(guestName, "Mira", "200");
        Assert.assertTrue(systemService.appointStoreOwner("Mira", "Mega", "Andalus2").isSuccess());
        systemService.login(guestName, "Andalus", "100");
        systemService.removeStoreOwner("Andalus","Mega","Mira");

    }

    @Test
    public void AppointNewOwnerFailTest() {
        Assert.assertFalse(systemService.appointStoreOwner("Andalus", "Mega", "Itay").isSuccess());
    }

    @Test
    public void AppointNewOwnerRemoveAndAppintAsManager() {
        Assert.assertTrue(systemService.appointStoreOwner("Andalus", "Mega", "Mira").isSuccess());
        Assert.assertTrue(systemService.removeStoreOwner("Andalus", "Mega", "Mira").isSuccess());
        Assert.assertTrue(systemService.appointStoreManager("Andalus", "Mega", "Mira").isSuccess());

    }

}
