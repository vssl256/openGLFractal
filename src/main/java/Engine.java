import org.joml.Vector2f;
import utils.Time;

import static org.lwjgl.glfw.GLFW.*;

public class Engine {

    private final Window win;
    private final Shader shader;
    private final Render render;
    private final Time time;

    public Engine( Window win ) {
        this.win = win;
        shader = new Shader( "test" );
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


    private double scale = 1d;
    private double offsetX = 0d, offsetY = 0d;
    private double prevOffsetX = offsetX, prevOffsetY = offsetY;
    private double prevScale = scale;
    private boolean playback = true;
    private void controlsHandler() {
        //if ( playback ) shader.set1f( "time", ( float ) glfwGetTime() );
        if ( prevScale != scale ) {
            prevScale = scale;
            shader.set( "scale", toDoubleSingle( scale ) );
        }
        if ( prevOffsetX != offsetX || prevOffsetY != offsetY ) {
            prevOffsetX = offsetX;
            prevOffsetY = offsetY;
            shader.set( "offsetX", toDoubleSingle( offsetX ) );
            shader.set( "offsetY", toDoubleSingle( offsetY ) );
        }
    }

    private static final double SPEED = 0.03d;
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
            scale *= 1.02d;
        }
        if ( win.keys[ GLFW_KEY_E ] ) {
            scale *= 0.98d;
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
        scale = 0.000003d;
        offsetY = 0.131825940205312d;
        offsetX = -0.7436438870371587d;
        prevOffsetY = offsetY;
        prevOffsetX = offsetX;
        prevScale = scale;
        shader.set( "scale", toDoubleSingle( scale ) );
        shader.set( "offsetX", toDoubleSingle( offsetX ) );
        shader.set( "offsetY", toDoubleSingle( offsetY ) );
    }

    private Vector2f toDoubleSingle( double value ) {
        float high = ( float ) value;
        float low = ( float ) ( value - ( double ) high );
        return new Vector2f( high, low );
    }
}
