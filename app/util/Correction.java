package util;

public class Correction {
    public Correction(String trigger, String suggestion) {
        _trigger = trigger;
        _suggestion = suggestion;
    }

    public String getTrigger() {
        return _trigger;
    }

    public String getSuggestion() {
        return _suggestion;
    }

    private String _trigger;
    private String _suggestion;
}