package ru.pchelicam.tools.du;

/**
 * Corrects tr and mr parameters
 */
public class CmdLineValidator {
    /**
     * Method that corrects tr and mr parameters
     */
    public void correctTRandMR(int tr, int mr) {
        if (tr == 0 || mr == 0)
            return;
        if (tr < 0 || mr < 0) {
            return;
        }
        if (tr < mr) {
            return;
        }
    }
}
