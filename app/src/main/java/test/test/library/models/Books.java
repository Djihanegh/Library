package test.test.library.models;

public class Books {
    public int id;
    public String nom;
    public String auteur;
    public String titre;
    public String motCle;
    public String resume;
    
    public Books(int id, String nom, String auteur, String titre, String motCle, String resume) {
        this.id = id;
        this.nom = nom;
        this.auteur = auteur;
        this.titre = titre;
        this.motCle = motCle;
        this.resume = resume;
    }
    
    public Books() {
    }
}
