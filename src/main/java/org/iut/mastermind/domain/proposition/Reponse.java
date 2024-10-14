package org.iut.mastermind.domain.proposition;

import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.unmodifiableList;

public class Reponse {
    private final String motSecret;
    private final List<Lettre> resultat = new ArrayList<>();

    public Reponse(String mot) {
        this.motSecret = mot;
    }

    // on récupère la lettre à la position dans le résultat
    public Lettre lettre(int position) {
        return resultat.get(position);
    }

    // on construit le résultat en analysant chaque lettre
    // du mot proposé
    public void compare(String essai) {
        for (int i = 0; i < essai.length(); i++) {
            resultat.add(evaluationCaractere(essai,i));
        }
    }

    // si toutes les lettres sont placées
    public boolean lettresToutesPlacees() {
        boolean toutesPlacees = true;
        for (Lettre lettre : resultat) {
            if (lettre != Lettre.PLACEE) {
                toutesPlacees = false;
                break;
            }
        }
        return toutesPlacees;
    }

    public List<Lettre> lettresResultat() {
        return unmodifiableList(resultat);
    }

    // renvoie le statut du caractère
    private Lettre evaluationCaractere(String essai, int position) {
        Lettre lettre = Lettre.INCORRECTE;
        if(essai.charAt(position)==motSecret.charAt(position)) {
            lettre = Lettre.PLACEE;
        } else if (motSecret.contains(String.valueOf(essai.charAt(position)))) {
            lettre = Lettre.NON_PLACEE;
        }
        return lettre;
    }
}
