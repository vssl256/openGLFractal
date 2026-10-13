public class Main {

    public static void main( String[] args ) {
        Window win = new Window( 640, 480, "window" );
        Engine engine = new Engine( win );

        engine.run();
    }
}
