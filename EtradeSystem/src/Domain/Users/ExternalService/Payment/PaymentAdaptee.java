package Domain.Users.ExternalService.Payment;


import java.time.LocalTime;

public class PaymentAdaptee {
    public boolean payment(int cardNum, LocalTime expDate, int cvv, double price,int cardTo){
        return false;
    }
    public boolean canPay(int cardFrom, LocalTime expDate, int cvv,double price){
        return false;
    }
    public int getBalance(int card, LocalTime exp, int cvv) {
        return 0;
    }
}
