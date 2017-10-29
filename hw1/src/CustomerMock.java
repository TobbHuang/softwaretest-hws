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

        int[] prices = seller.queryPrice_stub();
        int moneyNeed = a * prices[0] + b * prices[1] + c * prices[2];
        if (moneyNeed > money) {
            return "没钱";
        } else {
            String result = seller.sell_stub(a, b, c);
            if (result.equals("购买成功")) {
                money -= moneyNeed;
            }
            return result;
        }
    }

}
