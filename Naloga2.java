import java.io.*;
import java.util.*;

public class Naloga2 {
    public static void main(String[] args) throws Exception {
        //long startTime = System.nanoTime();
        File vhod = new File (args[0]);
        Struktura s = new Struktura();
            Scanner sc = new Scanner(vhod);

            int steviloUkazov = sc.nextInt();
            sc.nextLine();

            for (int i = 0; i < steviloUkazov; i++) {
                String vrstica = sc.nextLine();
                char ukaz = vrstica.charAt(0);
                int velikost, id , korakov;

                switch(ukaz) {
                    case 'i':
                    velikost = prviParameter(vrstica);
                    s.init(velikost);
                        break;
                    case 'a':
                        velikost = prviParameter(vrstica);
                        id = drugiParameter(vrstica);
                        s.alloc(velikost, id);
                        break;
                    case 'f':
                        id = prviParameter(vrstica);
                        s.free(id);
                        break;
                    case 'd':
                        korakov = prviParameter(vrstica);
                        s.defrag(korakov);
                        break;
                }

            }
        s.koncniIzpis(args[1]);
        /*long endTime = System.nanoTime();
        double sekunde = (double)(endTime - startTime) / 1000000000.0;
        System.out.println(sekunde);*/
    }

    private static int prviParameter (String x) {
        String [] tabela = x.split(",");
        return Integer.parseInt(tabela[1]);
    }

    private static int drugiParameter (String x) {
        String [] tabela = x.split(",");
        return Integer.parseInt(tabela[2]);
    }
}

class Struktura {
    public int tabela[];

    public void init (int size) {
        tabela = new int [size];
    }

    public boolean alloc(int size, int id){
        int prostor = 0, ind = 0;
        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] == 0 && prostor == 0) {
                ind = i; prostor = 1;
            } else if (tabela[i] == 0) {
                prostor++;
            } else if (tabela[i] == id) {
                return false;
            } else if (tabela[i] != 0 && prostor < size) {
                prostor = 0;
            }
        }
        if (prostor < size)
            return false;
        for (int i = ind; i < ind+size; i++)
            tabela[i] = id;
        return true;
    }

    public int free(int id) {
        boolean smoDosegli = false;
        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] == id)
                smoDosegli = true;
            if(tabela[i] != id && smoDosegli)
                return i+1;
            if(tabela[i] == id)
                tabela[i] = 0;
        }
        return 0;
    }

    public void defrag (int korakov) {
        int i = 0;
        while (i < tabela.length)   {
            if (tabela[i] != 0) {
                i++;
                continue;
            }
            else {
                int j = i;
                for (; j < tabela.length-1; j++)
                    if (tabela[j] != 0)
                        break;
                int id = tabela[j];

                for (; j < tabela.length; j++) {
                    if (tabela[j] != id)
                        break;
                    else{
                        tabela[i] = tabela[j];
                        tabela[j] = 0;
                        i++;
                    }
                }

                korakov --;
                if (korakov == 0)
                    return;
            }
        }
    }

    public void koncniIzpis (String args) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(args));

        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] == 0)
                continue;
            int iKonec = stevec(i, tabela[i]);

            pw.println(tabela[i] + "," + i + "," + iKonec);
            i = iKonec;
        }
        pw.close();
    }
    
    private int stevec (int i, int id) {
        while (tabela[i] == id) {
            i++;
            if (i >= tabela.length)
                return i-1;
        }
        return i-1;
    }
}