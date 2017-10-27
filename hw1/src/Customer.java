/**
 * Created by huangtao on 2017/10/26.
 */
public class Customer {

    private int money;
    Seller seller;

    public Customer() {
        money = 20;
    }

    public Customer(int money) {
        this.money = money;
    }

    public void enterShop(Seller seller) {
        this.seller = seller;
    }

    public String buy(int a, int b, int c) {
        if (a < 0 || b < 0 || c < 0) {
            return "参数错误";
        }

        int[] prices = seller.queryPrice();
        int moneyNeed = a * prices[0] + b * prices[1] + c * prices[2];
        if (moneyNeed > money) {
            return "没钱";
        } else {
            String result = seller.sell(a, b, c);
            if (result.equals("购买成功")) {
                money -= moneyNeed;
            }
            return result;
        }
    }




    /**
     * test function
     */
    public static void main(String[] args){
        Customer customer = new Customer(100);
        Seller seller = new Seller();

        customer.enterShop(seller);
        String result = customer.buy(15,5,5);
        System.out.println(result);
    }

}
