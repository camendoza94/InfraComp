/*
 * Decompiled with CFR 0_118.
 */
package caso3GeneratorSinSeguridad;

import caso3Core.LoadGenerator;
import caso3Core.Task;

public class Generator {
    private LoadGenerator generator;

    public Generator() {
        Task work = this.createTask();
        int numberOfTasks = 1;
        int gapBetweenTasks = 1000;
        this.generator = new LoadGenerator("Client - Server Load Test", numberOfTasks, work, gapBetweenTasks);
        this.generator.generate();
    }

    private Task createTask() {
        return new ClientServerTask();
    }

    public static /* varargs */ void main(String ... args) {
        Generator gen = new Generator();
    }
}

