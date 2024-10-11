package org.iut.mastermind.domain;

import org.iut.mastermind.domain.partie.Joueur;
import org.iut.mastermind.domain.partie.Partie;
import org.iut.mastermind.domain.partie.PartieRepository;
import org.iut.mastermind.domain.partie.ResultatPartie;
import org.iut.mastermind.domain.proposition.Reponse;
import org.iut.mastermind.domain.tirage.MotsRepository;
import org.iut.mastermind.domain.tirage.ServiceNombreAleatoire;
import org.iut.mastermind.domain.tirage.ServiceTirageMot;
import java.util.Optional;

public class Mastermind {
    private final PartieRepository partieRepository;
    private final ServiceTirageMot serviceTirageMot;

    public Mastermind(PartieRepository pr, MotsRepository mr, ServiceNombreAleatoire na) {
        this.partieRepository = pr;
        this.serviceTirageMot = new ServiceTirageMot(mr, na);
    }

    // on récupère éventuellement la partie enregistrée pour le joueur
    // si il y a une partie en cours, on renvoie false (pas de nouvelle partie)
    // sinon on utilise le service de tirage aléatoire pour obtenir un mot
    // et on initialise une nouvelle partie et on la stocke
    public boolean nouvellePartie(Joueur joueur) {
        if(!isJeuEnCours(partieRepository.getPartieEnregistree(joueur))){
            partieRepository.create(new Partie(joueur,serviceTirageMot.tirageMotAleatoire(),0,false));
            return true;
        }
        return false;
    }

    // on récupère éventuellement la partie enregistrée pour le joueur
    // si la partie n'est pas une partie en cours, on renvoie une erreur
    // sinon on retourne le resultat du mot proposé
    public ResultatPartie evaluation(Joueur joueur, String motPropose) {
        if(!isJeuEnCours(partieRepository.getPartieEnregistree(joueur))){
            return new ResultatPartie(null,false,true);
        }
        Partie partie = partieRepository.getPartieEnregistree(joueur).get();
        return calculeResultat(partie,motPropose);
    }

    // on évalue le résultat du mot proposé pour le tour de jeu
    // on met à jour la bd pour la partie
    // on retourne le résulat de la partie
    private ResultatPartie calculeResultat(Partie partie, String motPropose) {
        ResultatPartie resultatPartie;
        if(partie.isTerminee()) resultatPartie = new ResultatPartie(null,false,true);
        else {
            resultatPartie = new ResultatPartie(partie.tourDeJeu(motPropose),partie.isTerminee(),false);
            partieRepository.update(partie);
        }
        return resultatPartie;
    }

    // si la partie en cours est vide, on renvoie false
    // sinon, on évalue si la partie est terminée
    private boolean isJeuEnCours(Optional<Partie> partieEnCours) {
        if(partieEnCours.isEmpty()){
            return false;
        }
        return !partieEnCours.get().isTerminee();
    }
}
