package be.wamberchies.utils;

import be.wamberchies.utils.serializateur.Serializateur;
import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class Comptor implements Serializable {

    @Serial
    private static final long SerialversionUID = 1L;
    private static final String SAVE_FILEPATH = "Sauvegarde/Compteur.ser";

    private int comptor = 0;

    public static Comptor loadComptor() {
        try {
            System.out.println("Chargement du compteur");
            return Serializateur.deserialize(SAVE_FILEPATH);
        } catch (FileNotFoundException e) {
            return new Comptor();
        }
    }

    public void increment() {
        this.comptor++;
        System.out.println(comptor + " increment");
        save();
    }

    public void reset() {
        this.comptor = 0;
        System.out.println(comptor + " reset");
        save();
    }

    public boolean isBefore(int nombre) {
        return this.comptor == nombre - 1;
    }

    public void save() {
        Serializateur.serialize(this, SAVE_FILEPATH);
    }

}
