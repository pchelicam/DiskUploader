package ru.pchelicam.tools.du;

import java.util.Comparator;

/**
 * Uses to compare dates of FileInfo objects
 */
public class DateComparator implements Comparator<FileInfo> {
    /**
     * Overriding compare method
     */
    @Override
    public int compare(FileInfo fi1, FileInfo fi2) {
        return (fi1.getCreationDate().compareTo(fi2.getCreationDate()));
    }
}
