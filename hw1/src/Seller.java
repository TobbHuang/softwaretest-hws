/**
 * Created by huangtao on 2017/10/26.
 */
public class Seller {

    private int[] prices;
    private int[] resp;

    public Seller() {
        prices = new int[3];
        prices[0] = 1;
        prices[1] = 2;
        prices[2] = 3;

        resp = new int[3];
        resp[0] = 10;
        resp[1] = 15;
        resp[2] = 20;
    }

    public int[] queryPrice() {
        return prices;
    }

    private int[] queryResp() {
        return resp;
    }

    private void setResp(int[] curResp) {
        resp = curResp;
    }

    public String sell(int a, int b, int c) {
        if (a < 0 || b < 0 || c < 0) {
            return "参数错误";
        }

        int[] curResp = queryResp();
        if (curResp[0] >= a && curResp[1] >= b && curResp[2] >= c) {
            curResp[0] -= a;
            curResp[1] -= b;
            curResp[2] -= c;
            setResp(curResp);
            return "购买成功";
        } else {
            return "库存不足";
        }
    }


    /**
     * seller.queryPrice 桩函数
     * @return
     */
    public int[] queryPrice_stub() {
        int[] prices = {1, 2, 3};
        return prices;
    }

    /**
     * seller.sell 桩函数
     * @return
     */
    public String sell_stub(int a, int b, int c) {
        return "购买成功";
    }
}
