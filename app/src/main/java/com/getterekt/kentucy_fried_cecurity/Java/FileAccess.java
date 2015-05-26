package com.getterekt.kentucy_fried_cecurity.Java;

/**
 * Created by devon on 26/05/15.
 */
public class FileAccess {

    private String _accessorName;
    private String _fileAccessed;
    private String _time;

    public FileAccess(String accessorName, String fileAccessed, String time) {
        _accessorName = accessorName;
        _fileAccessed = fileAccessed;
        _time = time;
    }

    public String toString() {
        return _accessorName + "\t\t  " + _fileAccessed + "\t\t  " + _time;
    }
}
