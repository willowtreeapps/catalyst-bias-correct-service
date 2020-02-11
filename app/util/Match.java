package util;

public class Match {
    private String _trigger;
    private int _startIndex = Integer.MIN_VALUE;
    private int _endIndex = Integer.MIN_VALUE;

    public String getTrigger() {
        return _trigger;
    }

    public int getStartIndex() {
        return _startIndex;
    }

    public int getEndIndex() {
        return _endIndex;
    }

    private Match(MatchBuilder builder) {
        _trigger = builder._trigger;
        _startIndex = builder._startIndex;
        _endIndex = builder._endIndex;

        if (_trigger == null
                || _startIndex == Integer.MIN_VALUE
                || _endIndex == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Invalid values for Match constructor");
        }
    }

    public static class MatchBuilder {

        private String _trigger;
        private int _startIndex = Integer.MIN_VALUE;
        private int _endIndex = Integer.MIN_VALUE;

        public MatchBuilder setTrigger(String trigger) {
            this._trigger = trigger;
            return this;
        }

        public MatchBuilder setStartIndex(int startIndex) {
            this._startIndex = startIndex;
            return this;
        }

        public MatchBuilder setEndIndex(int endIndex) {
            this._endIndex = endIndex;
            return this;
        }

        public Match build() {
            return new Match(this);
        }
    }
}
