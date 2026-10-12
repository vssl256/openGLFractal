import org.lwjgl.opengl.GL11C;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.GL_FILL;
import static org.lwjgl.opengl.GL20C.glUseProgram;

public class Render {

    private final Window win;
    private final Mesh mesh;
    private final Shader shader;

    public Render( Window win, Shader shader ) {
        this.win = win;
        this.shader = shader;
        mesh = new Mesh( new float[] {
                1.0f,   1.0f,   0.0f,
                1.0f,   -1.0f,  0.0f,
                -1.0f,  -1.0f,  0.0f,
                -1.0f,  1.0f,   0.0f
        }, shader );
        System.out.println( "Setting width to " + win.width );
        shader.set1i( "width", win.width );
        shader.set1i( "height", win.height );
        shader.set1f( "scale", 1f );
        shader.set1f( "offsetX", 0f );
        shader.set1f( "offsetY", 0f );
    }

    public void render() {
        win.clear();
        if ( win.isResized ) {
            shader.set1i( "width", win.width );
            shader.set1i( "height", win.height );
            win.isResized = false;
        }
        mesh.draw();
    }
}
