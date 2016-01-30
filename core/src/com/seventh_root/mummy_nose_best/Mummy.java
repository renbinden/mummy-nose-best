package com.seventh_root.mummy_nose_best;

import static com.seventh_root.mummy_nose_best.Mummy.MummificationStage.SARCOPHAGUS;

public class Mummy {

    public enum MummificationStage {
        START,
        DEHYDRATED,
        BRAIN_REMOVED,
        SKULL_RINSED,
        FLANK_INCISION,
        ORGANS_REMOVED,
        CAVITY_RINSED,
        SALT_DEHYDRATED,
        BANDAGED,
        GUM_USED,
        SARCOPHAGUS
    }

    public final Player player;
    public MummificationStage mummificationStage;

    public Mummy(Player player) {
        this.player = player;
    }

    public boolean complete() {
        return mummificationStage == SARCOPHAGUS;
    }

}
