import utils.Time;

import static org.lwjgl.glfw.GLFW.*;

public class Engine {

    private final Window win;
    private final Shader shader;
    private final Render render;
    private final Time time;

    public Engine( Window win ) {
        this.win = win;
        shader = new Shader( "fractal" );
        render = new Render( win, shader );
        time = new Time();
    }

    public void run() {
        while ( !win.shouldClose() ) {
            time.start();

            inputHandler();
            controlsHandler();

            render.render();

            win.swapBuffers();

            win.setTitle( scale + " Scale; " + time.getFps() + " FPS"  );

            time.end();
        }
        win.terminate();
    }


    private float scale = 1f;
    private float offsetX = 0f, offsetY = 0f;
    private float prevOffsetX = offsetX, prevOffsetY = offsetY;
    private float prevScale = scale;
    private boolean playback = true;
    private void controlsHandler() {
        //if ( playback ) shader.set1f( "time", ( float ) glfwGetTime() );
        if ( prevScale != scale ) {
            prevScale = scale;
            shader.set( "scale", scale );
        }
        if ( prevOffsetX != offsetX || prevOffsetY != offsetY ) {
            prevOffsetX = offsetX;
            prevOffsetY = offsetY;
            shader.set( "offsetX", offsetX );
            shader.set( "offsetY", offsetY );
        }
    }

    private static final float SPEED = 0.03f;
    private void inputHandler() {
        if ( win.keys[ GLFW_KEY_R ] ) {
            reset();
            win.keys[ GLFW_KEY_R ] = false;
        }

        if ( win.keys[ GLFW_KEY_SPACE ] ) {
            playback = !playback;
            win.keys[ GLFW_KEY_SPACE ] = false;
        }
        if ( win.keys[ GLFW_KEY_Q ] ) {
            scale *= 1.02f;
        }
        if ( win.keys[ GLFW_KEY_E ] ) {
            scale *= 0.98f;
        }

        if ( win.keys[ GLFW_KEY_W ] ) {
            offsetY += SPEED * scale;
        }
        if ( win.keys[ GLFW_KEY_S ] ) {
            offsetY -= SPEED * scale;
        }
        if ( win.keys[ GLFW_KEY_D ] ) {
            offsetX += SPEED * scale;
        }
        if ( win.keys[ GLFW_KEY_A ] ) {
            offsetX -= SPEED * scale;
        }
    }

    private void reset() {
        scale = 0.000003f;
        offsetY = 0.131825940205312f;
        offsetX = -0.7436438870371587f;
        prevOffsetY = offsetY;
        prevOffsetX = offsetX;
        shader.set( "scale", scale );
        shader.set( "offsetX", offsetX );
        shader.set( "offsetY", offsetY );
    }
}
