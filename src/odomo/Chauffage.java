
package odomo;
import java.util.Scanner;

/**
 * Gestion de la partie Chauffage.
 */
class Chauffage {
    /**
     * Premier créneau du jour en mode normal.
     */
    static int[][] creneau1;
    
    /**
     * Deuxième créneau du jour en mode normal.
     */
    static int[][] creneau2;
    
    /**
     * Température de chauffage en mode Normal
     */
    static int temperNormal;
    
    /**
     * Température de chauffage en mode Eco
     */
    static int temperEco;
    
    /**
     * Initialiser les données de chauffage.
     */
    static void initialiser() {
        creneau1 = new int[7][2];
        creneau2 = new int[7][2];
        temperEco = 18;
        temperNormal = 21;
        
        for (int i=0; i<7; i++) {
                creneau1[i] = new int[]{1, 0};
                creneau2[i] = new int[]{1, 0};
        }
    }

    /**
     * Matrice des créneaux en mode normal, pour l'histogramme.
     *
     * @return la matrice des créneaux
     */
    static boolean[][] matriceCreneaux() {
        boolean[][] matrice = new boolean[8][24];
        
        for (int jour=0; jour<matrice.length-1; jour++) {
            for (int heure=0; heure<matrice[jour].length; heure++) {
                
                matrice[jour][heure] = creneau1[jour][0] <= heure 
                                        && heure <= creneau1[jour][1]
                                        && !creneauVide(creneau1[jour])
                                        || creneau2[jour][0] <= heure 
                                        && heure <= creneau2[jour][1]
                                        && !creneauVide(creneau2[jour]);
            }
        }
        return matrice;
    }
    
    /**
     * Rôle : permet de vérifier si un creneau est "vide" ou non, c'est à dire 
     *        s'il est de la forme [1, 0].
     * @param creneau 
     * @return true si le creneau passé en paramètre est "vide", false sinon.
     */
    static boolean creneauVide(int[] creneau) {
        return creneau[0] == 1 && creneau[1] == 0;
    }
    
    /**
     * Procédure de saisie des créneaux de chauffage.
     */
    static void saisieCreneaux() {
        Scanner scanner = new Scanner(System.in);
        String saisie = scanner.nextLine();
        
        traitementSaisieCreneaux(saisie);
    }
 
    /**
     * Traite la saisie de créneaux par l'utilisateur.
     *
     * @param saisie la saisie de l'utilisateur
     * @return true ssi la saisie a été correcte
     */
    static boolean traitementSaisieCreneaux(String saisie) {
        boolean correct = saisie != null;
        String[] champs = null;
        if (correct) {                                          
            champs = saisie.split(";");
            correct &= champs.length == 3 || champs.length == 5;
            if (!correct) {
                System.err.println("Format incorrect : 3 ou 5 champs separes " 
                        + "par des points-virgules sont attendus, " 
                        + champs.length + " ont été saisis.");
            }
        }
        if (correct) {
            correct &= Odomo.numeroJour(champs[0]) >= 0;
            if (!correct) {
                System.err.println("Nom de jour incorrect : " + champs[0] + ".");
            }
        }
        int creneau1debut = -1;
        if (correct) {
            creneau1debut = heureCreneau(champs[1]);
            correct = creneau1debut >= 0;
        }
        int creneau1fin = -1;
        if (correct) {
            creneau1fin = heureCreneau(champs[2]);
            correct = creneau1fin >= 0;
        }
        if (correct) {
            correct &= (creneau1debut <= creneau1fin) || (creneau1debut == 1 && creneau1fin == 0);
            if (!correct) {
                System.err.println("Créneau incorrect : l'heure de début doit " 
                        + "précéder (ou égaler) l'heure de fin " 
                        + "(ou choisir le créneau 1h-0h pour un créneau vide).");
            }
        }
        int creneau2debut = -1;
        int creneau2fin = -1;
        if (correct && champs.length == 5) {
            creneau2debut = heureCreneau(champs[3]);
            correct = creneau2debut >= 0;
            if (correct) {
                creneau2fin = heureCreneau(champs[4]);
                correct = creneau2fin >= 0;
            }
            if (correct) {
                correct &= (creneau2debut <= creneau2fin) || (creneau2debut == 1 && creneau2fin == 0);
                if (!correct) {
                    System.err.println("Créneau incorrect : l'heure de début doit " 
                            + "précéder (ou égaler) l'heure de fin " 
                            + "(ou choisir le créneau 1h-0h pour un créneau vide).");
                }
            }
        }
        if (correct) {
            int numJour = Odomo.numeroJour(champs[0]);
            Chauffage.creneau1[numJour][0] = creneau1debut;
            Chauffage.creneau1[numJour][1] = creneau1fin;
            if (champs.length == 5) {
                Chauffage.creneau2[numJour][0] = creneau2debut;
                Chauffage.creneau2[numJour][1] = creneau2fin;
            } else {
                Chauffage.creneau2[numJour][0] = 1;
                Chauffage.creneau2[numJour][1] = 0;
            }
        }
        return correct;
    }

    /**
     * Récupère l'heure d'un créneau donné sous forme de chaîne.
     *
     * @param chaineHeure l'heure sous forme de chaîne
     * @return l'heure sous forme d'entier (-1 si incorrecte)
     */
    static int heureCreneau(String chaineHeure) {
        int heure;
        try {
            heure = Integer.parseInt(chaineHeure);
        } catch (NumberFormatException e) {
            System.err.println("L'heure de créneau n'est pas un entier : " + chaineHeure);
            heure = -1;
        }
        if (heure > 23) {
            System.err.println("L'heure doit être comprise entre 0 et 23 " 
                    + "(inclus), au lieu de : " + chaineHeure + ".");
            heure = -1;
        }
        return heure;
    }
    
}
