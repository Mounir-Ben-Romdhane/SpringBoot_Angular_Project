package tn.esprit.spring.DAO.Services.Bloc;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class BlocService implements IBlocService{


    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    @Override
    public Bloc addBloc(Bloc b) {
        Set<Chambre> chambres = b.getChambres();
        Foyer idFoyer = b.getFoyer();
        b.setFoyer(idFoyer);
        b = blocRepository.save(b);
        for (Chambre c : chambres){
            c.setBloc(b);
            chambreRepository.save(c);
        }
        return b;
    }
/*
    @Override
    public Bloc addBloc(Bloc b){
    return blocRepository.save(b);
    }*/
    @Override
    public List<Bloc> addBlocs(List<Bloc> blocs) {

        return blocRepository.saveAll(blocs);
    }

    @Override
    public Bloc editBloc(Long id, Bloc b) {
        if (blocRepository.findById(id).isPresent()) {
            Bloc toUpdateBloc = blocRepository.findById(id).get();
            toUpdateBloc.setNomBloc(b.getNomBloc());
            toUpdateBloc.setCapaciteBloc(b.getCapaciteBloc());
            toUpdateBloc.setFoyer(b.getFoyer());
            toUpdateBloc.setChambres(b.getChambres());
            return blocRepository.save(toUpdateBloc);
        }
        return null;
    }

    @Override
    public List<Bloc> findAll() {
        return blocRepository.findAll();
    }

    @Override
    public Bloc findById(long id) {
        return blocRepository.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        Bloc b = blocRepository.findById(id).get();
        for (Chambre c : b.getChambres()){
            chambreRepository.delete(c);
        }
        blocRepository.deleteById(id);
    }

    @Override
    public void delete(Bloc b) {
        blocRepository.delete(b);
    }

    //A consomé
    @Override
    public Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc) {
        Bloc bloc = blocRepository.findByNomBloc(nomBloc);
        numChambre.forEach(numero ->{
            Chambre c =chambreRepository.findByNumeroChambre(numero);
            c.setBloc(bloc);
            chambreRepository.save(c);
        });
    return bloc;
    }

    @Override
    public Bloc affecterBlocAFoyer(String nomBloc, String nomFoyer) {
        //Recuperation des entity by noms
        Bloc bloc = blocRepository.findByNomBloc(nomBloc);
        Foyer foyer = foyerRepository.findByNomFoyer(nomFoyer);
        //Set foyer to bloc
        bloc.setFoyer(foyer);
        //Set bloc to foyer
        List<Bloc> blocs = foyer.getBlocs();
        blocs.add(bloc);
        foyer.setBlocs(blocs);
        //save to base
        blocRepository.save(bloc);
        foyerRepository.save(foyer);
        return bloc;
    }
    @Override
    public List<Bloc> findByNomBlocContaining(String nomBloc) {
        return blocRepository.findByNomBlocContaining(nomBloc);
    }
}
