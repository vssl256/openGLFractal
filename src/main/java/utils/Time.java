package utils;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time {

    private double frameStart;
    private int frameCount = 0;

    private double currentTime;
    private double prevTime = 0;

    private double deltaTime = 0;
    public double getDelta() {
        return deltaTime;
    }

    private int fps = 0;
    public int getFps() {
        return fps;
    }

    public Time() {  }

    public void start() {
        frameStart = glfwGetTime();
    }

    public void end() {
        frameCount++;
        setCurrentTime();
        if ( isSecondPassed() ) {
            fps = frameCount;
            frameCount = 0;
            prevTime = currentTime;
        }

        deltaTime = glfwGetTime() - frameStart;
    }

    private boolean isSecondPassed() {
        return currentTime - prevTime > 1d;
    }

    private void setCurrentTime() {
        currentTime = glfwGetTime();
    }
}
