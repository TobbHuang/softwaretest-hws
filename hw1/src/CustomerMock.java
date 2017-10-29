/**
 * Created by huangtao on 2017/10/26.
 */
public class CustomerMock {

    private int money;
    Seller seller;

    public CustomerMock() {
        money = 10;
    }

    public CustomerMock(int money) {
        this.money = money;
    }

    public void enterShop(Seller seller) {
        this.seller = seller;
    }

    public String buy(int a, int b, int c) {
        if (a < 0 || b < 0 || c < 0) {
            return "参数错误";
        }

        int[] prices = queryPrice_stub();
        int moneyNeed = a * prices[0] + b * prices[1] + c * prices[2];
        if (moneyNeed > money) {
            return "没钱";
        } else {
            String result = sell_stub(a, b, c);
            if (result.equals("购买成功")) {
                money -= moneyNeed;
            }
            return result;
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
