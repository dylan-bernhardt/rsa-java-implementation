public class Rsa {
    private BigNumber n, r, rp;
    int[] e, d;

    public Rsa(BigNumber n, int[] e, int[] d, BigNumber r, BigNumber rp) {
        this.n = n;
        this.e = e;
        this.d = d;
        this.r = r;
        this.rp = rp;
    }

    public BigNumber encrypt(BigNumber nb, boolean toMM) {
        BigNumber c = nb.squareAndMultiply(e, n, r, rp);
        if (toMM) {
            return c;
        }
        return c.multiplyMM(new BigNumber(new int[] { 1 }), n, r, rp);
    }

    public BigNumber decrypt(BigNumber nb, boolean toMM) {
        BigNumber m = nb.squareAndMultiply(d, n, r, rp);
        if (toMM) {
            return m;
        }
        return m.multiplyMM(new BigNumber(new int[] { 1 }), n, r, rp);
    }
}
