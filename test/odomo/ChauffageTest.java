package odomo;

import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests des méthodes de la classe Chauffage.
 */
public class ChauffageTest {
    
    @Test
    public void testInitialiser() {
        Chauffage.initialiser();
        assertNotNull(Chauffage.creneau1);
        assertNotNull(Chauffage.creneau2);
        assertNotNull(Chauffage.creneau1[0]);
        assertNotNull(Chauffage.creneau2[0]);
        assertEquals(18, Chauffage.temperEco);
        assertEquals(21, Chauffage.temperNormal);
        
    }
    
    @Test
    public void testMatriceCreneaux() {
        Chauffage.initialiser();
        
        int[][] referenceCreneau1 = {
                                {6,9}, 
                                {6,9}, 
                                {6,22}, 
                                {6,9}, 
                                {6,9}, 
                                {7,23}, 
                                {7,23}
                             };
        int[][] referenceCreneau2 = {
                                {17,22}, 
                                {17,22}, 
                                {1,0}, 
                                {17,22}, 
                                {17,22}, 
                                {1,0}, 
                                {1,0}
                             };
        
        boolean[][] matriceDeReference = {
            {false, false, false, false, false, false, true, true, true, true,
                false, false, false, false, false, false, false, true, true, 
                true, true, true, true, false},
            {false, false, false, false, false, false, true, true, true, true,
                false, false, false, false, false, false, false, true, true, 
                true, true, true, true, false},
            {false, false, false, false, false, false, true, true, true, true,
                true, true, true, true, true, true, true, true, true, true, 
                true,true, true, false},
            {false, false, false, false, false, false, true, true, true, true,
                false, false, false, false, false, false, false, true, true, 
                true, true, true, true, false},
            {false, false, false, false, false, false, true, true, true, true,
                false, false, false, false, false, false, false, true, true, 
                true, true, true, true, false},
            {false, false, false, false, false, false, false, true, true, true, 
                true ,true, true, true, true, true, true, true, true, true, 
                true, true, true, true},
            {false, false, false, false, false, false, false, true, true, true, 
                true ,true, true, true, true, true, true, true, true, true, 
                true, true, true, true},
            {false, false, false, false, false, false, false, false, false, 
                false,false, false, false, false, false, false, false, false, 
                false, false, false, false, false, false}
        };
        
        Chauffage.creneau1 = referenceCreneau1;
        Chauffage.creneau2 = referenceCreneau2;     
        boolean[][] matriceProduite = Chauffage.matriceCreneaux();
        
        assertArrayEquals(matriceDeReference, matriceProduite);
        matriceDeReference[0][6] = false;
        Chauffage.creneau1[0][0] = 7;
        matriceProduite = Chauffage.matriceCreneaux();
        assertArrayEquals(matriceDeReference, matriceProduite);
        
    }
    
    @Test
    public void testCreneauVide() {
        int[][] creneau = {{1,0}, {0,1}, {1,0}, {2,3}};
        
        assertTrue(Chauffage.creneauVide(creneau[0]));
        assertFalse(Chauffage.creneauVide(creneau[1]));
        assertTrue(Chauffage.creneauVide(creneau[2]));
        assertFalse(Chauffage.creneauVide(creneau[3]));
    }
    
    @Test
    public void testTraitementSaisieCreneaux() {
        Chauffage.initialiser();
        //un seul créneau défini
        assertTrue(Chauffage.traitementSaisieCreneaux("ven;12;13")); 
        //deux créneaux définis
        assertTrue(Chauffage.traitementSaisieCreneaux("ven;12;13;17;22")); 
        //superposition des créneaux
        assertTrue(Chauffage.traitementSaisieCreneaux("ven;12;13;11;15")); 
        //créneau2 actif avant créneau1
        assertTrue(Chauffage.traitementSaisieCreneaux("ven;12;13;9;10"));
        //heure de début de créneau supérieure à l'heure de fin
        assertFalse(Chauffage.traitementSaisieCreneaux("12;13"));
        //une valeur manquante
        assertFalse(Chauffage.traitementSaisieCreneaux("ven;13;12")); 
        //une valeur en trop
        assertFalse(Chauffage.traitementSaisieCreneaux("ven;12;13;17;22;10")); 
        //des valeurs hors interval journalier 
        assertFalse(Chauffage.traitementSaisieCreneaux("ven;12;24;11;-1")); 
    }
}
