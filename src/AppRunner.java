import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;

    private final CashAcceptor cashAcceptor;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(0);
        cashAcceptor = new CashAcceptor(0);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);

        print("\nБаланс: " + coinAcceptor.getAmount() + "\n");

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            coinAcceptor.setAmount(coinAcceptor.getAmount() + 10);
            print("Вы пополнили баланс на 10");
            return;
        }
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            } else {
                print("Недопустимая буква. Попрбуйте еще раз.");
                chooseAction(products);
            }
        }


    }

    private void paymentSelection() {
        System.out.println("\na - Монеты");
        System.out.println("b - Наличные");
        System.out.println("Выберите способ пополнения: ");

        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            refillBalanceWithCoins();
        } else if ("b".equalsIgnoreCase(action)) {

        } else {
            print("Недопустимая буква. Попрбуйте еще раз.");
            paymentSelection();
        }
    }

    private void refillBalanceWithCash() {
        print("1 - 20" +
                "\n2 - 50" +
                "\n3 - 100" +
                "\n4 - 200");

        print("Выберите сумму пополнения: ");
        String action = fromConsole().substring(0, 1);
        if ("1".equalsIgnoreCase(action)) {
            cashAcceptor.setAmount(cashAcceptor.getAmount() + 20);
        } else if ("2".equalsIgnoreCase(action)) {
            cashAcceptor.setAmount(cashAcceptor.getAmount() + 50);
        } else if ("3".equalsIgnoreCase(action)) {
            cashAcceptor.setAmount(cashAcceptor.getAmount() + 100);
        } else if ("4".equalsIgnoreCase(action)) {
            cashAcceptor.setAmount(cashAcceptor.getAmount() + 200);
        } else {
            print("Недопустимая цифра. Попрбуйте еще раз.");
            refillBalanceWithCash();
        }
    }

    private void refillBalanceWithCoins() {
        coinAcceptor.setAmount(coinAcceptor.getAmount() + 10);
        print("Вы пополнили баланс на 10");
    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
