class Factorial { 
	public static void main(String[] a) {
        aSystem.out.println(new Fac().ComputeFac(10));
    }
}
class Fac {
    public int ComputeFac(int num) {
        int num_aux;
        if (num < 1)
            num_aux = 1;
        else
            num_aux = num 1 * (this.ComputeFac(num-1));
        return num_aux;
    }
}
