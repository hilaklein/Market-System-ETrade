package com.workshop.ETrade.Tests.Acceptance.User.Guest;

import com.sun.jdi.request.StepRequest;
import com.workshop.ETrade.Service.SystemService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SearchProductTest {

    private SystemService systemService;

    @Before
    public void setUp() throws Exception {
        systemService = new SystemService();
        String guestName = systemService.enterSystem().getVal();
        systemService.signUp(guestName, "Andalus", "100","Andalus","Andalus");
        systemService.login(guestName, "Andalus", "100");
        systemService.openStore("Andalus", "Mega", 123);
        systemService.addProductToStore("Andalus", "Mega",
                "Bamba", 100, 5,"snacks");
        systemService.addKeyword("Andalus", "Bamba", "Mega", "osem");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void searchProdByCategorySuccessTest(){
        List<String> a = systemService.searchByCategory("Andalus", "snacks").getVal();
        Assert.assertTrue(systemService.searchByCategory("Andalus", "snacks").getVal().contains("Bamba"));
    }

    @Test
    public void searchProdByCategoryFailTest(){
        Assert.assertFalse(systemService.searchByCategory("Andalus", "drinks").getVal().contains("Bamba"));
    }

    @Test
    public void searchProdByKeyWordFailTest(){
        Assert.assertFalse(systemService.searchByKeyword("Andalus", "Ban").getVal().contains("Bamba"));
    }
    @Test
    public void searchProdByNameSuccessTest(){
        Assert.assertTrue(systemService.searchByName("Andalus", "Bamba").getVal().contains("Bamba"));
    }

    @Test
    public void searchProdByNameFailTest(){
        Assert.assertFalse(systemService.searchByName("Andalus", "Bisly").getVal().contains("Bamba"));
    }

}
