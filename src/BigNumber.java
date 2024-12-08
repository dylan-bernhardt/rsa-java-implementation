public class BigNumber {
    final int SIZE = 64;
    final int FIRST_BIT_VALUE = 1073741824; // 2 puissance 30
    int[] numbers = new int[SIZE];

    public BigNumber() {
        for (int i = 0; i < SIZE; ++i) {
            this.numbers[i] = 0;
        }
    }

    public BigNumber(int[] number) throws Exception {
        if (number.length > SIZE) {
            throw new Exception("Number too big");
        } else {
            for (int i = 0; i < number.length; ++i) {
                this.numbers[i] = number[i];
            }
        }
    }

    public static int getBit(int number, int pos) {
        return (number >> pos) & 1;
    }

    public static int getFirstDigit(int number) {
        int result = number;
        for (int i = 0; i < 9; ++i) {
            result /= 10;
        }
        return result;
    }

    public void display() {
        System.out.print("\n==============\n");
        for (int i = numbers.length - 1; i >= 0; --i) {
            System.out.print(numbers[i] + "|");
        }
        System.out.print("\n==============\n");
    }

    public BigNumber add(BigNumber numberToAdd) {
        BigNumber result = new BigNumber();

        int carry = 0;
        for (int i = 0; i < SIZE; ++i) {
            int firstOp = this.getBlock(i);
            int secondOp = numberToAdd.getBlock(i);

            if (getBit(firstOp, 30) + getBit(secondOp, 30) == 2) {
                firstOp -= FIRST_BIT_VALUE;
                secondOp -= FIRST_BIT_VALUE;
                result.setBlock(i, firstOp + secondOp + carry);
                carry = 1;
            } else if (getBit(firstOp, 30) + getBit(secondOp, 30) == 0) {
                result.setBlock(i, firstOp + secondOp + carry);
                carry = 0;
            } else if (getBit(firstOp, 30) + getBit(secondOp, 30) == 1) {
            }
        }
        return result;
    }

    public int getBlock(int index) {
        return this.numbers[index];
    }

    public void setBlock(int index, int value) {
        this.numbers[index] = value;
    }

}
