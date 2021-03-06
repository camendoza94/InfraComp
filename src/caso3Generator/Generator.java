/*
 * Decompiled with CFR 0_118.
 */
package caso3Generator;

import caso3Core.LoadGenerator;
import caso3Core.Task;

public class Generator {
    private LoadGenerator generator;
	public static final int numberOfTasks = 400;
	private static final int gapBetweenTasks = 20;

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

