/*
 * Decompiled with CFR 0_118.
 */
package caso3GeneratorSinSeguridad;

import caso3Core.LoadGenerator;
import caso3Core.Task;

public class Generator {
    private LoadGenerator generator;
	private static final int gapBetweenTasks = 1000;
	public static final int numberOfTasks = 1;

    public Generator() {
        Task work = this.createTask();
        this.generator = new LoadGenerator("Client - Server Load Test", numberOfTasks, work, gapBetweenTasks);
        this.generator.generate();
    }

    private Task createTask() {
        return new ClientServerTask();
    }

    public static /* varargs */ void main(String ... args) {
        new Generator();
    }
}

