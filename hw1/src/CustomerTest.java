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
        customer = new CustomerMock();
        customer.enterShop(new Seller());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void buy1() throws Exception {
        assertEquals("购买成功", customer.buy(1,1,1));
    }

    @Test
    public void buy2() throws Exception {
        assertEquals("没钱", customer.buy(2,2,2));
    }

    @Test
    public void buy3() throws Exception {
        assertEquals("参数错误", customer.buy(-1,1,1));
    }

    @Test
    public void buy4() throws Exception {
        assertEquals("参数错误", customer.buy(1,-1,1));
    }

    @Test
    public void buy5() throws Exception {
        assertEquals("参数错误", customer.buy(1,1,-1));
    }

    @Test
    public void buy6() throws Exception {
        assertEquals("参数错误", customer.buy(-1,-1,1));
    }

    @Test
    public void buy7() throws Exception {
        assertEquals("参数错误", customer.buy(-1,1,-1));
    }

    @Test
    public void buy8() throws Exception {
        assertEquals("参数错误", customer.buy(1,-1,-1));
    }

    @Test
    public void buy9() throws Exception {
        assertEquals("参数错误", customer.buy(-1,-1,-1));
    }
}