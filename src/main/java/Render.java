import org.joml.Vector2f;

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
        shader.set( "width", win.width );
        shader.set( "height", win.height );
        shader.set( "scale", new Vector2f( 1.0f, 0.0f ) );
        shader.set( "offsetX", new Vector2f( 0.0f ) );
        shader.set( "offsetY", new Vector2f( 0.0f ) );
    }

    public void render() {
        win.clear();
        if ( win.isResized ) {
            shader.set( "width", win.width );
            shader.set( "height", win.height );
            win.isResized = false;
        }
        mesh.draw();
    }
}
