import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by huangtao on 2017/10/26.
 */
public class CustomerTest {

     CustomerMock customer;

    @Before
    public void setUp() throws Exception {
        System.out.println("setUp");
        customer = new CustomerMock();
        customer.enterShop(new Seller());
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("tearDown");
    }

    @Test
    public void buy() throws Exception {
        assertEquals("购买成功", customer.buy(1,1,1));
    }
}