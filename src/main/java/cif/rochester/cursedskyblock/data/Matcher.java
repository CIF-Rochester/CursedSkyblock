package cif.rochester.cursedskyblock.data;

import java.util.Collection;

public enum Matcher {
    ANY,
    ONLY{
        @Override
        public <V> boolean matches(Collection<V> options, V dimid) {
            return options.contains(dimid);
        }
    },
    EXCEPT{
        @Override
        public <V> boolean matches(Collection<V> options, V dimid) {
            return !ONLY.matches(options,dimid);
        }
    };

    public <V> boolean matches(Collection<V> options, V dimid) {
        return true;
    }
}
