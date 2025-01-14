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

    public int getBlock(int index) {
        return this.numbers[index];
    }

    public void setBlock(int index, int value) {
        this.numbers[index] = value;
    }

    public int getSize() {
        return this.numbers.length;
    }

    public void pop() {
        int[] newNumbers = new int[this.getSize() - 1];
        for (int i = 0; i < this.getSize() - 1; ++i) {
            newNumbers[i] = this.numbers[i];
        }
        this.numbers = newNumbers;
    }

    public void display() {
        System.out.print("\n==============\n[");
        for (int i = this.getSize() - 1; i >= 0; --i) {
            System.out.print(numbers[i]);
            if (i != 0) {
                System.out.print(", ");
            }
        }
        System.out.print("] \n==============\n");
    }

    public BigNumber add(BigNumber numberToAdd) {
        int sizeMax;
        if (this.getSize() > numberToAdd.getSize()) {
            sizeMax = this.getSize();
        } else {
            sizeMax = numberToAdd.getSize();
        }

        BigNumber result = new BigNumber(sizeMax + 1);
        int carry = 0;
        for (int i = 0; i < sizeMax + 1; ++i) {
            int firstOp = 0;
            int secondOp = 0;
            if (i < this.getSize()) {
                firstOp = this.getBlock(i);
            }
            if (i < numberToAdd.getSize()) {
                secondOp = numberToAdd.getBlock(i);
            }
            if (firstOp + secondOp + carry < 0) {
                result.setBlock(i, (firstOp + secondOp + carry) & 0x7FFFFFFF);
                carry = 1;
            } else {
                result.setBlock(i, firstOp + secondOp + carry);
                carry = 0;
            }
        }
        if (result.getBlock(result.getSize() - 1) == 0) {
            result.pop();
        }
        return result;
    }

    public BigNumber multiply(BigNumber nb) {
        var result = new BigNumber(this.getSize() + nb.getSize());
        for (int i = 0; i < this.getSize(); ++i) {
            for (int j = 0; j < nb.getSize(); ++j) {
                int[] tab = new int[this.getSize() + nb.getSize()];
                long multResult = (long) this.getBlock(i) * (long) nb.getBlock(j);
                int lsbs = (int) (multResult & 0x7FFFFFFF);
                int msbs = (int) ((multResult >> 31) & 0x7FFFFFFF);
                tab[i + j] = lsbs;
                tab[i + j + 1] = msbs;
                var productBigNumber = new BigNumber(tab);
                result = result.add(productBigNumber);
            }
        }
        return result;
    }

    public boolean isLessThan(BigNumber nb) {
        if (this.getSize() > nb.getSize()) {
            return false;
        } else if (this.getSize() < nb.getSize()) {
            return true;
        } else {
            for (int i = nb.getSize() - 1; i >= 0; --i) {
                if (this.getBlock(i) < nb.getBlock(i)) {
                    return true;
                } else if (this.getBlock(i) > nb.getBlock(i)) {
                    return false;
                }
            }
        }
        return false;
    }

    public BigNumber substract(BigNumber nb) {
        if (this.isLessThan(nb)) {
            return new BigNumber(0);
        }

        int sizeMax;
        if (this.getSize() > nb.getSize()) {
            sizeMax = this.getSize();
        } else {
            sizeMax = nb.getSize();
        }

        BigNumber result = new BigNumber(sizeMax);
        int carry = 0;
        for (int i = 0; i < sizeMax; ++i) {
            int firstOp = 0;
            int secondOp = 0;
            if (i < this.getSize()) {
                firstOp = this.getBlock(i);
            }
            if (i < nb.getSize()) {
                secondOp = nb.getBlock(i);
            }
            if (firstOp - secondOp - carry < 0) {
                result.setBlock(i, (firstOp - secondOp - carry) & 0x7FFFFFFF);
                carry = 1;
            } else {
                result.setBlock(i, firstOp - secondOp - carry);
                carry = 0;
            }
        }
        return result;
    }

    public BigNumber addModular(BigNumber nbToAdd, BigNumber mod) {
        var sum = this.add(nbToAdd);
        if (sum.isLessThan(mod)) {
            return sum;
        }
        return sum.substract(mod);
    }

    public BigNumber multiplyMM(BigNumber nbToAdd, BigNumber mod, BigNumber r, BigNumber rp, int blocksAtRight) {
        System.err.println("block : " + blocksAtRight);
        BigNumber s = this.multiply(nbToAdd);
        BigNumber t = s.multiply(rp);
        for (int i = blocksAtRight; i < t.getSize(); ++i) {
            t.setBlock(i, 0);
        }
        BigNumber m = t.multiply(mod).add(s);

        BigNumber u = new BigNumber(m.getSize() - blocksAtRight);
        for (int i = blocksAtRight; i < t.getSize(); ++i) {
            u.setBlock(i - blocksAtRight, m.getBlock(i));
        }
        return u;
    }
}
