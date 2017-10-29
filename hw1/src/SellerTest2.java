import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by huangtao on 2017/10/27.
 */
public class SellerTest2 {

    SellerMock seller;

    @Before
    public void setUp() throws Exception {
        seller = new SellerMock();
    }

    @Test
    public void sell1() throws Exception {
        assertEquals("购买成功", seller.sell(5,5,5));
    }

    @Test
    public void sell2() throws Exception {
        assertEquals("库存不足", seller.sell(30,5,5));
    }

    @Test
    public void sell3() throws Exception {
        assertEquals("库存不足", seller.sell(30,30,5));
    }

    @Test
    public void sell4() throws Exception {
        assertEquals("库存不足", seller.sell(5,5,30));
    }

    @Test
    public void sell5() throws Exception {
        assertEquals("参数错误", seller.sell(5,30,-1));
    }

    @Test
    public void sell6() throws Exception {
        assertEquals("参数错误", seller.sell(30,-1,30));
    }

    @Test
    public void sell7() throws Exception {
        assertEquals("参数错误", seller.sell(-1,5,-1));
    }

    @Test
    public void sell8() throws Exception {
        assertEquals("参数错误", seller.sell(-1,-1,5));
    }

    @Test
    public void sell9() throws Exception {
        assertEquals("参数错误", seller.sell(30,-1,-1));
    }

    @Test
    public void sell10() throws Exception {
        assertEquals("参数错误", seller.sell(-1,30,30));
    }

    @Test
    public void sell11() throws Exception {
        assertEquals("参数错误", seller.sell(5,-1,5));
    }

}