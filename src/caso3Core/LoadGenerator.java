/*
 * Decompiled with CFR 0_118.
 */
package caso3Core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import caso3Core.LoadUnit;
import caso3Core.Task;

public class LoadGenerator {
    public static int SYNC_GAP = 5000;
    private String name;
    private int executorsNumber;
    private ExecutorService executors;
    private int loadUnits;
    private Task unit;
    private long timeGap;

    public LoadGenerator(String nameP, int executorsNumberP, int loadUnitsP, Task unitP, long timeGapP) {
        this.name = nameP;
        this.executorsNumber = executorsNumberP;
        this.executors = Executors.newFixedThreadPool(executorsNumberP);
        this.loadUnits = loadUnitsP;
        this.unit = unitP;
        this.timeGap = timeGapP;
    }

    public LoadGenerator(String nameP, int loadUnitsP, Task unitP, long timeGapP) {
        this.name = nameP;
        this.executorsNumber = loadUnitsP;
        this.executors = Executors.newFixedThreadPool(this.executorsNumber);
        this.loadUnits = loadUnitsP;
        this.unit = unitP;
        this.timeGap = timeGapP;
    }

    public void generate() {
        int i = 0;
        while (i < this.loadUnits) {
            boolean sync = false;
            if (this.timeGap == 0) {
                sync = true;
            }
            LoadUnit unidad = new LoadUnit(this.unit, i, this.timeGap * (long)i, sync);
            this.executors.execute(unidad);
            try {
                Thread.sleep(this.timeGap);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++i;
        }
    }

    public static int getSYNC_GAP() {
        return SYNC_GAP;
    }

    public static void setSYNC_GAP(int SYNC_GAP_P) {
        SYNC_GAP = SYNC_GAP_P;
    }
}

