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
    private boolean penaltyEnabled = false;

    public static Comptor loadComptor() {
        try {
            return Serializateur.deserialize(SAVE_FILEPATH);
        } catch (FileNotFoundException e) {
            return new Comptor();
        }
    }

    public void increment() {
        this.comptor++;
        save();
    }

    public void reset() {
        this.comptor = 0;
        save();
    }

    public boolean isBefore(int nombre) {
        return this.comptor == nombre - 1;
    }

    public void togglePenalty() {
        this.penaltyEnabled = !this.penaltyEnabled;
        System.out.println("Penalty set to " + this.penaltyEnabled);
        save();
    }

    public void save() {
        Serializateur.serialize(this, SAVE_FILEPATH);
    }

}
