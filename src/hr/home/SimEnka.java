package hr.home;


import java.util.*;

public class SimEnka{
    private static List<String> ulazniNizovi;
    private static Set<String> stanja;
    private static Set<String> abeceda;
    private static Set<String> prihvatljivaStanja;
    // String 1 je stanje u kojem se nalazimo a druga mapa je znak koji je dosao i stanje u koje prelazimo
    private static Map<String, Map<String, List<String>>> prijelazi;


    public static void main(String[] args) {
        Scanner sc = new Scanner (System.in);

        //Ucitavanje skupa ulaznih nizova
        String[] nizovi = sc.nextLine().split("\\|");
        ulazniNizovi = new ArrayList<>(Arrays.asList(nizovi));

        //Ucitavanje skupa svih mogucih stanja
        String[] stanjaArray = sc.nextLine().split(",");
        stanja = new HashSet<>(Arrays.asList(stanjaArray));

        //ucitavanje abecede za prijelaze iz stanja
        String[] abecedaArray = sc.nextLine().split(",");
        abeceda = new HashSet<>(Arrays.asList(abecedaArray));

        //ucitavanje prihvatlijivih stanja
        String[] prihvatlijvaStanjaArray = sc.nextLine().split(",");
        prihvatljivaStanja = new HashSet<>(Arrays.asList(prihvatlijvaStanjaArray));

        //ucitavanje pocetnog stanja
        String pocetnoStanje =  sc.nextLine();
        //incijalizaicja mape prijelazi
        prijelazi = new TreeMap<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            int zarez = line.indexOf(",");
            int strelica = line.indexOf("->");

            String stanjeTrenutno = line.substring(0, zarez);
            String znak = line.substring(zarez + 1, strelica);
            String[] stanjeNakon = line.substring(strelica + 2).split(",");


            Map<String, List<String>> tempMapa = new TreeMap<>();
            List<String> list;

            if (prijelazi.containsKey(stanjeTrenutno)) {
               tempMapa = prijelazi.get(stanjeTrenutno);
            }

            if (tempMapa.get(znak) == null) {
                list = new LinkedList<>();
            }

            else {
                list = tempMapa.get(znak);
            }

            for (int i = 0; i < stanjeNakon.length; i++) {
                list.add(stanjeNakon[i]);
            }
            tempMapa.put(znak, list);
            prijelazi.put(stanjeTrenutno, tempMapa);
        }



        List<String> izlazniNizovi = new LinkedList<>();
        Set<String> tempStanja = new TreeSet<>();
        Set<String> staraStanja = new TreeSet<>();
        //Obradjivanje skupa ulaznih nizova

        for (String s : ulazniNizovi) {
            StringBuilder sb = new StringBuilder(pocetnoStanje + "|");
            tempStanja.clear();
            staraStanja.clear();
            staraStanja.add(pocetnoStanje);

            //znakovi je ulazni niz npr znakovi[0] = a,  znakovi[1] = pnp,   znakovi [2] = a
            String[] znakovi = s.split(",");


            //staraStanja su stanja iz proslog koraka


            for (int i = 0; i < znakovi.length; i++) {
                //ova mapa je mapa iz prijelaza npr za neko stanje PRIMJER za stanje1  (a, Lista(stanje2, stanje3))
                Map<String, List<String>> map;
                for (String stanja : staraStanja) {
                    if (prijelazi.containsKey(stanja)) {
                        map = prijelazi.get(stanja);
                        if (map.containsKey(znakovi[i])) {
                            List<String> list = map.get(znakovi[i]);
                            for (String novaStanja : list) {
                               tempStanja.add(novaStanja);
                               if (prijelazi.containsKey(novaStanja) && prijelazi.get(novaStanja).containsKey("$")) {
                                   List<String> epsilonPrijelazi = prijelazi.get(novaStanja).get("$");
                                   for (String epsilon : epsilonPrijelazi) {
                                       tempStanja.add(epsilon);
                                   }
                               }
                            }
                        }
                        else {
                            tempStanja.add("#");
                        }
                    }
                    else {
                        tempStanja.add("#");
                    }
                }

                if (tempStanja.size() > 1)
                    tempStanja.remove("#");


                staraStanja.clear();
                staraStanja.addAll(tempStanja);
                tempStanja.clear();

                for (String ispis : staraStanja) {
                    sb.append(ispis + ",");
                }
                int index = sb.lastIndexOf(",");
                sb.replace(index,index+1, "");
                sb.append("|");
            }

            int index = sb.lastIndexOf("|");
            sb.replace(index, index + 1, "");
            System.out.println(sb);

        }
    }
}
