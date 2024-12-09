public class BigNumber {
    int[] numbers;

    public BigNumber(int size) {
        this.numbers = new int[size];
        for (int i = 0; i < size; ++i) {
            this.numbers[i] = 0;
        }
    }

    public BigNumber(int[] number) {
        this.numbers = new int[number.length];
        for (int i = 0; i < number.length; ++i) {
            this.numbers[i] = number[i];
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
        System.out.print("\n==============\n [");
        for (int i = this.getSize() - 1; i >= 0; --i) {
            System.out.print(numbers[i]);
            if (i != 0) {
                System.out.print(", ");
            }
        }
        System.out.print("] \n==============\n");
    }

    public BigNumber add(BigNumber numberToAdd) {
        int sizeMin;
        int sizeMax;
        if (this.getSize() > numberToAdd.getSize()) {
            sizeMin = numberToAdd.getSize();
            sizeMax = this.getSize();
        } else {
            sizeMin = this.getSize();
            sizeMax = numberToAdd.getSize();
        }

        BigNumber result = new BigNumber(sizeMax + 1);
        int carry = 0;
        for (int i = 0; i < sizeMin; ++i) {
            int firstOp = this.getBlock(i);
            int secondOp = numberToAdd.getBlock(i);
            if (firstOp + secondOp + carry < 0) {
                result.setBlock(i, (firstOp + secondOp + carry) & 0x7FFFFFFF);
                carry = 1;
            } else {
                result.setBlock(i, firstOp + secondOp + carry);
                carry = 0;
            }
        }

        for (int i = sizeMin; i < sizeMax; ++i) {
            if (i < this.getSize()) {
                result.setBlock(i, this.getBlock(i));
            } else if (i < numberToAdd.getSize()) {
                result.setBlock(i, numberToAdd.getBlock(i));
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

    public int getSize() {
        return this.numbers.length;
    }

}
